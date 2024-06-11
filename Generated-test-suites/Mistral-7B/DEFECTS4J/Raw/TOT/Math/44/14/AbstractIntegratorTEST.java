package org.apache.commons.math.ode.test;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.Integrator;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Precision;

/**
 * Test suite for the {@code AbstractIntegrator} class.
 */
public class AbstractIntegratorTest extends TestCase {

    /** Constants. */
    private static final double EPSILON = 1e-12;
    private static final double EPSILON_SMALL = 1e-15;

    /** Tested integrator. */
    private Integrator integrator;

    /** Tested ODE. */
    private FirstOrderDifferentialEquations ode;

    /** Tested step interpolator. */
    private AbstractStepInterpolator interpolator;

    /** Tested step handler. */
    private StepHandler stepHandler;

    /** Tested event handler. */
    private When eventHandler;

    /** Tested event state. */
    private EventState eventState;

    /** Tested event. */
    private Event event;

    /**
     * Set up.
     */
    protected void setUp() {
        integrator = new EulerIntegrator();
        ode = new MyDifferentialEquations();
        interpolator = new StepInterpolator();
        stepHandler = new MyStepHandler();
        eventHandler = new MyEventHandler();
        eventState = new EventState(eventHandler, 0.1, 0.001, 5, new BrentSolver(0.001, 5));
        event = new MyEvent();
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     * @throws Exception if any error occurs
     */
    public void testIntegrate() throws Exception {
        integrator.addStepHandler(stepHandler);
        integrator.addEventHandler(eventHandler, eventState);
        final double tEnd = 2.5;
        final double[] y0 = { 1.0, 1.0 };
        final double[] y = new double[2];
        integrator.setEquations(ode);
        integrator.integrate(ode, 0.0, y0, tEnd, y);
        Assert.assertArrayEquals(new double[]{2.718281828459045, 2.718281828459045}, y, EPSILON);
        Assert.assertEquals(tEnd, integrator.getCurrentStepStart(), EPSILON);
        Assert.assertFalse(integrator.isLastStep());
        Assert.assertFalse(integrator.resetOccurred());

        // step handler
        final int[] count = { 0 };
        stepHandler = new StepHandler() {
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                count[0]++;
            }
        };
        integrator.clearStepHandlers();
        integrator.addStepHandler(stepHandler);
        integrator.integrate(ode, 0.0, y0, tEnd, y);
        Assert.assertEquals(2, count[0]);

        // event handler
        integrator.clearEventHandlers();
        integrator.addEventHandler(eventHandler, eventState);
        integrator.integrate(ode, 0.0, y0, tEnd, y);
        Assert.assertEquals(EventState.Type.NORMAL, eventState.getType());
        Assert.assertFalse(eventState.isReset());

        // event
        integrator.clearEventHandlers();
        integrator.addEventHandler(eventHandler, eventState);
        integrator.addEvent(event);
        integrator.integrate(ode, 0.0, y0, tEnd, y);
        Assert.assertEquals(EventState.Type.EVENT, eventState.getType());
        Assert.assertTrue(eventState.isReset());

        // event and step handler
        integrator.clearEventHandlers();
        integrator.addEventHandler(eventHandler, eventState);
        integrator.addEvent(event);
        integrator.addStepHandler(stepHandler);
        integrator.integrate(ode, 0.0, y0, tEnd, y);
        Assert.assertEquals(EventState.Type.EVENT, eventState.getType());
        Assert.assertTrue(eventState.isReset());
        Assert.assertEquals(2, count[0]);

        // maximum number of evaluations
        integrator.setMaxEvaluations(1);
        integrator.integrate(ode, 0.0, y0, tEnd, y);
        Assert.assertEquals(1, integrator.getEvaluations());

        // maximum number of evaluations and event
        integrator.setMaxEvaluations(1);
        integrator.addEventHandler(eventHandler, eventState);
        integrator.addEvent(event);
        integrator.integrate(ode, 0.0, y0, tEnd, y);
        Assert.assertEquals(1, integrator.getEvaluations());

        // maximum number of evaluations and step handler
        integrator.setMaxEvaluations(1);
        integrator.addStepHandler(stepHandler);
        integrator.integrate(ode, 0.0, y0, tEnd, y);
        Assert.assertEquals(1, integrator.getEvaluations());

        // maximum number of evaluations and event and step handler
        integrator.setMaxEvaluations(1);
        integrator.addEventHandler(eventHandler, eventState);
        integrator.addEvent(event);
        integrator.addStepHandler(stepHandler);
        integrator.integrate(ode, 0.0, y0, tEnd, y);
        Assert.assertEquals(1, integrator.getEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     * @throws Exception if any error occurs
     */
    public void testAcceptStep() throws Exception {
        integrator.clearStepHandlers();
        integrator.clearEventHandlers();
        final double tEnd = 2.5;
        integrator.setEquations(ode);
        integrator.setMaxEvaluations(10);
        integrator.setStepSize(0.1);
        integrator.integrate(ode, 0.0, new double[]{0.0, 0.0}, tEnd, new double[2]);

        // normal step
        final double[] y = new double[2];
        AbstractStepInterpolator interpolator = new StepInterpolator();
        interpolator.setSoftCurrentTime(0.1);
        interpolator.setSoftPreviousTime(0.0);
        integrator.acceptStep(interpolator, y, new double[2], 0.2);
        Assert.assertEquals(0.1, integrator.getCurrentStepStart(), EPSILON_SMALL);
        Assert.assertFalse(integrator.isLastStep());
        Assert.assertFalse(integrator.resetOccurred());

        // last step
        final double[] yLast = new double[2];
        integrator.acceptStep(interpolator, yLast, new double[2], 2.5);
        Assert.assertEquals(2.5, integrator.getCurrentStepStart(), EPSILON_SMALL);
        Assert.assertTrue(integrator.isLastStep());
        Assert.assertFalse(integrator.resetOccurred());

        // reset
        final double[] yReset = new double[2];
        interpolator.setInterpolatedTime(1.5);
        integrator.acceptStep(interpolator, yReset, new double[2], 2.0);
        Assert.assertEquals(1.5, integrator.getCurrentStepStart(), EPSILON_SMALL);
        Assert.assertFalse(integrator.isLastStep());
        Assert.assertTrue(integrator.resetOccurred());

        // step handler
        final int[] count = { 0 };
        stepHandler = new StepHandler() {
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                count[0]++;
            }
        };
        integrator.clearStepHandlers();
        integrator.addStepHandler(stepHandler);
        integrator.acceptStep(interpolator, new double[2], new double[2], 0.2);
        Assert.assertEquals(1, count[0]);

        // event
        integrator.clearEventHandlers();
        integrator.addEventHandler(eventHandler, eventState);
        integrator.acceptStep(interpolator, new double[2], new double[2], 0.2);
        Assert.assertEquals(EventState.Type.NORMAL, eventState.getType());
        Assert.assertFalse(eventState.isReset());

        // event and reset
        integrator.clearEventHandlers();
        integrator.addEventHandler(eventHandler, eventState);
        integrator.acceptStep(interpolator, new double[2], new double[2], 1.5);
        Assert.assertEquals(EventState.Type.EVENT, eventState.getType());
        Assert.assertTrue(eventState.isReset());

        // event and step handler
        integrator.clearEventHandlers();
        integrator.addEventHandler(eventHandler, eventState);
        integrator.addStepHandler(stepHandler);
        integrator.acceptStep(interpolator, new double[2], new double[2], 0.2);
        Assert.assertEquals(1, count[0]);
        Assert.assertEquals(EventState.Type.NORMAL, eventState.getType());
        Assert.assertFalse(eventState.isReset());

        // event and reset and step handler
        integrator.clearEventHandlers();
        integrator.addEventHandler(eventHandler, eventState);
        integrator.addStepHandler(stepHandler);
        integrator.acceptStep(interpolator, new double[2], new double[2], 1.5);
        Assert.assertEquals(1, count[0]);
        Assert.assertEquals(EventState.Type.EVENT, eventState.getType());
        Assert.assertTrue(eventState.isReset());

        // maximum number of evaluations
        integrator.setMaxEvaluations(1);
        integrator.acceptStep(interpolator, new double[2], new double[2], 0.2);
        Assert.assertEquals(1, integrator.getEvaluations());

        // maximum number of evaluations and event
        integrator.setMaxEvaluations(1);
        integrator.addEventHandler(eventHandler, eventState);
        integrator.acceptStep(interpolator, new double[2], new double[2], 0.2);
        Assert.assertEquals(1, integrator.getEvaluations());

        // maximum number of evaluations and step handler
        integrator.setMaxEvaluations(1);
        integrator.addStepHandler(stepHandler);
        integrator.acceptStep(interpolator, new double[2], new double[2], 0.2);
        Assert.assertEquals(1, integrator.getEvaluations());

        // maximum number of evaluations and event and step handler
        integrator.setMaxEvaluations(1);
        integrator.addEventHandler(eventHandler, eventState);
        integrator.addStepHandler(stepHandler);
        integrator.acceptStep(interpolator, new double[2], new double[2], 0.2);
        Assert.assertEquals(1, integrator.getEvaluations());

        // step size
        integrator.setStepSize(0.01);
        integrator.acceptStep(interpolator, new double[2], new double[2], 0.2);
        Assert.assertFalse(Precision.equals(0.2, integrator.getCurrentStepStart(), EPSILON_SMALL));

        // integration span
        integrator.acceptStep(interpolator, new double[2], new double[2], 2.6);
        Assert.assertEquals(NumberIsTooSmallException.class, integrator.getCaughtException().getClass());
    }

    /**
     * Test method for {@link AbstractIntegrator#sanityChecks(ExpandableStatefulODE, double)}.
     * @throws Exception if any error occurs
     */
    public void testSanityChecks() throws Exception {
        integrator = new RungeKuttaIntegrator();
        integrator.setMaxEvaluations(1);
        integrator.setEquations(ode);
        try {
            integrator.sanityChecks(ode, 0.0);
        } catch (NumberIsTooSmallException e) {
            Assert.assertEquals(LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL, e.getMessage());
            return;
        }
        fail("Expected NumberIsTooSmallException");
    }

    /**
     * Test helper class for the differential equation.
     */
    private static class MyDifferentialEquations implements FirstOrderDifferentialEquations {

        /** {@inheritDoc} */
        public int getDimension() {
            return 2;
        }

        /** {@inheritDoc} */
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = 0.1 * y[1];
        }

    }

    /**
     * Test helper class for the step handler.
     */
    private static class MyStepHandler implements StepHandler {

        /** {@inheritDoc} */
        public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
            if (lastStep) {
                System.out.println("Last step.");
            }
        }

    }

    /**
     * Test helper class for the event handler.
     */
    private static class MyEventHandler implements EventHandler {

        /** {@inheritDoc} */
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return Precision.abs(interpolator.getInterpolatedTime() - 1.5) < 1e-14;
        }

        /** {@inheritDoc} */
        public void stepAccepted(double eventTime, double[] eventY) {
            System.out.println("Event " + eventTime + " accepted.");
            if (eventTime > 1.0) {
                Arrays.fill(eventY, 0.0);
            }
        }

        /** {@inheritDoc} */
        public void stepRejected(double eventTime, double[] eventY) {
            System.out.println("Event " + eventTime + " rejected.");
        }

        /** {@inheritDoc} */
        public void reset(double eventTime, double[] eventY) {
            System.out.println("Event " + eventTime + " reset.");
        }

    }

    /**
     * Test helper class for the event.
     */
    private static class MyEvent implements Event {

        /** {@inheritDoc} */
        public double getEventTime() {
            return 1.5;
        }

        /** {@inheritDoc} */
        public void reset(double eventTime, double[] currentState) {
            Arrays.fill(currentState, 0.0);
        }

    }

}