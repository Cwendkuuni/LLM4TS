package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.MockEvent;
import org.apache.commons.math3.ode.integration.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.MockStepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test constructor with given parameters.
     */
    @Test
    public void testConstructor() {
        EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegrator("test", true, new double[] { 0.3, 0.6, 0.8 },
                                             new double[][] { { 0.2, 0.1 }, { 0.1, 0.1, 0.1 } },
                                             new double[] { 0.5, 0.2, 0.3 },
                                             new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);
        assertNotNull(integrator);
    }

    /**
     * Test constructor with given parameters and vectors.
     */
    @Test
    public void testConstructorWithVectors() {
        double[] scalAbsoluteTolerance = new double[] { 1e-6, 1e-5 };
        double[] scalRelativeTolerance = new double[] { 1e-7, 1e-6 };
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[] { 0.3, 0.6, 0.8 },
                                                                                    new double[][] { { 0.2, 0.1 }, { 0.1, 0.1, 0.1 } },
                                                                                    new double[] { 0.5, 0.2, 0.3 },
                                                                                    new RungeKuttaStepInterpolator(), 0.001, 0.01, scalAbsoluteTolerance, scalRelativeTolerance);
        assertNotNull(integrator);
        assertEquals(scalAbsoluteTolerance, integrator.getAbsoluteTolerances());
        assertEquals(scalRelativeTolerance, integrator.getRelativeTolerances());
    }

    /**
     * Test the integrate method with a simple differential equation.
     */
    @Test
    public void testIntegrateSimpleEquation() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[] { 0.3, 0.6, 0.8 },
                                                                                    new double[][] { { 0.2, 0.1 }, { 0.1, 0.1, 0.1 } },
                                                                                    new double[] { 0.5, 0.2, 0.3 },
                                                                                    new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        double[] y0 = new double[] { 1.0, 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;

        StepHandler stepHandler = new MockStepHandler();
        integrator.addStepHandler(stepHandler);

        integrator.integrate(new MockDifferentialEquations(), t0, y0, t1, null);

        double[] expectedY1 = new double[] { 2.718281828, 2.718281828 };
        assertEquals(expectedY1, y0);

        // Test that the step handler was called
        assertEquals(1, stepHandler.getCallCount());
    }

    /**
     * Test the integrate method with a differential equation that has an event.
     */
    @Test(expected = IntegratorException.class)
    public void testIntegrateWithEvent() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[] { 0.3, 0.6, 0.8 },
                                                                                    new double[][] { { 0.2, 0.1 }, { 0.1, 0.1, 0.1 } },
                                                                                    new double[] { 0.5, 0.2, 0.3 },
                                                                                    new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        double[] y0 = new double[] { 0.0, 0.1 };
        double t0 = 0.0;
        double t1 = 1.0;

        StepHandler stepHandler = new MockStepHandler();
        integrator.addStepHandler(stepHandler);

        integrator.addEvent(new MockEvent(0.5, "TestEvent"));

        integrator.integrate(new MockDifferentialEquations(), t0, y0, t1, null);

        fail("An IntegratorException should have been thrown.");
    }

    /**
     * Test the integrate method with a differential equation that has a max count limit.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxCountLimit() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[] { 0.3, 0.6, 0.8 },
                                                                                    new double[][] { { 0.2, 0.1 }, { 0.1, 0.1, 0.1 } },
                                                                                    new double[] { 0.5, 0.2, 0.3 },
                                                                                    new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        double[] y0 = new double[] { 0.0, 0.1 };
        double t0 = 0.0;
        double t1 = 1.0;

        StepHandler stepHandler = new MockStepHandler();
        integrator.addStepHandler(stepHandler);

        integrator.setMaxStepCount(10);

        integrator.integrate(new MockDifferentialEquations(), t0, y0, t1, null);

        fail("A MaxCountExceededException should have been thrown.");
    }

    /**
     * Test the integrate method with a null differential equation.
     */
    @Test(expected = NullArgumentException.class)
    public void testIntegrateWithNullDifferentialEquations() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[] { 0.3, 0.6, 0.8 },
                                                                                    new double[][] { { 0.2, 0.1 }, { 0.1, 0.1, 0.1 } },
                                                                                    new double[] { 0.5, 0.2, 0.3 },
                                                                                    new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        double[] y0 = new double[] { 0.0, 0.1 };
        double t0 = 0.0;
        double t1 = 1.0;

        integrator.integrate(null, t0, y0, t1, null);

        fail("A NullArgumentException should have been thrown.");
    }

    /**
     * Test the integrate method with a null y array.
     */
    @Test(expected = NullArgumentException.class)
    public void testIntegrateWithNullY() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[] { 0.3, 0.6, 0.8 },
                                                                                    new double[][] { { 0.2, 0.1 }, { 0.1, 0.1, 0.1 } },
                                                                                    new double[] { 0.5, 0.2, 0.3 },
                                                                                    new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        double[] y0 = null;
        double t0 = 0.0;
        double t1 = 1.0;

        integrator.integrate(new MockDifferentialEquations(), t0, y0, t1, null);

        fail("A NullArgumentException should have been thrown.");
    }

    /**
     * Test the integrate method with a null y array at the start.
     */
    @Test(expected = NullArgumentException.class)
    public void testIntegrateWithNullYAtStart() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[] { 0.3, 0.6, 0.8 },
                                                                                    new double[][] { { 0.2, 0.1 }, { 0.1, 0.1, 0.1 } },
                                                                                    new double[] { 0.5, 0.2, 0.3 },
                                                                                    new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        double[] y0 = null;
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y = new double[] { 1.0, 1.0 };

        integrator.integrate(new MockDifferentialEquations(), t0, y0, t1, y);

        fail("A NullArgumentException should have been thrown.");
    }

    /**
     * Test the integrate method with a null y array at the end.
     */
    @Test(expected = NullArgumentException.class)
    public void testIntegrateWithNullYAtEnd() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[] { 0.3, 0.6, 0.8 },
                                                                                    new double[][] { { 0.2, 0.1 }, { 0.1, 0.1, 0.1 } },
                                                                                    new double[] { 0.5, 0.2, 0.3 },
                                                                                    new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        double[] y0 = new double[] { 1.0, 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = null;

        integrator.integrate(new MockDifferentialEquations(), t0, y0, t1, y1);

        fail("A NullArgumentException should have been thrown.");
    }

    /**
     * Test the integrate method with a null y array at both start and end.
     */
    @Test(expected = NullArgumentException.class)
    public void testIntegrateWithNullYAtBothEnds() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[] { 0.3, 0.6, 0.8 },
                                                                                    new double[][] { { 0.2, 0.1 }, { 0.1, 0.1, 0.1 } },
                                                                                    new double[] { 0.5, 0.2, 0.3 },
                                                                                    new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        double[] y0 = null;
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = null;

        integrator.integrate(new MockDifferentialEquations(), t0, y0, t1, y1);

        fail("A NullArgumentException should have been thrown.");
    }

    /**
     * Test the integrate method with a negative minimal step.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNegativeMinimalStep() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[] { 0.3, 0.6, 0.8 },
                                                                                    new double[][] { { 0.2, 0.1 }, { 0.1, 0.1, 0.1 } },
                                                                                    new double[] { 0.5, 0.2, 0.3 },
                                                                                    new RungeKuttaStepInterpolator(), -0.001, 0.01, null, null);

        double[] y0 = new double[] { 1.0, 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;

        integrator.integrate(new MockDifferentialEquations(), t0, y0, t1, null);

        fail("An IllegalArgumentException should have been thrown.");
    }

    /**
     * Test the integrate method with a negative maximal step.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNegativeMaximalStep() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[] { 0.3, 0.6, 0.8 },
                                                                                    new double[][] { { 0.2, 0.1 }, { 0.1, 0.1, 0.1 } },
                                                                                    new double[] { 0.5, 0.2, 0.3 },
                                                                                    new RungeKuttaStepInterpolator(), 0.001, -0.01, null, null);

        double[] y0 = new double[] { 1.0, 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;

        integrator.integrate(new MockDifferentialEquations(), t0, y0, t1, null);

        fail("An IllegalArgumentException should have been thrown.");
    }

    /**
     * Mock differential equation class.
     */
    public static class MockDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getNumberOfEquations() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = Math.exp(y[0]);
            yPrime[1] = Math.sin(y[1]);
        }
    }

    /**
     * Mock step handler class.
     */
    public static class MockStepHandler implements StepHandler {
        private int callCount = 0;

        @Override
        public void handleStep(StepInterpolator interpolator, int type) {
            callCount++;
        }

        public int getCallCount() {
            return callCount;
        }
    }

}