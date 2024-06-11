import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.NonNegativeConstraint;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunctionImpl;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optimization.nonlinear.scalar.ObjectiveFunction;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        SimplexSolver solverWithParams = new SimplexSolver(1e-7, 5);
        Assert.assertEquals(1e-6, solver.epsilon, 0);
        Assert.assertEquals(10, solver.maxUlps);
        Assert.assertEquals(1e-7, solverWithParams.epsilon, 0);
        Assert.assertEquals(5, solverWithParams.maxUlps);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionImpl(new double[]{-5.0, -3.0}, new double[]{1.0, 1.0}),
                new LinearConstraint[]{new LinearConstraint(2.0, new double[]{1.0}, null), new LinearConstraint(-3.0, new double[]{1.0}, null),
                        new LinearConstraint(0.0, new double[]{2.0, 1.0}, null)}, GoalType.MINIMIZE, new double[]{1e-6, 10}, null);
        Integer expected = 0;
        Integer result = new SimplexSolver().getPivotColumn(tableau);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionImpl(new double[]{-5.0, -3.0}, new double[]{1.0, 1.0}),
                new LinearConstraint[]{new LinearConstraint(2.0, new double[]{1.0}, null), new LinearConstraint(-3.0, new double[]{1.0}, null),
                        new LinearConstraint(0.0, new double[]{2.0, 1.0}, null)}, GoalType.MINIMIZE, new double[]{1e-6, 10}, null);
        Integer expected = 1;
        Integer result = new SimplexSolver().getPivotRow(tableau, 0);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionImpl(new double[]{-5.0, -3.0}, new double[]{1.0, 1.0}),
                new LinearConstraint[]{new LinearConstraint(2.0, new double[]{1.0}, null), new LinearConstraint(-3.0, new double[]{1.0}, null),
                        new LinearConstraint(0.0, new double[]{2.0, 1.0}, null)}, GoalType.MINIMIZE, new double[]{1e-6, 10}, null);

        new SimplexSolver().doIteration(tableau);

        // check that the pivot column is 0
        for (int i = 0; i < tableau.getHeight(); i++) {
            for (int j = 0; j < tableau.getWidth() - 1; j++) {
                if (j == 0) {
                    Assert.assertTrue(Precision.isClose(tableau.getEntry(i, j), 0.0, 10));
                } else {
                    Assert.assertTrue(Precision.isClose(tableau.getEntry(i, j), 0.0, 10 * Math.max(1.0, Math.abs(tableau.getEntry(i, 0)))));
                }
            }
            Assert.assertTrue(Precision.isClose(tableau.getEntry(i, tableau.getWidth() - 1), 0.0, 10));
        }

        // check that the pivot row is updated
        Assert.assertTrue(Precision.isClose(tableau.getEntry(0, 0), 1.0, 10));

        // check that the rest of the pivot column is updated
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != 0) {
                Assert.assertTrue(Precision.isClose(tableau.getEntry(i, 0), 0.0, 10 * Math.max(1.0, Math.abs(tableau.getEntry(i, 0)))));
            }
        }
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionImpl(new double[]{-5.0, -3.0}, new double[]{1.0, 1.0}),
                new LinearConstraint[]{new LinearConstraint(2.0, new double[]{1.0}, null), new LinearConstraint(-3.0, new double[]{1.0}, null),
                        new LinearConstraint(0.0, new double[]{2.0, 1.0}, null)}, GoalType.MINIMIZE, new double[]{1e-6, 10}, null);

        new SimplexSolver().solvePhase1(tableau);

        // check that the optimal solution is correct
        PointValuePair expected = new PointValuePair(new double[]{3.0 / 2.0, 3.0 / 2.0}, -5.0 / 2.0);
        PointValuePair result = tableau.getSolution();
        Assert.assertEquals(expected.getPoint(), result.getPoint(), 1e-6);
        Assert.assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction function = new LinearObjectiveFunctionImpl(new double[]{-5.0, -3.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(2.0, new double[]{1.0}, null),
            new LinearConstraint(-3.0, new double[]{1.0}, null),
            new LinearConstraint(0.0, new double[]{2.0, 1.0}, null)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair expected = new PointValuePair(new double[]{3.0 / 2.0, 3.0 / 2.0}, -5.0 / 2.0);
        PointValuePair result = solver.doOptimize();
        Assert.assertEquals(expected.getPoint(), result.getPoint(), 1e-6);
        Assert.assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionImpl(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                new double[]{1.0, 1.0}), new LinearConstraint[0], GoalType.MINIMIZE, new double[]{1e-6, 10}, null);
        new SimplexSolver().solvePhase1(tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionImpl(new double[]{Double.POSITIVE_INFINITY, 0.0},
                new double[]{1.0, 1.0}), new LinearConstraint[0], GoalType.MINIMIZE, new double[]{1e-6, 10}, null);
        new SimplexSolver().solvePhase1(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionImpl(new double[]{0.0, 0.0}, new double[]{1.0, 1.0}),
                new LinearConstraint[]{new LinearConstraint(-Double.POSITIVE_INFINITY, new double[]{1.0}, null), new LinearConstraint(Double.POSITIVE_INFINITY, new double[]{1.0}, null)},
                GoalType.MINIMIZE, new double[]{1e-6, 10}, null);
        new SimplexSolver().solvePhase1(tableau);
    }
}