import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.NodeTraversal.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    nodeFactory = compiler.nodeFactory();
  }

  @Test
  public void testDeadAssignmentsElimination() {
    String input = "function foo() {" +
        "  var a = 1, b = 2, c = 3;" +
        "  if (a > 1) {" +
        "    a = 5;" +
        "  } else {" +
        "    b = 4;" +
        "  }" +
        "  c = a + b;" +
        "  return c;" +
        "}";

    Node root = parser(input, null, null).getRootNode();
    Traverser traverser = new Traverser(new DeadAssignmentsElimination(compiler));
    NodeTraversal t = new NodeTraversal(compiler, traverser);
    traverser.traverse(root);
    Graph graph = compiler.getGraph();
    assertEquals(2, graph.numNodes());

    ControlFlowGraph cfg = graph.getControlFlowGraph();
    for (Node node : cfg.getNodes()) {
      if (node.hasAnnotation()) {
        FlowState<LiveVariableLattice> state = node.getAnnotation();
        String varName = "";
        if (node.getType() == Token.IF || node.getType() == Token.WHILE) {
          varName = NodeUtil.getConditionExpression(node).getString();
        } else if (node.hasChildren()) {
          varName = node.getFirstChild().getString();
        }
        if (!varName.isEmpty()) {
          LiveVariableLattice lattice = state.getOut();
          assertFalse(lattice.isLive(varName));
        }
      }
    }
  }

  @Test
  public void testDeadAssignmentsWithFunction() {
    String input = "function foo() {" +
        "  function bar() {" +
        "    var a = 1, b = 2;" +
        "    a = 3;" + // Dead assignment
        "  }" +
        "  bar();" +
        "}" +
        "foo();";

    Node root = parser(input, null, null).getRootNode();
    Traverser traverser = new Traverser(new DeadAssignmentsElimination(compiler));
    NodeTraversal t = new NodeTraversal(compiler, traverser);
    traverser.traverse(root);
    Graph graph = compiler.getGraph();
    assertEquals(3, graph.numNodes());

    ControlFlowGraph cfg = graph.getControlFlowGraph();
    for (Node node : cfg.getNodes()) {
      FlowState<LiveVariableLattice> state = node.getAnnotation();

      if (node.getType() == Token.FUNCTION &&
          node.hasChildren() &&
          node.getFirstChild().getType() == Token.NAME &&
          node.getFirstChild().getString().equals("bar")) {
        LiveVariableLattice lattice = state.getOut();
        assertFalse(lattice.isLive("a"));
      }
    }
  }

  @Test
  public void testDeadAssignmentsWithReturnStatement() {
    String input = "function foo() {" +
        "  var a = 1, b = 2;" +
        "  if (true) {" +
        "    return a = 3;" + // Dead assignment
        "  }" +
        "  return b;" +
        "}";

    Node root = parser(input, null, null).getRootNode();
    Traverser traverser = new Traverser(new DeadAssignmentsElimination(compiler));
    NodeTraversal t = new NodeTraversal(compiler, traverser);
    traverser.traverse(root);
    Graph graph = compiler.getGraph();
    assertEquals(2, graph.numNodes());

    ControlFlowGraph cfg = graph.getControlFlowGraph();
    for (Node node : cfg.getNodes()) {
      if (node.getType() == Token.RETURN) {
        FlowState<LiveVariableLattice> state = node.getAnnotation();
        LiveVariableLattice lattice = state.getOut();
        assertFalse(lattice.isLive("a"));
      }
    }
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentsWithNotRemovableAssignment() {
    String input = "function foo() {" +
        "  var a = 1;" +
        "  a = a;" + // Not removable assignment
        "}" +
        "foo();";

    Node root = parser(input, null, null).getRootNode();
    Traverser traverser = new Traverser(new DeadAssignmentsElimination(compiler));
    NodeTraversal t = new NodeTraversal(compiler, traverser);
    traverser.traverse(root);
  }

  @Test
  public void testDeadAssignmentsWithVarStatement() {
    String input = "function foo() {" +
        "  var a = 1, b = 2;" +
        "  var a = 3;" + // Dead assignment, but this is not removed because it's a var declaration
        "}" +
        "foo();";

    Node root = parser(input, null, null).getRootNode();
    Traverser traverser = new Traverser(new DeadAssignmentsElimination(compiler));
    NodeTraversal t = new NodeTraversal(compiler, traverser);
    traverser.traverse(root);
    Graph graph = compiler.getGraph();
    assertEquals(2, graph.numNodes());

    ControlFlowGraph cfg = graph.getControlFlowGraph();
    for (Node node : cfg.getNodes()) {
      if (node.getType() == Token.VAR) {
        Scope scope = node.getScope();
        Var var = scope.getVar("a");
        LiveVariableLattice lattice = ((FlowState<LiveVariableLattice>) node.getAnnotation())
            .getOut();
        assertTrue(lattice.isLive(var.name));
      }
    }
  }

  @Test
  public void testDeadAssignmentsWithDeepNests() {
    String input = "function foo() {" +
        "  function bar() {" +
        "    function baz() {" +
        "      var a = 1;" +
        "      if (true) {" +
        "        a = 2;" + // Dead assignment
        "      }" +
        "    }" +
        "    baz();" +
        "  }" +
        "  bar();" +
        "}" +
        "foo();";

    Node root = parser(input, null, null).getRootNode();
    Traverser traverser = new Traverser(new DeadAssignmentsElimination(compiler));
    NodeTraversal t = new NodeTraversal(compiler, traverser);
    traverser.traverse(root);
    Graph graph = compiler.getGraph();
    assertEquals(4, graph.numNodes());

    ControlFlowGraph cfg = graph.getControlFlowGraph();
    for (Node node : cfg.getNodes()) {
      if (node.getType() == Token.FUNCTION &&
          node.hasChildren() &&
          node.getFirstChild().getType() == Token.NAME &&
          node.getFirstChild().getString().equals("baz")) {
        FlowState<LiveVariableLattice> state = node.getAnnotation();
        LiveVariableLattice lattice = state.getOut();
        assertFalse(lattice.isLive("a"));
      }
    }
  }

  @Test
  public void testDeadAssignmentsWithConditionExpression() {
    String input = "function foo() {" +
        "  var a = 1;" +
        "  if (a = 2) {" + // Dead assignment, but it's inside a condition expression
        "    return true;" +
        "  }" +
        "  return false;" +
        "}" +
        "foo();";

    Node root = parser(input, null, null).getRootNode();
    Traverser traverser = new Traverser(new DeadAssignmentsElimination(compiler));
    NodeTraversal t = new NodeTraversal(compiler, traverser);
    traverser.traverse(root);
    Graph graph = compiler.getGraph();
    assertEquals(2, graph.numNodes());

    ControlFlowGraph cfg = graph.getControlFlowGraph();
    for (Node node : cfg.getNodes()) {
      if (node.getType() == Token.IF) {
        FlowState<LiveVariableLattice> state = node.getAnnotation();
        LiveVariableLattice lattice = state.getOut();
        assertTrue(lattice.isLive("a"));
      }
    }
  }

  @Test
  public void testDeadAssignmentsWithReturnStatementInsideIf() {
    String input = "function foo() {" +
        "  var a = 1;" +
        "  if (true) {" +
        "    return a = 2;" + // Dead assignment
        "  } else {" +
        "    return 42;" +
        "  }" +
        "}" +
        "foo();";

    Node root = parser(input, null, null).getRootNode();
    Traverser traverser = new Traverser(new DeadAssignmentsElimination(compiler));
    NodeTraversal t = new NodeTraversal(compiler, traverser);
    traverser.traverse(root);
    Graph graph = compiler.getGraph();
    assertEquals(2, graph.numNodes());

    ControlFlowGraph cfg = graph.getControlFlowGraph();
    for (Node node : cfg.getNodes()) {
      if (node.getType() == Token.RETURN &&
          node.getParent().getType() == Token.IF) {
        FlowState<LiveVariableLattice> state = node.getAnnotation();
        LiveVariableLattice lattice = state.getOut();
        assertFalse(lattice.isLive("a"));
      }
    }
  }

  @Test
  public void testDeadAssignmentsWithDeadVariable() {
    String input = "function foo() {" +
        "  var dead = 1;" +
        "  var a = 1;" +
        "  a = dead;" + // Dead assignment, but 'dead' is dead already
        "}" +
        "foo();";

    Node root = parser(input, null, null).getRootNode();
    Traverser traverser = new Traverser(new DeadAssignmentsElimination(compiler));
    NodeTraversal t = new NodeTraversal(compiler, traverser);
    traverser.traverse(root);
    Graph graph = compiler.getGraph();
    assertEquals(2, graph.numNodes());

    ControlFlowGraph cfg = graph.getControlFlowGraph();
    for (Node node : cfg.getNodes()) {
      if (node.getType() == Token.VAR && node.getString().equals("dead")) {
        LiveVariableLattice lattice = ((FlowState<LiveVariableLattice>) node.getAnnotation())
            .getIn().getLattice();
        assertTrue(lattice.isDead());
      }
    }
  }
}