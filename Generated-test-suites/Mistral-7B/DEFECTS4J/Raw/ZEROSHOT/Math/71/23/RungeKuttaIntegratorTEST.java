package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultEvaluationPoint;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.SingularityEvent;
import org.apache.commons.math3.ode.events.StopEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private StepInterpolator interpolator;
    private StepHandler stepHandler;
    private CombinedEventsManager eventsManager;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        equations = mock(FirstOrderDifferentialEquations.class);
        interpolator = mock(StepInterpolator.class);
        stepHandler = mock(StepHandler.class);
        eventsManager = new CombinedEventsManager();
        integrator = new RungeKuttaIntegrator("RK2", new double[] { 1.0 / 2.0 },
                new double[][] { { 0.0, 1.0 / 2.0 }, { 0.0, 1.0 / 2.0 } },
                new double[] { 1.0 / 2.0, 1.0 / 2.0 },
                new RungeKuttaStepInterpolator(2), 0.001) {
                    @Override
                    protected StepInterpolator createInterpolator() {
                        return interpolator;
                    }
                };
        integrator.setStepHandler(stepHandler);
        integrator.setEventsManager(eventsManager);
    }

    /**
     * Tests the constructor with valid parameters.
     */
    @Test
    public void constructorTest() {
        // no exception expected
    }

    /**
     * Tests the {@link RungeKuttaIntegrator#sanityChecks(FirstOrderDifferentialEquations, double, double[], double, double[])}
     * method with valid parameters.
     */
    @Test
    public void sanityChecksTest() {
        // no exception expected
    }

    /**
     * Tests the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}
     * method with valid parameters.
     */
    @Test
    public void integrateTest() throws Exception {
        when(equations.derive(eq -> {})).thenReturn(new double[] { 1.0, 1.0 });
        double[] initialValues = { 1.0, 1.0 };
        double startTime = 0.0;
        double stopTime = 2.0;
        double stepSize = 0.001;

        integrator.integrate(equations, startTime, initialValues, stopTime, new double[initialValues.length]);

        assertEquals(stopTime, integrator.getStepStart(), 0.0);
        assertEquals(stepSize, integrator.getStepSize(), 0.0);
        assertEquals(initialValues, integrator.getY(), 0.0);
    }

    /**
     * Tests the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}
     * method with null arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void integrateNullArgumentsTest() {
        integrator.integrate(null, 0.0, null, 2.0, null);
    }

    /**
     * Tests the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}
     * method with invalid step size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void integrateInvalidStepSizeTest() {
        integrator.setStepSize(0.0);
        integrator.integrate(equations, 0.0, new double[] { 1.0 }, 2.0, new double[1]);
    }

    /**
     * Tests the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}
     * method with events.
     */
    @Test
    public void integrateWithEventsTest() throws Exception {
        when(equations.derive(eq -> {})).thenReturn(new double[] { 1.0, 1.0 });
        double[] initialValues = { 1.0, 1.0 };
        double startTime = 0.0;
        double stopTime = 2.0;
        double stepSize = 0.001;
        final double eventTime = 1.0;
        final Event event = new SingularityEvent(eventTime, null);
        eventsManager.addEvent(event);

        integrator.integrate(equations, startTime, initialValues, stopTime, new double[initialValues.length]);

        assertEquals(stopTime, integrator.getStepStart(), 0.0);
        assertEquals(stepSize, integrator.getStepSize(), 0.0);
        assertEquals(initialValues, integrator.getY(), 0.0);
        assertEquals(eventTime, integrator.getLastEventTime(), 0.0);
    }

    /**
     * Tests the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}
     * method with stop event.
     */
    @Test
    public void integrateWithStopEventTest() throws Exception {
        when(equations.derive(eq -> {})).thenReturn(new double[] { 1.0, 1.0 });
        double[] initialValues = { 1.0, 1.0 };
        double startTime = 0.0;
        double stopTime = 2.0;
        double stepSize = 0.001;
        final double eventTime = 1.5;
        final double yStopValue = 2.0;
        final StopEvent event = new StopEvent(eventTime, yStopValue, null);
        eventsManager.addEvent(event);

        integrator.integrate(equations, startTime, initialValues, stopTime, new double[initialValues.length]);

        assertEquals(eventTime, integrator.getStepStart(), 0.0);
        assertEquals(yStopValue, integrator.getY()[0], 0.0);
    }

    /**
     * Tests the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}
     * method with singularity event.
     */
    @Test(expected = RuntimeException.class)
    public void integrateWithSingularityEventTest() throws Exception {
        when(equations.derive(eq -> {})).thenReturn(new double[] { 1.0, 1.0 });
        double[] initialValues = { 1.0, 1.0 };
        double startTime = 0.0;
        double stopTime = 2.0;
        double stepSize = 0.001;
        final double eventTime = 1.0;
        final Event event = new SingularityEvent(eventTime, null);
        eventsManager.addEvent(event);

        integrator.integrate(equations, startTime, initialValues, stopTime, new double[initialValues.length]);
    }

    /**
     * Tests the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}
     * method with events and step handler.
     */
    @Test
    public void integrateWithEventsAndStepHandlerTest() throws Exception {
        when(equations.derive(eq -> {})).thenReturn(new double[] { 1.0, 1.0 });
        double[] initialValues = { 1.0, 1.0 };
        double startTime = 0.0;
        double stopTime = 2.0;
        double stepSize = 0.001;
        final double eventTime = 1.0;
        final Event event = new SingularityEvent(eventTime, null);
        final double stepHandlerValue = 3.0;
        when(stepHandler.handleStep(mock(StepInterpolator.class), false)).thenReturn(stepHandlerValue);
        eventsManager.addEvent(event);
        integrator.setStepHandler(stepHandler);

        integrator.integrate(equations, startTime, initialValues, stopTime, new double[initialValues.length]);

        assertEquals(eventTime, integrator.getStepStart(), 0.0);
        assertEquals(stepHandlerValue, integrator.getY()[0], 0.0);
    }

}