import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.javascript.rhino.Node;

import junit.framework.JUnit4TestAdapter;

public class DeadAssignmentsEliminationTest extends JUnit4TestAdapter {

  private static final Predicate<Node> MATCH_ASSIGNMENTS =
      new Predicate<Node>() {
        @Override
        public boolean apply(Node node) {
          return NodeUtil.isAssignmentOp(node) ||
                 NodeUtil.isIncrementDecrementOperator(node);
        }
      };

  private static class TestDeadAssignmentsElimination implements
      CompilerPass, ScopedCallback {

    private final DeadAssignmentsElimination elimination;

    public TestDeadAssignmentsElimination(DeadAssignmentsElimination elimination) {
      this.elimination = elimination;
    }

    @Override
    public void process(Node externs, Node root) {
      elimination.process(externs, root);
    }

    @Override
    public void enterScope(NodeTraversal t) {
      elimination.enterScope(t);
    }

    @Override
    public void exitScope(NodeTraversal t) {
      elimination.exitScope(t);
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      elimination.visit(t, n, parent);
    }

    public void testDeadAssignmentsElimination() {
      Node root = new Node(Token.BLOCK, new Node(Token.EMPTY));

      Node assign1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
          new Node(Token.NUMBER, 1));
      Node assign2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
          new Node(Token.NUMBER, 2));
      Node assign3 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
          new Node(Token.NUMBER, 3));

      Node assignmentBlock = new Node(Token.BLOCK, new Node[] {assign1,
          assign2, assign3});
      root.setFirstChild(assignmentBlock);

      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination deadAssignmentsElimination =
          new DeadAssignmentsElimination(compiler);
      deadAssignmentsElimination.process(null, root);

      Node eliminationResult = root;

      // Test that the dead assignment has been eliminated.
      assertFalse(containsAssignment(eliminationResult, "x"));

      // Test that the remaining assignments are still present.
      assertTrue(containsAssignment(eliminationResult, "x"));
    }

    private boolean containsAssignment(Node node, String variable) {
      return NodeUtil.has(node, MATCH_ASSIGNMENTS,
          new Function<Node, Boolean>() {
            @Override
            public Boolean apply(Node n) {
              if (NodeUtil.isName(n.getFirstChild()) &&
                  n.getFirstChild().getString().equals(variable)) {
                return true;
              }
              return false;
            }
          });
    }
  }

  public static TestCase createTest(String testName) {
    return new TestCase(testName) {
      protected void setUp() throws Exception {
        AbstractCompiler compiler = new Compiler();
        DeadAssignmentsElimination elimination =
            new DeadAssignmentsElimination(compiler);
        DeadAssignmentsEliminationTest test = new DeadAssignmentsEliminationTest();
        test.elimination = elimination;
      }

      public void test_deadAssignmentsElimination() {
        test.elimination.testDeadAssignmentsElimination();
      }
    };
  }

  public static junit.framework.Test suite() {
    return new junit.framework.TestSuite(createTest("test_deadAssignmentsElimination"));
  }
}