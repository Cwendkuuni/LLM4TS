package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.OptimizationProblem;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.InitialGuess.RandomInitialStrategy;
import org.apache.commons.math3.optim.PointValuePairCollection;
import org.apache.commons.math3.optim.NonSmoothedPointValuedFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MAXIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.FIND_MIN_MAX;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.FIND_MIN_ONLY;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.FIND_MAX_ONLY;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.VALUE_RANGE;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.VALUE_MIN_MAX;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PowellOptimizerTest {

    private MultivariateFunction function;
    private GoalType goalType;
    private PointValuePair expectedResult;
    private double[] startPoint;

    public PowellOptimizerTest(MultivariateFunction function,
                               GoalType goalType,
                               PointValuePair expectedResult,
                               double[] startPoint) {
        this.function = function;
        this.goalType = goalType;
        this.expectedResult = expectedResult;
        this.startPoint = startPoint;
    }

    @Parameterized.Parameters(name = "Function: {0}, GoalType: {1}, Start Point: {2}, Expected Result: {3}")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][]{
                // Test case 1: Rosenbrock function, minimize
                new MultivariateFunction() {
                    @Override
                    public double value(double[] x) {
                        double sum = 0;
                        for (int i = 1; i < x.length; i++) {
                            double term1 = 100 * (x[i - 1] - x[i]) * (x[i - 1] - x[i]);
                            double term2 = x[i - 1] - 1;
                            sum += term1 + term2 * term2;
                        }
                        return sum;
                    }
                },
                MINIMIZE,
                new PointValuePair(new double[]{-3.0, -3.0}, 0.0),
                new double[]{-3.0, -3.0},

                // Test case 2: Rastrigin function, minimize
                new NonSmoothedPointValuedFunction(new PointValueFunction() {
                    @Override
                    public double value(double[] x) {
                        double sum = 0;
                        double term;
                        for (int i = 0; i < x.length; i++) {
                            sum += Math.pow(x[i] * Math.PI, 2) - 5 * Math.cos(2 * Math.PI * x[i]) + 1;
                            term = Math.pow(Math.PI - x[i], 2);
                            sum += term * term;
                        }
                        return sum;
                    }
                }, new double[]{2.0, 2.0}),
                MINIMIZE,
                new PointValuePair(new double[]{0.0, 0.0}, 0.0),
                new double[]{0.0, 0.0},

                // Test case 3: Ackley function, minimize
                new NonSmoothedPointValuedFunction(new PointValueFunction() {
                    @Override
                    public double value(double[] x) {
                        double sum = 0;
                        double term1 = 20;
                        double term2 = 0;
                        double expi = 0;
                        for (int i = 0; i < x.length; i++) {
                            term1 -= Math.exp(Math.sin(Math.sqrt(x[i])));
                            term2 += Math.cos(Math.sqrt(x[i] * Math.PI));
                            expi += Math.exp(Math.sin(Math.sqrt(-x[i])));
                            sum += term1 * term1 + term2 * term2;
                        }
                        sum /= x.length;
                        sum -= Math.exp(1) + expi;
                        return sum;
                    }
                }, new double[]{0.1, 0.1}),
                MINIMIZE,
                new PointValuePair(new double[]{0.0, 0.0}, 0.0),
                new double[]{0.0, 0.0},

                // Test case 4: Shekel's F6 function, minimize
                new MultivariateFunction() {
                    @Override
                    public double value(double[] x) {
                        double term1 = Math.pow(Math.sin(Math.PI * x[0]), 2);
                        double term2 = Math.pow(Math.sin(Math.PI * x[1]), 2);
                        double term3 = Math.pow(Math.sin(Math.PI * x[2]), 2);
                        double term4 = Math.pow(Math.sin(Math.PI * x[3]), 2);
                        double term5 = Math.sin(Math.PI * x[0]);
                        double term6 = Math.sin(Math.PI * x[1]);
                        double term7 = Math.sin(Math.PI * x[2]);
                        double term8 = Math.sin(Math.PI * x[3]);
                        double sum = term1 + term2 + term3 + term4;
                        sum -= term5 * term6 * term7 * term8;
                        return sum;
                    }
                },
                MINIMIZE,
                new PointValuePair(new double[]{0.125, 0.125, 0.125, 0.125}, -1.0),
                new double[]{0.125, 0.125, 0.125, 0.125},

                // Test case 5: Sphere function, minimize
                new MultivariateFunction() {
                    @Override
                    public double value(double[] x) {
                        double sum = 0;
                        for (int i = 0; i < x.length; i++) {
                            sum += Math.pow(x[i], 2);
                        }
                        return sum;
                    }
                },
                MINIMIZE,
                new PointValuePair(new double[]{0.0, 0.0}, 0.0),
                new double[]{0.0, 0.0},

                // Test case 6: Sum of squares function, minimize
                new MultivariateFunction() {
                    @Override
                    public double value(double[] x) {
                        double sum = 0;
                        for (int i = 0; i < x.length; i++) {
                            sum += Math.pow(x[i], 2);
                        }
                        return sum;
                    }
                },
                MINIMIZE,
                new PointValuePair(new double[]{0.0}, 0.0),
                new double[]{0.0},

                // Test case 7: Sum of squares function, maximize
                new MultivariateFunction() {
                    @Override
                    public double value(double[] x) {
                        double sum = 0;
                        for (int i = 0; i < x.length; i++) {
                            sum += Math.pow(x[i], 2);
                        }
                        return sum;
                    }
                },
                MAXIMIZE,
                new PointValuePair(new double[]{0.0}, Double.POSITIVE_INFINITY),
                new double[]{0.0},

                // Test case 8: Sum of squares function, find minimum and maximum
                new MultivariateFunction() {
                    @Override
                    public double value(double[] x) {
                        double sum = 0;
                        for (int i = 0; i < x.length; i++) {
                            sum += Math.pow(x[i], 2);
                        }
                        return sum;
                    }
                },
                FIND_MIN_MAX,
                new PointValuePair(new PointValuePair(new double[]{0.0}, 0.0), new PointValuePair(new double[]{0.0}, Double.POSITIVE_INFINITY)),
                new double[]{0.0},

                // Test case 9: Sum of squares function, find minimum only
                new MultivariateFunction() {
                    @Override
                    public double value(double[] x) {
                        double sum = 0;
                        for (int i = 0; i < x.length; i++) {
                            sum += Math.pow(x[i], 2);
                        }
                        return sum;
                    }
                },
                FIND_MIN_ONLY,
                new PointValuePair(new PointValuePair(new double[]{0.0}, 0.0), null),
                new double[]{0.0},

                // Test case 10: Sum of squares function, find maximum only
                new MultivariateFunction() {
                    @Override
                    public double value(double[] x) {
                        double sum = 0;
                        for (int i = 0; i < x.length; i++) {
                            sum += Math.pow(x[i], 2);
                        }
                        return sum;
                    }
                },
                FIND_MAX_ONLY,
                new PointValuePair(null, new PointValuePair(new double[]{0.0}, Double.POSITIVE_INFINITY)),
                new double[]{0.0},

                // Test case 11: Sum of squares function, find value range
                new MultivariateFunction() {
                    @Override
                    public double value(double[] x) {
                        double sum = 0;
                        for (int i = 0; i < x.length; i++) {
                            sum += Math.pow(x[i], 2);
                        }
                        return sum;
                    }
                },
                VALUE_RANGE,
                new PointValuePair(new PointValuePair(new double[]{0.0}, 0.0), new PointValuePair(new double[]{0.0}, Double.POSITIVE_INFINITY)),
                new double[]{0.0},

                // Test case 12: Sum of squares function, find minimum and value range
                new MultivariateFunction() {
                    @Override
                    public double value(double[] x) {
                        double sum = 0;
                        for (int i = 0; i < x.length; i++) {
                            sum += Math.pow(x[i], 2);
                        }
                        return sum;
                    }
                },
                VALUE_MIN_MAX,
                new PointValuePair(new PointValuePair(new double[]{0.0}, 0.0), new PointValuePair(new double[]{0.0}, Double.POSITIVE_INFINITY)),
                new double[]{0.0}
        });
    }

    @Test
    public void testOptimize() throws NotStrictlyPositiveException {
        OptimizationProblem problem = new OptimizationProblem(function, startPoint);
        problem.setGoalType(goalType);

        PointValuePair result = new PowellOptimizer(1e-5, 1e-5).optimize(new MaxEval(100), problem);

        assertEquals("Unexpected result for function: " + function.getClass().getName() +
                ", goalType: " + goalType + ", startPoint: " + Arrays.toString(startPoint) +
                ", expectedResult: " + expectedResult, expectedResult, result);
    }
}