import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertNotNull(solver.epsilon);
        assertEquals(1.0e-6, solver.epsilon, 0.0);
    }

    @Test
    public void testCustomConstructor() {
        SimplexSolver solver = new SimplexSolver(0.001);
        assertNotSame(1.0e-6, solver.epsilon);
        assertEquals(0.001, solver.epsilon, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPivotColumnNullTableau() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = null;
        solver.getPivotColumn(tableau);
    }

    @Test
    public void testGetPivotColumn() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1, -2, -3}, 0);
        LinearConstraint[] constraints = {
                new LinearConstraint(new double[]{1, 1}, 0),
                new LinearConstraint(new double[]{1, -1}, 1),
                new LinearConstraint(new double[]{2, 2}, 4)
        };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, null, solver.epsilon);

        Integer pivotCol = solver.getPivotColumn(tableau);
        assertEquals(0, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRowNullTableau() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = null;
        Integer pivotRow = solver.getPivotRow(0, tableau);
        assertNull(pivotRow);
    }

    @Test
    public void testGetPivotRow() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1, -2, -3}, 0);
        LinearConstraint[] constraints = {
                new LinearConstraint(new double[]{1, 1}, 0),
                new LinearConstraint(new double[]{1, -1}, 1),
                new LinearConstraint(new double[]{2, 2}, 4)
        };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, null, solver.epsilon);

        Integer pivotRow = solver.getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1, -2, -3}, 0);
        LinearConstraint[] constraints = {
                new LinearConstraint(new double[]{1, 1}, 0),
                new LinearConstraint(new double[]{1, -1}, 1),
                new LinearConstraint(new double[]{2, 2}, 4)
        };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, null, solver.epsilon);

        solver.doIteration(tableau);
    }

    @Test(expected = OptimizationException.class)
    public void testSolvePhase1NoFeasibleSolution() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{0, 0, 1}, 0);
        LinearConstraint[] constraints = {
                new LinearConstraint(new double[]{1, 1}, 0),
                new LinearConstraint(new double[]{1, -1}, 1),
                new LinearConstraint(new double[]{2, 2}, 4)
        };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, null, solver.epsilon);

        solver.solvePhase1(tableau);
    }

    @Test(expected = OptimizationException.class)
    public void testSolvePhase1MaxIterationsExceeded() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1, -2, -3}, 0);
        LinearConstraint[] constraints = {
                new LinearConstraint(new double[]{1, 1}, 0),
                new LinearConstraint(new double[]{1, -1}, 1),
                new LinearConstraint(new double[]{2, 2}, 4)
        };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, null, solver.epsilon);

        for (int i = 0; i < 1000; i++) {
            try {
                solver.solvePhase1(tableau);
            } catch (OptimizationException e) {
                // expected
            }
        }

        fail("Max iterations should have been exceeded");
    }

    @Test
    public void testDoOptimize() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1, -2, -3}, 0);
        LinearConstraint[] constraints = {
                new LinearConstraint(new double[]{1, 1}, 0),
                new LinearConstraint(new double[]{1, -1}, 1),
                new LinearConstraint(new double[]{2, 2}, 4)
        };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, null, solver.epsilon);

        RealPointValuePair solution = solver.doOptimize();
        PointValuePair expected = new PointValuePair(new double[]{1.0 / 3.0, 1.0 / 3.0, -1.0 / 3.0}, 0);
        assertEquals(expected.getValue(), solution.getValue(), 0.0);
        assertEquals(expected.getPoint().length, solution.getPoint().length);
        assertArrayEquals(expected.getPoint(), solution.getPoint(), 0.0);
    }
}