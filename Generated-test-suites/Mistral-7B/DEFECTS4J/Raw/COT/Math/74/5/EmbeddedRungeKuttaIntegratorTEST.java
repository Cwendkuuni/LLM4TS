package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.ImpulsiveEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test constructor with minimal arguments.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5}, new double[][] {
                        {0.0, 0.25, 0.25, 0.5, 0.5},
                        {0.25, 0.0, 0.25, 0.5, 0.5}
                }, new double[] {0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                new double[] {1.0e-8, 1.0e-8}) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + Math.abs(y1[0] - y0[0]);
            }
        };
        assertEquals("RK4", integrator.getName());
        assertEquals(false, integrator.isFsal());
        assertEquals(4, integrator.getOrder());
        assertEquals(new double[] {0.25, 0.25}, integrator.getC());
        assertEquals(new double[][] {
                {0.0, 0.25, 0.25, 0.5, 0.5},
                {0.25, 0.0, 0.25, 0.5, 0.5}
        }, integrator.getA());
        assertEquals(new double[] {0.25, 0.25}, integrator.getB());
        assertEquals(new RungeKuttaStepInterpolator(), integrator.getPrototype());
        assertEquals(0.001, integrator.getMinStep(), 1e-15);
        assertEquals(0.01, integrator.getMaxStep(), 1e-15);
        assertEquals(new double[] {1.0e-8, 1.0e-8}, integrator.getTolerances());
    }

    /**
     * Test constructor with maximal arguments.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorWithVecTolerances() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5}, new double[][] {
                        {0.0, 0.25, 0.25, 0.5, 0.5},
                        {0.25, 0.0, 0.25, 0.5, 0.5}
                }, new double[] {0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                new double[] {1.0e-8, 1.0e-8, 1.0e-8, 1.0e-8}) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + Math.abs(y1[0] - y0[0]);
            }
        };
        assertEquals("RK4", integrator.getName());
        assertEquals(false, integrator.isFsal());
        assertEquals(4, integrator.getOrder());
        assertEquals(new double[] {0.25, 0.25}, integrator.getC());
        assertEquals(new double[][] {
                {0.0, 0.25, 0.25, 0.5, 0.5},
                {0.25, 0.0, 0.25, 0.5, 0.5}
        }, integrator.getA());
        assertEquals(new double[] {0.25, 0.25}, integrator.getB());
        assertEquals(new RungeKuttaStepInterpolator(), integrator.getPrototype());
        assertEquals(0.001, integrator.getMinStep(), 1e-15);
        assertEquals(0.01, integrator.getMaxStep(), 1e-15);
        assertEquals(new double[] {1.0e-8, 1.0e-8, 1.0e-8, 1.0e-8}, integrator.getTolerances());
    }

    /**
     * Test illegal argument exception when step size is negative.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorNegativeStepSize() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5}, new double[][] {
                        {0.0, 0.25, 0.25, 0.5, 0.5},
                        {0.25, 0.0, 0.25, 0.5, 0.5}
                }, new double[] {0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.001, -0.01,
                new double[] {1.0e-8, 1.0e-8}) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + Math.abs(y1[0] - y0[0]);
            }
        };
        assertThrows(MathIllegalArgumentException.class, () -> integrator.integrate(null, 0.0, null, 1.0, null));
    }

    /**
     * Test integration of a simple differential equation with no events.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorNoEvents() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * (1.0 - y[0]);
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5}, new double[][] {
                        {0.0, 0.25, 0.25, 0.5, 0.5},
                        {0.25, 0.0, 0.25, 0.5, 0.5}
                }, new double[] {0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.01, 0.1,
                new double[] {1.0e-8, 1.0e-8}) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + Math.abs(y1[0] - y0[0]);
            }
        };

        double[] y0 = new double[] {0.5};
        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[1];

        integrator.integrate(equations, t0, y0, t, y);

        assertEquals(0.4768371582511045, y[0], 1e-6);
    }

    /**
     * Test integration of a differential equation with impulsive event.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorImpulsiveEvent() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * (1.0 - y[0]);
            }

            @Override
            public Event[] getEvents(double t, double[] y, double[] yDot, double[] yDotPrime,
                                      StepHandler handler) {
                if (y[0] >= 0.95) {
                    return new Event[] {new ImpulsiveEvent(1.0, 1.0, y[0])};
                }
                return new Event[0];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5}, new double[][] {
                        {0.0, 0.25, 0.25, 0.5, 0.5},
                        {0.25, 0.0, 0.25, 0.5, 0.5}
                }, new double[] {0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.01, 0.1,
                new double[] {1.0e-8, 1.0e-8}) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + Math.abs(y1[0] - y0[0]);
            }

            @Override
            protected void addStepHandler(StepHandler handler) {
                this.stepHandlers.add(handler);
            }
        };

        double[] y0 = new double[] {0.5};
        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[1];

        integrator.addStepHandler((interpolator, lastStep) -> {
            System.out.println("Step: " + interpolator.getTime() + " " + lastStep);
        });

        integrator.integrate(equations, t0, y0, t, y);

        assertEquals(0.95, y[0], 1e-6);

        StepHandler handler = mock(StepHandler.class);
        integrator.addStepHandler(handler);

        integrator.integrate(equations, t0, y0, t, y);

        verify(handler, times(2)).handleStep(any(RungeKuttaStepInterpolator.class), anyBoolean());
    }

}