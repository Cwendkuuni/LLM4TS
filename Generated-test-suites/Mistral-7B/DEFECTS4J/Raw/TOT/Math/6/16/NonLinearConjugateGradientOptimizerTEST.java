package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradientOptimizer.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.functions.PolynomialFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Function to optimize. */
    private UnivariateFunction f;

    /** Optimizer. */
    private ConjugateGradientOptimizer optimizer;

    /** Convergence checker. */
    private ConvergenceChecker<PointValuePair> checker;

    /** Solver for line search. */
    private UnivariateSolver solver;

    /** Preconditioner. */
    private Preconditioner preconditioner;

    /**
     * Set up test.
     * @throws Exception if initialization fails
     */
    @Before
    public void setUp() throws Exception {
        f = new PolynomialFunction(3, -1, 1, -1, 1, 1);
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES) {
            @Override
            protected Preconditioner createPreconditioner() {
                return new IdentityPreconditioner();
            }
        };
        checker = new SimpleValueChecker(1e-14, 1e-14);
        solver = new BrentSolver();
        preconditioner = new IdentityPreconditioner();
    }

    /**
     * Test optimization of a simple function.
     */
    @Test
    public void testSimpleFunction() {
        final double min = optimizer.optimize(f, new GoalType(GoalType.MINIMIZE),
                new MaxEval(100), new InitialGuess(1.0), checker, solver, preconditioner);
        Assert.assertEquals(0.0, min, 1e-14);
    }

    /**
     * Test optimization of a function with multiple local minima.
     */
    @Test
    public void testMultipleMinima() {
        final double[] initialGuesses = new double[] {-1.0, 1.0, 3.0};
        for (double initialGuess : initialGuesses) {
            final double min = optimizer.optimize(f, new GoalType(GoalType.MINIMIZE),
                    new MaxEval(100), new InitialGuess(initialGuess), checker, solver, preconditioner);
            Assert.assertEquals(0.0, min, 1e-14);
        }
    }

    /**
     * Test optimization of a function with no solution.
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testNoSolution() {
        optimizer.optimize(f.negate(), new GoalType(GoalType.MINIMIZE), new MaxEval(100),
                new InitialGuess(1.0), checker, solver, preconditioner);
    }

    /**
     * Test optimization of a function with wrong initial guess.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testWrongInitialGuess() {
        optimizer.optimize(f, new GoalType(GoalType.MINIMIZE), new MaxEval(100),
                new InitialGuess(-1.0), checker, solver, preconditioner);
    }
}