package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LcfGSolver;
import org.apache.commons.math3.analysis.solvers.NumericalMethodException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradientFormula;
import org.apache.commons.math3.optim.preconditions.IdentityPreconditioner;
import org.apache.commons.math3.optim.preconditions.Preconditioner;
import org.apache.commons.math3.test.BigDecimalTester;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimization of simple quadratic function f(x) = x^2.
     * @throws Exception if an error occurs during the optimization.
     */
    @Test
    public void testQuadraticFunction() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        final ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-8, 1e-8),
                new BrentSolver());

        optimizer.setInitialStep(1.0);

        // Test optimization from initial point x = 1.
        final PointValuePair result1 = optimizer.optimize(f, 1.0, new InitialGuess() {
            @Override
            public double[] getInitialGuess(int dimension) {
                return new double[] { 1.0 };
            }
        });
        final double objectiveValue1 = result1.getValue();
        final double[] solution1 = result1.getPoint();
        assertEquals(0.0, objectiveValue1, 1e-8);
        assertTrue(BigDecimalTester.isZero(new BigDecimal(solution1[0] - 0.5), 8));

        // Test optimization from initial point x = -1.
        final PointValuePair result2 = optimizer.optimize(f, -1.0, new InitialGuess() {
            @Override
            public double[] getInitialGuess(int dimension) {
                return new double[] { -1.0 };
            }
        });
        final double objectiveValue2 = result2.getValue();
        final double[] solution2 = result2.getPoint();
        assertEquals(0.0, objectiveValue2, 1e-8);
        assertTrue(BigDecimalTester.isZero(new BigDecimal(solution2[0] - 0.5), 8));
    }

    /**
     * Test optimization of a simple function with non-polynomial behavior.
     * @throws Exception if an error occurs during the optimization.
     */
    @Test
    public void testNonPolynomialFunction() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }

            @Override
            public double derivative(double x) {
                final double y = Math.sin(x) / x;
                return (Math.cos(x) / x - Math.sin(x) / (x * x)) / x;
            }
        };

        final ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-8, 1e-8),
                new LcfGSolver());

        optimizer.setInitialStep(1.0);

        // Test optimization from initial point x = 1.
        final PointValuePair result1 = optimizer.optimize(f, 1.0, new InitialGuess() {
            @Override
            public double[] getInitialGuess(int dimension) {
                return new double[] { 1.0 };
            }
        });
        final double objectiveValue1 = result1.getValue();
        final double[] solution1 = result1.getPoint();
        assertEquals(0.0, objectiveValue1, 1e-5);
        assertTrue(BigDecimalTester.isZero(new BigDecimal(solution1[0] - 1.5708), 8));

        // Test optimization from initial point x = -1.
        final PointValuePair result2 = optimizer.optimize(f, -1.0, new InitialGuess() {
            @Override
            public double[] getInitialGuess(int dimension) {
                return new double[] { -1.0 };
            }
        });
        final double objectiveValue2 = result2.getValue();
        final double[] solution2 = result2.getPoint();
        assertEquals(0.0, objectiveValue2, 1e-5);
        assertTrue(BigDecimalTester.isZero(new BigDecimal(solution2[0] - 1.5708), 8));
    }

    /**
     * Test optimization with an ill-conditioned preconditioner.
     * @throws Exception if an error occurs during the optimization.
     */
    @Test
    public void testIllConditionedPreconditioner() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }

            @Override
            public double derivative(double x) {
                final double sinX = Math.sin(x);
                return 2 * sinX * Math.cos(x);
            }
        };

        final Preconditioner identityPreconditioner = new IdentityPreconditioner();
        final Preconditioner illConditionedPreconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                final double[] result = new double[variables.length];
                for (int i = 0; i < variables.length; i++) {
                    result[i] = variables[i] * r[i] / 100;
                }
                return result;
            }
        };

        final ConjugateGradientOptimizer optimizer1 = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-8, 1e-8),
                new BrentSolver(),
                identityPreconditioner);

        final ConjugateGradientOptimizer optimizer2 = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-8, 1e-8),
                new BrentSolver(),
                illConditionedPreconditioner);

        // Test optimization with identity preconditioner.
        final PointValuePair result1 = optimizer1.optimize(f, 1.0, new InitialGuess() {
            @Override
            public double[] getInitialGuess(int dimension) {
                return new double[] { 1.0 };
            }
        });
        final double objectiveValue1 = result1.getValue();
        final double[] solution1 = result1.getPoint();
        assertEquals(0.0, objectiveValue1, 1e-8);
        assertTrue(BigDecimalTester.isZero(new BigDecimal(solution1[0] - 0.8414), 8));

        // Test optimization with ill-conditioned preconditioner.
        try {
            optimizer2.optimize(f, 1.0, new InitialGuess() {
                @Override
                public double[] getInitialGuess(int dimension) {
                    return new double[] { 1.0 };
                }
            });
            fail("Optimization with ill-conditioned preconditioner should have failed.");
        } catch (MathIllegalStateException e) {
            // Expected result
        }
    }

    /**
     * Test optimization with a non-converging function.
     * @throws Exception if an error occurs during the optimization.
     */
    @Test
    public void testNonConvergingFunction() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1 / (1 + Math.pow(Math.abs(x), 2));
            }

            @Override
            public double derivative(double x) {
                final double term1 = Math.pow(Math.abs(x), 2);
                final double term2 = 2 * x;
                return (term1 / (1 + Math.pow(Math.abs(x), 2)) * (term2 * Math.signum(x) - 1)) / (1 + Math.pow(Math.abs(x), 2));
            }
        };

        final ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-8, 1e-8),
                new BrentSolver());

        // Test optimization with a large initial step.
        try {
            optimizer.optimize(f, 10, new InitialGuess() {
                @Override
                public double[] getInitialGuess(int dimension) {
                    return new double[] { 0.0 };
                }
            });
            fail("Optimization with a non-converging function should have failed.");
        } catch (NumericalMethodException e) {
            // Expected result
        }

        // Test optimization with a small initial step.
        final PointValuePair result = optimizer.optimize(f, 0.001, new InitialGuess() {
            @Override
            public double[] getInitialGuess(int dimension) {
                return new double[] { 0.0 };
            }
        });
        final double objectiveValue = result.getValue();
        final double[] solution = result.getPoint();
        assertTrue(solution[0] > 0);
        assertTrue(solution[0] < 1);
        assertTrue(BigDecimalTester.isZero(new BigDecimal(objectiveValue - 0.5), 8));
    }
}