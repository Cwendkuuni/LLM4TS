package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.Integrator;
import org.apache.commons.math3.ode.events.StepEvent;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;

public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(EmbeddedRungeKuttaIntegratorTest.class);
    }

    public void testRK2() {
        final double[][] coefficients = {{0.0, 0.5, 0.5}, {0.25, 0.0, 0.5}, {0.0, 0.5, 0.5}};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(coefficients);
        Integrator integrator = new EmbeddedRungeKuttaIntegrator("RK2", false, coefficients[0], coefficients[1], coefficients[2], prototype, 0.01, 0.01) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (Math.abs(y1[0]) + Math.abs(y0[0]) + 1);
            }
        };

        integrator.setMinStep(0.001);
        integrator.setMaxStep(0.1);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 1;
            }

            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = -state[0] * state[0] * 0.01;
            }
        };

        double[] initialState = {1.0};
        double startTime = 0.0;
        double endTime = 10.0;
        double stepSize = 0.05;

        integrator.setEquations(equations);

        StepHandler stepHandler = new StepHandler() {
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double time = context.getStepTime();
                double[] state = context.getState();
                Assert.assertEquals(Math.sin(Math.sqrt(time)), state[0], 0.001);
            }
        };

        integrator.addStepHandler(stepHandler);

        try {
            integrator.integrate(equations, startTime, initialState, endTime, null);
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }

    }

    public void testRK3() {
        final double[][] coefficients = {{0.0, 0.3, 0.4, 0.3, 0.1}, {0.25, 0.0, 0.3, 0.4, 0.6}, {0.0, 0.3, 0.0, 1.0, 1.0}, {0.0, 0.3, 0.6, 0.0, 1.0}, {0.0, 0.6, 1.0, 0.0, 0.0}};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(coefficients);
        Integrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", true, coefficients[0], coefficients[1], coefficients[2], prototype, 0.01, 0.01) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (Math.abs(y1[0]) + Math.abs(y0[0]) + 1);
            }
        };

        integrator.setMinStep(0.001);
        integrator.setMaxStep(0.1);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 1;
            }

            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * state[0] * 0.01;
            }
        };

        double[] initialState = {1.0};
        double startTime = 0.0;
        double endTime = 10.0;

        integrator.setEquations(equations);

        StepHandler stepHandler = new StepHandler() {
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double time = context.getStepTime();
                double[] state = context.getState();
                Assert.assertEquals(Math.exp(Math.sqrt(time)), state[0], 0.001);
            }
        };

        integrator.addStepHandler(stepHandler);

        try {
            integrator.integrate(equations, startTime, initialState, endTime, null);
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }

    }

    public void testRootFinder() {
        final double[][] coefficients = {{0.0, 0.5, 0.5}, {0.25, 0.0, 0.5}, {0.0, 0.5, 0.5}};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(coefficients);
        Integrator integrator = new EmbeddedRungeKuttaIntegrator("RK2", false, coefficients[0], coefficients[1], coefficients[2], prototype, 0.01, 0.01) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (Math.abs(y1[0]) + Math.abs(y0[0]) + 1);
            }

            @Override
            public void integrate(FirstOrderDifferentialEquations equations, double start, double[] y0, double end, double[] y)
                    throws DerivativeException, IntegratorException {

                super.integrate(equations, start, y0, end, y);

                if (y[0] > 0) {
                    throw new IntegratorException("Expected root at x = 1.0, found x = " + y[0]);
                }
            }
        };

        integrator.setMinStep(0.001);
        integrator.setMaxStep(0.1);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 1;
            }

            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = -(time - 1.0) * state[0];
            }
        };

        double[] initialState = {1.0};
        double startTime = 0.0;
        double endTime = 1.1;

        integrator.setEquations(equations);

        StepHandler stepHandler = new StepHandler() {
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double time = context.getStepTime();
                double[] state = context.getState();
                Assert.assertEquals(1.0, state[0], 0.01);
            }
        };

        integrator.addStepHandler(stepHandler);

        try {
            integrator.integrate(equations, startTime, initialState, endTime, null);
            fail("Expected an IntegratorException to be thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IntegratorException);
        }

        double[] roots = new double[] {1.0};
        When event = new When(1.0, roots, new StepEvent() {
            public double getLocalTime() {
                return getState()[0];
            }
        });
        integrator.addEventHandler(event);

        try {
            integrator.integrate(equations, startTime, initialState, endTime, null);
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }

    }

    public void testStepInterpolation() {
        final double[][] coefficients = {{0.0, 0.3, 0.4, 0.3, 0.1}, {0.25, 0.0, 0.3, 0.4, 0.6}, {0.0, 0.3, 0.0, 1.0, 1.0}, {0.0, 0.3, 0.6, 0.0, 1.0}, {0.0, 0.6, 1.0, 0.0, 0.0}};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(coefficients);
        Integrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", true, coefficients[0], coefficients[1], coefficients[2], prototype, 0.01, 0.01) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (Math.abs(y1[0]) + Math.abs(y0[0]) + 1);
            }
        };

        integrator.setMinStep(0.001);
        integrator.setMaxStep(0.1);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 1;
            }

            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = Math.sin(time);
            }
        };

        double[] initialState = {0.0};
        double startTime = 0.0;
        double endTime = 2.0 * Math.PI;

        integrator.setEquations(equations);

        StepHandler stepHandler = new StepHandler() {
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double time = context.getStepTime();
                double[] state = context.getState();
                double expected = Polynomials.sinc(time);
                Assert.assertEquals(expected, state[0], 0.01);
            }
        };

        integrator.addStepHandler(stepHandler);

        try {
            integrator.integrate(equations, startTime, initialState, endTime, null);
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }

    }

}