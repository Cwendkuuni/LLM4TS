import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    // Test case for getPivotColumn method
    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        tableau.setWidth(5); // Setting the width of tableau to 5

        // Set the entry values in the objective function row
        tableau.setEntry(0, 0, -1); 
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, -3);
        tableau.setEntry(0, 3, 4);
        tableau.setEntry(0, 4, -5);

        Integer pivotColumn = solver.getPivotColumn(tableau);

        Assert.assertEquals(Integer.valueOf(4), pivotColumn);
    }

    // Test case for getPivotRow method
    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        tableau.setHeight(5); // Setting the height of tableau to 5

        // Set the entry values in the tableau
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, -2);
        tableau.setEntry(2, 0, 3);
        tableau.setEntry(3, 0, -4);
        tableau.setEntry(4, 0, 5);

        // Set the entry values in the objective function row
        tableau.setEntry(0, 1, -1);
        tableau.setEntry(0, 2, 2);
        tableau.setEntry(0, 3, -3);
        tableau.setEntry(0, 4, 4);

        Integer pivotRow = solver.getPivotRow(0, tableau);

        Assert.assertEquals(Integer.valueOf(3), pivotRow);
    }

    // Test case for doIteration method
    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        tableau.setHeight(4); // Setting the height of tableau to 4
        tableau.setWidth(5); // Setting the width of tableau to 5

        // Set the entry values in the tableau
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, -2);
        tableau.setEntry(2, 0, 3);
        tableau.setEntry(3, 0, -4);
        tableau.setEntry(0, 1, -1);
        tableau.setEntry(0, 2, 2);
        tableau.setEntry(0, 3, -3);
        tableau.setEntry(0, 4, 4);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(2, 2, -1);
        tableau.setEntry(3, 4, 2);

        // Execute one iteration
        solver.doIteration(tableau);

        // Verify the changes in tableau after one iteration
        Assert.assertEquals(1.0, tableau.getEntry(0, 0), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(1, 0), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(2, 0), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(3, 0), 0.0);
        Assert.assertEquals(1.0, tableau.getEntry(0, 1), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(0, 2), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(0, 3), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(0, 4), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(1, 1), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(2, 2), 0.0);
        Assert.assertEquals(1.0, tableau.getEntry(3, 4), 0.0);
    }

    // Test case for isPhase1Solved method
    @Test
    public void testIsPhase1Solved() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        tableau.setWidth(5); // Setting the width of tableau to 5

        // Set the entry values in the objective function row
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 1);
        tableau.setEntry(0, 2, -1);
        tableau.setEntry(0, 3, 1);
        tableau.setEntry(0, 4, -1);

        boolean isPhase1Solved = solver.isPhase1Solved(tableau);

        Assert.assertTrue(isPhase1Solved);
    }

    // Test case for isOptimal method
    @Test
    public void testIsOptimal() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        tableau.setWidth(5); // Setting the width of tableau to 5

        // Set the entry values in the objective function row
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 1);
        tableau.setEntry(0, 2, -1);
        tableau.setEntry(0, 3, 1);
        tableau.setEntry(0, 4, -1);

        boolean isOptimal = solver.isOptimal(tableau);

        Assert.assertTrue(isOptimal);
    }

    // Test case for solvePhase1 method
    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        tableau.setWidth(5); // Setting the width of tableau to 5

        // Set the entry values in the tableau
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, -2);
        tableau.setEntry(2, 0, 3);
        tableau.setEntry(3, 0, -4);
        tableau.setEntry(0, 1, -1);
        tableau.setEntry(0, 2, 2);
        tableau.setEntry(0, 3, -3);
        tableau.setEntry(0, 4, 4);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(2, 2, -1);
        tableau.setEntry(3, 4, 2);

        try {
            solver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            Assert.fail("Exception should not be thrown");
        }

        Assert.assertEquals(1.0, tableau.getEntry(0, 0), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(1, 0), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(2, 0), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(3, 0), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(0, 1), 0.0);
        Assert.assertEquals(1.0, tableau.getEntry(0, 2), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(0, 3), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(0, 4), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(1, 1), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(2, 2), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(3, 4), 0.0);
    }

    // Test case for doOptimize method
    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        double[] f = {1, 2, 3, 4, 5};
        double[] constraints = {1, 2, 3, 4, 5};
        boolean goalType = true;
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;

        // Set the required values for tableau
        tableau.setF(f);
        tableau.setConstraints(constraints);
        tableau.setGoalType(goalType);
        tableau.setRestrictToNonNegative(restrictToNonNegative);
        tableau.setEpsilon(epsilon);

        try {
            solver.doOptimize();
        } catch (OptimizationException e) {
            Assert.fail("Exception should not be thrown");
        }

        RealPointValuePair solution = tableau.getSolution();

        Assert.assertNotNull(solution);
    }

}