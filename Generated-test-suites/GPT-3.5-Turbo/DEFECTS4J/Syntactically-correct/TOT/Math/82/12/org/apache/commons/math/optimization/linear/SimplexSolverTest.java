import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    // Test getPivotColumn() method
    @Test
    public void testGetPivotColumn() {
        // Creating a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MINIMIZE, false, 1.0e-6);
        
        // Adding entries to tableau
        tableau.setEntry(0, 0, 4);
        tableau.setEntry(0, 1, -1);
        tableau.setEntry(0, 2, -7);
        
        SimplexSolver solver = new SimplexSolver();
        Integer pivotColumn = solver.getPivotColumn(tableau);
        
        // Expected pivot column is 2 (with the most negative coefficient)
        Assert.assertEquals((Integer) 2, pivotColumn);
    }

    // Test getPivotRow() method
    @Test
    public void testGetPivotRow() {
        // Creating a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MINIMIZE, false, 1.0e-6);
        
        // Adding entries to tableau
        tableau.setEntry(0, 0, 2);
        tableau.setEntry(1, 0, 3);
        tableau.setEntry(2, 0, 1);
        
        tableau.setEntry(0, 1, 4);
        tableau.setEntry(1, 1, -1);
        tableau.setEntry(2, 1, 5);
        
        SimplexSolver solver = new SimplexSolver();
        Integer pivotRow = solver.getPivotRow(1, tableau);
        
        // Expected pivot row is 1 (with the minimum ratio)
        Assert.assertEquals((Integer) 1, pivotRow);
    }

    // Test doIteration() method
    @Test
    public void testDoIteration() throws OptimizationException {
        // Creating a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MINIMIZE, false, 1.0e-6);
        
        // Adding entries to tableau
        tableau.setEntry(0, 0, 2);
        tableau.setEntry(1, 0, 3);
        tableau.setEntry(0, 1, 4);
        tableau.setEntry(1, 1, -1);
        
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        
        // After one iteration, the tableau should be updated
        // Assert the expected values
        Assert.assertEquals(1.0, tableau.getEntry(0, 0), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(1, 0), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(0, 1), 0.0);
        Assert.assertEquals(1.0, tableau.getEntry(1, 1), 0.0);
    }

    // Test isPhase1Solved() method
    @Test
    public void testIsPhase1Solved() {
        // Creating a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MINIMIZE, false, 1.0e-6);
        
        // Adding entries to tableau
        tableau.setEntry(0, 0, 2);
        tableau.setEntry(1, 0, 3);
        
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(1, 1, 0);
        
        tableau.setEntry(0, 2, 0);
        tableau.setEntry(1, 2, -1);
        
        SimplexSolver solver = new SimplexSolver();
        boolean isPhase1Solved = solver.isPhase1Solved(tableau);
        
        // Expected result is false as Phase 1 is not solved
        Assert.assertFalse(isPhase1Solved);
    }

    // Test isOptimal() method
    @Test
    public void testIsOptimal() {
        // Creating a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MINIMIZE, false, 1.0e-6);
        
        // Adding entries to tableau
        tableau.setEntry(0, 0, -2);
        tableau.setEntry(1, 0, -3);
        
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(1, 1, 0);
        
        tableau.setEntry(0, 2, 0);
        tableau.setEntry(1, 2, -1);
        
        SimplexSolver solver = new SimplexSolver();
        boolean isOptimal = solver.isOptimal(tableau);
        
        // Expected result is true as the model has been solved
        Assert.assertTrue(isOptimal);
    }

    // Test solvePhase1() method
    @Test
    public void testSolvePhase1() throws OptimizationException {
        // Creating a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MINIMIZE, false, 1.0e-6);
        
        // Adding entries to tableau
        tableau.setEntry(0, 0, -2);
        tableau.setEntry(1, 0, -3);
        
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(1, 1, 0);
        
        tableau.setEntry(0, 2, 0);
        tableau.setEntry(1, 2, -1);
        
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        
        // After solving Phase 1, the tableau should be updated
        // Assert the expected values
        Assert.assertEquals(1.0, tableau.getEntry(0, 0), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(1, 0), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(0, 1), 0.0);
        Assert.assertEquals(1.0, tableau.getEntry(1, 1), 0.0);
    }

    // Test doOptimize() method
    @Test
    public void testDoOptimize() throws OptimizationException {
        // Creating a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MINIMIZE, false, 1.0e-6);
        
        // Adding entries to tableau
        tableau.setEntry(0, 0, -2);
        tableau.setEntry(1, 0, -3);
        
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(1, 1, 0);
        
        tableau.setEntry(0, 2, 0);
        tableau.setEntry(1, 2, -1);
        
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize();
        
        // After optimizing, the solution should be obtained
        // Assert the expected values
        Assert.assertArrayEquals(new double[] {0.0, 0.0, 0.0}, solution.getPoint(), 0.0);
        Assert.assertEquals(0.0, solution.getValue(), 0.0);
    }

}