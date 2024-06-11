package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.ContinuousIntegratorEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link RungeKuttaIntegrator}
 */
public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private StepInterpolator interpolator;

    @Before
    public void setUp() {
        equations = new MyDifferentialEquations();
        integrator = new MyRungeKuttaIntegrator(0.001,
                new MyButcherArray(), new MyStepInterpolator(), 0.001);
        interpolator = integrator.getStepInterpolator();
    }

    @Test
    public void testIntegrate() throws Exception {
        // Test with no events
        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        double[] result = integrator.integrate(equations, t0, y0, 10.0, new double[y0.length]);
        Assert.assertArrayEquals("Integrated y values are incorrect", y0, result, 0.0001);
        Assert.assertEquals("Step size is incorrect", 0.001, integrator.getStepSize(), 0.00001);
        Assert.assertEquals("Step number is incorrect", 10, integrator.getStepCount(), 0);

        // Test with an event
        equations = new MyDifferentialEquationsWithEvent();
        integrator = new MyRungeKuttaIntegrator(0.001,
                new MyButcherArray(), new MyStepInterpolator(), 0.001);
        StepHandler eventHandler = new MyEventHandler();
        integrator.setStepHandlers(eventHandler);
        interpolator = integrator.getStepInterpolator();
        double[] y = new double[2];
        double t = 0.0;
        integrator.integrate(equations, t, y, 10.0, y);
        Assert.assertTrue("Event handler was not invoked", eventHandler.isEventTriggered());
        Assert.assertArrayEquals("Integrated y values are incorrect", y0, y, 0.0001);
        Assert.assertEquals("Step size is incorrect", 0.001, integrator.getStepSize(), 0.00001);
        Assert.assertEquals("Step number is incorrect", 9, integrator.getStepCount(), 0);
    }

    @Test(expected = DeriveException.class)
    public void testDerivativeException() throws Exception {
        equations = new MyDifferentialEquationsWithDerivativeException();
        integrator = new MyRungeKuttaIntegrator(0.001,
                new MyButcherArray(), new MyStepInterpolator(), 0.001);
        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        integrator.integrate(equations, t0, y0, 10.0, new double[y0.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() throws Exception {
        equations = new MyDifferentialEquationsWithIntegratorException();
        integrator = new MyRungeKuttaIntegrator(0.001,
                new MyButcherArray(), new MyStepInterpolator(), 0.001);
        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        integrator.integrate(equations, t0, y0, 10.0, new double[y0.length]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() throws Exception {
        equations = new MyDifferentialEquationsWithNumberIsTooSmallException();
        integrator = new MyRungeKuttaIntegrator(0.001,
                new MyButcherArray(), new MyStepInterpolator(), 0.001);
        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        integrator.integrate(equations, t0, y0, 10.0, new double[y0.length]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws Exception {
        equations = new MyDifferentialEquationsWithMaxCountExceededException();
        integrator = new MyRungeKuttaIntegrator(0.001,
                new MyButcherArray(), new MyStepInterpolator(), 0.001);
        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        integrator.setMaxSteps(0);
        integrator.integrate(equations, t0, y0, 10.0, new double[y0.length]);
    }

}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimensions() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0] + Math.sin(y[0]);
    }

}

class MyDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {

    private boolean eventTriggered = false;

    @Override
    public int getDimensions() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0] + Math.sin(y[0]);
    }

    @Override
    public Event[] getEvents(double t, double[] y, double[] yDot, int maxEvents) {
        if (y[0] > 2.0 && ! eventTriggered) {
            eventTriggered = true;
            return new Event[] {new ContinuousIntegratorEvent(t, y)};
        }
        return new Event[0];
    }

}

class MyDifferentialEquationsWithDerivativeException implements FirstOrderDifferentialEquations {

    @Override
    public int getDimensions() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new DeriveException("Derivative exception");
    }

}

class MyDifferentialEquationsWithIntegratorException implements FirstOrderDifferentialEquations {

    @Override
    public int getDimensions() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new IntegratorException("Integrator exception");
    }

}

class MyDifferentialEquationsWithNumberIsTooSmallException implements FirstOrderDifferentialEquations {

    @Override
    public int getDimensions() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        if (y[0] > 0.0) {
            yPrime[0] = y[1];
            yPrime[1] = y[0] + Math.sin(y[0]);
        } else {
            throw new NumberIsTooSmallException("Number is too small");
        }
    }

}

class MyDifferentialEquationsWithMaxCountExceededException implements FirstOrderDifferentialEquations {

    @Override
    public int getDimensions() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        if (integrator.getStepCount() > 1000) {
            throw new MaxCountExceededException("Max count exceeded");
        }
        yPrime[0] = y[1];
        yPrime[1] = y[0] + Math.sin(y[0]);
    }

}

class MyButcherArray {

    static double[][] butcherArray = {
            {0.0, 0.0},
            {0.25, 0.25}
    };

    static double[] butcherWeights = {0.5, 0.5};

    static int numberOfStages = 2;

}

class MyRungeKuttaIntegrator extends RungeKuttaIntegrator {

}

class MyStepInterpolator implements RungeKuttaStepInterpolator {

    @Override
    public double interpolate(double t, int stage) {
        return 0.0;
    }

}

class MyEventHandler implements StepHandler {

    private boolean eventTriggered = false;

    @Override
    public void handleStep(StepInterpolator interpolator, int stage) {
        eventTriggered = interpolator.getTime() > 2.0;
    }

    public boolean isEventTriggered() {
        boolean result = eventTriggered;
        eventTriggered = false;
        return result;
    }

}