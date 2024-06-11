import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.traverse.AbstractPostOrderCallback;
import com.google.javascript.jscomp.traverse.ScopedCallback;

public class UnreachableCodeEliminationTest {
  private Compiler compiler;

  @Before
  public void setup() {
    compiler = new Compiler();
  }

  @Test
  public void testUnreachableCodeElimination_Empty() {
    // Test with an empty script.
    Node root = Node.EMPTY;
    traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        assertNull(n);
      }
    }, new ScopedCallback() {
      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}
    });
  }

  @Test
  public void testUnreachableCodeElimination_Basic() {
    // Test with a simple script.
    String script = "function foo() { return; alert('unreachable'); }";
    Node root = Node.jsonParse(script);
    traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        assertNotNull(n);
        switch (n.getType()) {
          case Token.FUNCTION:
            assertNotNull(n.getChild(0).getValue());
            assertNotNull(n.getChild(1));
            assertNull(n.getChild(2));
            break;
          case Token.ALERT:
            assertNull(n.getParent());
            break;
          default:
            break;
        }
      }
    }, new ScopedCallback() {
      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}
    });
  }

  @Test
  public void testUnreachableCodeElimination_ReturnAfterFunction() {
    // Test with a script where a return statement comes after a function.
    String script = "function foo() {} return;";
    Node root = Node.jsonParse(script);
    traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        assertNotNull(n);
        switch (n.getType()) {
          case Token.FUNCTION:
            assertNotNull(n.getChild(0).getValue());
            assertNotNull(n.getChild(1));
            assertNull(n.getChild(2));
            break;
          case Token.RETURN:
            assertNull(n.getParent());
            break;
          default:
            break;
        }
      }
    }, new ScopedCallback() {
      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}
    });
  }

  @Test
  public void testUnreachableCodeElimination_NoOpStatements() {
    // Test with a script having no-op statements.
    String script = "function foo() { a.b.MyClass.prototype.propertyName; true; }";
    Node root = Node.jsonParse(script);
    traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        assertNotNull(n);
        switch (n.getType()) {
          case Token.FUNCTION:
            List<Node> functionChildren = NodeUtil.getDescendantsOfType(n, Token.ASSIGN);
            assertNotNull(functionChildren);
            assertFalse(functionChildren.isEmpty());
            for (Node child : functionChildren) {
              assertNull(child.getNext());
              assertNull(child.getNextSibling());
            }
            break;
          default:
            break;
        }
      }
    }, new ScopedCallback() {
      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}
    });
  }

  @Test
  public void testUnreachableCodeElimination_UnconditionalBranching() {
    // Test with a script having unconditional branching.
    String script = "function foo() {"
        + "if (true) {"
        + "  break; // unconditional break"
        + "}"
        + "return; // should not be removed"
        + "}";
    Node root = Node.jsonParse(script);
    traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        assertNotNull(n);
        switch (n.getType()) {
          case Token.FUNCTION:
            List<Node> functionChildren = NodeUtil.getDescendantsOfType(n, Token.BREAK);
            assertNotNull(functionChildren);
            assertFalse(functionChildren.isEmpty());
            Node breakNode = functionChildren.get(0);
            Node target = tryRemoveUnconditionalBranching(breakNode, root);
            assertNotNull(target);
            assertNotNull(target.getParent());
            assertNotNull(target.getParent().getNext());
            break;
          default:
            break;
        }
      }
    }, new ScopedCallback() {
      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}

      private Node tryRemoveUnconditionalBranching(Node branchNode, Node root) {
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
        elimination.process(root, root);
        DiGraphNode<Node, Branch> gNode = elimination.curCfg.getDirectedGraphNode(branchNode);
        return gNode.getDestination().getValue();
      }
    });
  }

  @Test
  public void testUnreachableCodeElimination_ConditionalBranching() {
    // Test with a script having conditional branching.
    String script = "function foo() {"
        + "if (true) {"
        + "  return;"
        + "} else {"
        + "  return;"
        + "}"
        + "}";
    Node root = Node.jsonParse(script);
    traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        assertNotNull(n);
        switch (n.getType()) {
          case Token.FUNCTION:
            List<Node> functionChildren = NodeUtil.getDescendantsOfType(n, Token.RETURN);
            assertNotNull(functionChildren);
            assertFalse(functionChildren.isEmpty());
            Node returnNode1 = functionChildren.get(0);
            Node returnNode2 = functionChildren.get(1);
            assertNotNull(returnNode1.getParent());
            assertNotNull(returnNode1.getParent().getPrevious());
            assertNotNull(returnNode2.getParent());
            assertNotNull(returnNode2.getParent().getPrevious());
            assertNotSame(returnNode1, returnNode2);
            break;
          default:
            break;
        }
      }
    }, new ScopedCallback() {
      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}
    });
  }

  @Test
  public void testUnreachableCodeElimination_ComplexConditionalBranching() {
    // Test with a script having complex conditional branching.
    String script = "function foo() {"
        + "if (x) {"
        + "  if (y) {"
        + "    return;"
        + "  } else {"
        + "    return;"
        + "  }"
        + "} else {"
        + "  return;"
        + "}"
        + "}";
    Node root = Node.jsonParse(script);
    traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        assertNotNull(n);
        switch (n.getType()) {
          case Token.FUNCTION:
            List<Node> functionChildren = NodeUtil.getDescendantsOfType(n, Token.RETURN);
            assertNotNull(functionChildren);
            assertFalse(functionChildren.isEmpty());
            Node returnNode1 = functionChildren.get(0);
            Node returnNode2 = functionChildren.get(1);
            Node returnNode3 = functionChildren.get(2);
            assertNotNull(returnNode1.getParent());
            assertNotNull(returnNode1.getParent().getPrevious());
            assertNotNull(returnNode2.getParent());
            assertNotNull(returnNode2.getParent().getPrevious());
            assertNotNull(returnNode3.getParent());
            assertNotNull(returnNode3.getParent().getPrevious());
            assertNotSame(returnNode1, returnNode2);
            assertNotSame(returnNode1, returnNode3);
            assertNotSame(returnNode2, returnNode3);
            break;
          default:
            break;
        }
      }
    }, new ScopedCallback() {
      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}
    });
  }
}