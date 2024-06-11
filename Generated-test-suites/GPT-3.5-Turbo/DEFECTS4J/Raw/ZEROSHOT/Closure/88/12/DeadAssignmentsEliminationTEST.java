import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {
  
  private DeadAssignmentsElimination elimination;
  
  @Before
  public void setUp() {
    elimination = new DeadAssignmentsElimination();
  }
  
  @Test
  public void processTest() {
    // implement test case for process method
  }
  
  @Test
  public void enterScopeTest() {
    // implement test case for enterScope method
  }
  
  @Test
  public void exitScopeTest() {
    // implement test case for exitScope method
  }
  
  @Test
  public void visitTest() {
    // implement test case for visit method
  }
  
  @Test
  public void tryRemoveDeadAssignmentsTest() {
    // implement test case for tryRemoveDeadAssignments method
  }
  
  @Test
  public void tryRemoveAssignmentTest() {
    // implement test case for tryRemoveAssignment method
  }
  
  @Test
  public void isVariableStillLiveWithinExpressionTest() {
    // implement test case for isVariableStillLiveWithinExpression method
  }
  
  @Test
  public void isVariableReadBeforeKillTest() {
    // implement test case for isVariableReadBeforeKill method
  }
}