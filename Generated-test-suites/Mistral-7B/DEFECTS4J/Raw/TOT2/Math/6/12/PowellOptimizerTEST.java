package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizerTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest extends UnivariateOptimizerTest {

    /**
     * Constructor.
     */
    public PowellOptimizerTest() {
        super(new PowellOptimizer(1e-6, 1e-6), new PowellOptimizer(1e-3, 1e-3),
              new PowellOptimizer(1e-4, 1e-4, new SimpleUnivariateValueChecker(1e-4, 1e-4)));
    }

    /**
     * Test for the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        try {
            new PowellOptimizer(1e-6, 1e-12);
            Assert.fail("Should have thrown an exception");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION_1, TEST_GOAL_MINIMIZE,
                                                   TEST_X_0, TEST_X_1);
        double[] expected = TEST_X_MIN;
        Assert.assertArrayEquals(expected, result.getPoint(), 1e-7);
    }

    /**
     * Test for the constructor with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceCheckerConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6,
                                                       new SimpleUnivariateValueChecker(1e-8, 1e-8));
        PointValuePair result = optimizer.optimize(TEST_FUNCTION_1, TEST_GOAL_MINIMIZE,
                                                   TEST_X_0, TEST_X_1);
        double[] expected = TEST_X_MIN;
        Assert.assertArrayEquals(expected, result.getPoint(), 1e-8);
    }

    /**
     * Test for the constructor with custom relative and absolute tolerances
     * and default convergence checker.
     */
    @Test
    public void testCustomTolerancesConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION_1, TEST_GOAL_MINIMIZE,
                                                   TEST_X_0, TEST_X_1);
        double[] expected = TEST_X_MIN;
        Assert.assertArrayEquals(expected, result.getPoint(), 1e-8);
    }

    /**
     * Test for the constructor with custom relative and absolute tolerances,
     * and custom convergence checker.
     */
    @Test
    public void testCustomTolerancesAndCustomCheckerConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8,
                                                       new MyCustomConvergenceChecker());
        PointValuePair result = optimizer.optimize(TEST_FUNCTION_1, TEST_GOAL_MINIMIZE,
                                                   TEST_X_0, TEST_X_1);
        double[] expected = TEST_X_MIN;
        Assert.assertArrayEquals(expected, result.getPoint(), 1e-8);
    }

    /**
     * Test for an incorrect number of dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfDimensions() {
        new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-4, 1e-4))
            .optimize(new UnivariateFunction() {
                        public double value(double x) {
                            return x * x;
                        }
                    },
                    TEST_GOAL_MINIMIZE,
                    new double[]{1.0, 1.0},
                    new double[]{0.0, 0.0});
    }

    /**
     * Test for an incorrect goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongGoalType() {
        new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-4, 1e-4))
            .optimize(new UnivariateFunction() {
                        public double value(double x) {
                            return x * x;
                        }
                    },
                    TEST_GOAL_MAXIMIZE,
                    new double[]{0.0},
                    new double[]{0.0});
    }

    /**
     * Test for a null start point.
     */
    @Test(expected = NullPointerException.class)
    public void testNullStartPoint() {
        new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-4, 1e-4))
            .optimize(new UnivariateFunction() {
                        public double value(double x) {
                            return x * x;
                        }
                    },
                    TEST_GOAL_MINIMIZE,
                    null,
                    new double[]{0.0});
    }

    /**
     * Test for a null end point.
     */
    @Test(expected = NullPointerException.class)
    public void testNullEndPoint() {
        new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-4, 1e-4))
            .optimize(new UnivariateFunction() {
                        public double value(double x) {
                            return x * x;
                        }
                    },
                    TEST_GOAL_MINIMIZE,
                    new double[]{0.0},
                    null);
    }

    /**
     * Test for a null convergence checker.
     */
    @Test(expected = NullPointerException.class)
    public void testNullConvergenceChecker() {
        new PowellOptimizer(1e-6, 1e-6, null)
            .optimize(new UnivariateFunction() {
                        public double value(double x) {
                            return x * x;
                        }
                    },
                    TEST_GOAL_MINIMIZE,
                    new double[]{0.0},
                    new double[]{0.0});
    }

    /**
     * Test for an incorrect convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectConvergenceChecker() {
        try {
            new PowellOptimizer(1e-6, 1e-6, new Object())
                .optimize(new UnivariateFunction() {
                            public double value(double x) {
                                return x * x;
                            }
                        },
                        TEST_GOAL_MINIMIZE,
                        new double[]{0.0},
                        new double[]{0.0});
            Assert.fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for an incorrect number of dimensions in the convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfDimensionsInConvergenceChecker() {
        try {
            new PowellOptimizer(1e-6, 1e-6, new ConvergenceChecker<PointValuePair>() {
                        public boolean converged(int iter, PointValuePair previous,
                                                PointValuePair current) {
                            throw new IllegalArgumentException();
                        }
                    })
                .optimize(new UnivariateFunction() {
                            public double value(double x) {
                                return x * x;
                            }
                        },
                        TEST_GOAL_MINIMIZE,
                        new double[]{0.0},
                        new double[]{0.0});
            Assert.fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for a null start point in convergence checker.
     */
    @Test(expected = NullPointerException.class)
    public void testNullStartPointInConvergenceChecker() {
        try {
            new PowellOptimizer(1e-6, 1e-6, new ConvergenceChecker<PointValuePair>() {
                        public boolean converged(int iter, PointValuePair previous,
                                                PointValuePair current) {
                            return previous.getPoint() == null;
                        }
                    })
                .optimize(new UnivariateFunction() {
                            public double value(double x) {
                                return x * x;
                            }
                        },
                        TEST_GOAL_MINIMIZE,
                        new double[]{0.0},
                        new double[]{0.0});
            Assert.fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test for a null end point in convergence checker.
     */
    @Test(expected = NullPointerException.class)
    public void testNullEndPointInConvergenceChecker() {
        try {
            new PowellOptimizer(1e-6, 1e-6, new ConvergenceChecker<PointValuePair>() {
                        public boolean converged(int iter, PointValuePair previous,
                                                PointValuePair current) {
                            return current.getPoint() == null;
                        }
                    })
                .optimize(new UnivariateFunction() {
                            public double value(double x) {
                                return x * x;
                            }
                        },
                        TEST_GOAL_MINIMIZE,
                        new double[]{0.0},
                        new double[]{0.0});
            Assert.fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test for a null optimization result.
     */
    @Test(expected = NullPointerException.class)
    public void testNullOptimizationResult() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(null, TEST_GOAL_MINIMIZE,
                           new double[]{0.0}, new double[]{0.0});
    }

    /**
     * Test for an incorrect optimization result.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectOptimizationResult() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(new UnivariateFunction() {
                               public double value(double x) {
                                   return x * x;
                               }
                           },
                           TEST_GOAL_MINIMIZE,
                           new double[]{1.0},
                           new PointValuePair[]{new PointValuePair(null, Double.NaN)});
    }
}

class MyCustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
    @Override
    public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
        return previous.getValue() > current.getValue();
    }
}