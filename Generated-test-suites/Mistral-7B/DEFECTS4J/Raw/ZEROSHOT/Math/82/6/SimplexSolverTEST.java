import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationTest;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraints;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SimplexSolverTest extends SimpleOptimizationTest {

    @Test
    public void testSimpleOptimization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{2.0});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{0.0, -1.0}, {-1.0, 0.0}, {1.0, 1.0}}, null);
        LinearConstraints constraintsSet = new LinearConstraints(constraints);

        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair solution = solver.optimize(f, constraintsSet, null, null, null);
            Assert.assertEquals(1.0, solution.getPoint()[0], 1e-6);
            Assert.assertEquals(2.0, solution.getValue(), 1e-6);
        } catch (OptimizationException e) {
            // This optimization should succeed
            Assert.fail("OptimizationException: " + e.getMessage());
        }
    }

    @Test
    public void testUnboundedSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{2.0});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{0.0, -1.0}, {-1.0, 0.0}, {1.0, 1.0}}, null);
        LinearConstraints constraintsSet = new LinearConstraints(constraints);

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(f, constraintsSet, null, null, null);
            Assert.fail("No UnboundedSolutionException thrown.");
        } catch (UnboundedSolutionException e) {
            // Expected exception
        }
    }

    @Test
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{2.0});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{0.0, 1.0}, {1.0, 1.0}}, null);
        LinearConstraints constraintsSet = new LinearConstraints(constraints);

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(f, constraintsSet, null, null, null);
            Assert.fail("NoNoFeasibleSolutionException thrown.");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception
        }
    }

    @Test
    public void testMaxIterations() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{2.0});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{0.0, -1.0}, {-1.0, 0.0}, {1.0, 1.0}}, null);
        LinearConstraints constraintsSet = new LinearConstraints(constraints);

        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(1);
        try {
            solver.optimize(f, constraintsSet, null, null, null);
            Assert.fail("NoOptimizationException thrown.");
        } catch (OptimizationException e) {
            Assert.assertEquals("Maximum number of iterations exceeded", e.getMessage());
        }
    }

    @Test
    public void testSolveMultipleTimes() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{2.0});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{0.0, -1.0}, {-1.0, 0.0}, {1.0, 1.0}}, null);
        LinearConstraints constraintsSet = new LinearConstraints(constraints);

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution1 = null;
        PointValuePair solution2 = null;
        try {
            solution1 = solver.optimize(f, constraintsSet, null, null, null);
            solution2 = solver.optimize(f, constraintsSet, null, null, null);
        } catch (OptimizationException e) {
            // Failures should be identical
            Assert.fail("Different exceptions: " + e.getMessage());
        }

        Assert.assertEquals(solution1.getPoint()[0], solution2.getPoint()[0], 1e-6);
        Assert.assertEquals(solution1.getValue(), solution2.getValue(), 1e-6);
    }

    @Test
    public void testSolveWithRestrictToNonNegative() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{2.0});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{0.0, -1.0}, {-1.0, 0.0}, {1.0, 1.0}}, null);
        LinearConstraints constraintsSet = new LinearConstraints(constraints);

        SimplexSolver solver = new SimplexSolver();
        solver.setRestrictToNonNegative(true);
        try {
            PointValuePair solution = solver.optimize(f, constraintsSet, null, null, null);
            Assert.assertEquals(0.0, solution.getPoint()[0], 1e-6);
            Assert.assertEquals(2.0, solution.getValue(), 1e-6);
        } catch (OptimizationException e) {
            // This optimization should succeed
            Assert.fail("OptimizationException: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRestrictToNonNegativeNull() {
        SimplexSolver solver = new SimplexSolver();
        solver.setRestrictToNonNegative(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSolveWithRestrictToNonNegativeAfterSolve() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{2.0});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{0.0, -1.0}, {-1.0, 0.0}, {1.0, 1.0}}, null);
        LinearConstraints constraintsSet = new LinearConstraints(constraints);

        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraintsSet, null, null, null);
        solver.setRestrictToNonNegative(true);
        try {
            solver.optimize(f, constraintsSet, null, null, null);
            Assert.fail("NoIllegalStateException thrown.");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
}