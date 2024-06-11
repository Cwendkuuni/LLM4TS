package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaxValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleVectorValueFunction;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test a simple 1D problem.
     */
    @Test
    public void testSimple1d() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE, new double[]{1d, 1d});
        Assert.assertEquals("Unexpected minimum", -4d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected minimum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a simple 2D problem.
     */
    @Test
    public void testSimple2d() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 1d, 2d, 2d}, {-1d, -1d, 3d, 3d}}, 0);
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE, new double[]{1d, 1d, 1d, 1d});
        Assert.assertEquals("Unexpected minimum", -3d * Math.sqrt(2d) / 2d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected minimum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with constraints.
     */
    @Test
    public void testConstraints1d() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        LinearConstraints constraints = new LinearConstraints(
                new LinearConstraint[]{{new SimpleBounds(1d, 2d), null}, {null, new SimpleBounds(1d, 3d)}});
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE, constraints, new double[]{1d, 1d});
        Assert.assertEquals("Unexpected minimum", -1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected minimum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with a custom convergence checker.
     */
    @Test
    public void testCustomChecker1d() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6,
                new GoalTypeMaxValueChecker(1e-6));
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE,
                new double[]{1d, 1d});
        Assert.assertEquals("Unexpected minimum", -1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected minimum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with a custom convergence checker and constraints.
     */
    @Test
    public void testCustomCheckerConstraints1d() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6,
                new GoalTypeMaxValueChecker(1e-6));
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        LinearConstraints constraints = new LinearConstraints(
                new LinearConstraint[]{{new SimpleBounds(1d, 2d), null}, {null, new SimpleBounds(1d, 3d)}});
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE, constraints,
                new double[]{1d, 1d});
        Assert.assertEquals("Unexpected minimum", -1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected minimum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with a custom convergence checker and a large search space.
     */
    @Test
    public void testCustomCheckerLargeSearchSpace1d() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6,
                new GoalTypeMaxValueChecker(1e-6));
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE,
                new double[]{-10d, 10d});
        Assert.assertEquals("Unexpected minimum", -1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected minimum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with a custom convergence checker and a large search space and constraints.
     */
    @Test
    public void testCustomCheckerLargeSearchSpaceConstraints1d() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6,
                new GoalTypeMaxValueChecker(1e-6));
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        LinearConstraints constraints = new LinearConstraints(
                new LinearConstraint[]{{new SimpleBounds(1d, 2d), null}, {null, new SimpleBounds(1d, 3d)}});
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE, constraints,
                new double[]{-10d, 10d});
        Assert.assertEquals("Unexpected minimum", -1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected minimum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with a custom bounds checker.
     */
    @Test
    public void testCustomBoundsChecker1d() {
        PowellOptimizer optimizer = new PowellOptimizer(new GoalTypeMinValueChecker(),
                1e-6, 1e-6);
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        PointValuePair solution = optimizer.optimize(f, GoalType.MAXIMIZE,
                new double[]{1d, 1d});
        Assert.assertEquals("Unexpected maximum", 1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected maximum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with a custom bounds checker and constraints.
     */
    @Test
    public void testCustomBoundsCheckerConstraints1d() {
        PowellOptimizer optimizer = new PowellOptimizer(new GoalTypeMinValueChecker(),
                1e-6, 1e-6);
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        LinearConstraints constraints = new LinearConstraints(
                new LinearConstraint[]{{new SimpleBounds(1d, 2d), null}, {null, new SimpleBounds(1d, 3d)}});
        PointValuePair solution = optimizer.optimize(f, GoalType.MAXIMIZE,
                constraints, new double[]{1d, 1d});
        Assert.assertEquals("Unexpected maximum", 1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected maximum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with a custom bounds checker and a large search space.
     */
    @Test
    public void testCustomBoundsCheckerLargeSearchSpace1d() {
        PowellOptimizer optimizer = new PowellOptimizer(new GoalTypeMinValueChecker(),
                1e-6, 1e-6);
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        PointValuePair solution = optimizer.optimize(f, GoalType.MAXIMIZE,
                new double[]{-10d, 10d});
        Assert.assertEquals("Unexpected maximum", 1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected maximum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with a custom bounds checker and a large search space and constraints.
     */
    @Test
    public void testCustomBoundsCheckerLargeSearchSpaceConstraints1d() {
        PowellOptimizer optimizer = new PowellOptimizer(new GoalTypeMinValueChecker(),
                1e-6, 1e-6);
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        LinearConstraints constraints = new LinearConstraints(
                new LinearConstraint[]{{new SimpleBounds(1d, 2d), null}, {null, new SimpleBounds(1d, 3d)}});
        PointValuePair solution = optimizer.optimize(f, GoalType.MAXIMIZE,
                constraints, new double[]{-10d, 10d});
        Assert.assertEquals("Unexpected maximum", 1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected maximum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with a custom line search.
     */
    @Test
    public void testCustomLineSearch1d() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6,
                new SimpleLineSearch(0.1, 0.2));
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE,
                new double[]{1d, 1d});
        Assert.assertEquals("Unexpected minimum", -1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected minimum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with a custom line search and constraints.
     */
    @Test
    public void testCustomLineSearchConstraints1d() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6,
                new SimpleLineSearch(0.1, 0.2));
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        LinearConstraints constraints = new LinearConstraints(
                new LinearConstraint[]{{new SimpleBounds(1d, 2d), null}, {null, new SimpleBounds(1d, 3d)}});
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE,
                constraints, new double[]{1d, 1d});
        Assert.assertEquals("Unexpected minimum", -1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected minimum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with a custom line search and a large search space.
     */
    @Test
    public void testCustomLineSearchLargeSearchSpace1d() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6,
                new SimpleLineSearch(0.1, 0.2));
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE,
                new double[]{-10d, 10d});
        Assert.assertEquals("Unexpected minimum", -1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected minimum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }

    /**
     * Test a problem with a custom line search and a large search space and constraints.
     */
    @Test
    public void testCustomLineSearchLargeSearchSpaceConstraints1d() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6,
                new SimpleLineSearch(0.1, 0.2));
        UnivariateFunction f = new SimpleVectorValueFunction(new double[][]{{1d, 2d}, {-1d, 3d}}, 0);
        LinearConstraints constraints = new LinearConstraints(
                new LinearConstraint[]{{new SimpleBounds(1d, 2d), null}, {null, new SimpleBounds(1d, 3d)}});
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE,
                constraints, new double[]{-10d, 10d});
        Assert.assertEquals("Unexpected minimum", -1d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals("Unexpected minimum point", new double[]{1d, 1d}, solution.getPoint(), 1e-6);
    }
}