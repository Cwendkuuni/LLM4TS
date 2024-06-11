package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.RelativisticEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

public class RungeKuttaIntegratorTest {
    /**
     * Test case for the RungeKuttaIntegrator class with a simple system of
     * differential equations.
     */
    @Test
    public void testSimpleSystem() {
        final double[][] func = {{-0.0, 1.0}, {1.0, -1.0}};
        final double[] initialValues = {1.0, 1.0};
        final double[] finalValues = {2.0, 2.0};
        final double startTime = 0.0;
        final double endTime = 2.0;
        final double stepSize = 0.1;

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4",
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0.}, {0.5, 0.25}, {0.8333333333333333, 0.5}, {1.0, 0.8333333333333333}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), stepSize) {
            @Override
            protected FirstOrderDifferentialEquations createDifferentialEquations(double[] y) {
                return new FirstOrderDifferentialEquations() {
                    @Override
                    public int getDimension() {
                        return func.length;
                    }

                    @Override
                    public void computeDerivatives(double t, double[] y, double[] yPrime) {
                        for (int i = 0; i < func.length; ++i) {
                            yPrime[i] = func[i][0] * y[i] + func[i][1] * y[i+1];
                        }
                    }
                };
            }
        };

        StepHandler stepHandler = new StepHandler() {
            private int counter = 0;

            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                if (stepNumber == 10) {
                    throw new MaxIterationsExceededException("Maximum iterations exceeded", 10);
                }
                ++counter;
            }
        };

        try {
            integrator.integrate(startTime, initialValues, endTime, new double[initialValues.length]);
            assertFalse("Expected an exception, but no exception was thrown", false);
        } catch (IntegratorException e) {
            assertTrue(true);
        }

        assertArrayEquals("Initial values should be the same as the final values", finalValues, integrator.getY());

        integrator = new RungeKuttaIntegrator("RK4",
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0.}, {0.5, 0.25}, {0.8333333333333333, 0.5}, {1.0, 0.8333333333333333}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), stepSize) {
            @Override
            protected FirstOrderDifferentialEquations createDifferentialEquations(double[] y) {
                return new FirstOrderDifferentialEquations() {
                    @Override
                    public int getDimension() {
                        return func.length;
                    }

                    @Override
                    public void computeDerivatives(double t, double[] y, double[] yPrime) {
                        for (int i = 0; i < func.length; ++i) {
                            yPrime[i] = func[i][0] * y[i] + func[i][1] * y[i+1];
                        }
                    }
                };
            }
        };

        integrator.addStepHandler(stepHandler);

        try {
            integrator.integrate(startTime, initialValues, endTime, new double[initialValues.length]);
            assertTrue("Expected an exception after 10 steps", stepHandler.isHandled());
        } catch (IntegratorException e) {
            assertTrue(false);
        }

        Event event = new DefaultValueEvent(1.5, 1.0);
        integrator = new RungeKuttaIntegrator("RK4",
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0.}, {0.5, 0.25}, {0.8333333333333333, 0.5}, {1.0, 0.8333333333333333}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), stepSize) {
            @Override
            protected FirstOrderDifferentialEquations createDifferentialEquations(double[] y) {
                return new FirstOrderDifferentialEquations() {
                    @Override
                    public int getDimension() {
                        return 1;
                    }

                    @Override
                    public void computeDerivatives(double t, double[] y, double[] yPrime) {
                        yPrime[0] = y[0];
                    }
                };
            }
        };

        integrator.setEventsHandlersManager(new CombinedEventsManager());
        integrator.addEndTimeChecker(1.5, new RelativisticEvent(1.5, 0.001, new double[]{}));

        try {
            integrator.integrate(0.0, new double[]{0.1}, 1.5, new double[1]);
            assertTrue("Expected an exception, but no exception was thrown", false);
        } catch (IntegratorException e) {
            assertTrue(true);
        }

        StepHandler lastStepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                if (stepNumber == integrator.getNumSteps()) {
                    throw new PrecisionException("Precision exceeded");
                }
            }
        };

        integrator.addStepHandler(lastStepHandler);

        try {
            integrator.integrate(0.0, new double[]{0.1}, 1.5, new double[1]);
            assertTrue("Expected an exception", false);
        } catch (IntegratorException e) {
            assertTrue(true);
        }

    }
}