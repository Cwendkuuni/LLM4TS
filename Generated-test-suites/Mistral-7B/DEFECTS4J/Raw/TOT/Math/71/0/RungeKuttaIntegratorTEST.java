package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativeSizeEvent;
import org.apache.commons.math3.ode.events.WhenPosReachEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /** The integrator under test. */
    private RungeKuttaIntegrator integrator;

    /** The differential equations to integrate. */
    private FirstOrderDifferentialEquations equations;

    /** The step handler. */
    private StepHandler stepHandler;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        // create the differential equations to integrate
        equations = new SimpleDifferentialEquations();
        // create a step handler that prints some debug information
        stepHandler = new SimpleStepHandler();
        // create the integrator under test
        integrator = new RungeKutta23Integrator(0.01);
    }

    /**
     * Tests the integration of a simple set of differential equations,
     * without any event.
     * @throws DerivativeException if the derivatives cannot be computed
     * @throws IntegratorException if the integration fails
     */
    @Test
    public void testIntegration() throws DerivativeException, IntegratorException {
        // integrate the differential equations from t = 0 to t = 1
        double[] y = new double[]{ 1.0, 0.0 };
        double tStart = 0.0;
        double tEnd = 1.0;
        integrator.integrate(equations, tStart, y, tEnd, new double[y.length]);
        // check that the result is correct
        double[] expected = new double[]{ 2.718281828, 2.718281828 };
        assertArrayEquals("The integration result is incorrect.", expected, y);
    }

    /**
     * Tests the integration of a set of differential equations with a
     * relative size event at t = 1.
     * @throws DerivativeException if the derivatives cannot be computed
     * @throws IntegratorException if the integration fails
     */
    @Test
    public void testIntegrationWithEvent() throws DerivativeException, IntegratorException {
        // integrate the differential equations from t = 0 to t = 1.5
        double[] y = new double[]{ 1.0, 0.0 };
        double tStart = 0.0;
        double tEnd = 1.5;
        integrator.setEventsHandler(new CombinedEventsManager());
        integrator.addEvent(new RelativeSizeEvent(1.0, 1.0e-6));
        integrator.integrate(equations, tStart, y, tEnd, new double[y.length]);
        // check that the result is correct
        double[] expected = new double[]{ 2.718281828, 1.337658877 };
        assertArrayEquals("The integration result is incorrect.", expected, y);
        // check that the event has been triggered
        assertTrue("The event has not been triggered.", integrator.isDone());
    }

    /**
     * Tests the integration of a set of differential equations with a
     * when position reaches event at t = 1.
     * @throws DerivativeException if the derivatives cannot be computed
     * @throws IntegratorException if the integration fails
     */
    @Test
    public void testIntegrationWithPositionReachEvent() throws DerivativeException, IntegratorException {
        // integrate the differential equations from t = 0 to t = 1.5
        double[] y = new double[]{ 0.0, 1.0 };
        double tStart = 0.0;
        double tEnd = 1.5;
        integrator.setEventsHandler(new CombinedEventsManager());
        integrator.addEvent(new WhenPosReachEvent(1.0, new double[]{ 1.0, 0.0 }));
        integrator.integrate(equations, tStart, y, tEnd, new double[y.length]);
        // check that the result is correct
        double[] expected = new double[]{ 1.0, 0.0 };
        assertArrayEquals("The integration result is incorrect.", expected, y);
        // check that the event has been triggered
        assertTrue("The event has not been triggered.", integrator.isDone());
    }

    /**
     * Tests the integration of a set of differential equations with an
     * illegal step size.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrationWithIllegalStepSize() throws IntegratorException {
        // create an integrator with a negative step size
        RungeKuttaIntegrator integrator = new RungeKutta23Integrator(-0.01);
        // integrate the differential equations from t = 0 to t = 1
        double[] y = new double[]{ 1.0, 0.0 };
        double tStart = 0.0;
        double tEnd = 1.0;
        integrator.integrate(equations, tStart, y, tEnd, new double[y.length]);
    }

    /**
     * Tests the integration of a set of differential equations with a
     * non-square matrix.
     * @throws SingularMatrixException if the matrix is singular
     * @throws IntegratorException if the integration fails
     */
    @Test(expected = SingularMatrixException.class)
    public void testIntegrationWithNonSquareMatrix() throws IntegratorException {
        // create a non-square matrix as the Butcher array
        double[][] a = new double[][]{
            {0.0, 0.0},
            {1.0, 0.0}
        };
        // create an integrator with the non-square matrix
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", new double[0], a, new double[0], null, 0.01) {
            @Override
            public double[][] a() {
                return a;
            }
        };
        // integrate the differential equations from t = 0 to t = 1
        double[] y = new double[]{ 1.0, 0.0 };
        double tStart = 0.0;
        double tEnd = 1.0;
        integrator.integrate(equations, tStart, y, tEnd, new double[y.length]);
    }

    /**
     * Test step handler.
     */
    private static class SimpleStepHandler implements StepHandler {
        /** The current step number. */
        private int step = 0;

        /**
         * Resets the step handler.
         */
        public void reset() {
            step = 0;
        }

        /**
         * Handles a step.
         * @param interpolator the interpolator
         * @param lastStep true if this is the last step
         */
        public void handleStep(RungeKuttaStepInterpolator interpolator, boolean lastStep) {
            ++step;
            System.out.println("Step " + step + ": t = " + interpolator.getStepTime() +
                    ", y = " + java.util.Arrays.toString(interpolator.getStepValues()));
            if (lastStep) {
                System.out.println("Last step.");
            }
        }
    }

    /**
     * Simple differential equations.
     */
    private static class SimpleDifferentialEquations implements FirstOrderDifferentialEquations {
        /**
         * Computes the derivatives.
         * @param t the current time
         * @param y the current state
         * @return the derivatives
         */
        public double[] computeDerivatives(double t, double[] y) {
            double[] result = new double[y.length];
            for (int i = 0; i < result.length; ++i) {
                result[i] = y[i] * (1.0 - y[i]);
            }
            return result;
        }
    }

}