import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertNotNull(solver.epsilon);
        assertNotNull(solver.maxUlps);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, -2.0, 3.0}),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(-1.0, true, 1.0),
                        new LinearConstraint(-1.0, true, 2.0),
                        new LinearConstraint(-0.5, false, 0.0),
                        new LinearConstraint(0.5, false, 0.0),
                        new LinearConstraint(1.0, false, 0.0)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, false, 1.0e-6, 10);

        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, -2.0, 3.0}),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(-1.0, true, 1.0),
                        new LinearConstraint(-1.0, true, 2.0),
                        new LinearConstraint(-0.5, false, 0.0),
                        new LinearConstraint(0.5, false, 0.0),
                        new LinearConstraint(1.0, false, 0.0)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, false, 1.0e-6, 10);

        Integer pivotRow = new SimplexSolver().getPivotRow(tableau, 1);
        assertEquals(2, pivotRow.intValue());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1WithUnboundedSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 0.0}),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(-1.0, true, Double.POSITIVE_INFINITY),
                        new LinearConstraint(-1.0, true, 0.0),
                        new LinearConstraint(0.0, false, Double.POSITIVE_INFINITY),
                        new LinearConstraint(1.0, false, 0.0)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, false, 1.0e-6, 10);
        new SimplexSolver().solvePhase1(tableau);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSolvePhase1WithMaxIterations() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, -2.0, 3.0}),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(-1.0, true, 1.0),
                        new LinearConstraint(-1.0, true, 2.0),
                        new LinearConstraint(-0.5, false, 0.0),
                        new LinearConstraint(0.5, false, 0.0),
                        new LinearConstraint(1.0, false, 0.0)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, false, 1.0e-6, 1);
        new SimplexSolver().solvePhase1(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, -2.0, 3.0}),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(-1.0, true, 1.0),
                        new LinearConstraint(-1.0, true, 2.0),
                        new LinearConstraint(-1.0, false, 0.0),
                        new LinearConstraint(0.5, false, 0.0),
                        new LinearConstraint(1.0, false, 0.0)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, false, 1.0e-6, 10);
        new SimplexSolver().solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, -2.0, 3.0}),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(-1.0, true, 1.0),
                        new LinearConstraint(-1.0, true, 2.0),
                        new LinearConstraint(-0.5, false, 0.0),
                        new LinearConstraint(0.5, false, 0.0),
                        new LinearConstraint(1.0, false, 0.0)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, false, 1.0e-6, 10);

        PointValuePair result = new SimplexSolver().doOptimize();
        assertEquals(-3.0, result.getValue(), 1.0e-6);
        assertEquals(new double[]{1.0, 1.0}, result.getPoint(), 1.0e-6);
    }

}