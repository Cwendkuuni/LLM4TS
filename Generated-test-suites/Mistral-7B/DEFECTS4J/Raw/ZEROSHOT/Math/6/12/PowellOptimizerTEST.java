package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.util.Precision;

/**
 * Tests for PowellOptimizer class.
 */
public class PowellOptimizerTest {

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}
     * with default parameters.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double)}
     */
    @org.junit.Test
    public void testCustomConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-2,
                new SimpleUnivariateValueChecker(1e-6, 1e-6));
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double)}
     */
    @org.junit.Test
    public void testCustomConstructorWithLineSearchTolerances() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-2,
                0.01, 0.01, new SimpleUnivariateValueChecker(1e-6, 1e-6));
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double, ConvergenceChecker)}
     */
    @org.junit.Test
    public void testCustomConstructorWithCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-2,
                0.01, 0.01, new MyConvergenceChecker());
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link PowellOptimizer#optimize(MultivariateFunction)}
     */
    @org.junit.Test
    public void testOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-2);
        MultivariateFunction f = new PolynomialFunction(2, 2, -12.0, 4.0, 16.0, 12.0, -8.0, 12.0);
        PointValuePair result = optimizer.optimize(f);
        double[] expected = {2.0, 2.0};
        assertEquals(expected[0], result.getPoint()[0], 1e-6);
        assertEquals(expected[1], result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link PowellOptimizer#optimize(MultivariateFunction)}
     * with custom convergence checker.
     */
    @org.junit.Test
    public void testOptimizeWithCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-2, new MyConvergenceChecker());
        MultivariateFunction f = new PolynomialFunction(2, 2, -12.0, 4.0, 16.0, 12.0, -8.0, 12.0);
        PointValuePair result = optimizer.optimize(f);
        double[] expected = {2.0, 2.0};
        assertEquals(expected[0], result.getPoint()[0], 1e-6);
        assertEquals(expected[1], result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {

        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double error = previous.getValue() - current.getValue();
            double absError = Math.abs(error);
            double relError = absError / (Math.max(Math.abs(previous.getValue()), Math.abs(current.getValue())) + 1);
            return relError < 1e-5 || absError < 1e-6;
        }
    }
}