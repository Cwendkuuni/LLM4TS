package org.apache.commons.math.ode.nonstiff; // package should match the class

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventIterator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.test.DummyDifferentialEquations;
import org.apache.commons.math3.ode.test.SimpleStepHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator = null;
    private FirstOrderDifferentialEquations equations = null;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaSecondOrderIntegrator(
                "RK23", true, new double[] { 0.25, 0.5 }, new double[][] { { 0.25, 0.5 }, { 0.5, 0 } },
                new double[] { 0.25, 0.5 }, new RungeKuttaStepInterpolator());
        equations = new DummyDifferentialEquations();
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() {
        double[] initialState = { 1.0, 1.0 };
        double stepSize = 0.1;
        double startTime = 0.0;
        double endTime = 2.0;
        double[] result = integrator.integrate(equations, startTime, initialState, endTime, new double[initialState.length]);
        assertEquals(endTime, result[0], 1e-12); // check end time
        // check result values
        for (int i = 0; i < initialState.length; i++) {
            assertEquals(initialState[i] + stepSize * initialState[1], result[i], 1e-12);
        }
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * When step size is not positive, an IllegalArgumentException should be thrown.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStepSize() {
        integrator.integrate(equations, 0.0, new double[] { 1.0 }, -0.1, new double[1]);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * When initial state array size is not equal to the dimension of differential equations, an IllegalArgumentException should be thrown.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidInitialStateSize() {
        integrator.integrate(equations, 0.0, new double[2], 1.0, new double[1]);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * When end time is less than the start time, an IllegalArgumentException should be thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEndTime() {
        integrator.integrate(equations, 1.0, new double[] { 1.0 }, 0.0, new double[1]);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Given a differential equation where the derivative becomes NaN at a certain point,
     * the integrator should stop at that point and return the last known valid state.
     */
    @Test
    public void testNaNDerivative() {
        double[] initialState = { 1.0, 1.0 };
        double[] derivatives = new double[initialState.length];
        double stepSize = 0.1;
        double startTime = 0.0;
        double endTime = 1.0;
        equations.setEquations((first, second, derivative) -> {
            derivative[0] = 1.0 / Math.sqrt(second[0]);
            if (second[0] <= 0.0) {
                derivative[0] = Double.NaN;
            }
        });
        double[] result = integrator.integrate(equations, startTime, initialState, endTime, new double[initialState.length]);
        // check end time
        assertEquals(startTime + stepSize, result[0], 1e-12);
        // check result values up to the NaN point
        for (int i = 0; i < initialState.length; i++) {
            assertEquals(initialState[i] + stepSize * initialState[1], result[i], 1e-12);
        }
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Given a differential equation with an event at a certain time, the integrator should stop at that time and call the event handler.
     */
    @Test
    public void testEventHandling() {
        double[] initialState = { 1.0, 1.0 };
        double[] derivatives = new double[initialState.length];
        double stepSize = 0.1;
        double startTime = 0.0;
        double endTime = 2.0;
        RungeKuttaStepInterpolator interpolator = new RungeKuttaStepInterpolator();
        SimpleStepHandler stepHandler = new SimpleStepHandler();
        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addStepHandler(stepHandler);
        manager.addEndTimeChecker(endTime, new SimpleEvent() {
            @Override
            public double getEventTime() {
                return 1.5;
            }

            @Override
            public void handleEvent(double time, double[] state) {
                stepHandler.handleEvent();
            }
        });
        equations.setEquations((first, second, derivative) -> {
            derivative[0] = second[0];
        });
        integrator = new EmbeddedRungeKuttaSecondOrderIntegrator(
                "RK23", true, new double[] { 0.25, 0.5 }, new double[][] { { 0.25, 0.5 }, { 0.5, 0 } },
                new double[] { 0.25, 0.5 }, interpolator);
        integrator.integrate(equations, startTime, initialState, endTime, new double[initialState.length]);
        assertTrue(stepHandler.isEventFired());
    }

}

class SimpleEvent implements Event {
    @Override
    public double getEventTime() {
        return 0;
    }

    @Override
    public void handleEvent(double time, double[] state) {
        // empty implementation
    }
}