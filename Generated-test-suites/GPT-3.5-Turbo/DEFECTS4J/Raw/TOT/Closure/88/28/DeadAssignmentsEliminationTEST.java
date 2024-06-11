import org.junit.Test;
import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testEnterScope_NoInnerFunction() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);

    // Act
    elimination.enterScope();

    // Assert
    // TODO - Add assertions
  }

  @Test
  public void testEnterScope_InnerFunctionExists() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);

    // Act
    elimination.enterScope();

    // Assert
    // TODO - Add assertions
  }

  @Test
  public void testEnterScope_NoRemovableAssigns() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);

    // Act
    elimination.enterScope();

    // Assert
    // TODO - Add assertions
  }

  @Test
  public void testExitScope() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);

    // Act
    elimination.exitScope();

    // Assert
    // TODO - Add assertions
  }

  @Test
  public void testVisit() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);

    // Act
    elimination.visit();

    // Assert
    // TODO - Add assertions
  }

  @Test
  public void testProcess() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);

    // Act
    elimination.process();

    // Assert
    // TODO - Add assertions
  }

  @Test
  public void testTryRemoveDeadAssignments() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);

    // Act
    elimination.tryRemoveDeadAssignments();

    // Assert
    // TODO - Add assertions
  }

  @Test
  public void testTryRemoveAssignment() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    FlowState<LiveVariableLattice> state = new FlowState<LiveVariableLattice>();

    // Act
    elimination.tryRemoveAssignment(t, n, state);

    // Assert
    // TODO - Add assertions
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node n = new Node();
    Node exprRoot = new Node();
    String variable = "";

    // Act
    boolean result = elimination.isVariableStillLiveWithinExpression(n, exprRoot, variable);

    // Assert
    // TODO - Add assertions
  }

  @Test
  public void testIsVariableReadBeforeKill() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node n = new Node();
    String variable = "";

    // Act
    DeadAssignmentsElimination.VariableLiveness state = elimination.isVariableReadBeforeKill(n, variable);

    // Assert
    // TODO - Add assertions
  }
}