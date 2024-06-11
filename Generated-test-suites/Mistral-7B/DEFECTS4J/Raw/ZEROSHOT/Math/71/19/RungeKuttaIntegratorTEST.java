package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.ContinuousIntegratorEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.AbstractDifferentialEquationsTest;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;

/**
 * Test for {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest extends AbstractDifferentialEquationsTest {

    /**
     * Test the basic functionality of Runge-Kutta integrator.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testBasicFunctionality() throws Exception {
        final RungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.01, 5);

        final double[] y0 = new double[] { 0.0, 1.0 };
        final double[] expected = new double[] { 1.001, 1.002 };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * (1.0 + 0.01 * i);
                }
            }
        };

        final double start = 0.0;
        final double end   = 1.0;
        final Event event = new ContinuousIntegratorEvent(1.0);

        integrator.addStepHandler(new StepHandler() {
            @Override
            public int handleStep(double t, double[] y, double[] yDot, int type) {
                assertArrayEquals(expected, yDot);
                return ContinuousStepController.CONTINUE_STEP;
            }
        });
        integrator.addEventHandler(event);

        integrator.integrate(equations, start, y0, end);

        assertFalse(integrator.hasEventOccurred());
        assertArrayEquals(expected, y0);
    }

    /**
     * Test the functionality of Runge-Kutta integrator with custom interpolator.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCustomInterpolator() throws Exception {
        final RungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.01, 5);

        final double[] y0 = new double[] { 0.0, 1.0 };
        final double[] expected = new double[] { 1.001, 1.002 };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * (1.0 + 0.01 * i);
                }
            }
        };

        final double start = 0.0;
        final double end   = 1.0;
        final Event event = new ContinuousIntegratorEvent(1.0);

        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(integrator.getName(), integrator.getC(),
                integrator.getA(), integrator.getB(), integrator.getPrototype());
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(prototype);
        integrator.setInterpolator(interpolator);

        integrator.addStepHandler(new StepHandler() {
            @Override
            public int handleStep(double t, double[] y, double[] yDot, int type) {
                assertArrayEquals(expected, yDot);
                return ContinuousStepController.CONTINUE_STEP;
            }
        });
        integrator.addEventHandler(event);

        integrator.integrate(equations, start, y0, end);

        assertFalse(integrator.hasEventOccurred());
        assertArrayEquals(expected, y0);

        // make sure interpolator is used
        assertEquals(interpolator, integrator.getInterpolator());
    }

    /**
     * Test the functionality of Runge-Kutta integrator with different step sizes.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testDifferentStepSizes() throws Exception {
        final RungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.01, 5);

        final double[] y0 = new double[] { 0.0, 1.0 };
        final double[] expected = new double[] { 1.001, 1.002 };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * (1.0 + 0.01 * i);
                }
            }
        };

        final double start = 0.0;
        final double end   = 1.0;
        final Event event = new ContinuousIntegratorEvent(1.0);

        integrator.addStepHandler(new StepHandler() {
            @Override
            public int handleStep(double t, double[] y, double[] yDot, int type) {
                assertArrayEquals(expected, yDot);
                return ContinuousStepController.CONTINUE_STEP;
            }
        });
        integrator.addEventHandler(event);

        integrator.integrate(equations, start, y0, end, 0.005);

        assertFalse(integrator.hasEventOccurred());
        assertArrayEquals(expected, y0);

        integrator.reset();

        integrator.integrate(equations, start, y0, end, 0.02);

        assertFalse(integrator.hasEventOccurred());
        assertNotSame(expected, y0);
        assertEquals(2, y0.length);

        integrator.reset();

        integrator.integrate(equations, start, y0, end, 0.001);

        assertFalse(integrator.hasEventOccurred());
        assertArrayEquals(expected, y0, 0.0001);
    }

}