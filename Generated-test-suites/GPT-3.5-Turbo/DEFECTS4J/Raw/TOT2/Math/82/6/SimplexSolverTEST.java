import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.linear.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, 2}, {3, 4}}, new double[]{5, 6}, GoalType.MAXIMIZE, true, 1e-6);
        Integer pivotCol = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(0), pivotCol);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, 2}, {3, 4}, {5, 6}}, new double[]{7, 8, 9}, GoalType.MAXIMIZE, true, 1e-6);
        Integer pivotRow = solver.getPivotRow(1, tableau);
        assertEquals(Integer.valueOf(0), pivotRow);
    }

    @Test
    public void testDoIteration() {
        double[][] coefficients = {{1, -2, 1, 1}, {-1, 1, 1, 0}, {0, -1, 1, -1}};
        double[] constants = {1, 1, 1};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = true;
        SimplexTableau tableau = new SimplexTableau(coefficients, constants, goalType, restrictToNonNegative, 1e-6);
        SimplexSolver solver = new SimplexSolver();
        assertDoesNotThrow(() -> solver.doIteration(tableau));
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        double[][] coefficients = {{1, -2, 1, 1}, {-1, 1, 1, 0}, {0, -1, 1, -1}};
        double[] constants = {1, 1, 1};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = true;
        SimplexTableau tableau = new SimplexTableau(coefficients, constants, goalType, restrictToNonNegative, 1e-6);
        assertDoesNotThrow(() -> solver.solvePhase1(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexSolver solver = new SimplexSolver();
        double[][] coefficients = {{1, -2, 1, 1}, {-1, 1, 1, 0}, {0, -1, 1, -1}};
        double[] constants = {1, 1, 1};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = true;
        SimplexTableau tableau = new SimplexTableau(coefficients, constants, goalType, restrictToNonNegative, 1e-6);
        assertTrue(solver.isOptimal(tableau));
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        double[][] coefficients = {{1, -2, 1, 1}, {-1, 1, 1, 0}, {0, -1, 1, -1}};
        double[] constants = {1, 1, 1};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = true;
        SimplexTableau tableau = new SimplexTableau(coefficients, constants, goalType, restrictToNonNegative, 1e-6);
        assertDoesNotThrow(() -> solver.doOptimize(tableau));
    }

}