package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.MockEvent;
import org.apache.commons.math3.ode.events.NoEventFoundException;
import org.apache.commons.math3.ode.events.OneStepEventHandler;
import org.apache.commons.math3.ode.events.TwoStepsEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testNullName() {
        new RungeKuttaIntegrator(null, new double[]{}, new double[][]{}, new double[]{}, null, 1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testEmptyButcherArray() {
        new RungeKuttaIntegrator("RK1", new double[]{0.0}, new double[][]{}, new double[]{0.0}, null, 1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testEmptyInternalWeights() {
        new RungeKuttaIntegrator("RK1", new double[]{0.0}, null, new double[]{0.0}, null, 1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testEmptyExternalWeights() {
        new RungeKuttaIntegrator("RK1", new double[]{0.0}, new double[][]{{}}, new double[]{0.0}, null, 1.0);
    }

    @Test
    public void testEulerIntegrator() {
        double[] a11 = {1.0};
        double[] c = {1.0};
        double[] b = {1.0};
        RungeKuttaIntegrator integrator = new EulerIntegrator("Euler", c, a11, b, null, 1.0);
        FirstOrderDifferentialEquations equations = mockDifferentialEquations();
        double y0[] = {1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];
        integrator.integrate(equations, t0, y0, t1, y1);
        assertEquals(1.0, y1[0], 1e-14);
    }

    @Test
    public void testClassicalRungeKuttaIntegrator() {
        double[] a11 = {0.5, 0.5};
        double[] a21 = {0.0, 0.5};
        double c[] = {0.5, 0.5};
        double[] b = {0.25, 0.25};
        RungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator("Classical RK2", c, a11, a21, b, 1.0);
        FirstOrderDifferentialEquations equations = mockDifferentialEquations();
        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];
        integrator.integrate(equations, t0, y0, t1, y1);
        assertArrayEquals(new double[]{1.0 + 0.5 * 0.5 * 1.0 * (1.0 - 0.5), 1.0 + 0.5 * 0.5 * 1.0 * (1.0 - 0.5)}, y1, 1e-14);
    }

    @Test
    public void testMidpointIntegrator() {
        double[] a11 = {1.0 / 2.0, 1.0 / 2.0};
        double c[] = {1.0};
        double[] b = {1.0 / 2.0};
        RungeKuttaIntegrator integrator = new MidpointIntegrator("Midpoint", c, a11, b, null, 1.0);
        FirstOrderDifferentialEquations equations = mockDifferentialEquations();
        double y0[] = {1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];
        integrator.integrate(equations, t0, y0, t1, y1);
        assertEquals(1.0 + 1.0 / 2.0 * 1.0 * (1.0 - 1.0 / 2.0), y1[0], 1e-14);
    }

    @Test
    public void testRungeKuttaIntegratorWithEventHandlers() throws NoEventFoundException, InterruptedException {
        double[] a11 = {0.5, 0.5};
        double[] a21 = {0.0, 0.5};
        double c[] = {0.5, 0.5};
        double[] b = {0.25, 0.25};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", c, a11, a21, b, 1.0) {
            @Override
            protected RungeKuttaStepInterpolator getPrototype() {
                return new RungeKuttaStepInterpolator() {
                    @Override
                    public StepInterpolator getInterpolator(double step) {
                        return new RungeKuttaStepInterpolator.RungeKuttaRungePolynomialInterpolator(this, step) {
                            @Override
                            public double[] interpolate(double t, double[] y) {
                                OneStepEventHandler handler = new OneStepEventHandler() {
                                    @Override
                                    public int handle(double t, double[] y, double[] yDot) {
                                        System.out.println("One event occurred at " + t);
                                        return 1;
                                    }
                                };
                                TwoStepsEventHandler handler2 = new TwoStepsEventHandler() {
                                    @Override
                                    public int handle(double t, double[] y, double[] yDot) {
                                        System.out.println("Two events occurred at " + t);
                                        return 1;
                                    }
                                };
                                integrator.addStepHandler(handler);
                                integrator.addStepHandler(handler2);
                                stepHandlers = integrator.getStepHandlers();
                                super.interpolate(t, y);
                                integrator.removeStepHandler(handler);
                                integrator.removeStepHandler(handler2);
                                return result;
                            }
                        };
                    }
                };
            }
        };
        FirstOrderDifferentialEquations equations = mockDifferentialEquationsWithEvent();
        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];
        integrator.integrate(equations, t0, y0, t1, y1);
        assertEquals(1.0 + 0.5 * 0.5 * 1.0 * (1.0 - 0.5), y1[0], 1e-14);
        assertEquals(1.0 + 0.5 * 0.5 * 1.0 * (1.0 - 0.5), y1[1], 1e-14);
        assertEquals("One event occurred at 0.5", integrator.getStepHandlers().get(0).getLog());
        assertEquals("One event occurred at 0.5", integrator.getStepHandlers().get(1).getLog());
        assertNull(integrator.getStepHandlers().get(2));
    }

    private FirstOrderDifferentialEquations mockDifferentialEquations() {
        return new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 1.0;
            }
        };
    }

    private FirstOrderDifferentialEquations mockDifferentialEquationsWithEvent() {
        return new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 1.0;
                yPrime[1] = y[1] * 1.0;
            }

            @Override
            public double[] getInitialValue(int i) {
                return new double[]{1.0, 1.0};
            }

            @Override
            public double getLowerBound(int i) {
                return -1.0;
            }

            @Override
            public double getUpperBound(int i) {
                return 1.0;
            }

            @Override
            public MockEvent getEvent(int index) {
                return new MockEvent(index, 0.5);
            }

            @Override
            public int getNumberOfEvents() {
                return 2;
            }
        };
    }

}