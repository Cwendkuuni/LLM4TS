import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

   @Test
   public void testGetPivotColumn() {
      SimplexSolver solver = new SimplexSolver();
      SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

      // Typical Use Case: Test with a tableau that has multiple columns with negative coefficients in the objective function row
      Integer expected = 1;
      Integer actual = solver.getPivotColumn(tableau);
      assertEquals(expected, actual);

      // Edge Case: Test with a tableau that has no negative coefficients in the objective function row
      tableau.setEntry(0, 0, 1.0);
      tableau.setEntry(0, 1, 2.0);
      tableau.setEntry(0, 2, 3.0);
      expected = null;
      actual = solver.getPivotColumn(tableau);
      assertEquals(expected, actual);

      // Edge Case: Test with a tableau that has only one column with a negative coefficient in the objective function row
      tableau.setEntry(0, 0, -1.0);
      expected = 0;
      actual = solver.getPivotColumn(tableau);
      assertEquals(expected, actual);
   }

   @Test
   public void testGetPivotRow() {
      SimplexSolver solver = new SimplexSolver();
      SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

      // Typical Use Case: Test with a tableau that has multiple rows with positive ratios in the MRT
      int pivotColumn = 1;
      Integer expected = 0;
      Integer actual = solver.getPivotRow(pivotColumn, tableau);
      assertEquals(expected, actual);

      // Edge Case: Test with a tableau that has no rows with positive ratios in the MRT
      tableau.setEntry(1, 0, 0.0);
      tableau.setEntry(1, 1, 0.0);
      tableau.setEntry(1, 2, 0.0);
      tableau.setEntry(1, 3, 0.0);
      tableau.setEntry(2, 0, 0.0);
      tableau.setEntry(2, 1, 0.0);
      tableau.setEntry(2, 2, 0.0);
      tableau.setEntry(2, 3, 0.0);
      expected = null;
      actual = solver.getPivotRow(pivotColumn, tableau);
      assertEquals(expected, actual);

      // Edge Case: Test with a tableau that has only one row with a positive ratio in the MRT
      tableau.setEntry(1, 0, 1.0);
      tableau.setEntry(1, 1, 2.0);
      tableau.setEntry(1, 2, 3.0);
      tableau.setEntry(1, 3, 4.0);
      expected = 1;
      actual = solver.getPivotRow(pivotColumn, tableau);
      assertEquals(expected, actual);
   }

   @Test
   public void testDoIteration() {
      SimplexSolver solver = new SimplexSolver();
      SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

      // Typical Use Case: Test with a tableau that can be solved with one iteration
      tableau.setEntry(0, 0, 1.0);
      tableau.setEntry(0, 1, 0.0);
      tableau.setEntry(0, 2, -2.0);
      tableau.setEntry(1, 0, 0.0);
      tableau.setEntry(1, 1, 1.0);
      tableau.setEntry(1, 2, -1.0);
      tableau.setEntry(2, 0, 0.0);
      tableau.setEntry(2, 1, 0.0);
      tableau.setEntry(2, 2, -1.0);
      solver.doIteration(tableau);
      double expected = 1.0;
      double actual = tableau.getEntry(0, 0);
      assertEquals(expected, actual, 0.0001);

      // Edge Case: Test with a tableau that cannot be solved with one iteration
      tableau.setEntry(0, 0, -1.0);
      tableau.setEntry(0, 1, 0.0);
      tableau.setEntry(0, 2, -2.0);
      tableau.setEntry(1, 0, 0.0);
      tableau.setEntry(1, 1, 1.0);
      tableau.setEntry(1, 2, -1.0);
      tableau.setEntry(2, 0, 0.0);
      tableau.setEntry(2, 1, 0.0);
      tableau.setEntry(2, 2, -1.0);
      solver.doIteration(tableau);
      expected = 0.0;
      actual = tableau.getEntry(0, 0);
      assertEquals(expected, actual, 0.0001);

   }

   @Test
   public void testIsPhase1Solved() {
      SimplexSolver solver = new SimplexSolver();
      SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

      // Typical Use Case: Test with a tableau that has a zero value in the W column
      boolean expected = true;
      boolean actual = solver.isPhase1Solved(tableau);
      assertEquals(expected, actual);

      // Edge Case: Test with a tableau that has a non-zero value in the W column
      tableau.setEntry(0, 3, 2.0);
      expected = false;
      actual = solver.isPhase1Solved(tableau);
      assertEquals(expected, actual);

   }

   @Test
   public void testIsOptimal() {
      SimplexSolver solver = new SimplexSolver();
      SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

      // Typical Use Case: Test with a tableau that has all positive coefficients in the objective function row
      boolean expected = true;
      boolean actual = solver.isOptimal(tableau);
      assertEquals(expected, actual);

      // Edge Case: Test with a tableau that has a negative coefficient in the objective function row
      tableau.setEntry(0, 0, -1.0);
      expected = false;
      actual = solver.isOptimal(tableau);
      assertEquals(expected, actual);

   }

   @Test
   public void testSolvePhase1() {
      SimplexSolver solver = new SimplexSolver();
      SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

      // Typical Use Case: Test with a tableau that has a feasible solution
      solver.solvePhase1(tableau);
      boolean expected = true;
      boolean actual = solver.isOptimal(tableau);
      assertEquals(expected, actual);

      // Error Scenario: Test with a tableau that has no feasible solution
      tableau.setEntry(0, 0, 1.0);
      tableau.setEntry(0, 1, 1.0);
      tableau.setEntry(0, 2, 1.0);
      tableau.setEntry(0, 3, 1.0);
      expected = false;
      boolean actual = false;
      try {
          actual = solver.solvePhase1(tableau);
          actual = solver.isOptimal(tableau);
      } catch (NoFeasibleSolutionException e) {
          actual = false;
      }
      assertEquals(expected, actual);
   }

   @Test
   public void testDoOptimize() {
      SimplexSolver solver = new SimplexSolver();
      RealPointValuePair expected = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0);
      RealPointValuePair actual = solver.doOptimize();
      assertArrayEquals(expected.getPoint(), actual.getPoint(), 0.0001);
      assertEquals(expected.getValue(), actual.getValue(), 0.0001);
   }

}