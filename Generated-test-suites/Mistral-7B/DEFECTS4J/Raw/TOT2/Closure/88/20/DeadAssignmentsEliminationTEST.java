import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.GraphPredicate;
import com.google.javascript.jscomp.GraphTraversal;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TraversalType;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CompilerOptions options;

  @Before
  public void setup() {
    compiler = new CompilerOptions();
    options = new CompilerOptions();
    nodeFactory = compiler.nodeFactory();
  }

  @Test
  public void testBasic() {
    Node root = NodeUtil.literal(nodeFactory, "3;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testBasicWithAssignments() {
    Node root = NodeUtil.literal(nodeFactory, "x = 3; y = x;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testBasicWithCondition() {
    Node root = NodeUtil.literal(nodeFactory, "if (x = 3) { y = x; }");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testBasicWithLoop() {
    Node root = NodeUtil.literal(nodeFactory, "for (var x = 0; x < 3; x++) { x = x + 1; }");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithMultipleAssignments() {
    Node root = NodeUtil.literal(nodeFactory, "x = 3; x = 5;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithReturn() {
    Node root = NodeUtil.literal(nodeFactory, "return x = 3;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithIncrement() {
    Node root = NodeUtil.literal(nodeFactory, "x = 3; x++;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDecrement() {
    Node root = NodeUtil.literal(nodeFactory, "x = 3; x--;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithVariableDeclaration() {
    Node root = NodeUtil.literal(nodeFactory, "var x = 3; x = 5;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDeadAssignments() {
    Node root = NodeUtil.literal(nodeFactory, "var x = 3; x = x;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDeadAssignmentsInCondition() {
    Node root = NodeUtil.literal(nodeFactory, "if (x = 3) { x = x; }");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDeadAssignmentsInLoop() {
    Node root = NodeUtil.literal(nodeFactory, "for (var x = 0; x = x < 3; x++) { x = x; }");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDeadAssignmentsInFunctionCall() {
    Node root = NodeUtil.literal(nodeFactory, "f(x = 3);");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDeadAssignmentsInReturnStatement() {
    Node root = NodeUtil.literal(nodeFactory, "return x = 3;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDeadAssignmentsInDeclarationAndAssignment() {
    Node root = NodeUtil.literal(nodeFactory, "var x = 3; x = x;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDeadAssignmentsInAssignmentAndDeclaration() {
    Node root = NodeUtil.literal(nodeFactory, "x = 3; var x;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDeadAssignmentsInAssignmentAndAssignment() {
    Node root = NodeUtil.literal(nodeFactory, "x = 3; x = 5;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDeadAssignmentsInIfAndAssignment() {
    Node root = NodeUtil.literal(nodeFactory, "if (x = 3) { x = x; }");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDeadAssignmentsInForAndAssignment() {
    Node root = NodeUtil.literal(nodeFactory, "for (var x = 0; x = x < 3; x++) { x = x; }");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDeadAssignmentsInFunctionCallAndAssignment() {
    Node root = NodeUtil.literal(nodeFactory, "f(x = 3); x = 5;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test
  public void testWithDeadAssignmentsInReturnStatementAndAssignment() {
    Node root = NodeUtil.literal(nodeFactory, "return x = 3; x = 5;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);

    assertTrue(NodeUtil.isEmpty(root.getFirstChild()));
  }

  @Test(expected = AssertionError.class)
  public void testWithCircularDependency() {
    Node root = createCircularDependencyNode();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);
  }

  @Test(expected = AssertionError.class)
  public void testWithIdentifierMismatch() {
    Node root = NodeUtil.literal(nodeFactory, "x = 3; y = x;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);
  }

  @Test(expected = AssertionError.class)
  public void testWithInvalidAssignmentExpression() {
    Node root = NodeUtil.literal(nodeFactory, "1 = 2;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);
  }

  @Test(expected = AssertionError.class)
  public void testWithInvalidVariableName() {
    Node root = NodeUtil.literal(nodeFactory, "x = 3; y__ = 5;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);
  }

  @Test(expected = AssertionError.class)
  public void testWithInvalidReturnType() {
    Node root = NodeUtil.literal(nodeFactory, "return 3; x = 5;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);
  }

  @Test(expected = AssertionError.class)
  public void testWithIncompleteStatement() {
    Node root = NodeUtil.literal(nodeFactory, "return x;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.PRE);
  }

  private Node createCircularDependencyNode() {
    Node x = NodeUtil.newName(nodeFactory, "x");
    Node y = NodeUtil.newName(nodeFactory, "y");

    Node assignXToY = NodeUtil.newAssignment(nodeFactory, x, y);
    Node assignYToX = NodeUtil.newAssignment(nodeFactory, y, x);

    Node root = NodeUtil.newBlock(nodeFactory, ImmutableList.of(
        assignXToY, assignYToX));

    return root;
  }
}