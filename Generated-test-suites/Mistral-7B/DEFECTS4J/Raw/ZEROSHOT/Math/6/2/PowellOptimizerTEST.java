package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer()}.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer();
        Assert.assertNull(optimizer.getConvergenceChecker());
        Assert.assertEquals(Double.MIN_VALUE, optimizer.getAbsoluteThreshold(), 0.0);
        Assert.assertEquals(2 * FastMath.ulp(1d), optimizer.getRelativeThreshold(), 0.0);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double)}.
     */
    @Test
    public void testConstructorWithDefaultCheckers() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 1.0);
        Assert.assertNull(optimizer.getConvergenceChecker());
        Assert.assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.0);
        Assert.assertEquals(2 * FastMath.ulp(1d), optimizer.getRelativeThreshold(), 0.0);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}.
     */
    @Test
    public void testConstructorWithCustomChecker() {
        ConvergenceChecker<PointValuePair> customChecker = (iter, previous, current) -> true;
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 1.0, customChecker);
        Assert.assertNotNull(optimizer.getConvergenceChecker());
        Assert.assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.0);
        Assert.assertEquals(2 * FastMath.ulp(1d), optimizer.getRelativeThreshold(), 0.0);
        Assert.assertEquals(customChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double)}.
     */
    @Test
    public void testConstructorWithCustomCheckersAndTolerances() {
        ConvergenceChecker<PointValuePair> customChecker = (iter, previous, current) -> true;
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 1.0, 1e-4, 1e-6, customChecker);
        Assert.assertNotNull(optimizer.getConvergenceChecker());
        Assert.assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.0);
        Assert.assertEquals(1e-4, optimizer.getRelativeThreshold(), 0.0);
        Assert.assertEquals(customChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double, double, double)}.
     */
    @Test
    public void testConstructorWithCustomCheckersAndTolerancesAndLineSearchTolerances() {
        ConvergenceChecker<PointValuePair> customChecker = (iter, previous, current) -> true;
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 1.0, 1e-4, 1e-6,
                                                       0.01, 1e-6, customChecker);
        Assert.assertNotNull(optimizer.getConvergenceChecker());
        Assert.assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.0);
        Assert.assertEquals(1e-4, optimizer.getRelativeThreshold(), 0.0);
        Assert.assertEquals(0.01, optimizer.getLineSearchAbsoluteThreshold(), 0.0);
        Assert.assertEquals(1e-6, optimizer.getLineSearchRelativeThreshold(), 0.0);
        Assert.assertEquals(customChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link PowellOptimizer#optimize(MultivariateFunction)}.
     */
    @Test
    public void testOptimizeFunction() {
        PowellOptimizer optimizer = new PowellOptimizer();
        MultivariateFunction f = (x) -> x[0] * x[0] + x[1] * x[1];
        PointValuePair result = optimizer.optimize(f);
        Assert.assertArrayEquals(new double[]{-1.0, -1.0}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test method for {@link PowellOptimizer#optimize(MultivariateFunction, ConvergenceChecker)}.
     */
    @Test
    public void testOptimizeFunctionWithCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(() -> true, 1e-4, 1e-6);
        MultivariateFunction f = (x) -> x[0] * x[0] + x[1] * x[1];
        PointValuePair result = optimizer.optimize(f);
        Assert.assertArrayEquals(new double[]{-1.0, -1.0}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }
}