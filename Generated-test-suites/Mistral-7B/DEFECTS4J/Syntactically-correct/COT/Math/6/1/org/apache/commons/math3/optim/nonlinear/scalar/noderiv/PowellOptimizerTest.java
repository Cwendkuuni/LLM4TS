package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.initializer.SimpleStartingPoint;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    /**
     * Test object.
     */
    private PowellOptimizer optimizer;

    /**
     * Set up test.
     */
    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(0.01, 0.001);
    }

    /**
     * Test method {@link PowellOptimizer#PowellOptimizer()}.
     */
    @Test
    public void testConstructor() {
        PowellOptimizer powellOptimizer = new PowellOptimizer();
        assertNotNull(powellOptimizer);
    }

    /**
     * Test method {@link PowellOptimizer#PowellOptimizer(double, double)}.
     */
    @Test
    public void testConstructorWithDefaults() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01);
        assertNotNull(powellOptimizer);
    }

    /**
     * Test method {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}.
     */
    @Test
    public void testConstructorWithCustomChecker() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001, new MyConvergenceChecker());
        assertNotNull(powellOptimizer);
    }

    /**
     * Test method {@link PowellOptimizer#optimize()}.
     */
    @Test
    public void testOptimizeWithDefaultFunction() {
        UnivariateOptimizer optimizer = new PowellOptimizer(0.01);
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(new PolynomialFunction(new double[]{-3.0, 2.0, 1.0}, startPoint));
        double[] expected = {1.0, 1.0};
        assertEquals(expected[0], result.getPoint()[0], 0.001);
        assertEquals(expected[0], result.getPoint()[1], 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    /**
     * Test method {@link PowellOptimizer#optimize()}.
     */
    @Test
    public void testOptimizeWithCustomStartingPoint() {
        UnivariateOptimizer optimizer = new PowellOptimizer(0.01);
        double[] startPoint = {1.5, 2.5};
        PointValuePair result = optimizer.optimize(new PolynomialFunction(new double[]{-3.0, 2.0, 1.0}, startPoint));
        double[] expected = {1.0, 1.0};
        assertEquals(expected[0], result.getPoint()[0], 0.001);
        assertEquals(expected[0], result.getPoint()[1], 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    /**
     * Test method {@link PowellOptimizer#optimize()}.
     */
    @Test
    public void testOptimizeWithCustomFunctionAndGoalType() {
        UnivariateOptimizer optimizer = new PowellOptimizer(0.01, new SimpleGoalType(SimpleGoalType.MAXIMIZE));
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(new PolynomialFunction(new double[]{-3.0, 2.0, 1.0}, startPoint));
        double[] expected = {1.5, 1.5};
        assertEquals(expected[0], result.getPoint()[0], 0.001);
        assertEquals(expected[0], result.getPoint()[1], 0.001);
        assertEquals(3.0, result.getValue(), 0.001);
    }

    /**
     * Test method {@link PowellOptimizer#optimize()}.
     */
    @Test
    public void testOptimizeWithCustomFunctionAndCustomStartingPointAndGoalType() {
        UnivariateOptimizer optimizer = new PowellOptimizer(0.01, new SimpleGoalType(SimpleGoalType.MAXIMIZE), new AveragedStartingPointGenerator(2));
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(new PolynomialFunction(new double[]{-3.0, 2.0, 1.0}, startPoint));
        double[] expected = {1.5, 1.5};
        assertEquals(expected[0], result.getPoint()[0], 0.001);
        assertEquals(expected[0], result.getPoint()[1], 0.001);
        assertEquals(3.0, result.getValue(), 0.001);
    }

    /**
     * Test method {@link PowellOptimizer#optimize()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNegativeAbsoluteThreshold() {
        UnivariateOptimizer optimizer = new PowellOptimizer(0.01, -0.001);
    }

    /**
     * Test method {@link PowellOptimizer#optimize()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithZeroRelativeThreshold() {
        UnivariateOptimizer optimizer = new PowellOptimizer(0.0, 0.001);
    }

    /**
     * Test method {@link PowellOptimizer#optimize()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNegativeRelativeThreshold() {
        UnivariateOptimizer optimizer = new PowellOptimizer(-0.01, 0.001);
    }

    /**
     * Test method {@link PowellOptimizer#optimize()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNegativeAbsoluteThresholdAndRelativeThreshold() {
        UnivariateOptimizer optimizer = new PowellOptimizer(-0.01, -0.001);
    }

    /**
     * Test method {@link PowellOptimizer#optimize()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullConvergenceChecker() {
        UnivariateOptimizer optimizer = new PowellOptimizer(0.01, null);
    }

    /**
     * Test method {@link PowellOptimizer#optimize()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizeWithNonMultivariateFunction() {
        UnivariateOptimizer optimizer = new PowellOptimizer(0.01);
        optimizer.optimize(new SimpleStartingPoint(1.0));
    }

    /**
     * Custom Convergence Checker.
     */
    static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iterationNumber, PointValuePair previous, PointValuePair current) {
            double difference = Precision.doubleValueDifference(previous.getValue(), current.getValue());
            return difference < 0.001;
        }
    }
}