package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.linear.GradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.simple.RiddersMethod;
import org.apache.commons.math3.optim.nonlinear.simple.SquaredErrorFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.initializer.StartingPointGenerator;
import org.apache.commons.math3.test.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Test case for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest extends TestCase {

    private GaussNewtonOptimizer optimizer;
    private double[] x;
    private double[] target;

    @Before
    public void setUp() {
        // Initialize the optimizer with LU decomposition and a simple convergence checker.
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        x = new double[]{-1.0, 1.0};
        target = new double[]{0.0, 0.0};
    }

    /**
     * Test optimization of a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        FunctionFunction f = new QuadraticFunction(x, 10.0);
        OptimizableObjectiveFunction gno = new GaussNewtonOptimizer(x, f, optimizer);
        StartingPointGenerator spg = new AveragedStartingPointGenerator(x);
        GradientOptimizer go = new GradientOptimizer(gno, spg);
        go.optimize();
        PointValuePair pvp = go.getCurrentValue();
        double[] expected = new double[]{-0.5, 0.5};
        assertArrayEquals(expected, pvp.getPoint(), 1e-8);
    }

    /**
     * Test optimization of a function with no solution.
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testFunctionWithNoSolution() {
        FunctionFunction f = new FunctionFunction(x, Double.POSITIVE_INFINITY);
        OptimizableObjectiveFunction gno = new GaussNewtonOptimizer(x, f, optimizer);
        StartingPointGenerator spg = new AveragedStartingPointGenerator(x);
        GradientOptimizer go = new GradientOptimizer(gno, spg);
        go.optimize();
    }

    /**
     * Test optimization of a function with an incorrect number of variables.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFunctionWithIncorrectNumberOfVariables() {
        FunctionFunction f = new FunctionFunction(new double[]{1.0}, new double[]{1.0, 1.0}[0]);
        OptimizableObjectiveFunction gno = new GaussNewtonOptimizer(new double[]{1.0}, f, optimizer);
    }

    /**
     * Test optimization of a function with no gradient.
     */
    @Test(expected = MathInternalError.class)
    public void testFunctionWithNoGradient() {
        FunctionFunction f = new FunctionFunction(x, Double.POSITIVE_INFINITY);
        OptimizableObjectiveFunction gno = new GaussNewtonOptimizer(x, f, optimizer);
        StartingPointGenerator spg = new AveragedStartingPointGenerator(x);
        Optimizer go = new RiddersMethod(gno);
        go.optimize();
    }

    /**
     * Test optimization of a function with incorrect initial point.
     */
    @Test
    public void testFunctionWithIncorrectInitialPoint() {
        FunctionFunction f = new QuadraticFunction(x, 10.0);
        OptimizableObjectiveFunction gno = new GaussNewtonOptimizer(x, f, optimizer);
        StartingPointGenerator spg = new AveragedStartingPointGenerator(new double[]{1.0, -1.0});
        GradientOptimizer go = new GradientOptimizer(gno, spg);
        go.optimize();
        PointValuePair pvp = go.getCurrentValue();
        double[] expected = new double[]{-0.5, 0.5};
        assertArrayEquals(expected, pvp.getPoint(), 1e-8);
    }

    /**
     * Test optimization of a function with incorrect target.
     */
    @Test
    public void testFunctionWithIncorrectTarget() {
        FunctionFunction f = new QuadraticFunction(x, 10.0);
        OptimizableObjectiveFunction gno = new GaussNewtonOptimizer(x, f, optimizer);
        StartingPointGenerator spg = new AveragedStartingPointGenerator(x);
        Optimizer go = new RiddersMethod(gno);
        go.setMaxIterations(100);
        go.setTolerance(1e-15);
        go.optimize();
        PointValuePair pvp = go.getCurrentValue();
        double[] expected = new double[]{-0.5, 0.5};
        assertArrayEquals(expected, pvp.getPoint(), 1e-8);
    }

    /**
     * Test optimization of a function with incorrect convergence checker.
     */
    @Test
    public void testFunctionWithIncorrectConvergenceChecker() {
        FunctionFunction f = new QuadraticFunction(x, 10.0);
        OptimizableObjectiveFunction gno = new GaussNewtonOptimizer(x, f, null);
        StartingPointGenerator spg = new AveragedStartingPointGenerator(x);
        Optimizer go = new RiddersMethod(gno);
        go.optimize();
        PointValuePair pvp = go.getCurrentValue();
        double[] expected = new double[]{-0.5, 0.5};
        assertArrayEquals(expected, pvp.getPoint(), 1e-8);
    }

    /**
     * Test optimization of a function with incorrect weight matrix.
     */
    @Test
    public void testFunctionWithIncorrectWeightMatrix() {
        FunctionFunction f = new QuadraticFunction(x, 10.0);
        OptimizableObjectiveFunction gno = new GaussNewtonOptimizer(x, f, optimizer);
        gno.setWeight(new RealMatrix(new double[1][2]));
        StartingPointGenerator spg = new AveragedStartingPointGenerator(x);
        Optimizer go = new RiddersMethod(gno);
        go.optimize();
        PointValuePair pvp = go.getCurrentValue();
        double[] expected = new double[]{-0.5, 0.5};
        assertArrayEquals(expected, pvp.getPoint(), 1e-8);
    }

    /**
     * Test optimization of a function with incorrect use of LU or QR decomposition.
     */
    @Test(expected = MathInternalError.class)
    public void testFunctionWithIncorrectDecomposition() {
        FunctionFunction f = new QuadraticFunction(x, 10.0);
        OptimizableObjectiveFunction gno = new GaussNewtonOptimizer(x, f, optimizer);
        gno.setUseLU(false);
        StartingPointGenerator spg = new AveragedStartingPointGenerator(x);
        Optimizer go = new RiddersMethod(gno);
        go.optimize();
    }
}