package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.GaussianEliminator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Tests the constructor with default settings.
     */
    @Test
    public void testDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        Assert.assertTrue(optimizer.isUseLU());
        Assert.assertNotNull(optimizer.getConvergenceChecker());
    }

    /**
     * Tests the constructor with specified settings.
     */
    @Test
    public void testSpecifiedConstructor() {
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, convergenceChecker);
        Assert.assertFalse(optimizer.isUseLU());
        Assert.assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Tests the optimization of a simple problem.
     */
    @Test
    public void testSimpleProblem() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) - 0.5, 2)
                        + Math.pow(Math.cos(x[1]) - 0.5, 2);
            }

            @Override
            public int numberOfVariables() {
                return 2;
            }

            @Override
            public InitialGuess getInitialGuess() {
                return new InitialGuess() {
                    @Override
                    public double[] getInitialPoint() {
                        return new double[]{0.0, 0.0};
                    }
                };
            }
        };

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        PointValuePair result = optimizer.optimize(problem, new MaxEval(100));
        double expected = Math.sqrt(0.5) * Math.PI / 2;
        Assert.assertArrayEquals(new double[]{expected, expected}, result.getPoint(), 1e-6);
    }

    /**
     * Tests the optimization with LU decomposition.
     */
    @Test
    public void testLUSolver() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) - 0.5, 2)
                        + Math.pow(Math.cos(x[1]) - 0.5, 2)
                        + Math.pow(Math.sin(x[2]) - 0.5, 2);
            }

            @Override
            public int numberOfVariables() {
                return 3;
            }

            @Override
            public InitialGuess getInitialGuess() {
                return new InitialGuess() {
                    @Override
                    public double[] getInitialPoint() {
                        return new double[]{0.0, 0.0, 0.0};
                    }
                };
            }
        };

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        PointValuePair result = optimizer.optimize(problem, new MaxEval(100));
        double expected = Math.sqrt(0.5) * Math.PI / 2;
        Assert.assertArrayEquals(new double[]{expected, expected, expected}, result.getPoint(), 1e-6);
    }

    /**
     * Tests the optimization with QR decomposition.
     */
    @Test
    public void testQRSolver() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) - 0.5, 2)
                        + Math.pow(Math.cos(x[1]) - 0.5, 2)
                        + Math.pow(Math.sin(x[2]) - 0.5, 2);
            }

            @Override
            public int numberOfVariables() {
                return 3;
            }

            @Override
            public InitialGuess getInitialGuess() {
                return new InitialGuess() {
                    @Override
                    public double[] getInitialPoint() {
                        return new double[]{0.0, 0.0, 0.0};
                    }
                };
            }
        };

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        PointValuePair result = optimizer.optimize(problem, new MaxEval(100));
        double expected = Math.sqrt(0.5) * Math.PI / 2;
        Assert.assertArrayEquals(new double[]{expected, expected, expected}, result.getPoint(), 1e-6);
    }

    /**
     * Tests the optimization with incorrect input.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        UnconstrainedOptimizationProblem problem = null;
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(problem, new MaxEval(100));
    }

    /**
     * Tests the optimization with incorrect convergence checker.
     */
    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }

            @Override
            public int numberOfVariables() {
                return 1;
            }

            @Override
            public InitialGuess getInitialGuess() {
                return new InitialGuess() {
                    @Override
                    public double[] getInitialPoint() {
                        return new double[]{0.0};
                    }
                };
            }
        };

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(null);
        optimizer.optimize(problem, new MaxEval(100));
    }
}