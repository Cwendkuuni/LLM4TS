package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Test case 1: Object literal with no references
    Node root = Node.objectLiteral(
        Node.newProperty("a", Node.newNumber(1)),
        Node.newProperty("b", Node.newNumber(2)),
        Node.newProperty("c", Node.newNumber(3))
    );

    AbstractCompiler compiler = new AbstractCompiler() {};
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, NodeUtil::newNameSupplier
    );

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        compiler, inlineObjectLiterals.new InliningBehavior()
    );
    callback.process(null, root);

    ReferenceCollection referenceInfo = callback.getReferenceInfo();
    Map<Var, List<Reference>> varReferences = Maps.newHashMap();
    for (Var var : referenceInfo.getScope().getVars()) {
      varReferences.put(var, Lists.newArrayList(
          referenceInfo.getReferences(var)
      ));
    }

    Map<String, String> expectedVarMap = Maps.newLinkedHashMap();
    expectedVarMap.put("a", "JSCompiler_object_inline_a_");
    expectedVarMap.put("b", "JSCompiler_object_inline_b_");
    expectedVarMap.put("c", "JSCompiler_object_inline_c_");

    for (Map.Entry<Var, List<Reference>> entry : varReferences.entrySet()) {
      Var var = entry.getKey();
      List<Reference> refs = entry.getValue();
      Map<String, Node> initvals = Maps.newHashMap();
      inlineObjectLiterals.new InliningBehavior().fillInitialValues(refs.get(0), initvals);

      Map<String, String> varmap = inlineObjectLiterals.new InliningBehavior().computeVarList(var, referenceInfo);
      assertEquals(expectedVarMap, varmap);

      for (Reference ref : refs) {
        Node getprop = ref.getParent();
        String var = getprop.getLastChild().getString();

        assertEquals(varmap.get(var), getprop.getChildAtIndex(1).getString());
      }
    }

    // Test case 2: Object literal with references
    root = Node.objectLiteral(
        Node.newProperty("a", Node.newNumber(1)),
        Node.newProperty("b", Node.newObjectLiteral(
            Node.newProperty("x", Node.newNumber(2)),
            Node.newProperty("y", Node.newNumber(3))
        )),
        Node.newProperty("c", Node.newNumber(3))
    );

    referenceInfo = callback.getReferenceInfo();
    varReferences = Maps.newHashMap();
    for (Var var : referenceInfo.getScope().getVars()) {
      varReferences.put(var, Lists.newArrayList(
          referenceInfo.getReferences(var)
      ));
    }

    expectedVarMap = Maps.newLinkedHashMap();
    expectedVarMap.put("a", "JSCompiler_object_inline_a_");
    expectedVarMap.put("b.x", "JSCompiler_object_inline_b_x_");
    expectedVarMap.put("b.y", "JSCompiler_object_inline_b_y_");
    expectedVarMap.put("c", "JSCompiler_object_inline_c_");

    for (Map.Entry<Var, List<Reference>> entry : varReferences.entrySet()) {
      Var var = entry.getKey();
      List<Reference> refs = entry.getValue();
      Map<String, Node> initvals = Maps.newHashMap();
      inlineObjectLiterals.new InliningBehavior().fillInitialValues(refs.get(0), initvals);

      Map<String, String> varmap = inlineObjectLiterals.new InliningBehavior().computeVarList(var, referenceInfo);
      assertEquals(expectedVarMap, varmap);

      for (Reference ref : refs) {
        Node getprop = ref.getParent();
        String var = getprop.getLastChild().getString();

        String expectedVar = var.substring(0, var.lastIndexOf('.'));
        assertEquals(expectedVarMap.get(var), getprop.getChildAtIndex(1).getString());
      }
    }

    // Test case 3: Object literal with self-referential property
    root = Node.objectLiteral(
        Node.newProperty("a", Node.newNumber(1)),
        Node.newProperty("b", Node.newObjectLiteral(
            Node.newProperty("x", Node.newNumber(2)),
            Node.newProperty("y", Node.newObjectLiteral(
                Node.newProperty("z", Node.newName("b"))
            ))
        ))
    );

    assertFalse(inlineObjectLiterals.new InliningBehavior().isInlinableObject(referenceInfo.references));

    // Test case 4: Object literal with initial value assignment
    root = Node.objectLiteral(
        Node.newProperty("a", Node.newNumber(1)),
        Node.newProperty("b", Node.newObjectLiteral(
            Node.newProperty("x", Node.newNumber(2)),
            Node.newProperty("y", Node.newNumber(3))
        )),
        Node.newProperty("c", Node.newNumber(3)),
        Node.newProperty("d", Node.newObjectLiteral(
            Node.newProperty("x", Node.newNumber(4)),
            Node.newProperty("y", Node.newNumber(5)),
            Node.newProperty("e", Node.newObjectLiteral(
                Node.newProperty("x", Node.newName("d"))
            ))
        ))
    );

    referenceInfo = callback.getReferenceInfo();
    varReferences = Maps.newHashMap();
    for (Var var : referenceInfo.getScope().getVars()) {
      varReferences.put(var, Lists.newArrayList(
          referenceInfo.getReferences(var)
      ));
    }

    expectedVarMap = Maps.newLinkedHashMap();
    expectedVarMap.put("a", "JSCompiler_object_inline_a_");
    expectedVarMap.put("b.x", "JSCompiler_object_inline_b_x_");
    expectedVarMap.put("b.y", "JSCompiler_object_inline_b_y_");
    expectedVarMap.put("c", "JSCompiler_object_inline_c_");
    expectedVarMap.put("d.x", "JSCompiler_object_inline_d_x_");
    expectedVarMap.put("d.y", "JSCompiler_object_inline_d_y_");
    expectedVarMap.put("d.e.x", "JSCompiler_object_inline_d_e_x_");

    for (Map.Entry<Var, List<Reference>> entry : varReferences.entrySet()) {
      Var var = entry.getKey();
      List<Reference> refs = entry.getValue();
      Map<String, Node> initvals = Maps.newHashMap();
      inlineObjectLiterals.new InliningBehavior().fillInitialValues(refs.get(0), initvals);

      Map<String, String> varmap = inlineObjectLiterals.new InliningBehavior().computeVarList(var, referenceInfo);
      assertEquals(expectedVarMap, varmap);

      for (Reference ref : refs) {
        Node getprop = ref.getParent();
        String var = getprop.getLastChild().getString();

        String expectedVar = var.substring(0, var.lastIndexOf('.'));
        assertEquals(expectedVarMap.get(var), getprop.getChildAtIndex(1).getString());
      }
    }

    // Test case 5: Object literal with cycle references
    root = Node.objectLiteral(
        Node.newProperty("a", Node.newObjectLiteral(
            Node.newProperty("b", Node.newObjectLiteral(
                Node.newProperty("a", Node.newName("a"))
            ))
        )),
        Node.newProperty("b", Node.newObjectLiteral(
            Node.newProperty("a", Node.newName("b"))
        ))
    );

    assertFalse(inlineObjectLiterals.new InliningBehavior().isInlinableObject(referenceInfo.references));
  }

}

class NodeUtil {
  public static Node newUndefinedNode(Node parent) {
    return new Node(Token.UNDEFINED, null);
  }

  public static Node newVarNode(String name, Node value) {
    return new Node(Token.VAR, Node.newString(Token.NAME, name), value);
  }

  public static Node newNameSupplier() {
    return new Supplier<String>() {
      private int counter = 0;

      @Override
      public String get() {
        return "name_" + counter++;
      }
    }.get();
  }
}