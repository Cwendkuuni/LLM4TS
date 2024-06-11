package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;

/**
 * Test cases for NonLinearConjugateGradientOptimizer class
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test constructor with default {@link SimpleValueChecker checker},
     * {@link BrentSolver line search solver} and
     * {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testConstructorWithDefaults() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        assertNotNull(optimizer.getSolver());
        assertNotNull(optimizer.getPreconditioner());
    }

    /**
     * Test constructor with default {@link IdentityPreconditioner preconditioner}.
     * @param updateFormula formula to use for updating the &beta; parameter,
     * must be one of {@link ConjugateGradientFormula#FLETCHER_REEVES} or {@link
     * ConjugateGradientFormula#POLAK_RIBIERE}.
     * @param checker Convergence checker.
     * @param lineSearchSolver Solver to use during line search.
     */
    @Test
    public void testConstructorWithPredefinedComponents(ConjugateGradientFormula updateFormula) {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 100);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        assertEquals(updateFormula, optimizer.getUpdateFormula());
        assertEquals(checker, optimizer.getChecker());
        assertEquals(lineSearchSolver, optimizer.getSolver());
        assertEquals(preconditioner, optimizer.getPreconditioner());
    }

    /**
     * Test optimization of a simple function.
     * @throws Exception if optimization fails
     */
    @Test
    public void testOptimizationWithSimpleFunction() throws Exception {
        double[] testValues = new double[] {-1.0, -0.5, 0.0, 0.5, 1.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }

            @Override
            public double derivative(double x) {
                return 4.0 * (x - 2.0);
            }
        };
        InitialGuess initialGuess = new InitialGuess(0.5);
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-8, 100);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                checker,
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        optimizer.optimize(function, initialGuess, new MaxEval(50));
        PointValuePair result = optimizer.getResult();
        assertTrue(checker.isConverged(optimizer.getIterations(), result, optimizer.getLastPoint(), optimizer.getLastValue()));
        assertEquals(2.0, result.getPoint()[0], 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    /**
     * Test optimization with a function that has multiple local minima.
     * @throws Exception if optimization fails
     */
    @Test
    public void testOptimizationWithFunctionMultipleLocalMinima() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(Math.PI * x), 2.0) - Math.pow(Math.cos(Math.PI * x), 2.0);
            }

            @Override
            public double derivative(double x) {
                return 2.0 * (Math.sin(Math.PI * x) * (Math.cos(Math.PI * x) + Math.sin(Math.PI * x)) +
                        Math.cos(Math.PI * x) * (-Math.sin(Math.PI * x) + Math.cos(Math.PI * x)));
            }
        };
        InitialGuess initialGuess = new InitialGuess(0.5);
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-8, 100);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                checker,
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        optimizer.optimize(function, initialGuess, new MaxEval(50));
        PointValuePair result = optimizer.getResult();
        assertTrue(checker.isConverged(optimizer.getIterations(), result, optimizer.getLastPoint(), optimizer.getLastValue()));
        double expectedValue = -1.2701274146897962;
        double expectedX = 0.5326191890857264;
        assertEquals(expectedValue, result.getValue(), 1e-5);
        assertEquals(expectedX, result.getPoint()[0], 1e-5);
    }

    /**
     * Test optimization failure with a function that has no local minima.
     * @throws Exception if optimization fails
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizationWithFunctionNoLocalMinima() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(Math.PI * x);
            }
        };
        InitialGuess initialGuess = new InitialGuess(0.5);
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-8, 100);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                checker,
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        optimizer.optimize(function, initialGuess, new MaxEval(1000));
        fail("Optimization should have failed.");
    }

    /**
     * Test optimization failure with a function that has an infinite local minimum.
     * @throws Exception if optimization fails
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizationWithFunctionInfiniteLocalMinima() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }

            @Override
            public double derivative(double x) {
                return -1.0 / (x * x);
            }
        };
        InitialGuess initialGuess = new InitialGuess(0.5);
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-8, 100);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                checker,
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        try {
            optimizer.optimize(function, initialGuess, new MaxEval(1000));
            fail("Optimization should have failed.");
        } catch (MaxEvalExceededException e) {
            // Expected exception
        }
    }

    /**
     * Test optimization failure with an ill-defined function.
     * @throws Exception if optimization fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithIllDefinedFunction() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public double derivative(double x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        InitialGuess initialGuess = new InitialGuess(0.5);
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-8, 100);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                checker,
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        try {
            optimizer.optimize(function, initialGuess, new MaxEval(1000));
            fail("Optimization should have failed.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test optimization failure with an non-differentiable function.
     * @throws Exception if optimization fails
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizationWithNonDifferentiableFunction() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.sqrt(Math.abs(x));
                } else {
                    throw new UnsupportedOperationException("Derivative not defined for x <= 0");
                }
            }

            @Override
            public double derivative(double x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        InitialGuess initialGuess = new InitialGuess(0.5);
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-8, 100);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                checker,
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        try {
            optimizer.optimize(function, initialGuess, new MaxEval(1000));
            fail("Optimization should have failed.");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
}