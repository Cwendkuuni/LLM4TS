package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

public class RungeKuttaIntegratorTest {
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorNullName() {
        new RungeKuttaIntegrator(null, new double[0], new double[][]{{0.0}}, new double[]{0.0}, null, 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorNullC() {
        new RungeKuttaIntegrator("RK1", null, new double[][]{{0.0}}, new double[]{0.0}, null, 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorNullA() {
        new RungeKuttaIntegrator("RK1", new double[0], null, new double[]{0.0}, null, 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorNullB() {
        new RungeKuttaIntegrator("RK1", new double[0], new double[][]{{0.0}}, null, null, 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorNegativeStep() {
        new RungeKuttaIntegrator("RK1", new double[0], new double[][]{{0.0}}, new double[]{0.0}, null, -0.01);
    }

    @Test
    public void testIntegrateEmpty() {
        double[] c = {};
        double[][] a = {};
        double[] b = {};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK1", c, a, b, null, 0.01);
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double[] y0 = {};
        double t0 = 0.0, t = 0.01;
        double[] y = new double[y0.length];
        integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, t);
        assertArrayEquals(y0, y);
    }

    @Test
    public void testIntegrateSimple() {
        double[] c = {0.5, 0.5};
        double[][] a = {{0.0, 0.0, 0.5, 0.5}, {0.5, 0.5, 0.0, 0.5}, {0.5, 0.5, 0.5, 0.0}};
        double[] b = {0.5, 0.5};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", c, a, b, null, 0.01);
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double[] y0 = {1.0, 1.0};
        double t0 = 0.0, t = 0.01;
        double[] y = new double[y0.length];
        integrator.integrate(equations, t0, y0, t, y);
        assertEquals(1.01, y[0], Math.ulp(1.0));
        assertEquals(1.01, y[1], Math.ulp(1.0));
    }

    @Test
    public void testIntegrateWithEvents() {
        double[] c = {0.5, 0.5};
        double[][] a = {{0.0, 0.0, 0.5, 0.5}, {0.5, 0.5, 0.0, 0.5}, {0.5, 0.5, 0.5, 0.0}};
        double[] b = {0.5, 0.5};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", c, a, b, null, 0.01);
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double[] y0 = {1.0, 1.0};
        double t0 = 0.0, t1 = 0.05;
        double[] y1 = {2.0, 2.0};
        double[][] yDot = new double[2][];
        yDot[0] = new double[y0.length];
        yDot[1] = new double[y0.length];
        StepHandler eventHandler = arg -> {
            assertArrayEquals(y0, arg.getState());
            assertArrayEquals(yDot[0], arg.getDerivatives());
            assertNull(arg.getEvent());
        };
        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addEvent(new StepEvent(t1, y1));
        integrator.addStepHandler(eventHandler);
        integrator.setEventsHandlerManager(manager);
        integrator.integrate(equations, t0, y0, t1, y);
        assertNull(y);
    }
}