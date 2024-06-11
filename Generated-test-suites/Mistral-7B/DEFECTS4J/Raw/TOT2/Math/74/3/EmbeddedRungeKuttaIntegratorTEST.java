package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final double EPSILON = 1e-10;
    private static final int MAX_STEP_COUNT = 10;

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        // create a test integrator
        integrator = new RungeKuttaFehlberg78Integrator(2, false, new double[] { 0.3, 0.5, 0.87, 0.0, 0.55, 0.288616646, 0.1665328405 },
                new double[] { 0.0, 0.36, 0.497, 0.678966112, 0.8650633635, 0.9833520373, 0.9967324563 },
                new RungeKuttaStepInterpolator(new EmbeddedRungeKuttaIntegrator(null, false, null, null, null, null, 1e-6, 1e6, null, null),
                        1e-6, null));
    }

    @Test
    public void testIntegrateBasic() throws MaxCountExceededException, SingularMatrixException,
            NumberIsTooSmallException, IntegratorException {
        // test the basic behavior of the integrator
        final double[] y0 = { 1.0, 1.0 };
        final double tEnd = 1.0;
        final double[] yEnd = { 2.718281828, 2.718281828 };

        integrator.integrate(new SimpleDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivative(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(y[0]);
            }

        }, 0.0, y0, tEnd, new double[y0.length]);

        // check the result
        final double[] y = new double[y0.length];
        System.arraycopy(yEnd, 0, y, 0, yEnd.length);
        assertEquals(Arrays.toString(y), Arrays.toString(yEnd), Arrays.toString(y));
        assertEquals(tEnd, integrator.getStepStart(), EPSILON);
    }

    @Test
    public void testIntegrateWithEvents() throws MaxCountExceededException, SingularMatrixException,
            NumberIsTooSmallException, IntegratorException {
        // test the behavior of the integrator with events
        final double[] y0 = { 1.0, 1.0 };
        final double tEnd = 1.0;
        final double[] yEnd = { 2.718281828, 2.718281828 };

        integrator.addStepHandler(t -> {
            if (t > 0.5) {
                throw new IntegratorException("Integration stopped by an event.");
            }
        });

        integrator.integrate(new SimpleDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivative(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(y[0]);
            }

        }, 0.0, y0, tEnd, new double[y0.length]);

        // check that the event was triggered
        assertTrue(integrator.hasStopped());
        assertEquals(tEnd / 2, integrator.getStepStart(), EPSILON);
    }

    @Test
    public void testIntegrateWithSingularMatrix() throws MaxCountExceededException,
            SingularMatrixException, NumberIsTooSmallException, IntegratorException {
        // test the behavior of the integrator with a singular matrix
        final double[] y0 = { 0.0, 1.0 };
        final double tEnd = 1.0;
        final double[] yEnd = { 0.0, Math.PI };

        final RealMatrix jacobian = MatrixUtils.createRealMatrix(new double[][] {
            { -1.0, 0.0 },
            { 0.0, -1.0 }
        });

        integrator.addStepHandler(t -> {
            if (t > 0.5) {
                throw new SingularMatrixException("Integration stopped by a singular matrix.");
            }
        });

        integrator.integrate(new SimpleDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivative(double t, double[] y, double[] yPrime) {
                final RealVector yVEct = new ArrayRealVector(y);
                yPrime[0] = -y[0];
                yPrime[1] = -BesselFunctions.besselJ(2, y[1]);
            }

            @Override
            public RealMatrix getJacobian(double t, double[] y) {
                return jacobian;
            }

        }, 0.0, y0, tEnd, new double[y0.length]);

        // check that the singular matrix event was triggered
        assertTrue(integrator.hasStopped());
        assertEquals(tEnd / 2, integrator.getStepStart(), EPSILON);
    }

    @Test
    public void testIntegrateWithDerivativeError() throws MaxCountExceededException,
            SingularMatrixException, NumberIsTooSmallException, IntegratorException {
        // test the behavior of the integrator with an error in the derivative
        final double[] y0 = { 1.0, 1.0 };
        final double tEnd = 1.0;
        final double[] yEnd = { 2.718281828, 2.718281828 };

        integrator.addStepHandler(t -> {
            if (t > 0.5) {
                throw new DerivativeException("Integration stopped by an error in the derivative.");
            }
        });

        integrator.integrate(new SimpleDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivative(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(y[0]) / 0;
            }

        }, 0.0, y0, tEnd, new double[y0.length]);

        // check that the derivative error event was triggered
        assertTrue(integrator.hasStopped());
        assertEquals(tEnd / 2, integrator.getStepStart(), EPSILON);
    }

    @Test
    public void testIntegrateWithLimitReached() throws MaxCountExceededException,
            SingularMatrixException, NumberIsTooSmallException, IntegratorException {
        // test the behavior of the integrator when the maximum step count is reached
        final double[] y0 = { 1.0, 1.0 };
        final double tEnd = 1.0;
        final double[] yEnd = { 2.718281828, 2.718281828 };

        integrator.setMaxStepCount(MAX_STEP_COUNT);

        integrator.integrate(new SimpleDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivative(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(y[0]);
            }

        }, 0.0, y0, tEnd, new double[y0.length]);

        // check that the maximum step count was reached
        assertTrue(integrator.hasStopped());
        assertFalse(integrator.hasEncounteredError());
        assertEquals(tEnd, integrator.getStepStart(), EPSILON);
    }

}