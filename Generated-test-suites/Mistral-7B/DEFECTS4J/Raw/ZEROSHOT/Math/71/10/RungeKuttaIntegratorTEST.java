package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.SimpleReleaseEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    @Test
    public void testRungeKuttaIntegrator() {
        double[] c = {0.0, 0.25, 0.5, 0.875};
        double[][] a = {
            {},
            {0.0, 0.25},
            {0.0, 0.25, 0.5},
            {0.0, 0.25, 0.5, 0.875}
        };
        double[] b = {0.25, 0.25, 0.5, 0.875};
        double step = 0.1;
        RungeKuttaIntegrator rk4 = new RungeKuttaIntegrator("rk4", c, a, b, new RungeKuttaStepInterpolator(), step) {
            @Override
            public void integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {
                // override to prevent actual integration
            }
        };

        // test initialization
        assertArrayEquals(c, rk4.getC(), 0.0);
        assertArrayEquals(a, rk4.getA(), 0.0);
        assertArrayEquals(b, rk4.getB(), 0.0);
        assertEquals(step, rk4.getStep(), 0.0);

        // test constructor with invalid input
        try {
            new RungeKuttaIntegrator("rk4", null, a, b, prototype, step);
            fail("Should have thrown IllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
        try {
            new RungeKuttaIntegrator("rk4", c, null, b, prototype, step);
            fail("Should have thrown IllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
        try {
            new RungeKuttaIntegrator("rk4", c, a, null, prototype, step);
            fail("Should have thrown IllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
        try {
            new RungeKuttaIntegrator("rk4", c, a, b, null, step);
            fail("Should have thrown IllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
        try {
            new RungeKuttaIntegrator("rk4", c, a, b, prototype, Double.NEGATIVE_INFINITY);
            fail("Should have thrown NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        // test integration with no events
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };
        double[] y0 = {1.0, 0.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = {1.0, 0.5};
        double[] expected = {1.0, 0.5};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("rk4", c, a, b, prototype, step) {
            @Override
            public void integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {
                assertArrayEquals(y0, y, 0.0);
                assertArrayEquals(equations.computeDerivatives(t, y), yPrime, 0.0);
            }
        };
        double result = integrator.integrate(equations, t0, y0, t1, null);
        assertArrayEquals(expected, y1, 0.0);
        assertEquals(t1, result, 0.0);

        // test integration with release event
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };
        SimpleReleaseEvent event = new SimpleReleaseEvent(1.5, 1.0);
        double t2 = 2.0;
        double[] y2 = {1.0, 1.0};
        RungeKuttaIntegrator integratorWithEvent = new RungeKuttaIntegrator("rk4", c, a, b, prototype, step) {
            @Override
            public void integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {
                assertArrayEquals(y0, y, 0.0);
                assertArrayEquals(equations.computeDerivatives(t, y), yPrime, 0.0);
            }

            @Override
            public void handleStep(StepHandler handler, int stepNumber) {
                handler.handleStep(integrator, stepNumber);
                if (event != null && integrator.eventsHandlersManager.hasEvent()) {
                    Event e = integrator.eventsHandlersManager.getNextEvent();
                    assertEquals(event.getTime(), e.getTime(), 0.0);
                    assertEquals(event.getY(), e.getY(), 0.0);
                    integrator.eventsHandlersManager.removeEvent();
                }
            }

            @Override
            public CombinedEventsManager addEndTimeChecker(double startTime, double endTime, CombinedEventsManager manager) {
                return super.addEndTimeChecker(startTime, endTime, manager);
            }
        };
        double resultWithEvent = integratorWithEvent.integrate(equations, t0, y0, t2, null);
        assertArrayEquals(y2, y, 0.0);
        assertEquals(t2, resultWithEvent, 0.0);
        assertTrue(integratorWithEvent.eventsHandlersManager.isEmpty());
    }

}