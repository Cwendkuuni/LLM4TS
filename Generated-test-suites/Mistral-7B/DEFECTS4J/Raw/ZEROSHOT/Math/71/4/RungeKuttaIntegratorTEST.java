package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        double[] c = {0.0, 0.25, 0.5, 0.875, 1.0};
        double[][] a = new double[][]{{0.0, 0.0},
                                        {0.25, 0.25},
                                        {0.5, 0.5},
                                        {0.875, 0.375},
                                        {1.0, 1.0}};
        double[] b = {1.0 / 6.0, 1. / 3.0, 1. / 2.0, 1. / 4.0, 1.};
        integrator = new RungeKuttaIntegrator("RK5", c, a, b, new RungeKuttaStepInterpolator(), 0.01) {
            @Override
            protected void sanityChecks(FirstOrderDifferentialEquations equations, double t, double[] y, double step)
                    throws IntegratorException {
                super.sanityChecks(equations, t, y, step);
                if (equations == null) {
                    throw new MathIllegalArgumentException("equations cannot be null");
                }
                if (step <= 0.0) {
                    throw new NumberIsTooSmallException("step cannot be less than or equal to zero");
                }
            }
        };
        equations = new MockDifferentialEquations();
    }

    @Test
    public void testIntegration() throws Exception {
        double y0[] = new double[]{0.0, 0.0};
        double tStart = 0.0;
        double tEnd = 1.0;

        integrator.integrate(equations, tStart, y0, tEnd, new double[2]);

        double expected[] = new double[]{0.31622776601683795, 0.31622776601683795};
        Assert.assertArrayEquals(expected, new double[]{1.0, 1.0}, new double[]{y[0], y[1]});
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() throws Exception {
        double y0[] = new double[]{0.0, 0.0};
        double tStart = 0.0;
        double tEnd = 1.0;
        FirstOrderDifferentialEquations equations = new MockDifferentialEquationsWithException();

        integrator.integrate(equations, tStart, y0, tEnd, new double[2]);
    }

    @Test
    public void testIntegrationWithStepHandler() throws Exception {
        double y0[] = new double[]{0.0, 0.0};
        double tStart = 0.0;
        double tEnd = 1.0;

        StepHandler stepHandler = new StepCounter();
        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, tStart, y0, tEnd, new double[2]);

        double expected[] = new double[]{1.0, 1.0};
        Assert.assertArrayEquals(expected, new double[]{y[0], y[1]});
        Assert.assertEquals(6, stepHandler.getNumberOfSteps());
    }

    @Test
    public void testIntegrationWithEvent() throws Exception {
        double y0[] = new double[]{0.0, 0.0};
        double tStart = 0.0;
        double tEnd = 2.0;

        FirstOrderDifferentialEquations equations = new MockDifferentialEquationsWithEvent();
        integrator.setEvents(new When(1.0, new Event() {
            @Override
            public int getId() {
                return 1;
            }
        }));

        integrator.integrate(equations, tStart, y0, tEnd, new double[2]);

        double expected[] = new double[]{1.0, 1.0};
        Assert.assertArrayEquals(expected, new double[]{y[0], y[1]});
    }

}

class MockDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[1];
    }

}

class MockDifferentialEquationsWithException implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new DerivativeException("Test DerivativeException");
    }

}

class MockDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {

    private int count = 0;

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[1];
        if (count++ >= 1) {
            throw new RuntimeException("Test Event");
        }
    }

}

class StepCounter implements StepHandler {

    private int numberOfSteps = 0;

    @Override
    public void handleStep(StepHandler.IntegratorStatus status, double[] y, double step) {
        numberOfSteps++;
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }

}