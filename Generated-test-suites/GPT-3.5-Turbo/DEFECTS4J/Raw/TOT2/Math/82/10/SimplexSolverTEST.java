import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test when the most negative coefficient is in the last column
        assertEquals(tableau.getWidth() - 1, solver.getPivotColumn(tableau).intValue());

        // Test when the most negative coefficient is in the middle column
        assertEquals(2, solver.getPivotColumn(tableau).intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        int pivotCol = 2;

        // Test when the ratio of a row with positive entry
        assertEquals(1, solver.getPivotRow(pivotCol, tableau).intValue());

        // Test when the ratio of a row with negative entry
        assertNull(solver.getPivotRow(pivotRow, tableau));
    }

    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        assertThrows(OptimizationException.class, () -> {
            solver.doIteration(tableau);
        });
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        assertThrows(OptimizationException.class, () -> {
            solver.solvePhase1(tableau);
        });
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        
        // Test optimal solution when tableau has artificial variables
        double[] f = {1, 1};
        LinearConstraintSet constraints = new LinearConstraintSet();
        OptimizationType goalType = OptimizationType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0e-6;

        solver.setFunction(new LinearObjectiveFunction(f, 0));
        solver.setConstraints(constraints);
        solver.setGoalType(goalType);
        solver.setRestrictToNonNegative(restrictToNonNegative);
        solver.setEpsilon(epsilon);

        RealPointValuePair solution = solver.doOptimize();
        assertEquals(0.0, solution.getValue(), epsilon);
        assertArrayEquals(new double[] {0.0, 0.0}, solution.getPoint(), epsilon);
    }

}