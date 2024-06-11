package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.BrentSolver;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.IdentityPreconditioner;
import org.apache.commons.math3.optim.precise.PrecisePointValuePair;
import org.apache.commons.math3.optim.precise.PreciseRealVector;
import org.apache.commons.math3.optim.precise.PreciseUnivariateFunction;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimization of a simple function using the default settings.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        final UnivariateFunction f = new PreciseUnivariateFunction() {
            @Override
            public PreciseReal value(PreciseReal x) {
                return PreciseReal.ONE.subtract(x.abs().multiply(x).square());
            }

            @Override
            public PreciseReal value(double x, int precision) {
                return Math.pow(1 - Math.abs(x), 2);
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        optimizer.setInitialStep(0.1);

        UnivariateOptimizer result = optimizer.optimize(f, new PreciseReal(0), null);

        PointValuePair expected = new PrecisePointValuePair(new PrecisePointValuePair(
                0.0, 1.0), new PrecisePointValuePair(0.0, -1.0));

        assertEquals(expected.getValue(), result.getValue(), 1e-6);
        assertArrayEquals(expected.getPoint().toArray(), result.getPoint().toArray(), 1e-6);
    }

    /**
     * Test optimization of a simple function using a custom preconditioner.
     */
    @Test
    public void testOptimizeSimpleFunctionWithCustomPreconditioner() {
        final UnivariateFunction f = new PreciseUnivariateFunction() {
            @Override
            public PreciseReal value(PreciseReal x) {
                return PreciseReal.ONE.subtract(x.abs().multiply(x).square());
            }

            @Override
            public PreciseReal value(double x, int precision) {
                return Math.pow(1 - Math.abs(x), 2);
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new PrecisePreconditioner());

        optimizer.setInitialStep(0.1);

        UnivariateOptimizer result = optimizer.optimize(f, new PreciseReal(0), null);

        PointValuePair expected = new PrecisePointValuePair(new PrecisePointValuePair(
                0.0, 1.0), new PrecisePointValuePair(0.0, -1.0));

        assertEquals(expected.getValue(), result.getValue(), 1e-6);
        assertArrayEquals(expected.getPoint().toArray(), result.getPoint().toArray(), 1e-6);
    }

    /**
     * Test optimization of a function with a non-convex local minimum.
     */
    @Test
    public void testNonConvexFunction() {
        final UnivariateFunction f = new PreciseUnivariateFunction() {
            @Override
            public PreciseReal value(PreciseReal x) {
                return PreciseReal.ONE.subtract(Math.pow(Math.abs(x), 0.1));
            }

            @Override
            public PreciseReal value(double x, int precision) {
                return Math.pow(Math.abs(x), 0.1) > 1 ? Math.pow(Math.abs(x), 0.1) : 1 - Math.pow(Math.abs(x), 0.1);
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        optimizer.setInitialStep(1.0);

        try {
            optimizer.optimize(f, new PreciseReal(-2), null);
            fail("Should throw an exception");
        } catch (MathIllegalStateException e) {
            // Expected
        }

        UnivariateOptimizer result = optimizer.optimize(f, new PreciseReal(0), null);

        PointValuePair expected = new PrecisePointValuePair(new PrecisePointValuePair(
                Math.pow(0.1, 1 / 0.1), 1 - Math.pow(Math.pow(0.1, 1 / 0.1), 0.1)),
                new PrecisePointValuePair(Math.pow(0.1, 1 / 0.1), 1 - Math.pow(Math.pow(0.1, 1 / 0.1), 0.1)));

        assertEquals(expected.getValue(), result.getValue(), 1e-6);
        assertArrayEquals(expected.getPoint().toArray(), result.getPoint().toArray(), 1e-6);
    }
}

class PrecisePreconditioner implements Preconditioner {

    /** {@inheritDoc} */
    public double[] precondition(double[] variables, double[] r) {
        return r.clone();
    }
}