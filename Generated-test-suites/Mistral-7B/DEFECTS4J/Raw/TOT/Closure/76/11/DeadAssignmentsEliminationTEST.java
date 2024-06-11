import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope scope;

  @Before
  public void setup() {
    compiler = new TestCompiler();
    nodeFactory = compiler.getFactory();
    controlFlowGraph = new ControlFlowGraph<Node>(compiler.getRootNode());
    scope = new Scope(null, null, nodeFactory);
  }

  @Test
  public void testDeadAssignmentRemoval() {
    String javaCode = "function foo() {" +
        "var x = 1, y = 2;" +
        "x = x;" + // Dead assignment
        "y = x;" +
        "return x + y;" +
        "}";

    compiler.compile(javaCode);
    Node root = compiler.getRootNode();

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.createNodeList(), root);

    NodeTraversal traversal = new NodeTraversal(root);
    controlFlowGraph.build(traversal);
    eliminator.tryRemoveDeadAssignments(traversal, controlFlowGraph);

    Node xAssignment = null;
    for (Node n = root; n != null; n = n.getNext()) {
      if (n.getType() == Token.ASSIGN && n.getString().equals("x =")) {
        xAssignment = n;
        break;
      }
    }

    // Check that x assignment was removed
    assertFalse(xAssignment == null);
    assertFalse(traversal.findNode(xAssignment) != null);
  }

  @Test
  public void testDeadAssignmentRemovalWithFunctionCall() {
    String javaCode = "function foo() {" +
        "var x = 1, y = 2;" +
        "x = x;" + // Dead assignment
        "y = foo();" + // Call to a function that returns a dead value
        "return x + y;" +
        "}";

    compiler.compile(javaCode);
    Node root = compiler.getRootNode();

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.createNodeList(), root);

    NodeTraversal traversal = new NodeTraversal(root);
    controlFlowGraph.build(traversal);
    eliminator.tryRemoveDeadAssignments(traversal, controlFlowGraph);

    Node xAssignment = null;
    for (Node n = root; n != null; n = n.getNext()) {
      if (n.getType() == Token.ASSIGN && n.getString().equals("x =")) {
        xAssignment = n;
        break;
      }
    }

    // Check that x assignment was removed
    assertFalse(xAssignment == null);
    assertFalse(traversal.findNode(xAssignment) != null);
  }

  @Test
  public void testDeadAssignmentRemovalWithReturnStatement() {
    String javaCode = "function foo() {" +
        "var x = 1, y = 2;" +
        "x = x;" + // Dead assignment
        "return x;" +
        "y = 3;" +
        "}";

    compiler.compile(javaCode);
    Node root = compiler.getRootNode();

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.createNodeList(), root);

    NodeTraversal traversal = new NodeTraversal(root);
    controlFlowGraph.build(traversal);
    eliminator.tryRemoveDeadAssignments(traversal, controlFlowGraph);

    Node xAssignment = null;
    for (Node n = root; n != null; n = n.getNext()) {
      if (n.getType() == Token.ASSIGN && n.getString().equals("x =")) {
        xAssignment = n;
        break;
      }
    }

    // Check that x assignment was removed
    assertFalse(xAssignment == null);
    assertFalse(traversal.findNode(xAssignment) != null);
  }

  @Test
  public void testDeadAssignmentRemovalWithMultipleAssignments() {
    String javaCode = "function foo() {" +
        "var x = 1, y = 2, z = 3;" +
        "x = x;" + // Dead assignment
        "y = x;" + // Dead assignment
        "z = y;" + // Dead assignment
        "return x + y + z;" +
        "}";

    compiler.compile(javaCode);
    Node root = compiler.getRootNode();

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.createNodeList(), root);

    NodeTraversal traversal = new NodeTraversal(root);
    controlFlowGraph.build(traversal);
    eliminator.tryRemoveDeadAssignments(traversal, controlFlowGraph);

    Node xAssignment = null, yAssignment = null, zAssignment = null;
    for (Node n = root; n != null; n = n.getNext()) {
      if (n.getType() == Token.ASSIGN && n.getString().equals("x =")) {
        xAssignment = n;
      }
      if (n.getType() == Token.ASSIGN && n.getString().equals("y =")) {
        yAssignment = n;
      }
      if (n.getType() == Token.ASSIGN && n.getString().equals("z =")) {
        zAssignment = n;
      }
    }

    // Check that all dead assignments were removed
    assertFalse(xAssignment == null);
    assertFalse(traversal.findNode(xAssignment) != null);
    assertFalse(yAssignment == null);
    assertFalse(traversal.findNode(yAssignment) != null);
    assertFalse(zAssignment == null);
    assertFalse(traversal.findNode(zAssignment) != null);
  }

  @Test
  public void testDeadAssignmentRemovalWithDeadVariable() {
    String javaCode = "function foo() {" +
        "var x = 1, dead_x = 2, y = 3;" +
        "x = x;" + // Dead assignment
        "dead_x = x;" + // Dead assignment to dead variable
        "y = y;" +
        "return x + y;" +
        "}";

    compiler.compile(javaCode);
    Node root = compiler.getRootNode();

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.createNodeList(), root);

    NodeTraversal traversal = new NodeTraversal(root);
    controlFlowGraph.build(traversal);
    eliminator.tryRemoveDeadAssignments(traversal, controlFlowGraph);

    Node xAssignment = null, deadxAssignment = null;
    for (Node n = root; n != null; n = n.getNext()) {
      if (n.getType() == Token.ASSIGN && n.getString().equals("x =")) {
        xAssignment = n;
      }
      if (n.getType() == Token.ASSIGN && n.getString().equals("dead_x =")) {
        deadxAssignment = n;
      }
    }

    // Check that dead x assignment was not removed
    assertFalse(deadxAssignment == null);
    assertTrue(traversal.findNode(deadxAssignment) != null);
    // Check that x assignment was removed
    assertFalse(xAssignment == null);
    assertFalse(traversal.findNode(xAssignment) != null);
  }

  @Test
  public void testDeadAssignmentRemovalWithLoop() {
    String javaCode = "function foo() {" +
        "var x = 0;" +
        "for (var i = 0; i < 10; i++) {" +
        "x = x;" + // Dead assignment
        "}" +
        "return x;" +
        "}";

    compiler.compile(javaCode);
    Node root = compiler.getRootNode();

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.createNodeList(), root);

    NodeTraversal traversal = new NodeTraversal(root);
    controlFlowGraph.build(traversal);
    eliminator.tryRemoveDeadAssignments(traversal, controlFlowGraph);

    Node xAssignment = null;
    for (Node n = root; n != null; n = n.getNext()) {
      if (n.getType() == Token.ASSIGN && n.getString().equals("x =")) {
        xAssignment = n;
        break;
      }
    }

    // Check that x assignment was removed
    assertFalse(xAssignment == null);
    assertFalse(traversal.findNode(xAssignment) != null);
  }
}