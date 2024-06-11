package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.FastMath;

import org.junit.Test;

public class ContinuedFractionTest {
    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethods() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected double getB(int n, double x) {
                throw new UnsupportedOperationException();
            }
        };

        try {
            cf.evaluate(0.0);
            fail("Expected UnsupportedOperationException");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateSimpleContinuedFraction() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                switch (n) {
                case 0:
                    return 1.0;
                case 1:
                    return 1.0;
                case 2:
                    return 2.0;
                case 3:
                    return 1.0;
                default:
                    return 0.0;
                }
            }

            @Override
            protected double getB(int n, double x) {
                switch (n) {
                case 0:
                    return 1.0;
                case 1:
                    return 1.0;
                case 2:
                    return 2.0;
                case 3:
                    return 1.0;
                default:
                    return 0.0;
                }
            }
        };

        assertEquals(3.0, cf.evaluate(0.0), 1e-15);
    }

    @Test
    public void testEvaluateComplexContinuedFraction() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                switch (n) {
                case 0:
                    return 1.0;
                case 1:
                    return 1.0;
                case 2:
                    return 2.0;
                case 3:
                    return -2.0;
                case 4:
                    return 1.0;
                default:
                    return 0.0;
                }
            }

            @Override
            protected double getB(int n, double x) {
                switch (n) {
                case 0:
                    return 1.0;
                case 1:
                    return 1.0;
                case 2:
                    return 1.0;
                case 3:
                    return -2.0;
                case 4:
                    return 1.0;
                default:
                    return 0.0;
                }
            }
        };

        assertEquals(sqrt(2.0), cf.evaluate(0.0), 1e-15);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return -1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        try {
            cf.evaluate(0.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return Math.pow(2.0, n);
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        try {
            cf.evaluate(0.0, 0, 1);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateWithGivenEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return Math.pow(2.0, n);
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        double epsilon = 1e-8;
        assertEquals(Math.log(2.0) / Math.log(2.0), cf.evaluate(0.0, epsilon, 100), epsilon);
    }

    @Test
    public void testEvaluateWithGivenMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return Math.pow(2.0, n);
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        double epsilon = 1e-15;
        assertEquals(Math.log(2.0) / Math.log(2.0), cf.evaluate(0.0, epsilon, 10), epsilon);
    }

    @Test
    public void testEvaluateWithGivenEpsilonAndMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return Math.pow(2.0, n);
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        double epsilon = 1e-15;
        double maxIterations = 20;
        assertEquals(Math.log(2.0) / Math.log(2.0), cf.evaluate(0.0, epsilon, maxIterations), epsilon);
    }

    @Test
    public void testEvaluateWithInfiniteA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return Double.POSITIVE_INFINITY;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        try {
            cf.evaluate(0.0, 1e-8, 100);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateWithNegativeA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return -Math.pow(2.0, n);
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        try {
            cf.evaluate(0.0, 1e-8, 100);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateWithNegativeB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return -Math.pow(2.0, n);
            }
        };

        try {
            cf.evaluate(0.0, 1e-8, 100);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateWithNaNB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return Math.NaN;
            }
        };

        try {
            cf.evaluate(0.0, 1e-8, 100);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    private static double sqrt(double value) {
        return MathUtils.sqrt(value);
    }
}