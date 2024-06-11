package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxAbsRelativeErrorExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKutta4Integrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    @Test
    public void testConstructor() {
        try {
            new RungeKuttaIntegrator("", null, null, null, null, 0);
            fail("Should not be able to create integrator with null arguments");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        double[] c = {0.5, 0.5};
        double[][] a = {{0.25, 0}, {0, 0.25}};
        double[] b = {0.5, 0.5};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestIntegrator", c, a, b, new RungeKuttaStepInterpolator(), 0.1);
        assertNotNull(integrator);
        assertEquals("TestIntegrator", integrator.getName());
        assertEquals(c, integrator.getC());
        assertEquals(a, integrator.getA());
        assertEquals(b, integrator.getB());
    }

    @Test
    public void testIntegrateBasic() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(y[0]);
            }
        };

        RungeKuttaIntegrator integrator = new RungeKutta4Integrator(0.1);
        double[] initialConditions = {0.1};
        double startTime = 0.0;
        double endTime = 1.0;

        double result = integrator.integrate(equations, startTime, initialConditions, endTime, new double[initialConditions.length]);

        // The solution for this problem is e^x, so we can check the result against that
        double expectedResult = Math.exp(endTime);
        assertEquals(expectedResult, result, 1e-6);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateDerivativeException() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new DerivativeException("Derivative error");
            }
        };

        RungeKuttaIntegrator integrator = new RungeKutta4Integrator(0.1);
        double[] initialConditions = {0.1};
        double startTime = 0.0;
        double endTime = 1.0;

        integrator.integrate(equations, startTime, initialConditions, endTime, new double[initialConditions.length]);
        fail("Should have thrown DerivativeException");
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateIntegratorException() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                // This test uses an integrator exception by not providing enough memory for the step interpolator
                RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
                prototype.setMaxSize(0);
                RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestIntegrator", null, null, null, prototype, 0.1);
                double[] initialConditions = {0.1};
                double startTime = 0.0;
                double endTime = 1.0;

                integrator.integrate(equations, startTime, initialConditions, endTime, new double[initialConditions.length]);
                fail("Should have thrown IntegratorException");
            }
        };

    }

    @Test
    public void testIntegrateWithEvents() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * (1 - y[0]);
            }
        };

        double[] initialConditions = {0.5};
        double startTime = 0.0;
        double endTime = 2.0;

        RungeKuttaIntegrator integrator = new RungeKutta4Integrator(0.1);

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double time = context.getState(0);
                if (time >= 1.0) {
                    throw new NoBracketingException("Reached end time");
                }
            }
        };
        integrator.addStepHandler(stepHandler);

        integrator. integrate(equations, startTime, initialConditions, endTime, new double[initialConditions.length]);

        // The solution for this problem is y(t) = 1/(1+e^(-t)), which goes to zero as t goes to infinity. However,
        // the integrator should stop at t=1 because of the event we added.
        double expectedResult = 1.0 / (1.0 + Math.E);
        assertEquals(expectedResult, initialConditions[0], 1e-6);

    }

    @Test
    public void testIntegrateMaxAbsRelativeErrorExceeded() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * (1 - y[0]);
            }
        };

        double[] initialConditions = {1.0};
        double startTime = 0.0;
        double endTime = 2.0;
        double maxAbsRelativeError = 1e-3;
        double maxStepSize = 0.01;

        RungeKuttaIntegrator integrator = new RungeKutta4Integrator(maxStepSize);
        integrator.setMaxAbsRelativeError(maxAbsRelativeError);

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double time = context.getState(0);
                if (time >= 1.0) {
                    throw new NoBracketingException("Reached end time");
                }
            }
        };
        integrator.addStepHandler(stepHandler);

        try {
            integrator.integrate(equations, startTime, initialConditions, endTime, new double[initialConditions.length]);
            fail("Should have thrown MaxAbsRelativeErrorExceededException");
        } catch (MaxAbsRelativeErrorExceededException e) {
            // Expected
        }
    }

    @Test
    public void testIntegrateWithCustomStepInterpolator() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        double[] initialConditions = {Math.PI / 2.0};
        double startTime = 0.0;
        double endTime = 2 * Math.PI;

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        prototype.setMaxSize(10);
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestIntegrator", null, null, null, prototype, 0.1);

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double time = context.getState(0);
                if (time > endTime) {
                    throw new NoBracketingException("Reached end time");
                }
                if (time > startTime + Math.PI) {
                    throw new IntegratorException("Reached a discontinuity");
                }
            }
        };
        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, startTime, initialConditions, endTime, new double[initialConditions.length]);

        double expectedResult = Math.sin(endTime);
        assertEquals(expectedResult, initialConditions[0], 1e-6);
    }

    @Test
    public void testIntegrateWithCustomStepInterpolatorAndEvents() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * (1 - y[0]);
            }
        };

        double[] initialConditions = {0.5};
        double startTime = 0.0;
        double endTime = 2.0;

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        prototype.setMaxSize(10);
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestIntegrator", null, null, null, prototype, 0.1);

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double time = context.getState(0);
                if (time >= 1.0) {
                    throw new NoBracketingException("Reached end time");
                }
            }
        };
        integrator.addStepHandler(stepHandler);

        When event = new When() {
            @Override
            public boolean isSatisfied(double t, double[] y) {
                // This test uses an event to stop the integration when y(t) reaches 1.0
                return y[0] >= 1.0;
            }
        };
        integrator.addEvent(event);

        integrator.integrate(equations, startTime, initialConditions, endTime, new double[initialConditions.length]);

        double expectedResult = 1.0;
        assertEquals(expectedResult, initialConditions[0], 1e-6);
    }

}