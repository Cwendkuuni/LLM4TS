package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.RelativetimeEvent;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.util.ODEIntegratorUtils;

/**
 * Tests for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest extends TestCase {

    /**
     * Test method for {@link RungeKuttaIntegrator#RungeKuttaIntegrator(String, double[], double[][], double[], RungeKuttaStepInterpolator, double)}.
     */
    public void testConstructor() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2",
                new double[] { 0.5 },
                new double[][] { { 1.0, 1.0 } },
                new double[] { 0.5, 0.5 },
                new RungeKuttaStepInterpolator(0.5),
                0.1);
        assertEquals("RK2", integrator.getName());
        assertEquals(new double[] { 0.5 }, integrator.getC());
        assertEquals(new double[][] { { 1.0, 1.0 } }, integrator.getA());
        assertEquals(new double[] { 0.5, 0.5 }, integrator.getB());
        assertEquals(new RungeKuttaStepInterpolator(0.5), integrator.getPrototype());
        assertEquals(0.1, integrator.getStep(), 0.0);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    public void testIntegrate() throws NumberFormatException, MathIllegalArgumentException {
        double[] initialValues = new double[] { 1.0, 0.0 };
        double[] values;
        double start = 0.0;
        double end = 10.0;
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2",
                new double[] { 0.5 },
                new double[][] { { 1.0, 1.0 } },
                new double[] { 0.5, 0.5 },
                new RungeKuttaStepInterpolator(0.5),
                0.1);

        integrator.initialize(equations);
        integrator.integrate(equations, start, initialValues, end, values);

        assertEquals(11, values.length);
        assertEquals(initialValues[0], values[0], 1e-9);
        assertEquals(initialValues[1], values[1], 1e-9);

        for (int i = 2; i < values.length; i++) {
            double expected = ODEIntegratorUtils.exp(i * 0.1) * initialValues[0];
            assertEquals(expected, values[i], 1e-9);
        }
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    public void testIntegrateWithStepHandler() throws NumberFormatException, MathIllegalArgumentException {
        double[] initialValues = new double[] { 1.0, 0.0 };
        double[] values;
        double start = 0.0;
        double end = 10.0;
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        StepHandler stepHandler = new DummyStepHandler();
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2",
                new double[] { 0.5 },
                new double[][] { { 1.0, 1.0 } },
                new double[] { 0.5, 0.5 },
                new RungeKuttaStepInterpolator(0.5),
                0.1);

        integrator.initialize(equations);
        integrator.setStepHandler(stepHandler);

        integrator.integrate(equations, start, initialValues, end, values);

        assertEquals(11, values.length);
        assertEquals(initialValues[0], values[0], 1e-9);
        assertEquals(initialValues[1], values[1], 1e-9);

        for (int i = 2; i < values.length; i++) {
            double expected = ODEIntegratorUtils.exp(i * 0.1) * initialValues[0];
            assertEquals(expected, values[i], 1e-9);
        }
        assertEquals(11, stepHandler.getNSteps());
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    public void testIntegrateWithEvent() throws NumberFormatException, MathIllegalArgumentException {
        double[] initialValues = new double[] { 1.0, 0.0 };
        double[] values;
        double start = 0.0;
        double end = 10.0;
        double eventTime = 4.0;
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquationsWithEvent();

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2",
                new double[] { 0.5 },
                new double[][] { { 1.0, 1.0 } },
                new double[] { 0.5, 0.5 },
                new RungeKuttaStepInterpolator(0.5),
                0.1);

        integrator.initialize(equations);

        integrator.integrate(equations, start, initialValues, end, values);

        assertEquals(12, values.length);
        assertEquals(initialValues[0], values[0], 1e-9);
        assertEquals(initialValues[1], values[1], 1e-9);

        for (int i = 2; i < 11; i++) {
            double expected = ODEIntegratorUtils.exp(i * 0.1) * initialValues[0];
            assertEquals(expected, values[i], 1e-9);
        }
        assertEquals(12, values[11], 1e-9);

        assertEquals(1, equations.getNumberOfEvents());
        Event event = equations.getEvent(0);
        assertTrue(event instanceof RelativetimeEvent);
        assertEquals(eventTime, ((RelativetimeEvent) event).getRelativeTime(), 1e-9);
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
    }

}

class DummyDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
    }

    @Override
    public Event getEventAtIndex(int index) {
        return new RelativetimeEvent(4.0);
    }

}

class DummyStepHandler implements StepHandler {

    private int steps;

    @Override
    public void handleStep(StepInterpolator interpolator, boolean success) {
        steps++;
    }

    public int getNSteps() {
        return steps;
    }

    public void reset() {
        steps = 0;
    }

}