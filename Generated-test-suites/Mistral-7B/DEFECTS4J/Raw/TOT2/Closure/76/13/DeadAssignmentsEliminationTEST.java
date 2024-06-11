import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.GraphUtil;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private Traversal traversal;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope scope;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    traversal = new NodeTraversal(compiler);
    controlFlowGraph = new ControlFlowGraph<>(compiler);
    scope = new Scope(null);
  }

  @Test
  public void testDeadAssignmentElimination() {
    String code = "function foo() {" +
        "  var a = 1;" +
        "  a = 2;" +
        "  return a;" +
        "}";

    Node root = AbstractCompiler.parseAndTraverse(code, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.process(null, root);

    // Check that dead assignments were removed
    Node fooFunction = NodeUtil.getFunctionDeclaration(root);
    Node aAssignment = NodeUtil.getFirstChildOfType(fooFunction, Node.ASSIGN);
    assertNull(aAssignment);

    // Check that function still returns correct result
    Node returnStatement = NodeUtil.getLastChildOfType(fooFunction, Node.RETURN);
    Node result = returnStatement.getFirstChild();
    assertEquals(2, result.getNumber().intValueExact());
  }

  @Test
  public void testDeadAssignmentEliminationWithConditionals() {
    String code = "function foo() {" +
        "  var a = 1;" +
        "  if (true) {" +
        "    a = 2;" +
        "  } else {" +
        "    a = 3;" +
        "  }" +
        "  return a;" +
        "}";

    Node root = AbstractCompiler.parseAndTraverse(code, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.process(null, root);

    // Check that dead assignments were removed
    Node fooFunction = NodeUtil.getFunctionDeclaration(root);
    List<Node> assignments = Arrays.asList(
        NodeUtil.getFirstChildOfType(fooFunction, Node.ASSIGN),
        NodeUtil.getLastChildOfType(fooFunction, Node.ASSIGN)
    );
    for (Node assignment : assignments) {
      assertNull(assignment);
    }

    // Check that function still returns correct result
    Node returnStatement = NodeUtil.getLastChildOfType(fooFunction, Node.RETURN);
    Node result = returnStatement.getFirstChild();
    assertEquals(1, result.getNumber().intValueExact());
  }

  @Test
  public void testDeadAssignmentEliminationInNestedScope() {
    String code = "function foo() {" +
        "  function bar() {" +
        "    var a = 1;" +
        "    a = 2;" +
        "  }" +
        "  bar();" +
        "  return 0;" +
        "}";

    Node root = AbstractCompiler.parseAndTraverse(code, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.process(null, root);

    // Check that dead assignments were removed
    Node fooFunction = NodeUtil.getFunctionDeclaration(root);
    FunctionDeclaration barFunction =
        NodeUtil.getFunctionDeclaration(
            NodeUtil.getFirstChildOfType(fooFunction, Node.FUNCTION));
    Node aAssignment = NodeUtil.getFirstChildOfType(barFunction, Node.ASSIGN);
    assertNull(aAssignment);

    // Check that function still returns correct result
    Node returnStatement = NodeUtil.getLastChildOfType(fooFunction, Node.RETURN);
    assertNull(returnStatement);
  }

  @Test(expected = IllegalStateException.class)
  public void testDeadAssignmentEliminationWithFunction() {
    String code = "function foo() {" +
        "  function bar() {" +
        "    return 1;" +
        "  }" +
        "  var a = bar();" +
        "  a = 2;" +
        "  return a;" +
        "}";

    Node root = AbstractCompiler.parseAndTraverse(code, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.process(null, root);

    // This assignment should not be removed because bar() is a function
    Node aAssignment = NodeUtil.getFirstChildOfType(root, Node.ASSIGN);
    assertNotNull(aAssignment);
  }

  @Test
  public void testDeadAssignmentEliminationWithIncrementDecrementOperator() {
    String code = "function foo() {" +
        "  var a = 1;" +
        "  a++;" +
        "  return a;" +
        "}";

    Node root = AbstractCompiler.parseAndTraverse(code, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.process(null, root);

    // Check that dead assignments were removed
    Node fooFunction = NodeUtil.getFunctionDeclaration(root);
    Node aAssignment = NodeUtil.getFirstChildOfType(fooFunction, Node.ASSIGN);
    assertNull(aAssignment);

    // Check that function still returns correct result
    Node returnStatement = NodeUtil.getLastChildOfType(fooFunction, Node.RETURN);
    Node result = returnStatement.getFirstChild();
    assertEquals(2, result.getNumber().intValueExact());
  }

  @Test
  public void testDeadAssignmentEliminationWithMultipleAssignments() {
    String code = "function foo() {" +
        "  var a = 1, b = 2;" +
        "  a = 3;" +
        "  b = 4;" +
        "  return a + b;" +
        "}";

    Node root = AbstractCompiler.parseAndTraverse(code, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.process(null, root);

    // Check that dead assignments were removed
    Node fooFunction = NodeUtil.getFunctionDeclaration(root);
    List<Node> assignments = Arrays.asList(
        NodeUtil.getFirstChildOfType(fooFunction, Node.ASSIGN),
        NodeUtil.getLastChildOfType(fooFunction, Node.ASSIGN)
    );
    for (Node assignment : assignments) {
      assertNull(assignment);
    }

    // Check that function still returns correct result
    Node returnStatement = NodeUtil.getLastChildOfType(fooFunction, Node.RETURN);
    Node sum = NodeUtil.getLastChildOfType(returnStatement, Node.BINARY_PLUS);
    Node a = sum.getItem(0);
    Node b = sum.getItem(1);
    assertEquals(5, sum.getInt());
    assertEquals(3, a.getNumber().intValueExact());
    assertEquals(2, b.getNumber().intValueExact());
  }

  @Test
  public void testDeadAssignmentEliminationWithMultipleFunctions() {
    String code = "function foo() {" +
        "  function bar() {" +
        "    var a = 1;" +
        "    a = 2;" +
        "  }" +
        "  function baz() {" +
        "    var b = 1;" +
        "    b = 3;" +
        "  }" +
        "  bar();" +
        "  baz();" +
        "  return 0;" +
        "}";

    Node root = AbstractCompiler.parseAndTraverse(code, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.process(null, root);

    // Check that dead assignments were removed
    Node fooFunction = NodeUtil.getFunctionDeclaration(root);
    List<FunctionDeclaration> functions = Arrays.asList(
        NodeUtil.getFunctionDeclaration(
            NodeUtil.getFirstChildOfType(fooFunction, Node.FUNCTION)),
        NodeUtil.getFunctionDeclaration(
            NodeUtil.getLastChildOfType(fooFunction, Node.FUNCTION))
    );
    for (FunctionDeclaration function : functions) {
      List<Node> assignments = Arrays.asList(
          NodeUtil.getFirstChildOfType(function, Node.ASSIGN)
      );
      for (Node assignment : assignments) {
        assertNull(assignment);
      }
    }

    // Check that functions still return correct result
    List<Node> functionCalls = Arrays.asList(
        NodeUtil.getLastChildOfType(fooFunction, Node.CALL),
        NodeUtil.getLastChildOfType(fooFunction, Node.CALL)
    );
    for (Node functionCall : functionCalls) {
      Node result = NodeUtil.getLastChildOfType(functionCall, Node.RETURN);
      assertEquals(0, result.getInt());
    }
  }

  private static void assertEquals(int expected, Node node) {
    assertEquals(expected, node.getNumber().intValueExact());
  }
}