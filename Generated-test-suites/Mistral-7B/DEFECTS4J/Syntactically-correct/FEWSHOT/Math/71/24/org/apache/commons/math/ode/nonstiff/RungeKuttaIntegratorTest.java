package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueHolder;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.MostRecentEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testNullConstructor() {
        new RungeKuttaIntegrator(null, null, null, null, null, 0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNullC() {
        double[][] a = new double[1][1];
        double[] b = new double[1];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, a, b, null);
        new RungeKuttaIntegrator("test", null, a, b, prototype, 0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNullA() {
        double[] c = new double[]{1.0};
        double[] b = new double[1];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, null, b, null);
        new RungeKuttaIntegrator("test", c, null, b, prototype, 0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNullB() {
        double[] c = new double[]{1.0, 1.0};
        double[][] a = new double[1][1];
        new RungeKuttaIntegrator("test", c, a, null, null, 0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStep() {
        double[] c = new double[]{1.0};
        double[][] a = new double[1][1];
        double[] b = new double[1];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, a, b, null);
        new RungeKuttaIntegrator("test", c, a, b, prototype, -0.1);
    }

    @Test
    public void testEmptyIntegrate() {
        double[] c = new double[]{1.0};
        double[][] a = new double[1][1];
        double[] b = new double[1];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, a, b, null);
        RungeKuttaIntegrator rki = new RungeKuttaIntegrator("test", c, a, b, prototype, 0.1) {
            @Override
            protected double doIntegrate(FirstOrderDifferentialEquations equations, double t, double[] y) throws DerivativeException, IntegratorException {
                return Double.NaN;
            }
        };
        double[] initialValues = new double[0];
        double start = 0.0;
        double end = 1.0;
        DefaultValueHolder holder = new DefaultValueHolder();
        StepHandler stepHandler = (interpolator, lastStep) -> holder.setValue(interpolator.getY());
        EventManager<MostRecentEvent> eventsManager = new EventManager<>();
        Event event = new Event() {
            @Override
            public double getAbsoluteTime() {
                return 0.5;
            }

            @Override
            public double getRelativeTime(double t) {
                return 0.5 - t;
            }
        };
        eventsManager.addEvent(event);
        rki.addEventManager(eventsManager);
        rki.addStepHandler(stepHandler);
        double result = rki.integrate(new DummyDifferentialEquations(), start, initialValues, end, new double[0]);
        assertEquals(end, result, 0.0);
        assertArrayEquals(holder.getValues(), new double[0]);
    }

    @Test
    public void testIntegrate() {
        double[] c = new double[]{1.0 / 2.0, 1.0 / 2.0};
        double[][] a = new double[][]{{1.0 / 4.0, 0.0}, {0.0, 1.0 / 4.0}};
        double[] b = new double[]{1.0 / 2.0, 1.0 / 2.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(2, a, b, null);
        RungeKuttaIntegrator rki = new RungeKuttaIntegrator("RK2", c, a, b, prototype, 0.1) {
            @Override
            protected double doIntegrate(FirstOrderDifferentialEquations equations, double t, double[] y) throws DerivativeException, IntegratorException {
                double[] derivatives = new double[y.length];
                equations.derivatives(t, y, derivatives, null);
                return t + 0.1;
            }
        };
        double[] initialValues = new double[]{0.0, 1.0};
        double start = 0.0;
        double end = 2.0;
        DefaultValueHolder holder = new DefaultValueHolder();
        StepHandler stepHandler = (interpolator, lastStep) -> holder.setValue(interpolator.getY());
        EventManager<MostRecentEvent> eventsManager = new EventManager<>();
        Event event = new Event() {
            @Override
            public double getAbsoluteTime() {
                return 1.0;
            }

            @Override
            public double getRelativeTime(double t) {
                return 1.0 - t;
            }

            @Override
            public void evaluateStep(StepInterpolation interpolation) {
                throw new UnsupportedOperationException();
            }
        };
        eventsManager.addEvent(event);
        rki.addEventManager(eventsManager);
        rki.addStepHandler(stepHandler);
        double result = rki.integrate(new DummyDifferentialEquations(), start, initialValues, end, new double[2]);
        assertEquals(end, result, 0.0);
        assertArrayEquals(new double[]{1.1, 0.9}, holder.getValues(), 0.001);
    }

    @Test
    public void testIntegrateWithEvents() {
        double[] c = new double[]{1.0 / 2.0, 1.0 / 2.0};
        double[][] a = new double[][]{{1.0 / 4.0, 0.0}, {0.0, 1.0 / 4.0}};
        double[] b = new double[]{1.0 / 2.0, 1.0 / 2.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(2, a, b, null);
        RungeKuttaIntegrator rki = new RungeKuttaIntegrator("RK2", c, a, b, prototype, 0.1) {
            @Override
            protected double doIntegrate(FirstOrderDifferentialEquations equations, double t, double[] y) throws DerivativeException, IntegratorException {
                double[] derivatives = new double[y.length];
                equations.derivatives(t, y, derivatives, null);
                return t + 0.1;
            }
        };
        double[] initialValues = new double[]{0.0, 1.0};
        double start = 0.0;
        double end = 3.0;
        DefaultValueHolder holder = new DefaultValueHolder();
        StepHandler stepHandler = (interpolator, lastStep) -> holder.setValue(interpolator.getY());
        EventManager<MostRecentEvent> eventsManager = new EventManager<>();
        Event firstEvent = new Event() {
            @Override
            public double getAbsoluteTime() {
                return 1.0;
            }

            @Override
            public double getRelativeTime(double t) {
                return 1.0 - t;
            }

            @Override
            public void evaluateStep(StepInterpolation interpolation) {
                interpolation.stop();
            }
        };
        Event secondEvent = new Event() {
            @Override
            public double getAbsoluteTime() {
                return 2.0;
            }

            @Override
            public double getRelativeTime(double t) {
                return 2.0 - t;
            }

            @Override
            public void evaluateStep(StepInterpolation interpolation) {
                interpolation.stop();
            }
        };
        eventsManager.addEvent(firstEvent);
        eventsManager.addEvent(secondEvent);
        rki.addEventManager(eventsManager);
        rki.addStepHandler(stepHandler);
        double result = rki.integrate(new DummyDifferentialEquations(), start, initialValues, end, new double[2]);
        assertEquals(end, result, 0.0);
        assertFalse(holder.isSet());
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public void derivatives(double t, double[] y, double[] yPrime, double[] Jacobian) throws DerivativeException {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
    }
}