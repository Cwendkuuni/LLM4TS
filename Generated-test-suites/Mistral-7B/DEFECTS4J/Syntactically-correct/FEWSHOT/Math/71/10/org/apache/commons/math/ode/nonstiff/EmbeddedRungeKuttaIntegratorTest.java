package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.ModelReleaseEvent;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.special.MathConstants;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static double epsilon = 1.0e-14;

    @Test
    public void testBasicIntegration() {
        double[][] coefficients = {{-0.0, 0.0, 0.5, 0.5}, {0.5, -1.0, 0.5, 0.0}, {0.0, 1.0, 0.0, 0.5}};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta23FehlbergIntegrator(
                "Embedded Runge-Kutta 23 Fehlberg method", true, coefficients[0], coefficients[1], coefficients[2], new RungeKuttaStepInterpolator(), 0.001d, 0.01d, null, null);

        FirstOrderDifferentialEquations equations = (firstOrderDiffs, y, dy) -> {
            dy[0] = firstOrderDiffs * y[0];
        };

        double[] y0 = {1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = {E.E.exp(1.0)};

        double result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        assertEquals(t1, result, epsilon);
        assertArrayEquals(y1, y0, epsilon);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() {
        double[][] coefficients = {{-0.0, 0.0, 0.5, 0.5}, {0.5, -1.0, 0.5, 0.0}, {0.0, 1.0, 0.0, 0.5}};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta23FehlbergIntegrator(
                "Embedded Runge-Kutta 23 Fehlberg method", true, coefficients[0], coefficients[1], coefficients[2], new RungeKuttaStepInterpolator(), 0.001d, 0.01d, null, null);

        FirstOrderDifferentialEquations equations = (firstOrderDiffs, y, dy) -> {
            throw new DerivativeException("Test DerivativeException", 1);
        };

        double[] y0 = {};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = {};

        integrator.integrate(equations, t0, y0, t1, y1);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() {
        double[][] coefficients = {{-0.0, 0.0, 0.5, 0.5}, {0.5, -1.0, 0.5, 0.0}, {0.0, 1.0, 0.0, 0.5}};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta23FehlbergIntegrator(
                "Embedded Runge-Kutta 23 Fehlberg method", true, coefficients[0], coefficients[1], coefficients[2], new RungeKuttaStepInterpolator(), 0.001d, 0.01d, null, null);

        FirstOrderDifferentialEquations equations = (firstOrderDiffs, y, dy) -> {
            throw new IntegratorException("Test IntegratorException");
        };

        double[] y0 = {};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = {};

        integrator.integrate(equations, t0, y0, t1, y1);
    }

    @Test
    public void testEventHandling() {
        double[][] coefficients = {{-0.0, 0.0, 0.5, 0.5}, {0.5, -1.0, 0.5, 0.0}, {0.0, 1.0, 0.0, 0.5}};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta23FehlbergIntegrator(
                "Embedded Runge-Kutta 23 Fehlberg method", true, coefficients[0], coefficients[1], coefficients[2], new RungeKuttaStepInterpolator(), 0.001d, 0.01d, null, null);

        class TestEvent extends ModelReleaseEvent {
            public TestEvent(double time) {
                super(time);
            }
        }

        FirstOrderDifferentialEquations equations = (firstOrderDiffs, y, dy) -> {
            dy[0] = firstOrderDiffs * y[0];
        };

        double[] y0 = {1.0};
        double t0 = 0.0;
        double t1 = MathConstants.PI;
        double[] y1 = {Math.cos(MathConstants.PI)};

        StepHandler eventHandler = (interpolator, lastStep) -> {
            if (lastStep) {
                assertEquals(t1, interpolator.getTime(), epsilon);
                assertTrue(interpolator.isLastStep());
            } else {
                assertFalse(interpolator.isLastStep());
            }
        };

        integrator.addStepHandler(eventHandler);

        Event event = new TestEvent(MathConstants.PI);
        integrator.addEvent(event);

        double result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        assertEquals(t1, result, epsilon);
        assertArrayEquals(y1, y0, epsilon);
    }

    @Test(expected = IllegalStateException.class)
    public void testEventBeforeStart() {
        double[][] coefficients = {{-0.0, 0.0, 0.5, 0.5}, {0.5, -1.0, 0.5, 0.0}, {0.0, 1.0, 0.0, 0.5}};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta23FehlbergIntegrator(
                "Embedded Runge-Kutta 23 Fehlberg method", true, coefficients[0], coefficients[1], coefficients[2], new RungeKuttaStepInterpolator(), 0.001d, 0.01d, null, null);

        FirstOrderDifferentialEquations equations = (firstOrderDiffs, y, dy) -> {
            dy[0] = firstOrderDiffs * y[0];
        };

        double[] y0 = {1.0};
        double t0 = 0.0;
        double t1 = -1.0;
        double[] y1 = {};

        integrator.addEvent(new ModelReleaseEvent(t1));
        integrator.integrate(equations, t0, y0, t1, y1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        double[][] coefficients = {{-0.0, 0.0, 0.5, 0.5}, {0.5, -1.0, 0.5, 0.0}, {0.0, 1.0, 0.0, 0.5}};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta23FehlbergIntegrator(
                "Embedded Runge-Kutta 23 Fehlberg method", true, coefficients[0], coefficients[1], coefficients[2], new RungeKuttaStepInterpolator(), 0.001d, 0.01d, null, null);

        FirstOrderDifferentialEquations equations = (firstOrderDiffs, y, dy) -> {
            dy[0] = firstOrderDiffs * y[0];
        };

        double[] y0 = {1.0 / Math.E};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = {};

        integrator.integrate(equations, t0, y0, t1, y1);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        double[][] coefficients = {{-0.0, 0.0, 0.5, 0.5}, {0.5, -1.0, 0.5, 0.0}, {0.0, 1.0, 0.0, 0.5}};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta23FehlbergIntegrator(
                "Embedded Runge-Kutta 23 Fehlberg method", true, coefficients[0], coefficients[1], coefficients[2], new RungeKuttaStepInterpolator(), 0.001d, 0.01d, null, null);

        FirstOrderDifferentialEquations equations = (firstOrderDiffs, y, dy) -> {
            dy[0] = firstOrderDiffs * y[0];
        };

        double[] y0 = {1.0};
        double t0 = 0.0;
        double t1 = 1.0e308;
        double[] y1 = {};

        integrator.setMaxEvaluations(10);
        integrator.integrate(equations, t0, y0, t1, y1);
    }

    @Test(expected = TooManyIterationsException.class)
    public void testTooManyIterationsException() {
        double[][] coefficients = {{-0.0, 0.0, 0.5, 0.5}, {0.5, -1.0, 0.5, 0.0}, {0.0, 1.0, 0.0, 0.5}};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta23FehlbergIntegrator(
                "Embedded Runge-Kutta 23 Fehlberg method", true, coefficients[0], coefficients[1], coefficients[2], new RungeKuttaStepInterpolator(), 0.001d, 0.01d, null, null);

        FirstOrderDifferentialEquations equations = (firstOrderDiffs, y, dy) -> {
            dy[0] = firstOrderDiffs * y[0];
        };

        double[] y0 = {1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = {};

        integrator.setMaxIterations(10);
        integrator.integrate(equations, t0, y0, t1, y1);
    }

}