package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialCondition;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };
        initialCondition = new double[]{0.0, 1.0};
        equations = new DummyDifferentialEquations(function);
    }

    @Test
    public void testIntegrate() throws Exception {
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{
                        {0.25, 0.0, 0.25, 0.0, 0.25, 0.0},
                        {0,   0.25, 0,   0.25, 0,   0.25},
                        {0.6875, 0.0, 0.0, 0.6875, 0.3125, 0.3125},
                        {0.0,   0.6875, 0.6875, 0.0,   0.3125, 0.3125}
                },
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(1.0),
                0.001, 0.01) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0,
                                           double[] y1, double h) {
                return Math.abs(Math.sub(y1, y0)[0]);
            }
        };

        double tEnd = 10.0;
        double result = integrator.integrate(equations, 0.0, initialCondition, tEnd, new double[initialCondition.length]);
        assertEquals(tEnd, result, 1e-9);
    }

    @Test
    public void testIntegratorWithEvent() throws Exception {
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{
                        {0.25, 0.0, 0.25, 0.0, 0.25, 0.0},
                        {0,   0.25, 0,   0.25, 0,   0.25},
                        {0.6875, 0.0, 0.0, 0.6875, 0.3125, 0.3125},
                        {0.0,   0.6875, 0.6875, 0.0,   0.3125, 0.3125}
                },
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(1.0),
                0.001, 0.01) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0,
                                           double[] y1, double h) {
                return Math.abs(Math.sub(y1, y0)[0]);
            }
        };

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double t = interpolator.getT();
                if (t >= 5.0) {
                    throw new EventOccurredException();
                }
            }
        };

        equations.setStepHandler(stepHandler);

        integrator.setStepHandlers(stepHandler);

        double tEnd = 10.0;
        integrateWithEvents(integrator, equations, initialCondition, tEnd);
    }

    @Test
    public void testIntegratorWithException() throws Exception {
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{
                        {0.25, 0.0, 0.25, 0.0, 0.25, 0.0},
                        {0,   0.25, 0,   0.25, 0,   0.25},
                        {0.6875, 0.0, 0.0, 0.6875, 0.3125, 0.3125},
                        {0.0,   0.6875, 0.6875, 0.0,   0.3125, 0.3125}
                },
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(1.0),
                0.001, 0.01) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0,
                                           double[] y1, double h) {
                throw new DerivativeException("Test Error");
            }
        };

        double tEnd = 10.0;
        assertThrows(DerivativeException.class, () -> integrator.integrate(equations, 0.0, initialCondition, tEnd, new double[initialCondition.length]));
    }

    @Test
    public void testIntegratorWithSingularMatrix() throws Exception {
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{
                        {0.25, 0.0, 0.25, 0.0, 0.25, 0.0},
                        {0,   0.25, 0,   0.25, 0,   0.25},
                        {0.6875, 0.0, 0.0, 0.6875, 0.3125, 0.3125},
                        {0.0,   0.6875, -0.6875, 0.0,   0.3125, 0.3125}
                },
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(1.0),
                0.001, 0.01) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0,
                                           double[] y1, double h) {
                return Math.abs(Math.sub(y1, y0)[0]);
            }
        };

        double tEnd = 10.0;
        integrateWithEvents(integrator, equations, initialCondition, tEnd);
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    private void integrateWithEvents(EmbeddedRungeKuttaIntegrator integrator,
                                    FirstOrderDifferentialEquations equations,
                                    double[] initialCondition, double tEnd) throws Exception {
        equations.setEvents(new Event[] {
                new When(t -> t >= 5.0, new Event() {
                    @Override
                    public void action(double t, double[] y, double[] yDot) {
                        throw new EventOccurredException();
                    }
                })
        });
        integrator.integrate(equations, 0.0, initialCondition, tEnd, new double[initialCondition.length]);
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    private UnivariateFunction function;

    public DummyDifferentialEquations(UnivariateFunction function) {
        this.function = function;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        for (int i = 0; i < y.length; i++) {
            yDot[i] = function.value(t) * Math.sin(BesselFunctions.BesselI(i, t));
        }
    }

}

class EventOccurredException extends Exception {

}