package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNotNull(optimizer.getConvergenceChecker());
        assertFalse(optimizer.isFindMinimum());
        assertFalse(optimizer.isFindMaximum());

        BrentOptimizer optimizerAbs = new BrentOptimizer(1e-5);
        assertEquals(GoalType.MINIMIZE, optimizerAbs.getGoalType());
        assertNull(optimizerAbs.getConvergenceChecker());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(-1.0, 1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        new BrentOptimizer(0.5 * Math.ulp(1), 1e-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullConvergenceChecker() {
        new BrentOptimizer(1e-5, 1e-5, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                throw new UnsupportedOperationException();
            }
        });
        assertNull(optimizer.doOptimize());
    }

    @Test
    public void testBrentOptimizerBasic() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() < 1.0e-8;
            }
        });
        UnivariatePointValuePair result = optimizer.optimize(FunctionTest.SIN_FUNCTION, -3.0, 3.0);
        assertTrue(Math.abs(Math.PI - result.getPoint()) < 0.01);
        assertLessThan(1e-8, result.getValue());
    }

    @Test
    public void testBrentOptimizerMultipleCalls() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() < 1.0e-8;
            }
        });

        UnivariatePointValuePair result1 = optimizer.optimize(FunctionTest.SIN_FUNCTION, -3.0, 3.0);
        UnivariatePointValuePair result2 = optimizer.optimize(FunctionTest.SIN_FUNCTION, -3.0, 3.0);

        assertEquals(result1, result2);
    }
}

class FunctionTest {
    static Function FUNCTION = new Function() {
        @Override
        public double value(double x) {
            return Math.sin(x);
        }
    };

    static Function SIN_FUNCTION = new Function() {
        @Override
        public double value(double x) {
            return Math.sin(x);
        }
    };
}

interface Function {
    double value(double x);
}