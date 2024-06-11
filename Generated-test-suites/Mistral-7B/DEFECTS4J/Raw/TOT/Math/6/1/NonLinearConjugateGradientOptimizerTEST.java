package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.ScaledConjugateGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.convergence.SimpleValueChecker;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    private UnivariateFunction f;
    private Preconditioner preconditioner;
    private UnivariateOptimizer optimizer;

    @Before
    public void setUp() {
        f = x -> Math.pow(x, 2) + Math.exp(x) - E.E;
        preconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                for (int i = 0; i < variables.length; ++i) {
                    r[i] *= variables[i];
                }
                return r;
            }
        };
        optimizer = new UnivariateOptimizer(new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-6, 1e-6),
                new BrentSolver(),
                preconditioner));
    }

    /**
     * Tests optimization of simple function.
     */
    @Test
    public void testSimpleFunction() {
        optimizer.optimize(f, -5, 5);
        final double optimum = optimizer.getValue();
        Assert.assertEquals(E.E, optimum, 1e-6);
        Assert.assertArrayEquals(new double[] {0}, optimizer.getPoint(), 1e-6);
    }

    /**
     * Tests optimization of function with multiple roots.
     */
    @Test
    public void testMultipleRoots() {
        optimizer.optimize(f, -5, 5);
        final double optimum1 = optimizer.getValue();
        Assert.assertEquals(E.E, optimum1, 1e-6);
        Assert.assertArrayEquals(new double[] {-1}, optimizer.getPoint(), 1e-6);

        optimizer.reset();
        optimizer.optimize(f, -5, 5);
        final double optimum2 = optimizer.getValue();
        Assert.assertEquals(E.E, optimum2, 1e-6);
        Assert.assertArrayEquals(new double[] {1}, optimizer.getPoint(), 1e-6);
    }

    /**
     * Tests optimization of function with no roots in the given interval.
     */
    @Test
    public void testNoRoots() {
        optimizer.optimize(x -> x * x, 1, 2);
        Assert.assertEquals(Double.NaN, optimizer.getValue(), 0);
        Assert.assertArrayEquals(new double[] {}, optimizer.getPoint(), 0);
    }

    /**
     * Tests optimization of function with an error in the input.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testInputError() {
        optimizer.optimize(null, 0, 0);
    }
}