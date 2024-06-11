package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.util.ODETestUtils;

/**
 * Tests for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest extends TestCase {

    /**
     * Test the Runge-Kutta integrator with a simple set of differential equations.
     */
    public void testSimpleIntegrator() {
        final double[] a = { 0.0, 0.5, 0.5, 1.0 };
        final double[] b = { 0.0, 0.25, 0.25, 0.5 };
        final double[] c = { 0.0, 0.2, 0.3, 0.5 };
        final double step = 0.1;

        RungeKuttaIntegrator integrator = new MidpointIntegrator(
                "Midpoint", a, b, c, new MidpointStepInterpolator(), step);

        double y0 = 1.0;
        double t0 = 0.0;
        double t1 = 2.0;
        double[] y1 = new double[] { 2.0 };

        RungeKuttaIntegrator.calculateStep(integrator, new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y1.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y1.length; ++i) {
                    yPrime[i] = y[i] * 0.1;
                }
            }
        }, t0, y0, t1, y1);

        double t2 = 3.0;
        double[] y2 = new double[] { 2.3 };
        RungeKuttaIntegrator.calculateStep(integrator, new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y2.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y2.length; ++i) {
                    yPrime[i] = y[i] * 0.1 + 0.1;
                }
            }
        }, t1, y1, t2, y2);

        double t3 = 4.0;
        double[] y3 = new double[] { 2.63 };

        RungeKuttaIntegrator.calculateStep(integrator, new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y3.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y3.length; ++i) {
                    yPrime[i] = y[i] * 0.1 + 0.1 + Math.exp(y[i]);
                }
            }
        }, t2, y2, t3, y3);

        StepHandler printer = new StepHandler() {
            @Override
            public void handleStep(RungeKuttaIntegrator.RungeKuttaStepInterpolator interpolator, boolean last) {
                System.out.println("Step " + interpolator.getStepNumber() + " from "
                        + interpolator.getOldTime() + " to " + interpolator.getTime()
                        + " with step size " + interpolator.getStepSize());
            }
        };
        integrator.setStepHandler(printer);

        double t4 = 4.5;
        double[] y4 = new double[] { 2.65 };

        try {
            RungeKuttaIntegrator.calculateStep(integrator, new FirstOrderDifferentialEquations() {
                @Override
                public int getDimension() {
                    return y4.length;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    for (int i = 0; i < y4.length; ++i) {
                        yPrime[i] = y[i] * 0.1 + 0.1 + Math.exp(y[i] * 2.0);
                    }
                }
            }, t3, y3, t4, y4);
            fail("Should have thrown an exception");
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        double[] y5 = new double[] { 2.65, 2.65 };
        double t5 = 4.6;
        RungeKuttaIntegrator.calculateStep(integrator, new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y5.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y5.length; ++i) {
                    yPrime[i] = y[i] * 0.1 + 0.1 + Math.exp(y[i] * 2.0);
                }
            }
        }, t3, y3, t5, y5);

        ODETestUtils.assertYEquals(y4, y5, 1e-10);

    }

    /**
     * Test the Runge-Kutta integrator with an event.
     */
    public void testIntegratorWithEvent() {
        final double[] a = { 0.0, 0.5, 0.5, 1.0 };
        final double[] b = { 0.0, 0.25, 0.25, 0.5 };
        final double[] c = { 0.0, 0.2, 0.3, 0.5 };
        final double step = 0.1;

        RungeKuttaIntegrator integrator = new MidpointIntegrator(
                "Midpoint", a, b, c, new MidpointStepInterpolator(), step);

        double y0 = 1.0;
        double t0 = 0.0;
        double t1 = 1.5;
        double[] y1 = new double[] { 1.5 };

        RungeKuttaIntegrator.calculateStep(integrator, new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y1.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y1.length; ++i) {
                    yPrime[i] = y[i] * 0.1;
                }
            }
        }, t0, y0, t1, y1);

        double[] y2 = new double[] { 1.6 };

        RungeKuttaIntegrator.calculateStep(integrator, new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y2.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y2.length; ++i) {
                    yPrime[i] = y[i] * 0.1;
                }
            }
        }, t1, y1, Double.POSITIVE_INFINITY, y2);

        final Event event = new When(1.75);

        integrator.setEventsManager(new CombinedEventsManager() {
            @Override
            public Event getNextEvent(double t, double[] y) {
                return event;
            }
        });

        double t2 = 2.0;
        double[] y3 = new double[] { 1.8 };

        RungeKuttaIntegrator.calculateStep(integrator, new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y3.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y3.length; ++i) {
                    yPrime[i] = y[i] * 0.1;
                }
            }
        }, t1, y1, t2, y3);

        StepHandler printer = new StepHandler() {
            @Override
            public void handleStep(RungeKuttaIntegrator.RungeKuttaStepInterpolator interpolator, boolean last) {
                System.out.println("Step " + interpolator.getStepNumber() + " from "
                        + interpolator.getOldTime() + " to " + interpolator.getTime()
                        + " with step size " + interpolator.getStepSize());
            }
        };
        integrator.setStepHandler(printer);

        double t3 = 2.1;
        double[] y4 = new double[] { 1.9 };

        RungeKuttaIntegrator.calculateStep(integrator, new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y4.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y4.length; ++i) {
                    yPrime[i] = y[i] * 0.1;
                }
            }
        }, t2, y3, t3, y4);

        ODETestUtils.assertYEquals(y3, y4, 1e-10);

    }

}