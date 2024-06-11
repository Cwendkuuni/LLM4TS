package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.GlobalEvent;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final double EPSILON = 1e-12;
    private static final double MAX_STEP_SIZE = 1.0;
    private static final double MIN_STEP_SIZE = 0.01;

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues;
    private RungeKuttaStepInterpolator prototype;

    /**
     * Sets up the test.
     *
     * @throws InstantiationException if an error occurs during object instantiation
     * @throws IllegalAccessException if an error occurs during object instantiation
     */
    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
        prototype = new RungeKuttaStepInterpolator(5, new DummyStepInterpolator(new double[5], false));
        equations = new DummyDifferentialEquations(initialValues);
        integrator = new EmbeddedRungeKuttaIntegrator(
                "RK4",
                false,
                new double[] { 0.25, 0.45, 0.6925, 0.84, 1.0 },
                new double[][] { { 0.25, 0 }, { 0.45, 0.25 },
                                  { 0.6925, 0.45 }, { 0.84, 0.6925 },
                                  { 1.0, 0.84 } },
                new double[] { 0.25, 0.6 },
                prototype,
                MIN_STEP_SIZE,
                MAX_STEP_SIZE,
                null,
                null) {

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public double estimateError(double[][] yDotK,
                                         double[] y0, double[] y1,
                                         double h) {
                return Math.abs(y1[0] - y0[0]) / Math.max(Math.abs(y1[0]), EPSILON);
            }
        };
    }

    /**
     * Tests the initialization.
     */
    @Test
    public void testInitialization() {
        assertNotNull(integrator);
        assertEquals("RK4", integrator.getName());
        assertEquals(5, integrator.getOrder());
        assertArrayEquals(new double[] { 0.25, 0.45, 0.6925, 0.84, 1.0 }, integrator.c, 0.0);
        assertArrayEquals(new double[][] { { 0.25, 0 }, { 0.45, 0.25 },
                                            { 0.6925, 0.45 }, { 0.84, 0.6925 },
                                            { 1.0, 0.84 } }, integrator.a, 0.0);
        assertArrayEquals(new double[] { 0.25, 0.6 }, integrator.b, 0.0);
        assertEquals(prototype, integrator.prototype);
        assertEquals(MIN_STEP_SIZE, integrator.getMinStep(), EPSILON);
        assertEquals(MAX_STEP_SIZE, integrator.getMaxStep(), EPSILON);
        assertArrayEquals(new double[0], integrator.vecAbsoluteTolerance, 0.0);
        assertArrayEquals(new double[0], integrator.vecRelativeTolerance, 0.0);
        assertEquals(0.9, integrator.getSafety(), EPSILON);
        assertEquals(0.2, integrator.getMinReduction(), EPSILON);
        assertEquals(10.0, integrator.getMaxGrowth(), EPSILON);
    }

    /**
     * Tests the integration with no events.
     */
    @Test
    public void testIntegrationNoEvents() throws Exception {
        double[] y = new double[initialValues.length];
        System.arraycopy(initialValues, 0, y, 0, initialValues.length);
        double start = 0.0;
        double end = 10.0;
        double result = integrator.integrate(equations, start, y, end, new double[y.length]);
        assertEquals(end, result, EPSILON);
        assertArrayEquals(new double[y.length], y, EPSILON);
    }

    /**
     * Tests the integration with a single event.
     */
    @Test
    public void testIntegrationWithEvent() throws Exception {
        double[] y = new double[initialValues.length];
        System.arraycopy(initialValues, 0, y, 0, initialValues.length);

        double start = 0.0;
        double end = 4.0; // event at t = 3.0
        double[] yAtEvent = new double[] { 1.0, 2.0 }; // event value

        // define a step handler that will trigger an event at t = 3.0
        StepHandler<FirstOrderDifferentialEquations> stepHandler = new StepHandlerAdapter() {

            @Override
            public void handleStep(StepHandlerContext context) throws Exception {
                if (context.isLastStep()) {
                    double[] yCurrent = context.getY();
                    int index = 0;
                    for (int i = 0; i < yCurrent.length; ++i) {
                        if (Math.abs(yCurrent[i] - yAtEvent[index]) <= EPSILON) {
                            index++;
                        }
                        if (index == yAtEvent.length) {
                            // event triggered, stop integration
                            throw new NoBracketingException(context.getT(), context.getY());
                        }
                    }
                }
            }
        };

        integrator.setEventsHandlerManager(new CombinedEventsManager(new GlobalEvent(3.0)));
        integrator.addStepHandler(stepHandler);

        double result = integrator.integrate(equations, start, y, end, new double[y.length]);
        assertEquals(3.0, result, EPSILON);
        assertArrayEquals(yAtEvent, y, EPSILON);

        // check that the integrator has stopped at the event
        assertEquals(3.0, integrator.getLastStepTime(), EPSILON);
        assertArrayEquals(yAtEvent, integrator.getLastStepY(), EPSILON);
    }

    /**
     * Tests the integration with an event that occurs at the start.
     */
    @Test
    public void testIntegrationWithEventAtStart() throws Exception {
        double[] y = new double[initialValues.length];
        System.arraycopy(initialValues, 0, y, 0, initialValues.length);

        double start = 1.0; // event at t = 1.0
        double end = 2.0;
        double[] yAtEvent = new double[] { 1.1, 1.2 }; // event value

        // define a step handler that will trigger an event at t = 1.0
        StepHandler<FirstOrderDifferentialEquations> stepHandler = new StepHandlerAdapter() {

            @Override
            public void handleStep(StepHandlerContext context) throws Exception {
                double[] yCurrent = context.getY();
                int index = 0;
                for (int i = 0; i < yCurrent.length; ++i) {
                    if (Math.abs(yCurrent[i] - yAtEvent[index]) <= EPSILON) {
                        index++;
                    }
                    if (index == yAtEvent.length) {
                        // event triggered, stop integration
                        throw new NoBracketingException(context.getT(), context.getY());
                    }
                }
            }
        };

        integrator.setEventsHandlerManager(new CombinedEventsManager(new GlobalEvent(start)));
        integrator.addStepHandler(stepHandler);

        assertThrows(NoBracketingException.class, () -> integrator.integrate(equations, start, y, end, new double[y.length]));
    }

    /**
     * Tests the integration with an event that cannot be bracketed.
     */
    @Test
    public void testIntegrationWithUnbracketedEvent() throws Exception {
        double[] y = new double[initialValues.length];
        System.arraycopy(initialValues, 0, y, 0, initialValues.length);

        double start = 0.0;
        double end = 2.0; // event at t = sqrt(2)
        double[] yAtEvent = new double[] { 1.0, 1.0 }; // event value

        // define a step handler that will trigger an event at t = sqrt(2)
        StepHandler<FirstOrderDifferentialEquations> stepHandler = new StepHandlerAdapter() {

            @Override
            public void handleStep(StepHandlerContext context) throws Exception {
                double[] yCurrent = context.getY();
                if (context.isLastStep() && Math.sqrt(Math.pow(yCurrent[0], 2) + Math.pow(yCurrent[1], 2)) >= 1.4142) {
                    // event triggered, stop integration
                    throw new NoBracketingException(context.getT(), context.getY());
                }
            }
        };

        integrator.setEventsHandlerManager(new CombinedEventsManager(new GlobalEvent(2.0)));
        integrator.addStepHandler(stepHandler);

        double result = integrator.integrate(equations, start, y, end, new double[y.length]);
        assertTrue(result > end);
    }

    /**
     * Tests the integration with an event that cannot be bracketed and an event that can be bracketed.
     */
    @Test
    public void testIntegrationWithMixedEvents() throws Exception {
        double[] y = new double[initialValues.length];
        System.arraycopy(initialValues, 0, y, 0, initialValues.length);

        double start = 0.0;
        double end = 3.0; // event at t = 1.0 that can be bracketed
        double[] yAtEvent1 = new double[] { 1.0, 1.0 }; // event value

        // define a step handler that will trigger an event at t = 1.0
        StepHandler<FirstOrderDifferentialEquations> stepHandler1 = new StepHandlerAdapter() {

            @Override
            public void handleStep(StepHandlerContext context) throws Exception {
                double[] yCurrent = context.getY();
                int index = 0;
                for (int i = 0; i < yCurrent.length; ++i) {
                    if (Math.abs(yCurrent[i] - yAtEvent1[index]) <= EPSILON) {
                        index++;
                    }
                    if (index == yAtEvent1.length) {
                        // event triggered, stop integration
                        throw new NoBracketingException(context.getT(), context.getY());
                    }
                }
            }
        };

        integrator.setEventsHandlerManager(new CombinedEventsManager(new GlobalEvent(1.0)));
        integrator.addStepHandler(stepHandler1);

        double result = integrator.integrate(equations, start, y, end, new double[y.length]);
        assertEquals(1.0, result, EPSILON);
        assertArrayEquals(yAtEvent1, y, EPSILON);

        // define a step handler that will trigger an event at t = sqrt(2) that cannot be bracketed
        StepHandler<FirstOrderDifferentialEquations> stepHandler2 = new StepHandlerAdapter() {

            @Override
            public void handleStep(StepHandlerContext context) throws Exception {
                if (context.isLastStep() && Math.sqrt(Math.pow(y[0], 2) + Math.pow(y[1], 2)) >= 1.4142) {
                    // event triggered, stop integration
                    throw new MaxCountExceededException(context.getT(), context.getY());
                }
            }
        };

        integrator.addStepHandler(stepHandler2);

        assertThrows(MaxCountExceededException.class, () -> integrator.integrate(equations, result, y, end, new double[y.length]));
    }

}