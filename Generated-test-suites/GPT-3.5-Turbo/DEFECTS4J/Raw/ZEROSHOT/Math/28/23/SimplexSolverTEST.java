import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.linear.SimplexSolver;

public class SimplexSolverTest {

  @Test
  public void testGetPivotColumn() {
    SimplexTableau tableau = new SimplexTableau();
    Integer pivotColumn = tableau.getPivotColumn();
    Assert.assertNull(pivotColumn);
  }

  @Test
  public void testGetPivotRow() {
    SimplexTableau tableau = new SimplexTableau();
    Integer pivotRow = tableau.getPivotRow(0);
    Assert.assertNull(pivotRow);
  }

  @Test(expected = UnboundedSolutionException.class)
  public void testDoIterationUnboundedSolutionException() throws MaxCountExceededException, UnboundedSolutionException {
    SimplexTableau tableau = new SimplexTableau();
    tableau.doIteration();
  }

  @Test
  public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 1);
    tableau.setEntry(0, 1, 1);
    tableau.setEntry(0, 2, 1);
    tableau.doIteration();
    Assert.assertEquals(0, tableau.getEntry(0, 0), 0.00001);
    Assert.assertEquals(0, tableau.getEntry(0, 1), 0.00001);
    Assert.assertEquals(0, tableau.getEntry(0, 2), 0.00001);
  }

  @Test(expected = NoFeasibleSolutionException.class)
  public void testSolvePhase1NoFeasibleSolutionException()
      throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
    SimplexSolver simplexSolver = new SimplexSolver();
    simplexSolver.solvePhase1();
  }

  @Test
  public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
    SimplexSolver simplexSolver = new SimplexSolver();
    simplexSolver.solvePhase1();
    SimplexTableau tableau = simplexSolver.getTableau();
    Assert.assertEquals(0, tableau.getEntry(0, tableau.getRhsOffset()), 0.00001);
  }

  @Test(expected = MaxCountExceededException.class)
  public void testDoOptimizeMaxCountExceededException()
      throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
    SimplexSolver simplexSolver = new SimplexSolver();
    simplexSolver.doOptimize();
  }

  @Test
  public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
    SimplexSolver simplexSolver = new SimplexSolver();
    PointValuePair pointValuePair = simplexSolver.doOptimize();
    Assert.assertNotNull(pointValuePair);
  }
}