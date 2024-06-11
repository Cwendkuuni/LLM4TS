package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.ValueChecker;
import org.apache.commons.math3.optim.univariate.bracketing.BracketFinder;
import org.apache.commons.math3.optim.univariate.nderiv.FiniteDifferenceDerivativeEstimator;
import org.junit.Test;

/**
 * Test class for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-10);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-10);
        assertInstanceOf(BrentOptimizer.class, optimizer.getLineSearch());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with custom convergence checker.
     */
    @Test
    public void testCustomConstructor() {
        ValueChecker checker = new ValueChecker(1e-5, 1e-5);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-10);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-10);
        assertInstanceOf(BrentOptimizer.class, optimizer.getLineSearch());
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test optimization with a simple test function.
     */
    @Test
    public void testOptimizeFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        double[] startPoint = new double[]{3.0};
        PointValuePair result = optimizer.optimize(f, startPoint);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization with a multivariate test function.
     */
    @Test
    public void testOptimizeMultivariateFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        final MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(f, startPoint);
        assertEquals(1.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization with a non-strictly positive initial threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInitializeWithNegativeThreshold() {
        new PowellOptimizer(-1.0, 1e-6);
    }

    /**
     * Test optimization with a non-strictly positive relative threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testInitializeWithSmallRelativeThreshold() {
        new PowellOptimizer(1e-8, 1e-6);
    }

    /**
     * Test optimization with a non-converging function.
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizeDivergingFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / (1 + Math.pow(x, 2));
            }
        };
        double[] startPoint = new double[]{1.0};
        optimizer.optimize(f, startPoint);
    }

    /**
     * Test optimization with a converging function that has no bracketed
     * interval.
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizeFunctionNoBracket() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double[] startPoint = new double[]{-1.0};
        optimizer.optimize(f, startPoint);
    }

    /**
     * Test optimization with a converging function that has a bracketed
     * interval but no convergence.
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizeFunctionNoConvergence() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 2) - 1.0;
            }
        };
        final BracketFinder bracket = new BracketFinder();
        bracket.search(f, GoalType.MINIMIZE, 0, 2);
        optimizer.setConvergenceChecker(bracket);
        double[] startPoint = new double[]{2.0};
        optimizer.optimize(f, startPoint);
    }
}