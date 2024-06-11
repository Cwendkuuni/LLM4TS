package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketsException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.GoalType;
import org.apache.commons.math3.ode.events.RelativisticEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    /**
     * Test constructor with null arguments
     */
    @Test(expected = NullArgumentException.class)
    public void testNullConstructor() {
        new RungeKuttaIntegrator(null, null, null, null, null, 0);
    }

    /**
     * Test constructor with empty array arguments
     */
    @Test(expected = NoBracketsException.class)
    public void testEmptyConstructor() {
        double[] c = {};
        double[][] a = {};
        double[] b = {};
        new RungeKuttaIntegrator("name", c, a, b, null, 0);
    }

    /**
     * Test constructor with incorrect array dimensions
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectConstructor() {
        double[] c = {0, 0};
        double[][] a = {{1, 1}, {1, 1}, {1, 1}};
        double[] b = {1, 1};
        new RungeKuttaIntegrator("name", c, a, b, null, 0);
    }

    /**
     * Test constructor with correct arguments
     */
    @Test
    public void testConstructor() {
        double[] c = {0.5, 0.2, 0.1};
        double[][] a = {{0.0,   0.0},
                         {0.5,   0.5},
                         {0.5,   0},
                         {1.0,   1.0}};
        double[] b = {0.25, 0.6, 0.1};
        RungeKuttaIntegrator rk = new RungeKuttaIntegrator("name", c, a, b, null, 0.1);
        assertEquals("name", rk.getName());
        assertArrayEquals(c, rk.getC());
        assertArrayEquals(a, rk.getA());
        assertArrayEquals(b, rk.getB());
    }

    /**
     * Test integrating a simple ODE
     */
    @Test
    public void testIntegration() throws Exception {
        double[] c = {0.5, 0.2, 0.1};
        double[][] a = {{0.0,   0.0},
                         {0.5,   0.5},
                         {0.5,   0},
                         {1.0,   1.0}};
        double[] b = {0.25, 0.6, 0.1};
        RungeKuttaIntegrator rk = new RungeKuttaIntegrator("name", c, a, b, null, 0.1);

        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        double t = 2.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return y0.length;
            }

            public void calculateDerivative(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * 2.0;
                }
            }
        };

        double result = rk.integrate(equations, t0, y0, t, new double[y0.length]);
        double[] yExpected = {Math.exp(2.0) * y0[0], Math.exp(4.0) * y0[1]};
        assertEquals(result, t, 1e-12);
        assertArrayEquals(yExpected, y0);
    }

    /**
     * Test integrating an ODE with events
     */
    @Test
    public void testIntegrationWithEvents() throws Exception {
        double[] c = {0.5, 0.2, 0.1};
        double[][] a = {{0.0,   0.0},
                         {0.5,   0.5},
                         {0.5,   0},
                         {1.0,   1.0}};
        double[] b = {0.25, 0.6, 0.1};
        RungeKuttaIntegrator rk = new RungeKuttaIntegrator("name", c, a, b, null, 0.1);

        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        double t = 5.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return y0.length;
            }

            public void calculateDerivative(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * 2.0;
                }
            }
        };

        double[][] events = new double[][]{{3.0, 0.5}, {4.0, 1.0}};
        CombinedEventsManager manager = new CombinedEventsManager();
        for (double[] event : events) {
            manager.addEvent(new RelativisticEvent(event[0], y0[0], GoalType.REACH, 0.5, 1.0, null));
        }

        double result = rk.integrate(equations, t0, y0, t, new double[y0.length]);
        double[] yExpected = {Math.exp(2.0) * y0[0], Math.exp(4.0) * y0[1]};
        assertEquals(result, t, 1e-12);
        assertArrayEquals(yExpected, y0);
        assertEquals(manager.getEventCount(), 2);
    }

    /**
     * Test integrating an ODE with step handlers
     */
    @Test
    public void testIntegrationWithStepHandlers() throws Exception {
        double[] c = {0.5, 0.2, 0.1};
        double[][] a = {{0.0,   0.0},
                         {0.5,   0.5},
                         {0.5,   0},
                         {1.0,   1.0}};
        double[] b = {0.25, 0.6, 0.1};
        RungeKuttaIntegrator rk = new RungeKuttaIntegrator("name", c, a, b, null, 0.1);

        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        double t = 5.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return y0.length;
            }

            public void calculateDerivative(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * 2.0;
                }
            }
        };

        StepHandler stepHandler = new StepHandler() {
            private int count = 0;

            public void handleStep(AbstractIntegrator integrator, boolean last) {
                count++;
            }
        };
        rk.setStepHandler(stepHandler);

        double result = rk.integrate(equations, t0, y0, t, new double[y0.length]);
        double[] yExpected = {Math.exp(2.0) * y0[0], Math.exp(4.0) * y0[1]};
        assertEquals(result, t, 1e-12);
        assertArrayEquals(yExpected, y0);
        assertEquals(stepHandler.getCallCount(), 11); // 10 steps + initial call
    }

}