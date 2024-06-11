package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.NoEventsReachedException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.stepper.RungeKuttaStepInterpolator;

import org.junit.Test;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** Simple test function. */
    private static final UnivariateFunction TEST_FUNCTION = new PolynomialFunction(3, -1.0, 1.0, 1.0, -1.0, 0.0);

    /**
     * Test the {@link EmbeddedRungeKuttaIntegrator} with a simple test function.
     *
     * @throws MaxIterException if maximum iterations reached
     * @throws PrecisionException if no solution found within tolerance
     */
    @Test
    public void testSimpleIntegration() throws MaxIterException, PrecisionException {
        final double[] y0 = { 1.0, 0.0, 0.0 };
        final double t0 = 0.0;
        final double t1 = 1.0;

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKutta25FehlbergIntegrator(
                "Runge-Kutta-Fehlberg 2-5", false, new double[] { 0.0, 0.4, 0.2, 0.8, 1.0 / 3.0, 1.0 / 4.0, 0.25, 0.6, 0.875, 1.0 / 8.0, 1.0 / 15.0, 1.0 / 23.0, 1.0 / 42.0 },
                new double[][] { { 0.0, 0.4 }, { 0.2, 0.12 }, { 0.6, 0.324 }, { 0.875, 0.46768 }, { 1.0 / 3.0, 0.49664 }, { 1.0 / 4.0, 0.49928 }, { 0.25, 0.49984615 }, { 0.6, 0.677121525 },
                                { 0.875, 0.873283048 }, { 1.0 / 8.0, 0.898332511 }, { 1.0 / 15.0, 0.923707345 }, { 1.0 / 23.0, 0.941759831 }, { 1.0 / 42.0, 0.960840843 } },
                new double[] { 0.2, 0.0, 0.0 }, 0.001, 0.001, new RungeKuttaStepInterpolator() );

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public double[] derivatives(double t, double[] y) {
                return TEST_FUNCTION.value(y);
            }
        };

        integrator.integrate(equations, t0, y0, t1, null);

        // check results
        final double[] y1 = new double[] { 2.0, 2.0, 1.0 };
        assertEquals(y1[0], integrator.getY()[0], 1e-10);
        assertEquals(y1[1], integrator.getY()[1], 1e-10);
        assertEquals(y1[2], integrator.getY()[2], 1e-10);
    }

    /**
     * Test the {@link EmbeddedRungeKuttaIntegrator} with an event handler.
     *
     * @throws MaxIterException if maximum iterations reached
     * @throws PrecisionException if no solution found within tolerance
     * @throws NoEventsReachedException if no event occurs before the end of integration
     */
    @Test
    public void testIntegrationWithEvents() throws MaxIterException, PrecisionException, NoEventsReachedException {
        final double[] y0 = { 1.0, 0.0, 0.0 };
        final double t0 = 0.0;
        final double t1 = 3.0;

        final double eventTime = 2.0;

        final StepHandler eventHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int step) {
                if (step > 0) {
                    assertFalse(event.isAccepted());
                    assertEquals(event.getStepSize(), 1.0, 1e-10);
                }
            }

            @Override
            public boolean handleEvent(double t, double[] y) {
                if (t == eventTime) {
                    assertNotSame(y, y0);
                    assertEquals(y[0], 1.0, 1e-10);
                    assertEquals(y[1], 1.0, 1e-10);
                    assertEquals(y[2], 1.0, 1e-10);
                    return true;
                }
                return false;
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Integrator(
                "Runge-Kutta-Fehlberg 7-8", true, new double[] { 0.3, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2 },
                new double[][] { { 0.3, 0.3 }, { 0.38291, 0.3 }, { 0.67082, 0.34656 }, { 0.9, 0.33333 }, { 1.26845, 0.41124 },
                                { 1.57464, 0.55556 }, { 1.88672, 0.68889 }, { 2.18310, 0.81873 }, { 2.48066, 0.91667 },
                                { 2.77173, 0.97225 }, { 3.06679, 0.99216 }, { 3.36919, 0.99808 }, { 3.67917, 0.99959 } },
                new double[] { 0.06, 0.0, 0.0 }, 0.001, 0.001, new RungeKuttaStepInterpolator() );

        integrator.setStepHandlers(eventHandler);

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public double[] derivatives(double t, double[] y) {
                if (t > 1.0) {
                    throw new RuntimeException("no derivatives after event");
                }
                return TEST_FUNCTION.value(y);
            }
        };

        integrator.integrate(equations, t0, y0, t1, null);

        // check results
        final double[] y1 = new double[] { 2.0, 2.0, 1.0 };
        assertEquals(y1[0], integrator.getY()[0], 1e-10);
        assertEquals(y1[1], integrator.getY()[1], 1e-10);
        assertEquals(y1[2], integrator.getY()[2], 1e-10);

        // check event handling
        assertEquals(eventHandler.getNSteps(), 12);
        assertEquals(eventHandler.getEventTimes()[0], eventTime, 1e-10);
        assertEquals(eventHandler.getAcceptedSteps()[0], 1, 1e-10);
    }

    /**
     * Test the {@link EmbeddedRungeKuttaIntegrator} with an error scenario.
     *
     * @throws MaxIterException if maximum iterations reached
     * @throws PrecisionException if no solution found within tolerance
     */
    @Test
    public void testErrorScenario() throws MaxIterException, PrecisionException {
        final double[] y0 = { 1.0, 0.0, 0.0 };
        final double t0 = 0.0;
        final double t1 = 1.0;

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Integrator(
                "Runge-Kutta-Fehlberg 7-8", true, new double[] { 0.3, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2 },
                new double[][] { { 0.3, 0.3 }, { 0.38291, 0.3 }, { 0.67082, 0.34656 }, { 0.9, 0.33333 }, { 1.26845, 0.41124 },
                                { 1.57464, 0.55556 }, { 1.88672, 0.68889 }, { 2.18310, 0.81873 }, { 2.48066, 0.91667 },
                                { 2.77173, 0.97225 }, { 3.06679, 0.99216 }, { 3.36919, 0.99808 }, { 3.67917, 0.99959 } },
                new double[] { 0.06, 0.0, 0.0 }, 0.001, 0.001, new RungeKuttaStepInterpolator() );

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public double[] derivatives(double t, double[] y) {
                throw new RuntimeException("error scenario");
            }
        };

        assertThrows(IntegratorException.class, () -> integrator.integrate(equations, t0, y0, t1, null));
    }

}