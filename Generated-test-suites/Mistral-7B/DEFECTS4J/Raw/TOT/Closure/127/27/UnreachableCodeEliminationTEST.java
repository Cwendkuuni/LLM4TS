import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.jscomp.NodeUtil.createNode;
import static com.google.javascript.jscomp.NodeUtil.isFunction;
import static com.google.javascript.jscomp.NodeUtil.isScript;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;

public class UnreachableCodeEliminationTest extends CompilerTestCase {

  public UnreachableCodeEliminationTest() {
    super(new CompilerPass[] {
        new UnreachableCodeElimination(compiler, true),
        new UnreachableCodeElimination(compiler, false)
    });
  }

  public void testUnreachableCodeAfterReturn() {
    Node root = createNode("function foo() { return; alert('unreachable'); }");
    traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        assertEquals("Expected return statement", Node.RETURN, n.getType());
        assertNull("Expected alert statement to be removed", n.getNext());
      }
    });
  }

  public void testUnreachableCodeWithNoOpStatement() {
    Node root = createNode("function foo() { true; }");
    traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        assertEquals("Expected true statement to be removed", null, n);
      }
    });
  }

  public void testUnreachableCodeWithPrototypeProperty() {
    Node root = createNode("function foo() { return {}; }");
    Node propNode = createNode("a.b.MyClass.prototype.propertyName");
    Node statement = createNode("return {}", propNode);
    Node body = createNode("{}", statement);
    Node functionNode = createNode("function foo() { " + body.toString() + " }");
    traverse(compiler, functionNode, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        assertFalse("Expected prototype property to be kept", n.isRemoved());
      }
    });
  }

  public void testUnreachableCodeWithControlFlowGraph() {
    Node root = createNode("function foo() { if (true) { return; } if (false) { return; } }");
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    cfa.process(null, root);
    ControlFlowGraph<Node> cfg = cfa.getCfg();
    Node entryNode = cfg.getEntry().getValue();
    assertFalse("Expected entry node to be reachable", !GraphReachability.isReachable(entryNode, cfg));
    assertEquals("Expected number of nodes in CFG to be 3", 3, cfg.size());
  }

  public void testUnreachableCodeWithNestedFunctions() {
    Node root = createNode("function foo() {" +
        "function bar() { return; }" +
        "return bar(); }");
    traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        assertTrue("Expected function bar to be removed", isScript(n) || isFunction(n));
      }
    });
  }

  public void testUnreachableCodeWithBreakStatement() {
    Node root = createNode("function foo() {" +
        "for (var i = 0; i < 1; i++) {" +
        "  break;" +
        "}" +
        "return; }");
    traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        assertTrue("Expected break statement to be removed", n.isRemoved());
        assertEquals("Expected return statement to remain", Node.RETURN, n.getPrevious().getType());
      }
    });
  }

  public void testUnreachableCodeWithContinueStatement() {
    Node root = createNode("function foo() {" +
        "for (var i = 0; i < 1; i++) {" +
        "  continue;" +
        "}" +
        "return; }");
    traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        assertTrue("Expected continue statement to be removed", n.isRemoved());
        assertEquals("Expected return statement to remain", Node.RETURN, n.getPrevious().getType());
      }
    });
  }

  public void testUnreachableCodeWithFunctionWithEmptyBody() {
    Node root = createNode("function foo() {}");
    traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (isFunction(n)) {
          Node body = n.getLastChild();
          assertTrue("Expected empty function body to be removed", body.isRemoved());
          assertNull("Expected function to have no children", n.getLastChild());
        }
      }
    });
  }

  public void testUnreachableCodeWithFunctionWithSingleStatementBody() {
    Node root = createNode("function foo() { return 1; }");
    traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (isFunction(n)) {
          Node body = n.getLastChild();
          assertFalse("Expected single statement function body to remain", body.isRemoved());
          assertEquals("Expected single statement to remain", Node.RETURN, body.getType());
        }
      }
    });
  }

  public void testUnreachableCodeWithUnreachableCodePassTwice() {
    Node root = createNode("function foo() {" +
        "function bar() {" +
        "  return; }" +
        "return bar(); }");
    NodeTraversal.traverseChangedFunctions(compiler, new NodeTraversal.FunctionCallback() {
      @Override
      public void visit(AbstractCompiler compiler, Node root) {
        UnreachableCodeElimination pass1 = new UnreachableCodeElimination(compiler, true);
        pass1.process(null, root);
        UnreachableCodeElimination pass2 = new UnreachableCodeElimination(compiler, true);
        pass2.process(null, root);
      }
    });
    Node functionNode = NodeUtil.getFunctionNode(root, "foo");
    Node barFunctionNode = NodeUtil.getFunctionNode(functionNode, "bar");
    Node returnNode = NodeUtil.getReturnNode(functionNode);
    assertFalse("Expected function bar to be removed after first pass", isFunction(barFunctionNode));
    assertTrue("Expected function bar to be removed after second pass", barFunctionNode.isRemoved());
    assertTrue("Expected return statement to be removed", returnNode.isRemoved());
  }

  public void testUnreachableCodeWithUnreachableCodePassDifferentOrder() {
    Node root = createNode("function foo() {" +
        "function bar() {" +
        "  return; }" +
        "return bar(); }");
    NodeTraversal.traverseChangedFunctions(compiler, new NodeTraversal.FunctionCallback() {
      @Override
      public void visit(AbstractCompiler compiler, Node root) {
        UnreachableCodeElimination pass1 = new UnreachableCodeElimination(compiler, false);
        pass1.process(null, root);
        UnreachableCodeElimination pass2 = new UnreachableCodeElimination(compiler, true);
        pass2.process(null, root);
      }
    });
    Node functionNode = NodeUtil.getFunctionNode(root, "foo");
    Node barFunctionNode = NodeUtil.getFunctionNode(functionNode, "bar");
    Node returnNode = NodeUtil.getReturnNode(functionNode);
    assertFalse("Expected function bar to be removed after first pass", barFunctionNode.isRemoved());
    assertTrue("Expected function bar to be removed after second pass", barFunctionNode.isRemoved());
    assertTrue("Expected return statement to be removed", returnNode.isRemoved());
  }
}