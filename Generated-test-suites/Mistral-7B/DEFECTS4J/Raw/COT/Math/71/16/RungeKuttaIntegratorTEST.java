package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.SimpleEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialCondition;
    private double step;
    private double startTime;
    private double endTime;
    private EventManager<Event> manager;
    private Event event;

    @Before
    public void setUp() {
        integrator = new RungeKutta4Integrator("Runge-Kutta 4",
                0.25, new double[] {0.25, 0.25, 0.5, 0.5},
                new double[] {0.25, 0.4, 0.25, 0},
                new double[] {0.25, 0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator(5), 0.01);
        equations = createDummyEquations();
        initialCondition = new double[] {0.0, 1.0};
        step = 0.1;
        startTime = 0.0;
        endTime = 1.0;
        manager = new EventManager<>();
        event = new SimpleEvent(0.5);
    }

    /**
     * Tests that the integrator returns the expected result when the ODE is
     * simple enough.
     *
     * @throws Exception if an error occurs during the integration
     */
    @Test
    public void testSimpleIntegration() throws Exception {
        double[] result = integrator.integrate(equations, startTime, initialCondition, endTime);
        double[] expected = new double[] {1.7320508075688772935, 1.7320508075688772935};
        assertArrayEquals(expected, result, 1e-12);
    }

    /**
     * Tests that the integrator throws an exception when the step size is too
     * small.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSmallStepSize() {
        integrator.setStepSize(1e-10);
        integrator.integrate(equations, startTime, initialCondition, endTime);
    }

    /**
     * Tests that the integrator throws an exception when the ODE returns NaN
     * values.
     *
     * @throws Exception if an error occurs during the integration
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNaNValues() throws Exception {
        equations = createNaNEquations();
        integrator.integrate(equations, startTime, initialCondition, endTime);
    }

    /**
     * Tests that the integrator handles a simple event correctly.
     *
     * @throws Exception if an error occurs during the integration
     */
    @Test
    public void testEventHandling() throws Exception {
        integrator.addEventHandler(new StepHandler() {
            @Override
            public void handleStep(double time, double[] y, double[] yDot) {
                if (time >= event.getTime()) {
                    manager.addEvent(event);
                    throw new RuntimeException("Event detected");
                }
            }
        });
        integrator.integrate(equations, startTime, initialCondition, endTime);
        assertTrue(manager.isEventSet());
        assertEquals(event.getTime(), manager.getLastEventTime(), 1e-12);
    }

    /**
     * Tests that the integrator handles a complex event manager correctly.
     *
     * @throws Exception if an error occurs during the integration
     */
    @Test
    public void testComplexEventHandling() throws Exception {
        integrator.addEventHandler(new StepHandler() {
            @Override
            public void handleStep(double time, double[] y, double[] yDot) {
                if (time >= event.getTime()) {
                    manager.addEvent(event);
                    throw new RuntimeException("Event detected");
                }
            }
        });
        integrator.addEventManager(manager);
        integrator.addEvent(event);
        integrator.integrate(equations, startTime, initialCondition, endTime);
        assertTrue(manager.isEventSet());
        assertEquals(event.getTime(), manager.getLastEventTime(), 1e-12);
    }

    /**
     * Tests that the integrator handles a combination of events and discrete
     * events correctly.
     *
     * @throws Exception if an error occurs during the integration
     */
    @Test
    public void testCombinedEventHandling() throws Exception {
        integrator.addEventHandler(new StepHandler() {
            @Override
            public void handleStep(double time, double[] y, double[] yDot) {
                if (time >= event.getTime()) {
                    manager.addEvent(event);
                    throw new RuntimeException("Event detected");
                }
            }
        });
        integrator.addEventManager(manager);
        integrator.addEvent(event);
        integrator.addEvent(new DefaultValueEvent(0.25, 1.0));
        integrator.integrate(equations, startTime, initialCondition, endTime);
        assertTrue(manager.isEventSet());
        assertEquals(event.getTime(), manager.getLastEventTime(), 1e-12);
    }

    /**
     * Tests that the integrator stops when an event is detected.
     *
     * @throws Exception if an error occurs during the integration
     */
    @Test
    public void testEventStop() throws Exception {
        integrator.addEventHandler(new StepHandler() {
            @Override
            public void handleStep(double time, double[] y, double[] yDot) {
                if (time >= event.getTime()) {
                    throw new RuntimeException("Event detected");
                }
            }
        });
        integrator.addEventManager(manager);
        integrator.addEvent(event);
        integrator.integrate(equations, startTime, initialCondition, endTime);
        assertTrue(manager.isEventSet());
        assertEquals(event.getTime(), manager.getLastEventTime(), 1e-12);
        assertFalse(equlations.isSolved());
    }

}