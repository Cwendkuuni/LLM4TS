package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /**
     * A simple test case for the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegration() {
        // Given
        double[] initialCondition = {0.0, 1.0};
        FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        double startTime = 0.0;
        double endTime = 1.0;
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.01);

        // When
        double result = integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);

        // Then
        assertEquals(endTime, result, 0.0001);
        assertArrayEquals(new double[]{1.718281828, 1.718281828}, initialCondition, 0.0001);
    }

    /**
     * A test case for the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * This test case checks the exception handling for the case when the step size is too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrationStepTooSmall() {
        // Given
        double[] initialCondition = {0.0, 1.0};
        FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        double startTime = 0.0;
        double endTime = 1.0;
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.000001); // step size is too small

        // When
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);

        // This test should fail with NumberIsTooSmallException
    }

    /**
     * A test case for the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * This test case checks the exception handling for the case when the initial condition is null.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrationInitialConditionNull() {
        // Given
        double[] initialCondition = null;
        FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        double startTime = 0.0;
        double endTime = 1.0;
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.01);

        // When
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);

        // This test should fail with MathIllegalArgumentException
    }

    /**
     * A test case for the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * This test case checks the integration of a simple differential equation
     * with an event handling step handler.
     */
    @Test
    public void testIntegrationWithStepHandler() {
        // Given
        double[] initialCondition = {0.0, 1.0};
        FirstOrderDifferentialEquations equations = new SimpleDifferentialEquationsWithEvent();
        double startTime = 0.0;
        double endTime = 2.0;
        double[] expectedResult = {0.367879441, 1.0}; // calculated manually
        RungeKuttaIntegrator integrator = new MidpointIntegrator(0.01);
        StepHandler stepHandler = new StepHandler() {
            public void handleStep(StepHandler handler, int stepNumber) {
                // do something
            }
        };

        // When
        double result[] = integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);

        // Then
        assertEquals(endTime, result[0], 0.0001);
        assertArrayEquals(expectedResult, result, 0.0001);
    }

    /**
     * A test case for the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * This test case checks the exception handling for the case when the equations are null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIntegrationEquationsNull() {
        // Given
        double[] initialCondition = {0.0, 1.0};
        FirstOrderDifferentialEquations equations = null;
        double startTime = 0.0;
        double endTime = 1.0;
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.01);

        // When
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);

        // This test should fail with IllegalArgumentException
    }

    /**
     * A test case for the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * This test case checks the exception handling for the case when the end time is less than the start time.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIntegrationEndTimeLessThanStartTime() {
        // Given
        double[] initialCondition = {0.0, 1.0};
        FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        double startTime = 0.0;
        double endTime = -1.0; // end time is less than start time
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.01);

        // When
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);

        // This test should fail with IllegalArgumentException
    }

    /**
     * A test case for the {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * This test case checks the integration of a simple differential equation
     * with a non-empty list of step handlers.
     */
    @Test
    public void testIntegrationWithMultipleStepHandlers() {
        // Given
        double[] initialCondition = {0.0, 1.0};
        FirstOrderDifferentialEquations equations = new SimpleDifferentialEquationsWithMultipleStepHandlers();
        double startTime = 0.0;
        double endTime = 2.0;
        double[] expectedResult = {0.367879441, 1.0}; // calculated manually
        RungeKuttaIntegrator integrator = new MidpointIntegrator(0.01);
        StepHandler stepHandler1 = new StepHandler() {
            public void handleStep(StepHandler handler, int stepNumber) {
                // do something
            }
        };
        StepHandler stepHandler2 = new StepHandler() {
            public void handleStep(StepHandler handler, int stepNumber) {
                // do something else
            }
        };
        integrator.setStepHandlers(stepHandler1, stepHandler2);

        // When
        double result[] = integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);

        // Then
        assertEquals(endTime, result[0], 0.0001);
        assertArrayEquals(expectedResult, result, 0.0001);
    }

    /**
     * A simple differential equation for testing.
     */
    private static class SimpleDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public double[] evaluateDerivative(double t, double[] y) {
            double[] result = new double[2];
            result[0] = y[1];
            result[1] = -result[0];
            return result;
        }
    }

    /**
     * A simple differential equation for testing with an event handling step handler.
     */
    private static class SimpleDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public double[] evaluateDerivative(double t, double[] y) {
            double[] result = new double[2];
            result[0] = y[1];
            result[1] = -result[0];
            return result;
        }

        @Override
        public double[] computeInitialValueDerivative(double[] initialValue) {
            double[] result = new double[2];
            result[0] = initialValue[1];
            result[1] = -result[0];
            return result;
        }

        @Override
        public Event[] getEvents(double t) {
            if (t > 1.0) {
                return new Event[]{new Event() {
                    @Override
                    public double getAbsoluteTime() {
                        return t;
                    }

                    @Override
                    public int getType() {
                        return 1;
                    }

                    @Override
                    public double[] getValues() {
                        return new double[]{2.0, 2.0};
                    }
                }};
            }
            return new Event[0];
        }
    }

    /**
     * A simple differential equation for testing with multiple step handlers.
     */
    private static class SimpleDifferentialEquationsWithMultipleStepHandlers implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public double[] evaluateDerivative(double t, double[] y) {
            double[] result = new double[2];
            result[0] = y[1];
            result[1] = -result[0];
            return result;
        }

        @Override
        public double[] computeInitialValueDerivative(double[] initialValue) {
            double[] result = new double[2];
            result[0] = initialValue[1];
            result[1] = -result[0];
            return result;
        }

        @Override
        public Event[] getEvents(double t) {
            return new Event[0];
        }
    }
}