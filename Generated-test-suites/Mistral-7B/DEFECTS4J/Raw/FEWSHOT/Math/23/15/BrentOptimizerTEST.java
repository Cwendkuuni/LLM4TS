package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        ConvergenceChecker<UnivariatePointValuePair> nullChecker = null;
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001, nullChecker);
        assertNotNull(brentOptimizer);

        BrentOptimizer brentOptimizer2 = new BrentOptimizer(0.01, 0.001);
        assertNotNull(brentOptimizer2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructorNegativeAbs() {
        ConvergenceChecker<UnivariatePointValuePair> nullChecker = null;
        new BrentOptimizer(0.01, -0.001, nullChecker);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructorSmallRel() {
        ConvergenceChecker<UnivariatePointValuePair> nullChecker = null;
        new BrentOptimizer(0.001, 0.001, nullChecker);
    }

    @Test
    public void testDoOptimizeInitialValues() {
        Function1D f = new Function1D() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * x + 1;
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> nullChecker = null;
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001, nullChecker);
        UnivariatePointValuePair result = brentOptimizer.optimize(f, 0.1, 2.0);

        assertEquals(1.0, result.getX(), 0.001);
        assertEquals(-2.449489742741692E-14, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeConvergenceCriteria() {
        Function1D f = new Function1D() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * x + 1;
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> customChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() <= previous.getValue() &&
                       Math.abs(current.getValue()) < 0.0001;
            }
        };

        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001, customChecker);
        UnivariatePointValuePair result = brentOptimizer.optimize(f, 0.1, 2.0);

        assertEquals(1.0, result.getX(), 0.001);
        assertEquals(-1.2246068142522724E-16, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeCustomConvergenceCriteria() {
        Function1D f = new Function1D() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> customChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Math.abs(current.getValue()) < 0.001;
            }
        };

        BrentOptimizer brentOptimizer = new BrentOptimizer(0.001, 0.0, customChecker);
        UnivariatePointValuePair result = brentOptimizer.optimize(f, 0, 1);

        assertFalse(Double.isNaN(result.getX()));
        assertFalse(Double.isNaN(result.getValue()));
        assertTrue(Math.abs(result.getValue()) < 0.001);
        assertEquals(Math.PI / 4.0, result.getX(), 0.001);
    }

    @Test
    public void testDoOptimizeCustomConvergenceCriteriaFail() {
        Function1D f = new Function1D() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> customChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Math.abs(current.getValue()) > 0.001;
            }
        };

        BrentOptimizer brentOptimizer = new BrentOptimizer(0.001, 0.0, customChecker);
        UnivariatePointValuePair result = brentOptimizer.optimize(f, 0, 1);

        assertNull(result);
    }

    @Test
    public void testDoOptimizeDifferentGoalType() {
        Function1D f = new Function1D() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * x + 1;
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> nullChecker = null;
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001, nullChecker);
        UnivariatePointValuePair result = brentOptimizer.optimize(f, 0.1, 2.0, GoalType.MAXIMIZE);

        assertEquals(2.0, result.getX(), 0.001);
        assertEquals(2.449489742741692E-14, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeEmptyInterval() {
        Function1D f = new Function1D() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> nullChecker = null;
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001, nullChecker);

        try {
            UnivariatePointValuePair result = brentOptimizer.optimize(f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDoOptimizeInitialPointIsBetterThanInterval() {
        Function1D f = new Function1D() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> nullChecker = null;
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001, nullChecker);

        try {
            brentOptimizer.optimize(f, 1.0, 0.1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDoOptimizeIterations() {
        Function1D f = new Function1D() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * x + 1;
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> nullChecker = null;
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001, nullChecker);
        UnivariatePointValuePair result = brentOptimizer.optimize(f, 0.1, 2.0);

        int expectedIterations = 14;
        int iterations = 0;
        while (brentOptimizer.hasNext()) {
            UnivariatePointValuePair point = brentOptimizer.next();
            iterations++;
            if (point.getX() == result.getX() && point.getValue() == result.getValue()) {
                break;
            }
        }

        assertEquals(expectedIterations, iterations);
    }

    @Test
    public void testDoOptimizeIterationsWithCustomConvergenceChecker() {
        Function1D f = new Function1D() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> customChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Math.abs(current.getValue()) < 0.001;
            }
        };

        BrentOptimizer brentOptimizer = new BrentOptimizer(0.001, 0.0, customChecker);
        UnivariatePointValuePair result = brentOptimizer.optimize(f, 0, 1);

        int expectedIterations = 20;
        int iterations = 0;
        while (!customChecker.converged(iterations, null, result)) {
            UnivariatePointValuePair point = brentOptimizer.next();
            iterations++;
        }

        assertEquals(expectedIterations, iterations);
    }

    @Test
    public void testDoOptimizeStepSizeTooSmall() {
        Function1D f = new Function1D() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> nullChecker = null;
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.00001, 0.0, nullChecker);
        UnivariatePointValuePair result = brentOptimizer.optimize(f, 0, 1);

        assertNull(result);
    }
}

class Function1D {
    abstract public double value(double x);
}