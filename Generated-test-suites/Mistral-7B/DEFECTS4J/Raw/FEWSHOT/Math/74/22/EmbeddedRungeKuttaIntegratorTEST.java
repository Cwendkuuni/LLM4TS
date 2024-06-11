package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.InitialValueProblem;
import org.apache.commons.math3.ode.continuous.AbstractODEFunction;
import org.apache.commons.math3.ode.events.RelativeEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.step.RungeKuttaFehlberg78StepHandler;
import org.apache.commons.math3.ode.step.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for EmbeddedRungeKuttaIntegrator abstract class.
 */
public class EmbeddedRungeKuttaIntegratorTest extends AbstractODETest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#estimateError(double[][], double[], double[], double)}.
     */
    @Test
    public void testEstimateError() {
        // Test a simple embedded Runge-Kutta method
        final double[] c = { 1.0 / 3.0, 1.0 / 4.0, 1.0 / 2.0 };
        final double[][] a = {
            { 1.0 / 3.0, 1.0 / 4.0 },
            { 1.0 / 4.0, 1.0 / 3.0 },
            { 1.0, 0.0 }
        };
        final double[] b = { 1.0 / 3.0, 1.0 / 4.0, 1.0 };

        // Create a mock differential equation
        final AbstractODEFunction func = new AbstractODEFunction() {
            @Override
            public double value(double t, double[] y) {
                return y[0];
            }

            @Override
            public double[] value(double t, double[] y, double[] derivatives) {
                derivatives[0] = 1.0;
                return derivatives;
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Integrator(c, a, b, new RungeKuttaStepInterpolator());

        final double[] initialCondition = { 1.0 };
        final InitialValueProblem ivp = new InitialValueProblem(func, initialCondition, 0.0, 1.0);

        // Test error estimation with a small step size
        final double[] y0 = new double[initialCondition.length];
        System.arraycopy(initialCondition, 0, y0, 0, initialCondition.length);
        double stepSize = 0.01;
        double error = integrator.estimateError(new double[][] { y0 }, initialCondition, y0, stepSize);
        assertTrue(Precision.isGreaterOrEqual(error, 0.0));
        assertTrue(Precision.isLessOrEqual(error, 1.0));

        // Test error estimation with a larger step size
        stepSize = 0.1;
        error = integrator.estimateError(new double[][] { y0 }, initialCondition, y0, stepSize);
        assertTrue(Precision.isGreaterThan(error, 1.0));

        // Test error estimation with a large step size
        stepSize = 1.0;
        error = integrator.estimateError(new double[][] { y0 }, initialCondition, y0, stepSize);
        assertTrue(Precision.isGreaterThan(error, 1.0));
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#initializeStep(FirstOrderDifferentialEquations, boolean, int, double[], double, double[], double[], double[], double[])}.
     */
    @Test
    public void testInitializeStep() throws NumberIsTooSmallException, MaxCountExceededException {
        // Test a simple embedded Runge-Kutta method
        final double[] c = { 1.0 / 3.0, 1.0 / 4.0, 1.0 / 2.0 };
        final double[][] a = {
            { 1.0 / 3.0, 1.0 / 4.0 },
            { 1.0 / 4.0, 1.0 / 3.0 },
            { 1.0, 0.0 }
        };
        final double[] b = { 1.0 / 3.0, 1.0 / 4.0, 1.0 };

        // Create a mock differential equation
        final AbstractODEFunction func = new AbstractODEFunction() {
            @Override
            public double value(double t, double[] y) {
                return y[0];
            }

            @Override
            public double[] value(double t, double[] y, double[] derivatives) {
                derivatives[0] = 1.0;
                return derivatives;
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Integrator(c, a, b, new RungeKuttaStepInterpolator());

        final double[] initialCondition = { 1.0 };
        final InitialValueProblem ivp = new InitialValueProblem(func, initialCondition, 0.0, 1.0);

        // Test step initialization with a small step size
        double stepSize = 0.01;
        double[] yDotK = new double[c.length + 1][initialCondition.length];
        double[] yTmp = new double[initialCondition.length];
        double[] y = new double[initialCondition.length];
        System.arraycopy(initialCondition, 0, y, 0, initialCondition.length);

        integrator.initializeStep(ivp.getDifferentialEquations(), true, integrator.getOrder(), null, 0.0, y, yDotK[0], yTmp, yDotK[1]);

        // Check that the derivatives have been computed correctly
        assertArrayEquals(new double[] { 1.0 }, yDotK[0], 0.0);

        // Test step initialization with a larger step size
        stepSize = 0.1;
        integrator.initializeStep(ivp.getDifferentialEquations(), true, integrator.getOrder(), null, 0.0, y, yDotK[0], yTmp, yDotK[1]);

        // Check that the derivatives have been computed correctly
        assertArrayEquals(new double[] { 1.0 }, yDotK[0], 0.0);

        // Test step initialization with a larger step size that triggers an event
        ivp.addEvent(new RelativeEvent(0.5, 1.2));

        stepSize = 0.5;
        integrator.initializeStep(ivp.getDifferentialEquations(), true, integrator.getOrder(), null, 0.0, y, yDotK[0], yTmp, yDotK[1]);

        // Check that the derivatives have been recomputed due to the event
        assertArrayEquals(new double[] { 1.2 }, yDotK[0], 0.0);

        // Test step initialization with a negative step size
        stepSize = -0.1;
        integrator.initializeStep(ivp.getDifferentialEquations(), true, integrator.getOrder(), null, 0.0, y, yDotK[0], yTmp, yDotK[1]);

        // Check that an exception is thrown
        assertFalse(ivp.isIntegratorStateValid(y, yDotK[0]));
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[], double, double[], double[], double[])}.
     */
    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        // Test a simple embedded Runge-Kutta method
        final double[] c = { 1.0 / 3.0, 1.0 / 4.0, 1.0 / 2.0 };
        final double[][] a = {
            { 1.0 / 3.0, 1.0 / 4.0 },
            { 1.0 / 4.0, 1.0 / 3.0 },
            { 1.0, 0.0 }
        };
        final double[] b = { 1.0 / 3.0, 1.0 / 4.0, 1.0 };

        // Create a mock differential equation
        final AbstractODEFunction func = new AbstractODEFunction() {
            @Override
            public double value(double t, double[] y) {
                return y[0];
            }

            @Override
            public double[] value(double t, double[] y, double[] derivatives) {
                derivatives[0] = 1.0;
                return derivatives;
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Integrator(c, a, b, new RungeKuttaStepInterpolator());

        final double[] initialCondition = { 1.0 };
        final InitialValueProblem ivp = new InitialValueProblem(func, initialCondition, 0.0, 1.0);

        // Test integration with a small step size
        double[] y = new double[initialCondition.length];
        System.arraycopy(initialCondition, 0, y, 0, initialCondition.length);
        double stepSize = 0.01;
        double[] yDotK = new double[c.length + 1][initialCondition.length];
        double[] yTmp = new double[initialCondition.length];

        RungeKuttaStepInterpolator rki = new RungeKuttaStepInterpolator();
        StepHandler stepHandler = new RungeKuttaFehlberg78StepHandler(rki);
        integrator.addStepHandler(stepHandler);

        double t = 0.0;
        do {
            integrator.integrate(ivp.getDifferentialEquations(), t, y, t + stepSize, yDotK[0]);

            // Check that the step has been accepted
            assertEquals(stepSize, integrator.getStepSize(), 0.0);

            // Check that the derivatives have been computed correctly
            assertArrayEquals(new double[] { 1.0 }, yDotK[0], 0.0);

            // Check that the step size has been updated correctly
            stepSize *= 1.1;

            // Check that the event handler has been called
            assertEquals(1, stepHandler.getNumberOfStepsTaken());

            // Check that the step handler has been notified of the new state
            RungeKuttaFehlberg78StepHandler stepHandlerInstance = (RungeKuttaFehlberg78StepHandler) stepHandler;
            assertEquals(t, stepHandlerInstance.getPreviousStepTime(), 0.0);
            assertArrayEquals(y, stepHandlerInstance.getPreviousStepState(), 0.0);
            assertArrayEquals(yDotK[0], stepHandlerInstance.getPreviousStepDerivatives(), 0.0);

            // Check that the event manager has not thrown an exception
            ivp.assertIsIntegratorStateValid(y, yDotK[0]);

            t += stepSize;
        } while (t < 1.0);

        // Test integration with a larger step size that triggers an event
        ivp.addEvent(new RelativeEvent(0.5, 1.2));

        stepSize = 0.5;
        do {
            integrator.integrate(ivp.getDifferentialEquations(), t, y, t + stepSize, yDotK[0]);

            // Check that the step has been rejected
            assertFalse(integrator.isLastStepAccepted());

            // Check that the step size has been reduced
            assertEquals(stepSize / 2.0, integrator.getStepSize(), 0.0);

            // Check that the event handler has been called
            assertEquals(1, stepHandler.getNumberOfStepsTaken());

            // Check that the event manager has thrown an exception
            ivp.assertIsIntegratorStateInvalid(y, yDotK[0]);

            t += stepSize;
        } while (! integrator.isLastStepAccepted());

        assertEquals(1.0, integrator.getState()[0], 0.0);
    }
}