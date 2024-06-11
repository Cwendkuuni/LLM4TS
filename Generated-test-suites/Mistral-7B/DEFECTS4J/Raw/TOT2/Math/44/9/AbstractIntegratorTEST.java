package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.ode.Events;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.InitialValueProblem;
import org.apache.commons.math3.ode.StepInterpolator;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.DefaultEventManager;
import org.apache.commons.math3.ode.integrators.AbstractIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.Precision;

/**
 * Test for AbstractIntegrator class.
 */
public class AbstractIntegratorTest {

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     */
    @org.junit.Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithInvalidEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
                // empty method
            }
        };
        integrator.integrate(null, 0.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     */
    @org.junit.Test
    public void testIntegrateSimple() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
                // empty method
            }
        };

        FirstOrderDifferentialEquations equations = new PolynomialFunction(2).derivative();
        double[] y0 = { 0.0, 1.0 };
        double tEnd = 2.0;

        integrator.integrate(new InitialValueProblem<FirstOrderDifferentialEquations, double[]>(equations, y0, 0.0), tEnd);

        double t = integrator.getCurrentStepStart();
        double[] y = new double[y0.length];
        integrator.acceptStep(new StepInterpolator(), y, null, tEnd);

        assertEquals(tEnd, integrator.getCurrentStepStart(), 1e-6);
        assertEquals(y0[0], y[0], 1e-6);
        assertEquals(y0[1] + Precision.safeDouble(2.0, 1e-6), y[1], 1e-6);

        // check that no event was triggered during integration
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     * with a step handlers.
     */
    @org.junit.Test
    public void testIntegrateWithStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
                // empty method
            }
        };

        integrator.addStepHandler(new StepHandler() {
            public void handleStep(StepHandler handler, boolean isLast) {
                handler.setAccepted(false);
            }
        });

        FirstOrderDifferentialEquations equations = new PolynomialFunction(2).derivative();
        double[] y0 = { 0.0, 1.0 };
        double tEnd = 2.0;

        try {
            integrator.integrate(new InitialValueProblem<FirstOrderDifferentialEquations, double[]>(equations, y0, 0.0), tEnd);
            fail();
        } catch (MathIllegalStateException e) {
            // expected
        }

        // check that no event was triggered during integration
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     * with a event handlers.
     */
    @org.junit.Test
    public void testIntegrateWithEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
                // empty method
            }
        };

        integrator.addEventHandler(new EventHandler() {
            public void handleEvent(double t, double[] y, double[] yDot) {
                throw new RuntimeException("Event handler should not be called during integration");
            }
        }, 0.0, 1.0, Integer.MAX_VALUE, new BrentSolver());

        FirstOrderDifferentialEquations equations = new PolynomialFunction(2).derivative();
        double[] y0 = { 0.0, 1.0 };
        double tEnd = 2.0;

        try {
            integrator.integrate(new InitialValueProblem<FirstOrderDifferentialEquations, double[]>(equations, y0, 0.0), tEnd);
            fail();
        } catch (MathIllegalStateException e) {
            // expected
        }

        // check that the event handler was called during integration
        Event event = new Event() {
            @Override
            public double getEventTime() {
                return 1.5;
            }
        };
        DefaultEventManager eventManager = new DefaultEventManager();
        eventManager.addEvent(event);

        integrator.addEventHandler(eventManager, 1.0, 1.0, 1, new BrentSolver());
        integrator.addEventHandler(eventManager, 1.5, 1.0, 1, new BrentSolver());

        integrator.integrate(new InitialValueProblem<FirstOrderDifferentialEquations, double[]>(equations, y0, 0.0), tEnd);

        assertTrue(integrator.getEventHandlers().size() == 2);
        assertFalse(integrator.getEventHandlers().contains(null));
        assertEquals(event, integrator.getEventHandlers().get(0).getEventHandler());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     * with a step handlers and event handlers.
     */
    @org.junit.Test
    public void testIntegrateWithStepHandlersAndEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
                // empty method
            }
        };

        integrator.addEventHandler(new EventHandler() {
            public void handleEvent(double t, double[] y, double[] yDot) {
                throw new RuntimeException("Event handler should not be called during integration");
            }
        }, 0.0, 1.0, Integer.MAX_VALUE, new BrentSolver());
        integrator.addEventHandler(new EventHandler() {
            public void handleEvent(double t, double[] y, double[] yDot) {
                throw new RuntimeException("Event handler should not be called during integration");
            }
        }, 1.5, 1.0, Integer.MAX_VALUE, new BrentSolver());

        integrator.addStepHandler(new StepHandler() {
            public void handleStep(StepHandler handler, boolean isLast) {
                handler.setAccepted(false);
            }
        });

        FirstOrderDifferentialEquations equations = new PolynomialFunction(2).derivative();
        double[] y0 = { 0.0, 1.0 };
        double tEnd = 2.0;

        try {
            integrator.integrate(new InitialValueProblem<FirstOrderDifferentialEquations, double[]>(equations, y0, 0.0), tEnd);
            fail();
        } catch (MathIllegalStateException e) {
            // expected
        }

        // check that no event was triggered during integration
        assertTrue(integrator.getEventHandlers().isEmpty());

        // check that the step handlers were called during integration
        int stepHandlerCalledCount = 0;
        integrator.addStepHandler(new StepHandler() {
            public void handleStep(StepHandler handler, boolean isLast) {
                stepHandlerCalledCount++;
            }
        });
        integrator.integrate(new InitialValueProblem<FirstOrderDifferentialEquations, double[]>(equations, y0, 0.0), tEnd);
        assertEquals(1, stepHandlerCalledCount);
    }

}