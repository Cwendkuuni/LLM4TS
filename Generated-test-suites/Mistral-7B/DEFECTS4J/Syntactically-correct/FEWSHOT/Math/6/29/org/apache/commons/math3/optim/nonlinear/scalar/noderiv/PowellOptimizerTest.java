package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.UnivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);
        assertEquals(0.001, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
        assertEquals(BrentOptimizer.class, optimizer.getLineSearch().getClass());
    }

    @Test
    public void testCustomConstructorWithChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return previous.getValue() - current.getValue() < 0.001;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01, convergenceChecker);
        assertEquals(0.001, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(convergenceChecker.getClass(), optimizer.getConvergenceChecker().getClass());
        assertEquals(BrentOptimizer.class, optimizer.getLineSearch().getClass());
    }

    @Test
    public void testCustomConstructorWithLineSearchTolerances() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01, 1e-5, 1e-5);
        assertEquals(BrentOptimizer.class, optimizer.getLineSearch().getClass());
        assertEquals(1e-5, optimizer.getLineSearch().getRelativeThreshold(), 0.0);
        assertEquals(1e-5, optimizer.getLineSearch().getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testOptimizeSimpleFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.1);
        UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        PointValuePair result = optimizer.optimize(func, GoalType.MINIMIZE, 0.0, null);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeFunctionWithCustomChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return previous.getValue() - current.getValue() < 0.001;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01, convergenceChecker);
        UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        PointValuePair result = optimizer.optimize(func, GoalType.MINIMIZE, 0.0, null);
        assertEquals(Math.PI / 2.0, result.getValue(), 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteThreshold() {
        new PowellOptimizer(0.001, -0.01);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelativeThreshold() {
        new PowellOptimizer(-0.001, 0.01);
    }

    @Test
    public void testFindMinimumMultipleCalls() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.1);
        UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };

        PointValuePair result1 = optimizer.optimize(func, GoalType.MINIMIZE, 0.0, null);
        PointValuePair result2 = optimizer.optimize(func, GoalType.MINIMIZE, 0.0, null);

        assertFalse(result1.equals(result2));
    }
}