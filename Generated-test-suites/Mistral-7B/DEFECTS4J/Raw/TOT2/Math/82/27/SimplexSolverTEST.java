import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstraintSet() throws OptimizationException {
        ObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, 0.0);
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        PointValuePair solution = solver.optimize(objFunc, new LinearConstraintSet(), GoalType.MINIMIZE);
        Assert.assertEquals(0, solution.getValue(), 1.0e-6);
        Assert.assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1.0e-6);
    }

    @Test
    public void testSimpleConstraintSet() throws OptimizationException {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, 0.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraint c2 = new LinearConstraint(new double[]{-2.0, 1.0}, 2.0);
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        constraintSet.addConstraint(c1);
        constraintSet.addConstraint(c2);
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        PointValuePair solution = solver.optimize(objFunc, constraintSet, GoalType.MINIMIZE);
        Assert.assertEquals(-1.0, solution.getValue(), 1.0e-6);
        Assert.assertEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 1.0e-6);
    }

    @Test
    public void testUnboundedSolutionException() {
        ObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, Double.NEGATIVE_INFINITY);
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        try {
            solver.optimize(objFunc, new LinearConstraintSet(), GoalType.MINIMIZE);
            Assert.fail("Unbounded solution exception not thrown");
        } catch (OptimizationException e) {
            Assert.assertEquals(OptimizationException.Type.UNBOUNDED_SOLUTION, e.getType());
        }
    }

    @Test
    public void testFeasibleSolutionException() {
        ObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, 0.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 1.0}, Double.NEGATIVE_INFINITY);
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        constraintSet.addConstraint(c1);
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        try {
            solver.optimize(objFunc, constraintSet, GoalType.MINIMIZE);
            Assert.fail("No feasible solution exception not thrown");
        } catch (OptimizationException e) {
            Assert.assertEquals(OptimizationException.Type.NO_FEASIBLE_SOLUTION, e.getType());
        }
    }

    @Test
    @Ignore("This test is not feasible as the Simplex solver cannot handle non-linear constraints")
    public void testNonLinearConstraintSet() {
        // This test is not feasible as the Simplex solver cannot handle non-linear constraints
        // It is included for completeness
    }

    @Test
    public void testNoFeasibleSolutionException() {
        ObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, -1.0}, 0.0);
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint c2 = new LinearConstraint(new double[]{-1.0, 1.0}, 0.0);
        constraintSet.addConstraint(c1);
        constraintSet.addConstraint(c2);
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        try {
            solver.optimize(objFunc, constraintSet, GoalType.MINIMIZE);
            Assert.fail("No feasible solution exception not thrown");
        } catch (OptimizationException e) {
            Assert.assertEquals(OptimizationException.Type.NO_FEASIBLE_SOLUTION, e.getType());
        }
    }

    @Test
    public void testOptimalSolution() throws OptimizationException {
        ObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, 0.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, -1.0}, 0.0);
        LinearConstraint c2 = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        constraintSet.addConstraint(c1);
        constraintSet.addConstraint(c2);
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        PointValuePair solution = solver.optimize(objFunc, constraintSet, GoalType.MINIMIZE);
        Assert.assertEquals(0.0, solution.getValue(), 1.0e-6);
        Assert.assertEquals(new double[]{0.5, 0.5}, solution.getPoint(), 1.0e-6);
    }

    @Test
    public void testMaximalIterationsException() {
        ObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, 0.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        constraintSet.addConstraint(c1);
        SimplexSolver solver = new SimplexSolver(1.0e-6, 1);
        try {
            solver.optimize(objFunc, constraintSet, GoalType.MINIMIZE);
            Assert.fail("Maximal iterations exception not thrown");
        } catch (OptimizationException e) {
            Assert.assertEquals(OptimizationException.Type.MAX_ITER, e.getType());
        }
    }
}