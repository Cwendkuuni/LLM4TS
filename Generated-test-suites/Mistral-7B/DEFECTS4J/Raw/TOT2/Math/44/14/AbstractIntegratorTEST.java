package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.integrators.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.DefaultEventHandler;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.RungeKuttaFehlberg78StepHandler;
import org.apache.commons.math3.ode.sampling.RungeKuttaFehlberg85StepHandler;
import org.apache.commons.math3.ode.sampling.RungeKuttaFehlberg95StepHandler;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @org.junit.Test
    public void testIntegration() {
        // Test with a simple ODE
        final FirstOrderDifferentialEquations ode = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public UnivariateFunction getFunction(double time, double[] state) {
                return new PolynomialFunction(new double[] { -1.0, 1.0 }, state[0]);
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[] { 1.0 };
            }
        };
        final AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // empty body
            }
        };
        integrator.setEquations(ode);
        integrator.addStepHandler(new RungeKuttaFehlberg78StepHandler(0.1));
        integrator.addStepHandler(new RungeKuttaFehlberg85StepHandler(0.1));
        integrator.addStepHandler(new RungeKuttaFehlberg95StepHandler(0.1));
        integrator.setMaxEvaluations(100);

        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0 };
        final double tEnd = 2.0;
        double t = t0;
        try {
            integrator.integrate(ode, tEnd);
            fail("Expected integration to fail due to too small integration span.");
        } catch (final MathIllegalArgumentException e) {
            // expected
        }

        integrator.setMaxEvaluations(1000);
        integrator.sanityChecks(ode, tEnd);

        // Test with no events or step handlers
        integrator.setMaxEvaluations(-1);
        integrator.resetEvaluations();
        final double[] y = new double[] { 1.0 };
        final double[] yDot = new double[1];
        integrator.integrate(ode, tEnd, y0, y, yDot);
        assertEquals(tEnd, integrator.getCurrentStepStart());
        assertEquals(Precision.SAFE_EPSILON, y[0] - yDot[0], 0.0);
        assertEquals(tEnd, integrator.getEvaluations());

        // Test with a single event
        final Event event = new Event() {
            @Override
            public boolean isRegular() {
                return false;
            }

            @Override
            public double getEventTime() {
                return 1.0;
            }

            @Override
            public void evaluateDerivative(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 2.0;
            }

            @Override
            public void evaluateState(double t, double[] y) {
                y[0] = 1.0;
            }

            @Override
            public boolean isActive(double t, double[] y) {
                return Precision.abs(y[0] - 1.0) < 1.0e-6;
            }

            @Override
            public void handleEvent(double t, double[] y) {
                y[0] = 2.0;
            }

            @Override
            public double[] getDerivative(double t, double[] y) {
                return new double[] { y[0] * 2.0 };
            }
        };
        integrator.addEventHandler(new DefaultEventHandler(), 1.0e-6, 1.0e-6, 1, null);
        integrator.addEventHandler(event, 1.0, 1.0e-6, 1, null);
        integrator.setMaxEvaluations(1000);
        integrator.resetEvaluations();
        integrator.integrate(ode, tEnd, y0, y, yDot);
        assertEquals(tEnd, integrator.getCurrentStepStart());
        assertEquals(2.0, y[0], 1.0e-6);
        assertEquals(4.0, yDot[0], 1.0e-6);
        assertEquals(tEnd, integrator.getEvaluations());

        // Test with multiple events
        final EventState eventState = new EventState(event, 1.0, 1.0e-6, 1, null);
        integrator.addEventHandler(eventState);
        integrator.addEventHandler(new DefaultEventHandler(), 1.0e-6, 1.0e-6, 1, null);
        integrator.setMaxEvaluations(1000);
        integrator.resetEvaluations();
        integrator.integrate(ode, tEnd, y0, y, yDot);
        assertEquals(tEnd, integrator.getCurrentStepStart());
        assertEquals(2.0, y[0], 1.0e-6);
        assertEquals(4.0, yDot[0], 1.0e-6);
        assertEquals(tEnd, integrator.getEvaluations());
        assertEquals(1, integrator.getEventHandlers().size());
        assertEquals(event, integrator.getEventHandlers().get(0).getEventHandler());

        // Test with a reset event
        final MockEventHandler mockEventHandler = new MockEventHandler();
        integrator.addEventHandler(new DefaultEventHandler(), 1.0e-6, 1.0e-6, 1, mockEventHandler);
        integrator.addEventHandler(new DefaultEventHandler(), 1.0e-6, 1.0e-6, 1, null);
        integrator.setMaxEvaluations(1000);
        integrator.resetEvaluations();
        integrator.integrate(ode, tEnd, y0, y, yDot);
        assertEquals(tEnd, integrator.getCurrentStepStart());
        assertEquals(1.0, y[0], 1.0e-6);
        assertEquals(1.0, yDot[0], 1.0e-6);
        assertEquals(tEnd, integrator.getEvaluations());
        assertEquals(2, integrator.getEventHandlers().size());
        assertThrows(MathIllegalStateException.class, () -> integrator.integrate(ode, tEnd, y0, y, yDot));
        assertEquals(1, mockEventHandler.getResetCount());

        // Test with a step handler
        final StepHandler stepHandler = new RungeKuttaFehlberg95StepHandler(0.1);
        integrator.addStepHandler(new RungeKuttaFehlberg78StepHandler(0.1));
        integrator.addStepHandler(stepHandler);
        integrator.setMaxEvaluations(1000);
        integrator.resetEvaluations();
        integrator.integrate(ode, tEnd, y0, y, yDot);
        assertEquals(tEnd, integrator.getCurrentStepStart());
        assertEquals(1.4496981012373196, y[0], 1.0e-6);
        assertEquals(2.778944522132876, yDot[0], 1.0e-6);
        assertEquals(tEnd, integrator.getEvaluations());
        assertEquals(1, stepHandler.getCallCount());
    }

}