package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  @Test
  public void testInlineObjectLiterals() {
    String source = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: function() {" +
        "      return 2;" +
        "    }," +
        "    c: [{x: 1}]" +
        "  };" +
        "  return obj.a;" +
        "}" +
        "";

    Node root = parse(source);
    AbstractCompiler compiler = newCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_" + System.nanoTime();

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    inlineObjectLiterals.process(null, root);

    Scope scope = getScopeAtRoot(root);
    assertNotNull(scope);

    // Check that variables have been expanded correctly
    for (Var var : scope.getVars()) {
      if (var.isGlobal()) continue;
      String name = var.name;
      Node node = var.node;
      if (name.startsWith("JSCompiler_object_inline_")) {
        Node value = node.getFirstChild();
        assertEquals(value.getType(), Token.NAME);
        String expectedVarName = name.substring(0, name.length() - "_".length());
        assertEquals(expectedVarName, value.getString());

        Node parent = node.getParent();
        if (parent.getType() == Token.ASSIGN) {
          Node parentOfParent = parent.getParent();
          assertEquals(parentOfParent.getType(), Token.EXPR_STATEMENT);
          Node objectLiteral = parentOfParent.getFirstChild();
          Iterator<Node> it = objectLiteral.getChildren().iterator();
          while (it.hasNext()) {
            Node child = it.next();
            if (child.getType() == Token.GETPROP && child.getLastChild().getString().equals(name)) {
              Node valueNode = child.getPreviousSibling();
              assertEquals(value, valueNode);
              break;
            }
          }
        }
      }
    }

    // Check that original object literal has been replaced with variables
    Node objNode = findNodeWithName(root, "obj");
    assertNull(objNode);

    // Check that functions are not expanded
    Node functionAssignmentNode = findNodeWithName(root, "b");
    assertNotNull(functionAssignmentNode);
    Node functionNode = functionAssignmentNode.getParent();
    assertEquals(Token.ASSIGN, functionNode.getType());
    assertEquals(Token.GETPROP, functionNode.getPreviousSibling().getType());
    assertEquals(Token.NAME, functionNode.getFirstChild().getType());
    assertEquals("b", functionNode.getFirstChild().getString());

    // Check that array is not expanded
    Node arrayAssignmentNode = findNodeWithName(root, "c");
    assertNotNull(arrayAssignmentNode);
    Node arrayNode = arrayAssignmentNode.getParent();
    assertEquals(Token.ASSIGN, arrayNode.getType());
    assertEquals(Token.GETPROP, arrayNode.getPreviousSibling().getType());
    assertEquals(Token.NAME, arrayNode.getFirstChild().getType());
    assertEquals("c", arrayNode.getFirstChild().getString());
  }

  private Node findNodeWithName(Node root, String name) {
    NodeUtil.visitPostOrder(root, new NodeUtil.Visitor() {
      @Override
      public void visit(Node node) {
        if (node.getType() == Token.NAME && node.getString().equals(name)) {
          return;
        }
      }
    }, NodeUtil.MATCH_NOT_FUNCTION);
    return null;
  }
}