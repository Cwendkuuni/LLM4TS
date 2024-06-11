package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.Gradient;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Fibonacci1D;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NoDerivativesProvidedException;
import org.apache.commons.math3.optim.simple.SimpleValueChecker;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMaxIterations() {
        UnivariateRealOptimizer optimizer = new Fibonacci1D();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());
        int maxIterations = 10;
        msOptimizer.setMaximalIterationCount(maxIterations);
        UnivariateRealFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public Gradient gradient(double x) throws NoDerivativesProvidedException {
                return new Gradient() {
                    @Override
                    public double value(double x) {
                        return Math.cos(x);
                    }
                };
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        try {
            PointValuePair result = msOptimizer.optimize(f, goalType, min, max);
            assertTrue(msOptimizer.getIterationCount() <= maxIterations);
        } catch (Exception e) {
            // ignored
        }
    }

    @Test
    public void testMaxEvaluations() {
        UnivariateRealOptimizer optimizer = new Fibonacci1D();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());
        int maxEvaluations = 20;
        msOptimizer.setMaxEvaluations(maxEvaluations);
        UnivariateRealFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public Gradient gradient(double x) throws NoDerivativesProvidedException {
                return new Gradient() {
                    @Override
                    public double value(double x) {
                        return Math.cos(x);
                    }
                };
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        try {
            PointValuePair result = msOptimizer.optimize(f, goalType, min, max);
            assertTrue(msOptimizer.getEvaluations() <= maxEvaluations);
        } catch (Exception e) {
            // ignored
        }
    }

    @Test
    public void testOptimaAndOptimaValues() throws Exception {
        UnivariateRealOptimizer optimizer = new Fibonacci1D();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());
        UnivariateRealFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public Gradient gradient(double x) throws NoDerivativesProvidedException {
                return new Gradient() {
                    @Override
                    public double value(double x) {
                        return Math.cos(x);
                    }
                };
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double[] expectedOptima = {1.23456, 2.34567, 3.45678, 4.56789, 5.67891};
        double[] expectedOptimaValues = {-0.9999, -0.9998, -0.9997, -0.9996, -0.9995};
        PointValuePair result = msOptimizer.optimize(f, goalType, min, max);
        double[] actualOptima = msOptimizer.getOptima();
        double[] actualOptimaValues = msOptimizer.getOptimaValues();
        assertArrayEquals(expectedOptima, actualOptima, 0.001);
        assertArrayEquals(expectedOptimaValues, actualOptimaValues, 0.001);
    }

    @Test
    public void testConvergenceException() {
        UnivariateRealOptimizer optimizer = new Fibonacci1D();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());
        UnivariateRealFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test");
            }

            @Override
            public Gradient gradient(double x) throws NoDerivativesProvidedException {
                return null;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        try {
            msOptimizer.optimize(f, goalType, min, max);
            assertFalse(true);
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testIllegalStateException() {
        UnivariateRealOptimizer optimizer = new Fibonacci1D();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());
        UnivariateRealFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public Gradient gradient(double x) throws NoDerivativesProvidedException {
                return new Gradient() {
                    @Override
                    public double value(double x) {
                        return Math.cos(x);
                    }
                };
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        try {
            msOptimizer.optimize(f, goalType, min, max);
            msOptimizer.optimize(f, goalType, min, max);
            try {
                msOptimizer.getOptima();
                assertFalse(true);
            } catch (IllegalStateException e) {
                // expected
            }
        } catch (Exception e) {
            // ignored
        }
    }

    @Test
    public void testMultipleStartPoints() {
        UnivariateRealOptimizer optimizer = new Fibonacci1D();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());
        UnivariateRealFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 1.0), 2);
            }

            @Override
            public Gradient gradient(double x) throws NoDerivativesProvidedException {
                return new Gradient() {
                    @Override
                    public double value(double x) {
                        return 2 * (x - 1.0);
                    }
                };
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        try {
            List<Double> expectedOptima = Arrays.asList(1.5, 2.5, 3.5, 4.5, 5.5);
            List<Double> expectedOptimaValues = Arrays.asList(0.5625, 0.5625, 0.5625, 0.5625, 0.5625);
            PointValuePair result = msOptimizer.optimize(f, goalType, min, max);
            double[] actualOptima = msOptimizer.getOptima();
            double[] actualOptimaValues = msOptimizer.getOptimaValues();
            assertArrayEquals(expectedOptima.stream().mapToDouble(Double::doubleValue).toArray(), actualOptima, 0.001);
            assertArrayEquals(expectedOptimaValues.stream().mapToDouble(Double::doubleValue).toArray(), actualOptimaValues, 0.001);
        } catch (Exception e) {
            // ignored
        }
    }

    @Test
    public void testMaxIterationsReached() {
        UnivariateRealOptimizer optimizer = new Fibonacci1D();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());
        UnivariateRealFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public Gradient gradient(double x) throws NoDerivativesProvidedException {
                return new Gradient() {
                    @Override
                    public double value(double x) {
                        return Math.cos(x);
                    }
                };
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        try {
            msOptimizer.setMaximalIterationCount(1);
            msOptimizer.optimize(f, goalType, min, max);
            assertNull(msOptimizer.getOptima());
            assertNull(msOptimizer.getOptimaValues());
        } catch (Exception e) {
            // ignored
        }
    }

    @Test
    public void testMaxEvaluationsReached() {
        UnivariateRealOptimizer optimizer = new Fibonacci1D();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());
        UnivariateRealFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public Gradient gradient(double x) throws NoDerivativesProvidedException {
                return new Gradient() {
                    @Override
                    public double value(double x) {
                        return Math.cos(x);
                    }
                };
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        msOptimizer.setMaxEvaluations(1);
        try {
            msOptimizer.optimize(f, goalType, min, max);
            assertNull(msOptimizer.getOptima());
            assertNull(msOptimizer.getOptimaValues());
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }
}