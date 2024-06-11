package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for AbstractIntegrator.
 */
public class AbstractIntegratorTest {

    /**
     * Test method for {@link AbstractIntegrator#AbstractIntegrator(String)}.
     */
    @Test
    public void testAbstractIntegrator() {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        assertNull(integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
        assertEquals(0, integrator.getEvaluations());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.getResetOccurred());
        assertNull(integrator.getExpandable());
    }

    /**
     * Test method for {@link AbstractIntegrator#AbstractIntegrator()}.
     */
    @Test
    public void testAbstractIntegratorEmpty() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNull(integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
        assertEquals(0, integrator.getEvaluations());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.getResetOccurred());
        assertNull(integrator.getExpandable());
    }

    /**
     * Test method for {@link AbstractIntegrator#addEventHandler(EventHandler, double, double, int, UnivariateSolver)}.
     */
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.addEventHandler(new TestEventHandler(), 1.0, 1e-6, 3, new BracketingNthOrderBrentSolver(1e-6, 5));
        assertEquals(1, integrator.getEventHandlers().size());
        assertEquals(0, integrator.getEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#clearEventHandler()}.
     */
    @Test
    public void testClearEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.addEventHandler(new TestEventHandler(), 1.0, 1e-6, 3, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.clearEventHandler();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#clearEventHandlers()}.
     */
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.addEventHandler(new TestEventHandler(), 1.0, 1e-6, 3, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.addEventHandler(new TestEventHandler(), 2.0, 1e-6, 3, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#getEventHandlers()}.
     */
    @Test
    public void testGetEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.addEventHandler(new TestEventHandler(), 1.0, 1e-6, 3, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.addEventHandler(new TestEventHandler(), 2.0, 1e-6, 3, new BracketingNthOrderBrentSolver(1e-6, 5));
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(2, eventHandlers.size());
    }

    /**
     * Test method for {@link AbstractIntegrator#initIntegration(double, double[], double)}.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInitIntegrationDimensionMismatch() {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.setMaxEvaluations(1);
        integrator.initIntegration(1.0, new double[] {1.0}, 2.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegrate() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.addEventHandler(new TestEventHandler(), 1.0, 1e-6, 3, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.setMaxEvaluations(1);
        integrator.setEquations(new TestODE());
        integrator.initIntegration(0.0, new double[] {1.0}, 2.0);
        double result = integrator.integrate(integrator.getExpandable(), 2.0);
        assertEquals(2.0, result, 1e-6);
        assertEquals(1, integrator.getEvaluations());
        assertTrue(integrator.isLastStep());
        assertFalse(integrator.getResetOccurred());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxEvaluations() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.addEventHandler(new TestEventHandler(), 1.0, 1e-6, 3, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.setMaxEvaluations(0);
        integrator.setEquations(new TestODE());
        integrator.initIntegration(0.0, new double[] {1.0}, 2.0);
        integrator.integrate(integrator.getExpandable(), 2.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = NoBracketingException.class)
    public void testIntegrateNoBracketing() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.addEventHandler(new TestEventHandlerWithNoBracketing(), 1.0, 1e-6, 3, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.setMaxEvaluations(1);
        integrator.setEquations(new TestODE());
        integrator.initIntegration(0.0, new double[] {1.0}, 2.0);
        integrator.integrate(integrator.getExpandable(), 2.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#setMaxEvaluations(int)}.
     */
    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.setMaxEvaluations(1);
        assertEquals(1, integrator.getMaxEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     */
    @Test
    public void testAcceptStep() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.addEventHandler(new TestEventHandler(), 1.0, 1e-6, 3, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.setMaxEvaluations(1);
        integrator.setEquations(new TestODE());
        integrator.initIntegration(0.0, new double[] {1.0}, 2.0);
        StepHandler stepHandler = new TestStepHandler();
        integrator.addStepHandler(stepHandler);
        AbstractStepInterpolator interpolator = new TestStepInterpolator();
        double[] y = new double[] {1.0, 2.0};
        double[] yDot = new double[2];
        double tEnd = 3.0;
        double t = integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(2.0, t, 1e-6);
        assertEquals(1, integrator.getEvaluations());
        assertEquals(true, integrator.isLastStep());
        assertEquals(false, integrator.getResetOccurred());
        assertTrue(stepHandler.isCalled());
    }

    /**
     * Test method for {@link AbstractIntegrator#setEquations(ExpandableStatefulODE)}.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testSetEquationsDimensionMismatch() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.setEquations(new TestODEWithDimensionMismatch());
    }

    /**
     * Test method for {@link AbstractIntegrator#setStateInitialized()}.
     */
    @Test
    public void testGetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        assertFalse(integrator.isStateInitialized());
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
    }

}

class TestEventHandler implements EventHandler {

    /** {@inheritDoc} */
    public boolean evaluateStep(AbstractStepInterpolator interpolator) {
        return false;
    }

    /** {@inheritDoc} */
    public void stepAccepted(double t, double[] y) {
    }

    /** {@inheritDoc} */
    public boolean stop() {
        return false;
    }

    /** {@inheritDoc} */
    public void reinitializeBegin(AbstractStepInterpolator interpolator) {
    }

}

class TestEventHandlerWithNoBracketing implements EventHandler {

    /** {@inheritDoc} */
    public boolean evaluateStep(AbstractStepInterpolator interpolator) {
        return true;
    }

    /** {@inheritDoc} */
    public void stepAccepted(double t, double[] y) {
    }

    /** {@inheritDoc} */
    public boolean stop() {
        return false;
    }

    /** {@inheritDoc} */
    public void reinitializeBegin(AbstractStepInterpolator interpolator) {
    }

}

class TestStepHandler implements StepHandler {

    /** The flag indicating if the handler has been called. */
    private boolean called;

    /**
     * {@inheritDoc}
     */
    public void handleStep(AbstractIntegrator integrator, boolean isLastStep) {
        this.called = true;
    }

    /**
     * Check if the handler has been called.
     */
    public boolean isCalled() {
        return called;
    }

}

class TestODE implements FirstOrderDifferentialEquations {

    /** {@inheritDoc} */
    public int getDimension() {
        return 2;
    }

    /** {@inheritDoc} */
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -y[0];
    }

    /** {@inheritDoc} */
    public double getTime() {
        return 0.0;
    }

    /** {@inheritDoc} */
    public void setTime(double time) {
    }

    /** {@inheritDoc} */
    public double[] getPrimaryState() {
        return new double[] {1.0, 0.0};
    }

    /** {@inheritDoc} */
    public void setPrimaryState(double[] state) {
    }

}

class TestODEWithDimensionMismatch implements FirstOrderDifferentialEquations {

    /** {@inheritDoc} */
    public int getDimension() {
        return 3;
    }

    /** {@inheritDoc} */
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -y[0];
        yDot[2] = 0.0;
    }

    /** {@inheritDoc} */
    public double getTime() {
        return 0.0;
    }

    /** {@inheritDoc} */
    public void setTime(double time) {
    }

    /** {@inheritDoc} */
    public double[] getPrimaryState() {
        return new double[] {1.0, 0.0, 0.0};
    }

    /** {@inheritDoc} */
    public void setPrimaryState(double[] state) {
    }

}

class TestStepInterpolator implements AbstractStepInterpolator {

    /** The soft current time. */
    private double softCurrentTime;

    /** The soft previous time. */
    private double softPreviousTime;

    /** The interpolated time. */
    private double interpolatedTime;

    /** The interpolated state. */
    private ExpandableStatefulODE interpolatedState;

    /**
     * {@inheritDoc}
     */
    public double getGlobalPreviousTime() {
        return softPreviousTime;
    }

    /**
     * {@inheritDoc}
     */
    public double getGlobalCurrentTime() {
        return interpolatedTime;
    }

    /**
     * {@inheritDoc}
     */
    public void setInterpolatedTime(double time) {
        this.interpolatedTime = time;
    }

    /**
     * {@inheritDoc}
     */
    public void setInterpolatedState(ExpandableStatefulODE state) {
        this.interpolatedState = state;
    }

    /**
     * {@inheritDoc}
     */
    public double getLocalPreviousTime() {
        return softPreviousTime;
    }

    /**
     * {@inheritDoc}
     */
    public double getLocalCurrentTime() {
        return interpolatedTime;
    }

    /**
     * {@inheritDoc}
     */
    public void setSoftPreviousTime(double time) {
        this.softPreviousTime = time;
    }

    /**
     * {@inheritDoc}
     */
    public void setSoftCurrentTime(double time) {
        this.softCurrentTime = time;
    }

    /**
     * {@inheritDoc}
     */
    public ExpandableStatefulODE getInterpolatedState() {
        return interpolatedState;
    }

}