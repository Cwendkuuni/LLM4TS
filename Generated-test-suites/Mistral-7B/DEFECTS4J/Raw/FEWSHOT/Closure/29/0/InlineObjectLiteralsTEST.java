package org.apache.commons.cli.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.NodeMatcher;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String testCode = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: 2,\n" +
        "    c: function() {}\n" +
        "  };\n" +
        "  var x = obj.a;\n" +
        "  var y = obj.b;\n" +
        "  obj.c();\n" +
        "  var z = obj.c;\n" +
        "}";

    Node root = AbstractCompiler.compile(testCode, null);

    Scope scope = NodeUtil.getFunctionScope(root);

    InlineObjectLiterals pass = new InlineObjectLiterals(
        null, NodeUtil.newNameIdSupplier());
    pass.process(null, root);

    NodeTraversal traversal = NodeTraversal.createWithRoot(root);
    traversal.traverse();

    // Check that variables have been inlined.
    for (Var var : scope.getVars()) {
      if (var.isGlobal() || var.isExtern()) {
        continue;
      }
      String variableName = "JSCompiler_object_inline_a_" + var.id;
      assertNotNull(NodeMatcher.match(traversal, new NodeMatcher() {
        @Override
        public boolean matches(Node node, String message) {
          return NodeUtil.isVar(node) &&
              NodeUtil.getName(node).equals(variableName);
        }
      }));

      // Check that the variable has the correct value.
      Node valueNode = NodeUtil.getVarValue(node, traversal);
      if (var.name.equals("a")) {
        assertEquals(1, NodeUtil.getIntValue(valueNode));
      } else if (var.name.equals("b")) {
        assertEquals(2, NodeUtil.getIntValue(valueNode));
      }
    }

    // Check that object properties have been inlined.
    for (Var var : scope.getVars()) {
      if (var.isGlobal() || var.isExtern()) {
        continue;
      }
      String variableName = "JSCompiler_object_inline_" + var.name + "_";
      Node varNode = NodeMatcher.match(traversal, new NodeMatcher() {
        @Override
        public boolean matches(Node node, String message) {
          return NodeUtil.isVar(node) &&
              NodeUtil.getName(node).startsWith(variableName);
        }
      }).get(0);

      Node objNode = NodeMatcher.match(traversal, new NodeMatcher() {
        @Override
        public boolean matches(Node node, String message) {
          return NodeUtil.isObjectLit(node);
        }
      }).get(0);

      for (Node keyNode : objNode.getChildren()) {
        String keyName = keyNode.getString();
        String variableValueName = variableName + keyName;
        Node valueNode = NodeMatcher.match(traversal, new NodeMatcher() {
          @Override
          public boolean matches(Node node, String message) {
            return NodeUtil.isVar(node) &&
                NodeUtil.getName(node).equals(variableValueName);
          }
        }).get(0);

        if (keyName.equals("a")) {
          assertEquals(1, NodeUtil.getIntValue(valueNode));
        } else if (keyName.equals("b")) {
          assertEquals(2, NodeUtil.getIntValue(valueNode));
        } else if (keyName.equals("c")) {
          assertNotNull(NodeMatcher.match(traversal, new NodeMatcher() {
            @Override
            public boolean matches(Node node, String message) {
              return NodeUtil.isFunction(node) &&
                  NodeUtil.getName(node.getFirstChild()).equals("c") &&
                  NodeUtil.getName(node.getFirstChild().getParent()).equals(variableName);
            }
          }));
        }
      }
    }

    // Check that functions are not inlined.
    Node functionNode = NodeMatcher.match(traversal, new NodeMatcher() {
      @Override
      public boolean matches(Node node, String message) {
        return NodeUtil.isFunction(node);
      }
    }).get(0);

    assertNull(NodeMatcher.match(traversal, new NodeMatcher() {
      @Override
      public boolean matches(Node node, String message) {
        return NodeUtil.isVar(node) && NodeUtil.getName(node).startsWith("JSCompiler_object_inline_c_");
      }
    }));
  }
}