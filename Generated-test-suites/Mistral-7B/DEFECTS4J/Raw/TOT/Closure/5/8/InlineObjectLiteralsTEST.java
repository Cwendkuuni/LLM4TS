package com.google.javascript.jscomp;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "inlineVar_");
    List<Node> nodes = List.of(new Node(Token.BLOCK, null),
        new Node(Token.VAR, null, "obj", null, null, null, null, null, null,
            new Node(Token.OBJECT_LITERAL, null), null),
        new Node(Token.NAME, "obj.key", null, null, null, null, null, null, null,
            new Node(Token.IDENT, "key"), new Node(Token.COLON, null),
            new Node(Token.INT_LITERAL, 42)),
        new Node(Token.NAME, "obj.otherKey", null, null, null, null, null, null, null,
            new Node(Token.IDENT, "otherKey"), new Node(Token.COLON, null),
            new Node(Token.STRING_LITERAL, "value")),
        new Node(Token.NAME, "obj.fn", null, null, null, null, null, null, null,
            new Node(Token.FUNCTION, null, null, null, null, null, null, null, null,
                new Node(Token.BLOCK, null), null), null),
        new Node(Token.NAME, "fn", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "fn()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call(obj)", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call(null)", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({})", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2})", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}.baz)", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}.baz = 42)", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}.baz = 42).qux", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).qux = 42", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).qux()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "fn = obj.fn.call({foo: 1, bar: 2}).qux", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "fn()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call(obj).fn()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call(null).fn()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({}).fn()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call(obj)", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call(null)", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call({})", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2})", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).qux", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).qux = 42", null, null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).qux()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "fn = obj.fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).qux", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "fn()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call(obj).fn.call(obj).fn()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call(null).fn.call(null).fn()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({}).fn.call({}).fn()", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).fn.call(obj)", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).fn.call(null)", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).fn.call({})", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2})", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).qux", null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).qux = 42", null, null, null, null, null, null, null, null,
            null),
        new Node(Token.NAME, "obj.fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).fn.call({foo: 1, bar: 2}).qux()", null, null, null, null, null, null, null,
            null),
        new Node(Token.BLOCK, null),
        new Node(Token.END, null));

    compiler.compile(nodes, null, null);

    Scope rootScope = compiler.getRootScope();
    Scope objScope = rootScope.getChildScope();

    // Check definitions
    Var objVar = objScope.getVar("obj");
    Var fnVar = objScope.getVar("fn");

    Preconditions.checkState(objVar != null);
    Preconditions.checkState(fnVar != null);

    Map<String, Node> expectedInitValues = Maps.newHashMap();
    expectedInitValues.put("key", new Node(Token.INT_LITERAL, 42));
    expectedInitValues.put("otherKey", new Node(Token.STRING_LITERAL, "value"));

    Preconditions.checkState(ReferenceCollectingCallback.isWellDefined(objVar));
    Preconditions.checkState(ReferenceCollectingCallback.isWellDefined(fnVar));

    ReferenceCollection referenceInfo = ReferenceCollectingCallback.getReferences(objVar);
    Map<String, Node> actualInitValues = Maps.newHashMap();

    if (ReferenceCollectingCallback.isWellDefined(referenceInfo)) {
      fillInitialValues(referenceInfo.getInitializingReference(), actualInitValues);
    }

    Preconditions.checkState(actualInitValues.equals(expectedInitValues));

    // Check references
    List<Reference> refs = referenceInfo.references;

    // Check direct references
    for (int i = 0; i < refs.size(); i++) {
      Reference ref = refs.get(i);
      Node refNode = ref.getNode();

      Preconditions.checkState(refNode != null);

      if (refNode.isGetProp() && refNode.getLastChild().isName()) {
        String key = refNode.getLastChild().getString();

        if (!expectedInitValues.containsKey(key)) {
          throw new AssertionError("Unexpected reference to " + key);
        }

        Node expectedValue = expectedInitValues.get(key);
        Node actualValue = actualInitValues.get(key);

        Preconditions.checkState(expectedValue != null);
        Preconditions.checkState(actualValue != null);

        Preconditions.checkState(expectedValue.equals(actualValue));
      }
    }

    // Check indirect references
    for (int i = 0; i < nodes.size(); i++) {
      Node node = nodes.get(i);
      if (node.isGetProp()) {
        String key = node.getLastChild().getString();

        if (!expectedInitValues.containsKey(key)) {
          continue;
        }

        Node expectedValue = expectedInitValues.get(key);
        Node actualValue = node.getFirstChild();

        Preconditions.checkState(expectedValue != null);
        Preconditions.checkState(actualValue != null);

        Preconditions.checkState(expectedValue.equals(actualValue));
      }
    }

    // Check function calls
    for (int i = 0; i < nodes.size(); i++) {
      Node node = nodes.get(i);
      if (node.isCall() && node.getFirstChild() != null && node.getFirstChild().isName()) {
        String key = node.getFirstChild().getString();

        if (!expectedInitValues.containsKey(key)) {
          continue;
        }

        Var fnVarInScope = rootScope.getVar(key);

        Preconditions.checkState(fnVarInScope != null);

        List<Reference> refs = ReferenceCollectingCallback.getReferences(fnVarInScope);

        if (refs == null) {
          throw new AssertionError("Function " + key + " is not defined");
        }

        for (Reference ref : refs) {
          if (ref.isLvalue() && ref.getParent().isAssign()) {
            Node assignNode = ref.getParent();
            Node lhs = assignNode.getFirstChild();

            Preconditions.checkState(lhs.isName());
            Preconditions.checkState(lhs.getString().equals(key));

            Node rhs = assignNode.getLastChild();
            if (rhs != null && rhs.isObjectLit()) {
              for (Node child = rhs.getFirstChild(); child != null; child = child.getNext()) {
                if (child.isGetProp() && child.getLastChild().isName()) {
                  String propKey = child.getLastChild().getString();
                  Node expectedPropValue = expectedInitValues.get(propKey);
                  Node actualPropValue = child.getFirstChild();

                  Preconditions.checkState(expectedPropValue != null);
                  Preconditions.checkState(actualPropValue != null);

                  Preconditions.checkState(expectedPropValue.equals(actualPropValue));
                }
              }
            }
          }
        }
      }
    }
  }

  private void fillInitialValues(Reference init, Map<String, Node> initvals) {
    Node object = init.getAssignedValue();
    Preconditions.checkState(object.isObjectLit());

    for (Node key = object.getFirstChild(); key != null; key = key.getNext()) {
      String keyName = key.getString();
      initvals.put(keyName, key.removeFirstChild());
    }
  }
}