import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

  SimplexSolver solver = new SimplexSolver();
  
  @Test
  public void testGetPivotColumn() {
    SimplexTableau tableau = new SimplexTableau(getFunction(), getConstraints(), getGoalType(), restrictToNonNegative(), epsilon, maxUlps);
    Integer pivotColumn = solver.getPivotColumn(tableau);
    assertNotNull(pivotColumn);
  }

  @Test
  public void testGetPivotRow() {
    SimplexTableau tableau = new SimplexTableau(getFunction(), getConstraints(), getGoalType(), restrictToNonNegative(), epsilon, maxUlps);
    Integer pivotColumn = solver.getPivotColumn(tableau);
    assertNotNull(pivotColumn);
    Integer pivotRow = solver.getPivotRow(tableau, pivotColumn);
    assertNotNull(pivotRow);
  }

  @Test
  public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
    SimplexTableau tableau = new SimplexTableau(getFunction(), getConstraints(), getGoalType(), restrictToNonNegative(), epsilon, maxUlps);
    solver.doIteration(tableau);
  }

  @Test
  public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
    SimplexTableau tableau = new SimplexTableau(getFunction(), getConstraints(), getGoalType(), restrictToNonNegative(), epsilon, maxUlps);
    solver.solvePhase1(tableau);
  }

  @Test
  public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
    solver.doOptimize();
  }
  
  // Helper methods and fields for building the test cases
  
  private double[] getFunction() {
    return new double[] { 1, 1, 1 };
  }
  
  private double[][] getConstraints() {
    return new double[][] {
      { 1, 0, 0 },
      { 0, 1, 0 },
      { 0, 0, 1 }
    };
  }
  
  private LinearObjectiveFunction getGoalType() {
    return new LinearObjectiveFunction(new double[] { 0, 0, 0 }, 0);
  }
  
  private boolean[] restrictToNonNegative() {
    return new boolean[] { true, true, true };
  }
  
  private double epsilon = 1.0e-6;
  private int maxUlps = 10;

}