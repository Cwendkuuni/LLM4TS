package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EmptyCombinedEventsManager;
import org.apache.commons.math3.ode.events.EventsManager;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import java.util.ArrayList;
import java.util.List;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testBasicIntegration() {
        double[][] a = { { 0.0, 0.0, 0.3, 0.3, 0.5 },
                          { 0.0, 0.3, 0.3, 0.27, 0.45, 0.42 },
                          { 0.0, 0.3, 0.45, 0.54, 0.67, 0.68 },
                          { 0.0, 0.6, 0.64, 0.729, 0.812, 0.816, 0.824 },
                          { 0.0, 0.6, 0.729, 0.812, 0.891, 0.906, 0.923, 0.925 },
                          { 0.0, 0.729, 0.812, 0.891, 0.969, 0.984, 0.991, 0.992, 0.993 },
                          { 0.0, 0.812, 0.891, 0.969, 0.998, 0.999, 1.0 } };
        double[] b = { 0.0, 0.3, 0.45, 0.6, 0.812, 0.9 };
        double[] c = { 0.0, 0.3, 0.45, 0.6, 0.729, 0.812, 0.891, 0.969, 0.998 };
        double safety = 0.9;
        double minReduction = 0.2;
        double maxGrowth = 10.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta Method", false, c, a, b, new RungeKuttaStepInterpolator(), 0.01, 1.0, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2.0);
                }
                error = Math.sqrt(error);
                return error;
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = Math.sin(y[i]);
                }
            }
        };

        double[] y0 = new double[] { 0.1, 0.2 };
        double t0 = 0.0;
        double t1 = Math.PI / 4.0;
        double[] y1 = new double[] { Math.sin(0.1), Math.sin(0.2) };

        double result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        assertEquals(t1, result, 1e-14);
        assertArrayEquals(y1, equations.getY(result), 1e-14);
    }

    @Test
    public void testIntegrationWithEvents() {
        double[][] a = { { 0.0, 0.0, 0.3, 0.3, 0.5 },
                          { 0.0, 0.3, 0.3, 0.27, 0.45, 0.42 },
                          { 0.0, 0.3, 0.45, 0.54, 0.67, 0.68 },
                          { 0.0, 0.6, 0.64, 0.729, 0.812, 0.816, 0.824 },
                          { 0.0, 0.6, 0.729, 0.812, 0.891, 0.906, 0.923, 0.925 },
                          { 0.0, 0.729, 0.812, 0.891, 0.969, 0.984, 0.991, 0.992, 0.993 },
                          { 0.0, 0.812, 0.891, 0.969, 0.998, 0.999, 1.0 } };
        double[] b = { 0.0, 0.3, 0.45, 0.6, 0.812, 0.9 };
        double[] c = { 0.0, 0.3, 0.45, 0.6, 0.729, 0.812, 0.891, 0.969, 0.998 };
        double safety = 0.9;
        double minReduction = 0.2;
        double maxGrowth = 10.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta Method", false, c, a, b, new RungeKuttaStepInterpolator(), 0.01, 1.0, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2.0);
                }
                error = Math.sqrt(error);
                return error;
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = Math.sin(y[i]);
                }
            }
        };

        double[] y0 = new double[] { 0.1, 0.2 };
        double t0 = 0.0;
        double t1 = Math.PI / 4.0;
        double[] y1 = new double[] { Math.sin(0.1), Math.sin(0.2) };

        EventsManager eventsManager = new EmptyCombinedEventsManager();
        StepHandler stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double t = interpolator.getTime();
                double[] y = interpolator.getY();
                for (int i = 0; i < y.length; ++i) {
                    if (Math.abs(y[i]) > 1.0) {
                        eventsManager.addEvent(t, new TestEvent());
                    }
                }
            }
        };
        integrator.addStepHandler(stepHandler);

        try {
            integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
            fail();
        } catch (MaxCountExceededException e) {
            // expected
        }

        assertNotNull(eventsManager.getEvents());
        assertTrue(eventsManager.getEvents().size() > 0);
    }

    @Test
    public void testIntegrationWithStepSizeControl() {
        double[][] a = { { 0.0, 0.0, 0.3, 0.3, 0.5 },
                          { 0.0, 0.3, 0.3, 0.27, 0.45, 0.42 },
                          { 0.0, 0.3, 0.45, 0.54, 0.67, 0.68 },
                          { 0.0, 0.6, 0.64, 0.729, 0.812, 0.816, 0.824 },
                          { 0.0, 0.6, 0.729, 0.812, 0.891, 0.906, 0.923, 0.925 },
                          { 0.0, 0.729, 0.812, 0.891, 0.969, 0.984, 0.991, 0.992, 0.993 },
                          { 0.0, 0.812, 0.891, 0.969, 0.998, 0.999, 1.0 } };
        double[] b = { 0.0, 0.3, 0.45, 0.6, 0.812, 0.9 };
        double[] c = { 0.0, 0.3, 0.45, 0.6, 0.729, 0.812, 0.891, 0.969, 0.998 };
        double safety = 0.9;
        double minReduction = 0.2;
        double maxGrowth = 10.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta Method", false, c, a, b, new RungeKuttaStepInterpolator(), 0.01, 1.0, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2.0);
                }
                error = Math.sqrt(error);
                return error;
            }

            @Override
            public void setSafety(double safety) {
                this.safety = 0.1; // override the default value
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = Math.sin(y[i]);
                }
            }
        };

        double[] y0 = new double[] { 0.1, 0.2 };
        double t0 = 0.0;
        double t1 = Math.PI / 4.0;
        double[] y1 = new double[] { Math.sin(0.1), Math.sin(0.2) };

        try {
            integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
            fail();
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        assertEquals(t1, integrator.getCurrentStepStart(), 1e-14);
        assertEquals(1.0 / Math.sin(Math.PI / 4.0), integrator.getStepSize(), 1e-14);
    }
}

class TestEvent {
    // empty class for testing purposes
}