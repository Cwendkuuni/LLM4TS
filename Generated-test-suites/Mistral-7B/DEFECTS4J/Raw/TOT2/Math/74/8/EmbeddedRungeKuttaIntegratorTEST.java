package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.DerivativeException;
import org.apache.commons.math3.ode.IntegratorException;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.Verifyable;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testSimpleIntegration() {
        final double[] initialValues = {1.0};
        final double startTime = 0.0;
        final double endTime = 2.0;

        // Testing RK2
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RK2", false,
                new double[]{0.5},
                new double[][]{{0.5, 0.25}},
                new double[]{0.5, 0.5},
                new RungeKuttaStepInterpolator() {

                    @Override
                    public int getOrder() {
                        return 2;
                    }

                    @Override
                    public void initialize(EmbeddedRungeKuttaIntegrator integrator, double[] y, double[] yDot, boolean forward) {
                        // empty
                    }

                }, 0.001, 0.01, null, null) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]);
            }
        };

        integrator.setMinStep(0.01);
        integrator.setMaxStep(2.0);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -y[0] * 0.1;
            }
        };

        try {
            double result = integrator.integrate(equations, startTime, initialValues, endTime);
            assertEquals(endTime, result, 1e-6);
        } catch (DerivativeException | IntegratorException e) {
            fail("Integration failed: " + e.getMessage());
        }
    }

    @Test
    public void testEdgeCases() {
        // Testing RK3
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RK3", false,
                new double[]{0.3, 0.4, 0.3},
                new double[][]{{0.3, 0.216666667, 0.1},
                              {0.4, 0.333333333, 0.3}},
                new double[]{0.333333333, 0.333333333},
                new RungeKuttaStepInterpolator() {

                    @Override
                    public int getOrder() {
                        return 3;
                    }

                    @Override
                    public void initialize(EmbeddedRungeKuttaIntegrator integrator, double[] y, double[] yDot, boolean forward) {
                        // empty
                    }

                }, 0.001, 0.01, null, null) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]);
            }
        };

        integrator.setMinStep(0.01);
        integrator.setMaxStep(2.0);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -y[0] * 0.1;
            }
        };

        try {
            double result = integrator.integrate(equations, 0.0, new double[]{1.0}, 0.01);
            assertEquals(0.01, result, 1e-6);
        } catch (DerivativeException | IntegratorException e) {
            fail("Integration failed: " + e.getMessage());
        }

        try {
            double result = integrator.integrate(equations, 1.0, new double[]{1.0}, 0.01);
            assertEquals(1.0, result, 1e-6);
        } catch (DerivativeException | IntegratorException e) {
            fail("Integration failed: " + e.getMessage());
        }

        try {
            double result = integrator.integrate(equations, 0.0, new double[]{1.0}, -0.01);
            fail("Integration should have failed due to negative step size");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            double result = integrator.integrate(equations, 0.0, new double[]{1.0}, 10.0);
            fail("Integration should have failed due to step size too large");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            double result = integrator.integrate(equations, 1.0, new double[]{1.0}, 0.01);
            assertEquals(1.0, result, 1e-6);
        } catch (DerivativeException | IntegratorException e) {
            fail("Integration failed: " + e.getMessage());
        }
    }

    @Test
    public void testExceptionHandling() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RK4", false,
                new double[]{0.5, 0.5, 0.5, 0.5},
                new double[][]{{0.5, 0.25, 0.25, 0.25},
                              {0.5, 0.125, 0.125, 0.375}},
                new double[]{0.625, 0.375},
                new RungeKuttaStepInterpolator() {

                    @Override
                    public int getOrder() {
                        return 4;
                    }

                    @Override
                    public void initialize(EmbeddedRungeKuttaIntegrator integrator, double[] y, double[] yDot, boolean forward) {
                        // empty
                    }

                }, 0.001, 0.01, null, null) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]);
            }

            @Override
            public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t)
                    throws DerivativeException, IntegratorException {
                throw new NotStrictlyPositiveException("t must be strictly positive");
            }
        };

        integrator.setMinStep(0.01);
        integrator.setMaxStep(2.0);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -y[0] * 0.1;
            }
        };

        try {
            integrator.integrate(equations, 0.0, new double[]{1.0}, -0.01);
            fail("Integration should have failed due to negative step size");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            integrator.integrate(equations, 0.0, new double[]{1.0}, 10.0);
            fail("Integration should have failed due to step size too large");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            integrator.integrate(equations, 0.0, new double[]{1.0}, 0.01);
            fail("Integration should have thrown NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            integrator.integrate(new FirstOrderDifferentialEquations() {

                @Override
                public int getDimension() {
                    return 1;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    throw new MaxCountExceededException("Max count exceeded");
                }
            }, 0.0, new double[]{1.0}, 0.01);
            fail("Integration should have thrown MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testEvents() throws EventException {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RKF45", true,
                new double[]{0.5, 0.5},
                new double[][]{{0.25, 0.25}, {0.25, 0.375}},
                new double[]{0.3125, 0.3125},
                new RungeKuttaStepInterpolator() {

                    @Override
                    public int getOrder() {
                        return 4;
                    }

                    @Override
                    public void initialize(EmbeddedRungeKuttaIntegrator integrator, double[] y, double[] yDot, boolean forward) {
                        // empty
                    }

                }, 0.001, 0.01, null, null) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]);
            }

            @Override
            public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, StepHandler handler)
                    throws DerivativeException, IntegratorException {
                if (t > 2.0) {
                    throw new EventException(new VerifyableEvent() {

                        @Override
                        public boolean isSatisfied(double t, double[] y) {
                            return y[0] > 2.0;
                        }
                    });
                }
                return super.integrate(equations, t0, y0, t, handler);
            }
        };

        integrator.setMinStep(0.01);
        integrator.setMaxStep(2.0);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -y[0] * 0.1;
            }
        };

        StepHandler handler = new StepHandler() {
            private int stepCount = 0;

            @Override
            public void handleStep(StepHandler handler, int stepNumber) {
                System.out.println("Step " + stepNumber);
                stepCount++;
            }
        };

        integrator.addStepHandler(handler);

        integrator.integrate(equations, 0.0, new double[]{1.0}, 3.0, null);
        assertEquals(2, stepCount);
    }

}