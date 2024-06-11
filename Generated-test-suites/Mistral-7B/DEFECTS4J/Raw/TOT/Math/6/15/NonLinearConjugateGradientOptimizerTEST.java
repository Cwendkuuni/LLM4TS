package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugate.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugate.PolakRibiereConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.linear.BrentSolver;
import org.apache.commons.math3.optim.nonlinear.linear.SimpleBounds;
import org.apache.commons.math3.test.AbstractMathTestCase;

/**
 * Test cases for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest extends AbstractMathTestCase {

    /**
     * Test the optimizer with a simple 1D function.
     */
    public void testSimple() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        final UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-8),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.optimize(function, new SimpleBounds(0, 5));
        double[] result = optimizer.getCurrentPoint();
        assertEquals(2.0, result[0], 1e-8);
    }

    /**
     * Test the optimizer with a simple 1D function with initial point far
     * from the optimum.
     */
    public void testInitialPointFar() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        final UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-8),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.optimize(function, new SimpleBounds(-5, 5));
        double[] result = optimizer.getCurrentPoint();
        assertEquals(2.0, result[0], 1e-8);
    }

    /**
     * Test the optimizer with a simple 1D function that has a local minimum and
     * a local maximum.
     */
    public void testLocalMinimumAndMaximum() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 3) * (x - 3) - 2 * Math.log(Math.abs(x - 2));
            }
        };
        final UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-8),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.optimize(function, new SimpleBounds(-5, 5));
        double[] result = optimizer.getCurrentPoint();
        assertEquals(2.0, result[0], 1e-8);
    }

    /**
     * Test the optimizer with a simple 1D function where the initial point is
     * outside of the bounds.
     */
    public void testInitialPointOutsideBounds() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        final UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-8),
                new BrentSolver(),
                new IdentityPreconditioner());
        try {
            optimizer.optimize(function, new SimpleBounds(-5, 0));
            fail();
        } catch (IllegalArgumentException e) {
            // expected result
        }
    }

    /**
     * Test the optimizer with a simple 1D function where the search direction
     * is not updated correctly.
     */
    public void testSearchDirectionNotUpdated() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        final UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-8),
                new BrentSolver(),
                new IdentityPreconditioner());
        try {
            optimizer.optimize(function, new SimpleBounds(0, 1));
            fail();
        } catch (MathIllegalStateException e) {
            // expected result
        }
    }
}