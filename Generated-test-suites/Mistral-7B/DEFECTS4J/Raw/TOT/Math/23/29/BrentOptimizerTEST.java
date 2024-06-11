package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.ValuesGenerator;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.GoalType;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@code BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @Test
    public void testBrentOptimizerWithDefaults() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-4, 1e-6);
        UnivariateOptimizer result = optimizer.optimize(Math::sin, 0.0, Math.PI / 2.0);
        PointValuePair expected = new PointValuePair(Math.PI / 2.0, Math.sin(Math.PI / 2.0));
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}.
     */
    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return (iter > 10 && current.getValue() < 1e-5);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-4, 1e-6, customChecker);
        UnivariateOptimizer result = optimizer.optimize(Math::sin, 0.0, Math.PI / 2.0);
        PointValuePair expected = new PointValuePair(Math.PI / 2.0, Math.sin(Math.PI / 2.0));
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, UnivariateOptimizer.ConvergenceChecker)}.
     */
    @Test
    public void testBrentOptimizerWithCustomFunction() {
        UnivariateFunction customFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Erf.erf(x) * Erf.erf(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-4, 1e-6);
        UnivariateOptimizer result = optimizer.optimize(customFunction, -1.0, 1.0);
        PointValuePair expected = new PointValuePair(0.0, 0.252670202995);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, UnivariateOptimizer.ConvergenceChecker, ValuesGenerator)}.
     */
    @Test
    public void testBrentOptimizerWithCustomFunctionAndInitialValues() {
        UnivariateFunction customFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        ValuesGenerator<Double> valuesGenerator = new ValuesGenerator<Double>() {
            @Override
            public Double generate(int index) {
                return index * 0.1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-4, 1e-6);
        UnivariateOptimizer result = optimizer.optimize(customFunction, -2.0, 2.0, null, valuesGenerator);
        PointValuePair expected = new PointValuePair(1.0, 1.0);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double) with negative arguments}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeArguments() {
        new BrentOptimizer(-1.0, 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double) with relative tolerance less than minimal}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithMinimalRelativeTolerance() {
        new BrentOptimizer(1e-8, 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker) with null arguments}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNullCheckers() {
        new BrentOptimizer(1e-4, 1e-6, null);
    }
}