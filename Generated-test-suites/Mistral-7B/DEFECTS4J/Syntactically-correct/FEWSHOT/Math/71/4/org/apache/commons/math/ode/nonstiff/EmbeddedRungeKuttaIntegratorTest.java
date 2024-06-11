package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.IgnoreEventsManager;
import org.apache.commons.math3.ode.events.StopEvent;
import org.apache.commons.math3.ode.events.TimeValue;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double DELTA = 1e-10;
    private static final double TOLERANCE = 1e-12;
    private static final double MAX_STEP = 10.0;
    private static final double MIN_STEP = 0.1;
    private static final double EPSILON = 1e-13;

    private FirstOrderDifferentialEquations equations;
    private EmbeddedRungeKuttaIntegrator integrator;
    private IgnoreEventsManager eventsManager;

    @Before
    public void setUp() {
        equations = new DummyDifferentialEquations();
        integrator = new RungeKuttaFehlberg78Method();
        integrator.setMinStep(MIN_STEP);
        integrator.setMaxStep(MAX_STEP);
        eventsManager = new IgnoreEventsManager();
    }

    @Test
    public void testIntegrate_singleStep_forward() throws Exception {
        double initialTime = 0.0;
        double[] initialConditions = {0.0};
        double finalTime = 0.5;
        double[] expectedFinalConditions = {1.0};

        double result = integrator.integrate(equations, initialTime, initialConditions, finalTime, new double[initialConditions.length]);
        assertSame(expectedFinalConditions, initialConditions);
        assertEquals(finalTime, result);
    }

    @Test
    public void testIntegrate_singleStep_backward() throws Exception {
        double initialTime = 1.0;
        double[] initialConditions = {1.0};
        double finalTime = 0.5;
        double[] expectedFinalConditions = {0.5 * Math.exp(1.0)};

        double result = integrator.integrate(equations, initialTime, initialConditions, finalTime, new double[initialConditions.length]);
        assertArrayEquals(expectedFinalConditions, initialConditions);
        assertEquals(finalTime, result);
    }

    @Test
    public void testIntegrate_multipleSteps_forward() throws Exception {
        double initialTime = 0.0;
        double[] initialConditions = {0.0};
        double[] expectedFinalConditions = {0.1, 0.2, 0.3, 0.4, 0.5};

        double[] intermediateResults = new double[expectedFinalConditions.length];

        double stepSize = MIN_STEP;
        for (int i = 0; i < expectedFinalConditions.length; ++i) {
            double finalTime = initialTime + stepSize;
            double result = integrator.integrate(equations, initialTime, initialConditions, finalTime, intermediateResults);
            initialTime = finalTime;
            initialConditions = intermediateResults;
        }

        assertArrayEquals(expectedFinalConditions, initialConditions);
        assertEquals(initialTime, result);
    }

    @Test
    public void testIntegrate_multipleSteps_backward() throws Exception {
        double initialTime = 1.0;
        double[] initialConditions = {1.0};
        double[] expectedFinalConditions = {0.6, 0.4, 0.2, 0.1, 0.05};

        double[] intermediateResults = new double[expectedFinalConditions.length];

        double stepSize = MIN_STEP;
        for (int i = expectedFinalConditions.length - 1; i >= 0; --i) {
            double finalTime = initialTime - stepSize;
            double result = integrator.integrate(equations, initialTime, initialConditions, finalTime, intermediateResults);
            initialTime = finalTime;
            initialConditions = intermediateResults;
        }

        assertArrayEquals(expectedFinalConditions, initialConditions);
        assertEquals(initialTime, result);
    }

    @Test
    public void testIntegrate_eventHandler() throws Exception {
        double initialTime = 0.0;
        double[] initialConditions = {0.0};
        double finalTime = 1.0;

        StopEvent stopEvent = new StopEvent(new TimeValue(0.5), null);
        eventsManager.clear();
        eventsManager.addEvent(stopEvent);

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                if (stepNumber == 1) {
                    eventsManager.handleEvent(interpolator);
                }
            }
        });

        integrator.integrate(equations, initialTime, initialConditions, finalTime, new double[initialConditions.length]);

        assertTrue(stopEvent.isHandled());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrate_invalidArguments() throws Exception {
        double[] initialConditions = {1.0};
        integrator.integrate(null, 0.0, initialConditions, 1.0, new double[initialConditions.length]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrate_invalidArguments2() throws Exception {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                throw new UnsupportedOperationException();
            }
        };
        integrator.integrate(equations, 0.0, null, 1.0, new double[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrate_invalidState() throws Exception {
        double[] initialConditions = {1.0, 2.0};
        integrator.integrate(equations, 0.0, initialConditions, 1.0, new double[1]);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrate_derivativeException() throws Exception {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                throw new DerivativeException("Test");
            }
        };
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrate_integratorException() throws Exception {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                throw new NumberIsTooSmallException("Test");
            }
        };
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrate_maxCountExceededException() throws Exception {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                // noop
            }
        };
        integrator.setMaxIterations(0);
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test
    public void testGetName() {
        assertEquals("RungeKuttaFehlberg78Method", integrator.getName());
    }

    @Test
    public void testGetSafety() {
        assertEquals(0.9, integrator.getSafety(), TOLERANCE);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.5);
        assertEquals(0.5, integrator.getSafety(), TOLERANCE);
    }

    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, integrator.getMinReduction(), TOLERANCE);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), TOLERANCE);
    }

    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, integrator.getMaxGrowth(), TOLERANCE);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), TOLERANCE);
    }

    @Test
    public void testGetStepSize() {
        integrator.setStepSize(2.0);
        assertEquals(2.0, integrator.getStepSize(), TOLERANCE);
    }

    @Test
    public void testSetStepSize() {
        integrator.setStepSize(1.5);
        assertEquals(1.5, integrator.getStepSize(), TOLERANCE);
    }

    @Test
    public void testGetMinStep() {
        assertEquals(0.1, integrator.getMinStep(), TOLERANCE);
    }

    @Test
    public void testSetMinStep() {
        integrator.setMinStep(0.05);
        assertEquals(0.05, integrator.getMinStep(), TOLERANCE);
    }

    @Test
    public void testGetMaxStep() {
        assertEquals(10.0, integrator.getMaxStep(), TOLERANCE);
    }

    @Test
    public void testSetMaxStep() {
        integrator.setMaxStep(5.0);
        assertEquals(5.0, integrator.getMaxStep(), TOLERANCE);
    }

    @Test
    public void testGetScaledAbsoluteTolerance() {
        double[] scalRelativeTolerance = {1e-3, 1e-4};
        integrator.setScaledAbsoluteTolerance(1e-5);
        assertEquals(1e-5, integrator.getScaledAbsoluteTolerance()[0], TOLERANCE);
        assertEquals(1e-5, integrator.getScaledAbsoluteTolerance()[1], TOLERANCE);
    }

    @Test
    public void testSetScaledAbsoluteTolerance() {
        double[] scalRelativeTolerance = {1e-3, 1e-4};
        integrator.setScaledAbsoluteTolerance(new double[] {1e-4, 1e-5});
        assertEquals(1e-4, integrator.getScaledAbsoluteTolerance()[0], TOLERANCE);
        assertEquals(1e-5, integrator.getScaledAbsoluteTolerance()[1], TOLERANCE);
    }

    @Test
    public void testGetScaledRelativeTolerance() {
        integrator.setScaledRelativeTolerance(new double[] {1e-3, 1e-4});
        assertEquals(1e-3, integrator.getScaledRelativeTolerance()[0], TOLERANCE);
        assertEquals(1e-4, integrator.getScaledRelativeTolerance()[1], TOLERANCE);
    }

    @Test
    public void testSetScaledRelativeTolerance() {
        integrator.setScaledRelativeTolerance(new double[] {1e-2, 1e-3});
        assertEquals(1e-2, integrator.getScaledRelativeTolerance()[0], TOLERANCE);
        assertEquals(1e-3, integrator.getScaledRelativeTolerance()[1], TOLERANCE);
    }

    @Test
    public void testGetOrder() {
        assertEquals(7, integrator.getOrder());
    }

    private static class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getNumberOfEquations() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
            yDot[0] = Math.exp(t);
        }
    }

}