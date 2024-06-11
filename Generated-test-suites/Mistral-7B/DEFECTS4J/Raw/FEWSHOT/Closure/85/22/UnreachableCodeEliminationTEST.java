package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphEdge;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test class for UnreachableCodeElimination class.
 */
public class UnreachableCodeEliminationTest {

  @Test
  public void testUnreachableCodeElimination() {
    // Test case 1: simple return statement
    Node root = createTestNode(Token.RETURN, null, null);
    AbstractCompiler compiler = createMockCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root));

    // Test case 2: return statement with children
    root = createTestNode(Token.RETURN, null, createTestNode(Token.BLOCK, null, null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild()));

    // Test case 3: unreachable empty statement
    root = createTestNode(Token.BLOCK, null, createTestNode(Token.EMPTY, null, null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild()));

    // Test case 4: unreachable block statement
    root = createTestNode(Token.BLOCK, null, null);
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root));

    // Test case 5: unreachable expression statement
    root = createTestNode(Token.EXPR_STATEMENT, null, createTestNode(Token.IDENT, "x", null));
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root));

    // Test case 6: unreachable break statement
    root = createTestNode(Token.BLOCK, null, createTestNode(Token.IF, null, null,
            createTestNode(Token.BLOCK, null, createTestNode(Token.BREAK, null, null), null), null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild().getFirstChild().getFirstChild()));

    // Test case 7: unreachable continue statement
    root = createTestNode(Token.BLOCK, null, createTestNode(Token.IF, null, null,
            createTestNode(Token.BLOCK, null, null,
                    createTestNode(Token.WHILE, null, null,
                            createTestNode(Token.BLOCK, null, null,
                                    createTestNode(Token.CONTINUE, null, null), null), null), null), null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild()));

    // Test case 8: unreachable return statement with conditions
    root = createTestNode(Token.IF, null, null, createTestNode(Token.BLOCK, null,
            createTestNode(Token.RETURN, null, null), null),
            createTestNode(Token.BLOCK, null, null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertFalse(isNodeRemoved(root.getFirstChild().getFirstChild()));

    // Test case 9: unreachable function call with no side effects
    root = createTestNode(Token.EXPR_STATEMENT, null,
            createTestNode(Token.MEMBER, null, createTestNode(Token.IDENT, "foo", null),
                    createTestNode(Token.IDENT, "bar", null)));
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertFalse(isNodeRemoved(root));

    // Test case 10: unreachable function call without arguments
    root = createTestNode(Token.EXPR_STATEMENT, null,
            createTestNode(Token.MEMBER, null, createTestNode(Token.IDENT, "foo", null), null));
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertFalse(isNodeRemoved(root));

    // Test case 11: unreachable function call with arguments
    root = createTestNode(Token.EXPR_STATEMENT, null,
            createTestNode(Token.MEMBER, null, createTestNode(Token.IDENT, "foo", null),
                    createTestNode(Token.ARRAY, null, createTestNode(Token.NUL, null),
                            createTestNode(Token.NUMBER, 42.0, null))));
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertFalse(isNodeRemoved(root));

    // Test case 12: unreachable function call with side effects
    root = createTestNode(Token.EXPR_STATEMENT, null,
            createTestNode(Token.ASSIGN, null, createTestNode(Token.IDENT, "x", null),
                    createTestNode(Token.MEMBER, null, createTestNode(Token.IDENT, "foo", null),
                            createTestNode(Token.IDENT, "bar", null))));
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertFalse(isNodeRemoved(root));

    // Test case 13: unreachable break statement in a loop
    root = createTestNode(Token.BLOCK, null, createTestNode(Token.WHILE, null, null,
            createTestNode(Token.BLOCK, null, createTestNode(Token.BREAK, null, null), null), null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild().getFirstChild().getFirstChild()));

    // Test case 14: unreachable continue statement in a loop
    root = createTestNode(Token.BLOCK, null, createTestNode(Token.WHILE, null, null,
            createTestNode(Token.BLOCK, null, null,
                    createTestNode(Token.CONTINUE, null, null), null), null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild().getFirstChild().getFirstChild().getFirstChild()));

    // Test case 15: unreachable return statement in a loop
    root = createTestNode(Token.BLOCK, null, createTestNode(Token.WHILE, null, null,
            createTestNode(Token.BLOCK, null, createTestNode(Token.RETURN, null, null), null), null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild().getFirstChild()));

    // Test case 16: unreachable break statement in a switch statement
    root = createTestNode(Token.BLOCK, null, createTestNode(Token.SWITCH, null, null,
            createTestNode(Token.CASE, null, null, createTestNode(Token.BREAK, null, null), null), null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild().getFirstChild().getFirstChild()));

    // Test case 17: unreachable continue statement in a switch statement
    root = createTestNode(Token.BLOCK, null, createTestNode(Token.SWITCH, null, null,
            createTestNode(Token.CASE, null, null, null,
                    createTestNode(Token.BLOCK, null,
                            createTestNode(Token.CONTINUE, null, null), null)), null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild().getFirstChild().getFirstChild().getFirstChild()));

    // Test case 18: unreachable return statement in a switch statement
    root = createTestNode(Token.BLOCK, null, createTestNode(Token.SWITCH, null, null,
            createTestNode(Token.CASE, null, null, null,
                    createTestNode(Token.BLOCK, null, createTestNode(Token.RETURN, null, null), null)), null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild().getFirstChild().getFirstChild()));

    // Test case 19: unreachable break statement in nested switch statement
    root = createTestNode(Token.BLOCK, null, createTestNode(Token.SWITCH, null, null,
            createTestNode(Token.CASE, null, null, null,
                    createTestNode(Token.SWITCH, null, null,
                            createTestNode(Token.CASE, null, null, createTestNode(Token.BREAK, null, null), null), null)), null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild().getFirstChild().getFirstChild().getFirstChild()));

    // Test case 20: unreachable continue statement in nested switch statement
    root = createTestNode(Token.BLOCK, null, createTestNode(Token.SWITCH, null, null,
            createTestNode(Token.CASE, null, null, null,
                    createTestNode(Token.SWITCH, null, null,
                            createTestNode(Token.CASE, null, null, null,
                                    createTestNode(Token.BLOCK, null,
                                            createTestNode(Token.CONTINUE, null, null), null)), null)), null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild()));

    // Test case 21: unreachable return statement in nested switch statement
    root = createTestNode(Token.BLOCK, null, createTestNode(Token.SWITCH, null, null,
            createTestNode(Token.CASE, null, null, null,
                    createTestNode(Token.SWITCH, null, null,
                            createTestNode(Token.CASE, null, null, null,
                                    createTestNode(Token.BLOCK, null, createTestNode(Token.RETURN, null, null), null)), null)), null));
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertTrue(isNodeRemoved(root.getFirstChild().getFirstChild().getFirstChild().getFirstChild()));
  }

  private Node createTestNode(int type, Node... children) {
    Node node = NodeFactory.createNode(type);
    for (Node child : children) {
      NodeUtil.addChild(node, child);
    }
    return node;
  }

  private AbstractCompiler createMockCompiler() {
    // Create a mock compiler to be passed to the UnreachableCodeElimination
    // instance. Since we don't need to use the actual compiler functionality,
    // returning a mock compiler should be sufficient.
    return new MockCompiler();
  }

  private boolean isNodeRemoved(Node node) {
    if (node == null) {
      return true;
    }
    return node.getParent() == null;
  }
}

class MockCompiler extends AbstractCompiler {
  @Override
  public void reportCodeChange() {}
}