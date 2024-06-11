package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.ScalarDifferentiableOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Test function for optimization. */
    private static final UnivariateFunction TEST_FUNCTION = new TestFunction();

    /** Optimizer under test. */
    private UnivariateOptimizer optimizer;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-8),
                new BrentSolver()) {
            @Override
            protected Preconditioner getPreconditioner() {
                return new Preconditioner() {
                    @Override
                    public double[] precondition(double[] variables, double[] r) {
                        for (int i = 0; i < variables.length; ++i) {
                            r[i] *= 2;
                        }
                        return r;
                    }
                };
            }
        };
    }

    /**
     * Test optimization with a simple function.
     */
    @Test
    public void testSimpleFunction() {
        final double x0 = 0.5;
        final double xExpected = 0.32305858;

        optimizer.optimize(new InitialGuess(x0), TEST_FUNCTION, GoalType.MINIMIZE);
        PointValuePair pvp = optimizer.getCurrentPointAndValue();
        Assert.assertEquals(xExpected, pvp.getPoint()[0], 1e-6);
        Assert.assertEquals(-0.01625591, pvp.getValue(), 1e-6);
    }

    /**
     * Test optimization with an incorrect initial guess.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testIncorrectInitialGuess() {
        optimizer.optimize(new InitialGuess(-1), TEST_FUNCTION, GoalType.MINIMIZE);
    }
}