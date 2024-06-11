import static com.google.javascript.jscomp.NodeUtil.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination elimination;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    elimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testSimpleDeadAssignment() {
    Node root = new Node(Token.PROGRAM, null);
    Node assign = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
    Node print = new Node(Token.CALL, new Node(Token.NAME, "print"), new Node(Token.NAME, "x"));
    root.appendChildren(assign, print);

    Scope scope = new Scope(root);
    NodeTraversal traversal = new NodeTraversal(compiler, root, scope);
    elimination.process(null, root);
    NodeTraversal.traverse(compiler, root, elimination);

    assertTrue("Dead assignment should have been eliminated", assign.isEmpty());
    assertFalse("Print statement should still be present", print.isEmpty());
  }

  @Test
  public void testDeadAssignmentInLoop() {
    Node root = new Node(Token.PROGRAM, null);
    Node assign = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
    Node forStmt = new Node(Token.FOR, new Node(Token.NAME, "i"), new Node(Token.NAME, "0"),
        new Node(Token.NAME, "1"), new Node(Token.NAME, "1"), new Node(Token.BLOCK, null));
    Node assignInLoop = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), assign);
    forStmt.appendChildren(assignInLoop);
    root.appendChildren(forStmt);

    Scope scope = new Scope(root);
    NodeTraversal traversal = new NodeTraversal(compiler, root, scope);
    elimination.process(null, root);
    NodeTraversal.traverse(compiler, root, elimination);

    assertTrue("Dead assignment should have been eliminated", assign.isEmpty());
    assertFalse("Assignment inside loop should still be present", assignInLoop.isEmpty());
  }

  @Test
  public void testDeadAssignmentInIf() {
    Node root = new Node(Token.PROGRAM, null);
    Node assign = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
    Node ifStmt = new Node(Token.IF, new Node(Token.NAME, "cond"), new Node(Token.BLOCK, null));
    Node assignInIf = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), assign);
    ifStmt.appendChildren(assignInIf);
    Node print = new Node(Token.CALL, new Node(Token.NAME, "print"), new Node(Token.NAME, "x"));
    root.appendChildren(assign, print, ifStmt, new Node(Token.NAME, "cond"));

    Scope scope = new Scope(root);
    NodeTraversal traversal = new NodeTraversal(compiler, root, scope);
    elimination.process(null, root);
    NodeTraversal.traverse(compiler, root, elimination);

    assertTrue("Dead assignment should have been eliminated", assign.isEmpty());
    assertFalse("Assignment inside if statement should still be present", assignInIf.isEmpty());
    assertTrue("Print statement should still be present", print.isEmpty());
  }

  @Test
  public void testDeadAssignmentInFunction() {
    Node root = new Node(Token.PROGRAM, null);
    Node function = new Node(Token.FUNCTION, null, null, new Node(Token.BLOCK, null));
    Node assign = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
    Node print = new Node(Token.CALL, new Node(Token.NAME, "print"), new Node(Token.NAME, "x"));
    function.appendChildren(assign, print);
    root.appendChildren(function);

    Scope scope = new Scope(root);
    NodeTraversal traversal = new NodeTraversal(compiler, root, scope);
    elimination.process(null, root);
    NodeTraversal.traverse(compiler, root, elimination);

    assertTrue("Dead assignment should have been eliminated", assign.isEmpty());
    assertFalse("Print statement should still be present", print.isEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullExterns() {
    Node root = new Node(Token.PROGRAM, null);
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    elimination.process(null, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRoot() {
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    elimination.process(null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullScope() {
    Node root = new Node(Token.PROGRAM, null);
    Scope scope = null;
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    elimination.process(null, root);
    NodeTraversal traversal = new NodeTraversal(compiler, root, scope);
    elimination.enterScope(traversal);
  }
}