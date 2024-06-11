package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Node root = Node.objectLiteral(
        "obj",
        Node.newProperty("a", Node.numberLiteral(1)),
        Node.newProperty("b", Node.numberLiteral(2)),
        Node.newProperty("c", Node.stringLiteral("foo"))
    );

    Scope scope = new Scope(null, null, Node.SCOPE_GLOBAL);
    Var objVar = scope.declareVar("obj");
    Node objDecl = new Node(Token.VAR, Lists.newArrayList(objVar), root);
    scope.setRoot(objDecl);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        new NoOpCompiler(), new InliningBehavior());
    callback.process(null, root);

    ReferenceCollection referenceInfo = callback.getReferenceInfo();
    InliningBehavior behavior = (InliningBehavior) callback.getBehavior();

    assertEquals(3, referenceInfo.references.size());

    for (Reference ref : referenceInfo.references) {
      if (ref.getNode().getType() == Token.GETPROP) {
        Node getprop = ref.getNode();
        String var = getprop.getChildAtIndex(1).getString();
        assertEquals(var, behavior.varmap.get(var).toString());
      }
    }

    assertEquals(3, behavior.staleVars.size());

    for (Var v : behavior.staleVars) {
      assertEquals("obj_a", v.name);
      assertEquals("JSCompiler_object_inline_a_" + referenceInfo.idCounter,
          v.value.toString());
    }

    assertEquals(0, behavior.staleVars.size());
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    Node root = Node.objectLiteral(
        "obj",
        Node.newProperty("a", Node.numberLiteral(1)),
        Node.newProperty("b", Node.newObjectLiteral(
            "innerObj", Node.newProperty("c", Node.stringLiteral("foo")),
            Node.newProperty("d", Node.newGetterSetter("getA", "setA",
                Node.newExpression(Node.newName("obj.a"), null)))
        ))
    );

    Scope scope = new Scope(null, null, Node.SCOPE_GLOBAL);
    Var objVar = scope.declareVar("obj");
    Node objDecl = new Node(Token.VAR, Lists.newArrayList(objVar), root);
    scope.setRoot(objDecl);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        new NoOpCompiler(), new InliningBehavior());
    callback.process(null, root);

    ReferenceCollection referenceInfo = callback.getReferenceInfo();
    InliningBehavior behavior = (InliningBehavior) callback.getBehavior();

    assertEquals(5, referenceInfo.references.size());

    for (Reference ref : referenceInfo.references) {
      if (ref.getNode().getType() == Token.GETPROP) {
        Node getprop = ref.getNode();
        String var;
        if (getprop.getLastChild().getType() == Token.GET) {
          var = getprop.getString();
        } else {
          var = getprop.getChildAtIndex(1).getString();
        }
        assertEquals(var, behavior.varmap.get(var).toString());
      }
    }

    assertEquals(5, behavior.staleVars.size());

    for (Var v : behavior.staleVars) {
      assertEquals("obj_a", v.name);
      assertEquals("JSCompiler_object_inline_a_" + referenceInfo.idCounter,
          v.value.toString());
      if (v.name.equals("obj_innerObj_c")) {
        assertEquals("JSCompiler_object_inline_innerObj_c_" + referenceInfo.idCounter,
            v.value.getChild(0).toString());
      }
    }

    assertEquals(0, behavior.staleVars.size());
  }

  @Test
  public void testInlineObjectLiteralsWithReusedKey() {
    Node root = Node.objectLiteral(
        "obj",
        Node.newProperty("a", Node.numberLiteral(1)),
        Node.newProperty("a", Node.numberLiteral(2)),
        Node.newProperty("b", Node.numberLiteral(2))
    );

    Scope scope = new Scope(null, null, Node.SCOPE_GLOBAL);
    Var objVar = scope.declareVar("obj");
    Node objDecl = new Node(Token.VAR, Lists.newArrayList(objVar), root);
    scope.setRoot(objDecl);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        new NoOpCompiler(), new InliningBehavior());
    callback.process(null, root);

    ReferenceCollection referenceInfo = callback.getReferenceInfo();
    InliningBehavior behavior = (InliningBehavior) callback.getBehavior();

    assertEquals(3, referenceInfo.references.size());

    for (Reference ref : referenceInfo.references) {
      if (ref.getNode().getType() == Token.GETPROP) {
        Node getprop = ref.getNode();
        String var;
        if (getprop.getLastChild().getType() == Token.GET) {
          var = getprop.getString();
        } else {
          var = getprop.getChildAtIndex(1).getString();
        }
        if (var.equals("a")) {
          assertEquals("JSCompiler_object_inline_a_" + referenceInfo.idCounter,
              behavior.varmap.get("obj_a").toString());
          assertEquals("JSCompiler_object_inline_a_" + referenceInfo.idCounter,
              behavior.varmap.get("obj_a_1").toString());
        } else {
          assertEquals(var, behavior.varmap.get(var).toString());
        }
      }
    }

    assertEquals(3, behavior.staleVars.size());

    for (Var v : behavior.staleVars) {
      assertEquals("obj_a", v.name);
      assertEquals("JSCompiler_object_inline_a_" + referenceInfo.idCounter, v.value.toString());
      assertEquals("obj_a_1", v.name + "_1");
      assertEquals("JSCompiler_object_inline_a_1_" + referenceInfo.idCounter, v.value.getChild(0).toString());
    }

    assertEquals(0, behavior.staleVars.size());
  }
}

class NoOpCompiler implements Compiler {
  @Override
  public void registerPasses(PassManager passManager) {}
}