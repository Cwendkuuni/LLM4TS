package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private StepInterpolator interpolator;
    private StepHandler stepHandler;

    @Before
    public void setup() {
        integrator = new EmbeddedRungeKuttaIntegrator("testIntegrator", true,
                new double[] {0.3, 0.6, 1.0, 1.0},
                new double[][] {{-0.3, 0.3, 0.6, 1.0}, {0.3, -0.3, 0.6, 1.0},
                        {0.6, 0.3, 0, 1.0}, {1.0, 1.0, 1.0, 0.0}},
                new double[] {0.5, 0.25, 0.25, 0.0},
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                new double[] {1e-12, 1e-12}, null) {
                    @Override
                    protected double estimateError(double[][] yDotK,
                                                   double[] y0, double[] y1,
                                                   double h) {
                        double error = 0.0;
                        for (int i = 0; i < y0.length; i++) {
                            error += Math.abs(y1[i] - y0[i]);
                        }
                        return error;
                    }
                };

        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.sin(y[0]);
            }
        };

        interpolator = integrator.getPrototype().copy();
        stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                System.out.println("Step " + stepNumber + " at time "
                        + interpolator.getTime());
            }
        };
    }

    @Test
    public void testIntegration() {
        double[] y0 = {Math.PI / 4.0};
        double t = 2.0;
        double result = integrator.integrate(equations, 0.0, y0, t, new double[y0.length]);
        assertEquals(t, result, 1e-12);
        // check intermediate steps
        interpolator.shift();
        assertEquals(Math.PI / 4.0, interpolator.getY()[0], 1e-12);
        assertEquals(0.3 * Math.sin(Math.PI / 4.0), interpolator.getYDot()[0], 1e-12);
        interpolator.shift();
        assertEquals(Math.PI / 4.0 + 0.3 * Math.sin(Math.PI / 4.0), interpolator.getY()[0], 1e-12);
        assertEquals(0.6 * Math.sin(Math.PI / 4.0 + 0.3 * Math.sin(Math.PI / 4.0)), interpolator.getYDot()[0], 1e-12);
        interpolator.shift();
        assertEquals(Math.PI / 4.0 + 0.3 * Math.sin(Math.PI / 4.0) + 0.6 * Math.sin(Math.PI / 4.0 + 0.3 * Math.sin(Math.PI / 4.0)), interpolator.getY()[0], 1e-12);
        assertEquals(1.0 * Math.sin(Math.PI / 4.0 + 0.3 * Math.sin(Math.PI / 4.0) + 0.6 * Math.sin(Math.PI / 4.0 + 0.3 * Math.sin(Math.PI / 4.0))), interpolator.getYDot()[0], 1e-12);
        interpolator.shift();
        assertEquals(t, interpolator.getTime(), 1e-12);
    }

    @Test
    public void testIntegrationWithEvents() {
        double[] y0 = {0.0};
        double tStart = 0.0;
        double tEnd = Math.PI;
        double stepSize = 0.1;
        integrator = new EmbeddedRungeKuttaIntegrator("testIntegrator", true,
                new double[] {0.3, 0.6, 1.0, 1.0},
                new double[][] {{-0.3, 0.3, 0.6, 1.0}, {0.3, -0.3, 0.6, 1.0},
                        {
                                0.6, 0.3, 0, 1.0}, {1.0, 1.0, 1.0, 0.0}},
                new double[] {0.5, 0.25, 0.25, 0.0},
                new RungeKuttaStepInterpolator(),
                tStart, tEnd,
                new double[] {1e-12, 1e-12},
                new double[] {Math.PI / 2.0}) {
                    @Override
                    protected double estimateError(double[][] yDotK,
                                                   double[] y0, double[] y1,
                                                   double h) {
                        double error = 0.0;
                        for (int i = 0; i < y0.length; i++) {
                            error += Math.abs(y1[i] - y0[i]);
                        }
                        return error;
                    }

                    @Override
                    protected void addEvent(double t, double[] value) {
                        super.addEvent(new When(t) {
                            @Override
                            public void handle(double t, double[] y) {
                                stepHandler.handleStep(interpolator, interpolator.getStepNumber());
                            }
                        });
                    }
                };

        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                System.out.println("Step " + stepNumber + " at time "
                        + interpolator.getTime());
            }
        };
        integrator.addStepHandler(handler);
        integrator.addEventsManager(new CombinedEventsManager());
        integrator.integrate(equations, tStart, y0, tEnd, new double[y0.length]);
    }

}