package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.ContinuousEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.RelativisticEvent;
import org.apache.commons.math3.ode.events.RootFunction;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    private static final double EPSILON = 1.0e-13;
    private static final double RELATIVE_TOL = 1.0e-12;
    private static final double ABSOLUTE_TOL = 1.0e-13;

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        equations = new TestDifferentialEquations();
        integrator = new EulerIntegrator(0.01, new EulerStepInterpolator(), 0.01);
        integrator.setEquations(equations);
    }

    /**
     * Test integration of a simple function.
     */
    @Test
    public void testSimpleFunction() {
        double[] y0 = {0.0, 0.0};
        double t0 = 0.0;
        double t = 1.0;
        double[] yExpected = {1.0, 1.0};
        double result = integrator.integrate(equations, t0, y0, t, new double[y0.length]);
        assertArrayEquals(yExpected, y0, EPSILON);
        assertEquals(t, result, EPSILON);
    }

    /**
     * Test integration of a function with root.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testRootFunction() {
        RungeKuttaIntegrator integrator = new MidpointIntegrator(0.01, new MidpointStepInterpolator(), 0.01);
        integrator.setEquations(new TestDifferentialEquationsWithRoot());
        integrator.integrate(0.0, new double[]{0.0}, 1.0, new double[2]);
    }

    /**
     * Test integration of a function with continuous event.
     */
    @Test
    public void testContinuousEvent() {
        RungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.01, new ClassicalRungeKuttaStepInterpolator(), 0.01);
        integrator.setEquations(new TestDifferentialEquationsWithContinuousEvent());
        integrator.setEventsHandlerManager(new CombinedEventsManager());
        integrator.addStepHandler(new TestStepHandler());
        double[] y0 = {0.0, 0.0};
        double t0 = 0.0;
        double t = 2.0;
        integrator.integrate(t0, y0, t, new double[y0.length]);
        assertEquals(2.0, integrator.getStepStart(), EPSILON);
        assertEquals(2.0, integrator.getStepSize(), EPSILON);
        assertEquals(3.0, integrator.getStep(), EPSILON);
        StepHandler stepHandler = integrator.getStepHandlers().get(0);
        assertEquals(1.0, stepHandler.getStepSize(), EPSILON);
        assertArrayEquals(new double[]{1.0, 1.0}, stepHandler.getState(), EPSILON);
    }

    /**
     * Test integration of a function with relativistic event.
     */
    @Test
    public void testRelativisticEvent() {
        RungeKuttaIntegrator integrator = new RungeKutta78Integrator(0.01, new RungeKutta78StepInterpolator(), 0.01);
        integrator.setEquations(new TestDifferentialEquationsWithRelativisticEvent());
        integrator.setEventsHandlerManager(new CombinedEventsManager());
        integrator.addStepHandler(new TestStepHandler());
        double[] y0 = {0.0, 0.0};
        double t0 = 0.0;
        double t = 2.0;
        integrator.integrate(t0, y0, t, new double[y0.length]);
        assertEquals(2.0, integrator.getStepStart(), RELATIVE_TOL);
        assertEquals(2.0, integrator.getStepSize(), RELATIVE_TOL);
        assertEquals(2.0, integrator.getStep(), RELATIVE_TOL);
        StepHandler stepHandler = integrator.getStepHandlers().get(0);
        assertEquals(1.0, stepHandler.getStepSize(), RELATIVE_TOL);
        assertArrayEquals(new double[]{1.1, 1.1}, stepHandler.getState(), ABSOLUTE_TOL);
    }

}

class TestDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0];
    }

}

class TestDifferentialEquationsWithRoot implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0] - 1.0;
    }

    @Override
    public RootFunction getRootFunction(double t, double[] y) {
        return new RootFunction() {
            @Override
            public double value(double t, double[] y) {
                return y[0] - 1.0;
            }
        };
    }

}

class TestDifferentialEquationsWithContinuousEvent implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0];
    }

    @Override
    public Event[] getEvents(double t, double[] y, double[] yDot, int index) {
        if (Double.isNaN(y[0])) {
            return new Event[] {new ContinuousEvent(t, new TestContinuousEventHandler())};
        }
        return new Event[0];
    }

}

class TestDifferentialEquationsWithRelativisticEvent implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0];
    }

    @Override
    public Event[] getEvents(double t, double[] y, double[] yDot, int index) {
        if (Double.isNaN(y[0])) {
            return new Event[] {new RelativisticEvent(t, new TestRelativisticEventHandler())};
        }
        return new Event[0];
    }

}

class TestContinuousEventHandler implements Event {

    @Override
    public double getAbsoluteTime() {
        return 1.0;
    }

    @Override
    public double getRelativeTime() {
        return 0.0;
    }

    @Override
    public void handle(double t, double[] y, double[] yDot) {
        y[0] = Double.NaN;
    }

}

class TestRelativisticEventHandler implements Event {

    @Override
    public double getAbsoluteTime() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getRelativeTime() {
        return 0.0;
    }

    @Override
    public void handle(double t, double[] y, double[] yDot) {
        y[0] = 1.1;
    }

}

class TestStepHandler implements StepHandler {

    private double stepSize;
    private double[] state;

    @Override
    public void handleStep(StepHandler handler, boolean last) {
        this.stepSize = handler.getStepSize();
        this.state = handler.getState();
    }

    public double getStepSize() {
        return stepSize;
    }

    public double[] getState() {
        return state;
    }

}