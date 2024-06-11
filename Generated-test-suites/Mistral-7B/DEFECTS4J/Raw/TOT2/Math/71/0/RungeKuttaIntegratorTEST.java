package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativeEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the RungeKuttaIntegrator class.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test the RungeKuttaIntegrator integration of a simple differential equation.
     */
    @Test
    public void testSimpleIntegration() throws Exception {
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new double[] { 1.0 }, new double[][] { { 1.0 } }, new double[] { 0.0 }, new EulerInterpolator(), 0.1);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 0.1;
            }
        };

        double startTime = 0.0;
        double endTime = 1.0;
        double[] initialCondition = new double[] { 1.0 };

        double result = integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
        Assert.assertEquals(endTime, result, 1e-9);
    }

    /**
     * Test the RungeKuttaIntegrator integration with events.
     */
    @Test
    public void testIntegrationWithEvents() throws Exception {
        RungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.1, new double[] { 0.5, 1.0 }, new double[][] { { 0.25, 0.6 }, { 0, 1.0 } }, new double[] { 0.25, 0.75 }, new ClassicalRungeKuttaInterpolator(), 0.1);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 0.1;
            }
        };

        double startTime = 0.0;
        double endTime = 2.0;
        double[] initialCondition = new double[] { 1.0 };

        integrator.addStepHandler(t -> {
            if (t >= 1.0) {
                throw new RelativeEvent(t, 1.0);
            }
        });

        try {
            integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
            Assert.fail();
        } catch (IntegratorException e) {
            // expected
        }
    }

    /**
     * Test the RungeKuttaIntegrator when the differential equation throws an exception.
     */
    @Test(expected = NumberFormatException.class)
    public void testExceptionInDifferentialEquation() throws Exception {
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new double[] { 1.0 }, new double[][] { { 1.0 } }, new double[] { 0.0 }, new EulerInterpolator(), 0.1);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) throws NumberFormatException {
                throw new NumberFormatException("Invalid format: test");
            }
        };

        double startTime = 0.0;
        double endTime = 1.0;
        double[] initialCondition = new double[] { 1.0 };

        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
    }

    /**
     * Test the RungeKuttaIntegrator when the differential equation throws a maximum count exceeded exception.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testExceptionInDifferentialEquationMaxCountExceeded() throws Exception {
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new double[] { 1.0 }, new double[][] { { 1.0 } }, new double[] { 0.0 }, new EulerInterpolator(), 0.1);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) throws MaxCountExceededException {
                throw new MaxCountExceededException("Maximum count exceeded: test");
            }
        };

        double startTime = 0.0;
        double endTime = 1.0;
        double[] initialCondition = new double[] { 1.0 };

        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
    }
}