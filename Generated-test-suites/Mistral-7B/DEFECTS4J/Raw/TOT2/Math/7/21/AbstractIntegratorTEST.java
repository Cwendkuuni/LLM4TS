package org.apache.commons.math3.ode.test;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.stepper.AbstractStepInterpolator;
import org.apache.commons.math3.ode.stepper.StepHandler;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for AbstractIntegrator class.
 */
public class AbstractIntegratorTest {

    /**
     * Test method for {@link AbstractIntegrator#AbstractIntegrator()}.
     */
    @Test
    public void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNotNull(integrator);
    }

    /**
     * Test method for {@link AbstractIntegrator#addStepHandler(StepHandler)}.
     */
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addStepHandler(new MockStepHandler());
        assertEquals(1, integrator.getStepHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#clearStepHandlers()}.
     */
    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addStepHandler(new MockStepHandler());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#getName()}.
     */
    @Test
    public void testName() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        assertEquals("TestIntegrator", integrator.getName());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0, 2.0 };
        final double t = 10.0;
        final double[] yExpected = new double[] { 3.141592653589793, 2.718281828459045 };

        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op, we will test the integrator via the public interface
            }
        };
        integrator.initIntegration(t0, y0, t);
        integrator.integrate(equations, t);
        assertEquals(yExpected[0], integrator.getState()[0], 1e-5);
        assertEquals(yExpected[1], integrator.getState()[1], 1e-5);
        assertEquals(t, integrator.getCurrentStepStart());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIntegrateUnsupported() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.integrate(new ExpandableStatefulODE(new MockDifferentialEquations()), 1.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#setMaxEvaluations(int)}.
     */
    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertEquals(0, integrator.getEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#addEventHandler(EventHandler, double, double, int, UnivariateSolver)}.
     */
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addEventHandler(new MockEventHandler(), 1.0, 1.0, 1, new MockSolver());
        assertEquals(1, integrator.getEventHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#clearEventHandlers()}.
     */
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addEventHandler(new MockEventHandler(), 1.0, 1.0, 1, new MockSolver());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#getEventHandlers()}.
     */
    @Test
    public void testGetEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addEventHandler(new MockEventHandler(), 1.0, 1.0, 1, new MockSolver());
        assertEquals(1, integrator.getEventHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     */
    @Test
    public void testAcceptStep() throws Exception {
        final double dt = 1.0;
        final double[] y = new double[] { 1.0, 2.0 };
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op, we will test the integrator via the public interface
            }

            @Override
            void acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd)
                    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
                assertEquals(y[0], yDot[0], 1e-15);
                assertEquals(y[1], yDot[1], 1e-15);
            }
        };
        integrator.initIntegration(0.0, y, 1.0);
        integrator.acceptStep(new MockStepInterpolator(), y, new double[y.length], dt);
    }

    /**
     * Test method for {@link AbstractIntegrator#setStateInitialized()}.
     */
    @Test
    public void testStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertFalse(integrator.isStateInitialized());
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
    }

    /**
     * Test method for {@link AbstractIntegrator#setStateInitialized(boolean)}.
     */
    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
    }

    /**
     * Test method for {@link AbstractIntegrator#isLastStep()}.
     */
    @Test
    public void testIsLastStep() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertFalse(integrator.isLastStep());
    }

    /**
     * Test method for {@link AbstractIntegrator#resetOccurred()}.
     */
    @Test
    public void testResetOccurred() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertFalse(integrator.resetOccurred());
    }

}

class MockDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[0] * 0.1;
        yDot[1] = y[1] * 0.2;
    }

}

class MockEventHandler implements EventHandler {

    private double eventTime;
    private double[] eventY;

    @Override
    public void init(double t0, double[] y0, double t) {
        eventTime = t;
        eventY = y0;
    }

    @Override
    public boolean evaluateStep(AbstractStepInterpolator interpolator) {
        return eventTime == interpolator.getGlobalCurrentTime();
    }

    @Override
    public void stepAccepted(double t, double[] y) {
        assertArrayEquals(eventY, y);
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    public void reset(double t, double[] y) {
        // no-op
    }

    public double getEventTime() {
        return eventTime;
    }

    public double[] getEventHandler() {
        return this;
    }

}

class MockSolver implements UnivariateSolver {

    @Override
    public double solve(double x, double fx) throws NoBracketingException {
        return x + 1.0;
    }

}

class MockStepHandler implements StepHandler {

    @Override
    public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
        // no-op
    }

}

class MockStepInterpolator extends AbstractStepInterpolator {

    @Override
    public double getGlobalPreviousTime() {
        return 0.0;
    }

    @Override
    public double getGlobalCurrentTime() {
        return 1.0;
    }

    @Override
    public double getInterpolatedTime() {
        return getGlobalCurrentTime();
    }

    @Override
    public double[] getInterpolatedState() {
        return new double[0];
    }

}