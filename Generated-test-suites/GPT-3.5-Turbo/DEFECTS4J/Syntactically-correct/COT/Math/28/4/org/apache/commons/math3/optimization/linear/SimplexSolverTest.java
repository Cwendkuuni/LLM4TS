
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

  @Test
  public void testGetPivotColumn_ValidTableau_ReturnsColumnWithMostNegativeCoefficient() {
    SimplexSolver simplexSolver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();
    
    // Test case 1: tableau with all positive coefficients
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 2.0);
    tableau.setEntry(0, 2, 3.0);
    
    Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
    
    assertEquals(Integer.valueOf(0), pivotColumn);
    
    // Test case 2: tableau with negative coefficients
    tableau.setEntry(0, 0, -1.0);
    tableau.setEntry(0, 1, -2.0);
    tableau.setEntry(0, 2, -3.0);

    pivotColumn = simplexSolver.getPivotColumn(tableau);
    
    assertEquals(Integer.valueOf(2), pivotColumn);
  }

  @Test
  public void testGetPivotRow_ValidTableauAndColumn_ReturnsRowWithMinimumRatio() {
    SimplexSolver simplexSolver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();

    // Test case 1: tableau with positive ratios
    tableau.setEntry(1, 0, 2.0);
    tableau.setEntry(2, 0, 3.0);
    tableau.setEntry(3, 0, 4.0);

    Integer pivotRow = simplexSolver.getPivotRow(tableau, 0);

    assertEquals(Integer.valueOf(1), pivotRow);

    // Test case 2: tableau with negative ratios
    tableau.setEntry(1, 0, -2.0);
    tableau.setEntry(2, 0, -3.0);
    tableau.setEntry(3, 0, -4.0);

    pivotRow = simplexSolver.getPivotRow(tableau, 0);

    assertEquals(Integer.valueOf(3), pivotRow);
  }

  @Test
  public void testDoIteration_ValidTableau_PerformsOneIterationOfSimplexMethod() throws MaxCountExceededException, UnboundedSolutionException {
    SimplexSolver simplexSolver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();

    // Test case 1: tableau with pivot element
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 2.0);
    tableau.setEntry(0, 2, 3.0);
    tableau.setEntry(1, 0, 4.0);
    tableau.setEntry(2, 0, 5.0);

    simplexSolver.doIteration(tableau);

    // Check if pivot element is 1 and rest of the pivot column is 0
    assertEquals(1.0, tableau.getEntry(1, 0), 0.0);
    assertEquals(0.0, tableau.getEntry(2, 0), 0.0);

    // Test case 2: tableau without pivot element
    tableau.setEntry(0, 0, 0.0);
    tableau.setEntry(0, 1, 2.0);
    tableau.setEntry(0, 2, 3.0);
    tableau.setEntry(1, 0, 4.0);
    tableau.setEntry(2, 0, 5.0);

    simplexSolver.doIteration(tableau);

    // Check if pivot element is 1 and rest of the pivot column is not 0
    assertEquals(1.0, tableau.getEntry(1, 0), 0.0);
    assertTrue(tableau.getEntry(2, 0) != 0.0);
  }

  @Test
  public void testSolvePhase1_ValidTableau_SolvesPhase1OfSimplexMethod() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
    SimplexSolver simplexSolver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();

    // Test case 1: tableau with feasible solution
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 2.0);
    tableau.setEntry(0, 2, 3.0);

    simplexSolver.solvePhase1(tableau);

    // Check if tableau is optimal and has a feasible solution
    assertTrue(tableau.isOptimal());
    assertEquals(0, Double.compare(tableau.getEntry(0, tableau.getRhsOffset()), 0.0));

    // Test case 2: tableau without feasible solution
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 2.0);
    tableau.setEntry(0, 2, 3.0);
    tableau.setEntry(1, 0, -1.0);
    tableau.setEntry(1, 1, -2.0);
    tableau.setEntry(1, 2, -3.0);
    tableau.setEntry(1, tableau.getWidth() - 1, -4.0);

    try {
      simplexSolver.solvePhase1(tableau);
      fail("NoFeasibleSolutionException not thrown");
    } catch (NoFeasibleSolutionException e) {
      // Exception correctly thrown
    }
  }

  @Test
  public void testDoOptimize_ValidTableau_OptimizesTheSimplexMethod() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
    SimplexSolver simplexSolver = new SimplexSolver();

    // Test case 1: tableau with feasible solution
    SimplexTableau tableau1 = new SimplexTableau();
    tableau1.setEntry(0, 0, 1.0);
    tableau1.setEntry(0, 1, 2.0);
    tableau1.setEntry(0, 2, 3.0);

    PointValuePair solution1 = simplexSolver.doOptimize(tableau1);

    // Check if tableau is optimal and has a feasible solution
    assertTrue(tableau1.isOptimal());
    assertEquals(0, Double.compare(tableau1.getEntry(0, tableau.getRhsOffset()), 0.0));

    // Test case 2: tableau without feasible solution
    SimplexTableau tableau2 = new SimplexTableau();
    tableau2.setEntry(0, 0, 1.0);
    tableau2.setEntry(0, 1, 2.0);
    tableau2.setEntry(0, 2, 3.0);
    tableau2.setEntry(1, 0, -1.0);
    tableau2.setEntry(1, 1, -2.0);
    tableau2.setEntry(1, 2, -3.0);
    tableau2.setEntry(1, tableau2.getWidth() - 1, -4.0);

    try {
      simplexSolver.doOptimize(tableau2);
      fail("NoFeasibleSolutionException not thrown");
    } catch (NoFeasibleSolutionException e) {
      // Exception correctly thrown
    }
  }

}
