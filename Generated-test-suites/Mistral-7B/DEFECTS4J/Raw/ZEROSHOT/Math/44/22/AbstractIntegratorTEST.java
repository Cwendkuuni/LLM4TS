package org.apache.commons.math.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.EventState;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    /**
     * Test method for {@link AbstractIntegrator#AbstractIntegrator(String)}.
     */
    @Test
    public void testAbstractIntegrator() {
        // empty constructor
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNull(integrator.getName());
        assertNull(integrator.getStepHandlers());
        assertNull(integrator.getCurrentStepStart());
        assertNull(integrator.getCurrentSignedStepsize());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertNull(integrator.getEquations());
        assertFalse(integrator.isStateInitialized());
        assertEquals(0, integrator.getEvaluations());

        // constructor with name
        AbstractIntegrator integratorNamed = new AbstractIntegrator("TestIntegrator");
        assertEquals("TestIntegrator", integratorNamed.getName());
        assertNull(integratorNamed.getStepHandlers());
        assertNull(integratorNamed.getCurrentStepStart());
        assertNull(integratorNamed.getCurrentSignedStepsize());
        assertFalse(integratorNamed.isLastStep());
        assertFalse(integratorNamed.resetOccurred);
        assertNull(integratorNamed.getEquations());
        assertFalse(integratorNamed.isStateInitialized());
        assertEquals(0, integratorNamed.getEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#addStepHandler(StepHandler)}.
     */
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler stepHandler = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(stepHandler));
    }

    /**
     * Test method for {@link AbstractIntegrator#clearStepHandlers()}.
     */
    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addStepHandler(mock(StepHandler.class));
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#getCurrentStepStart()}.
     */
    @Test
    public void testGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNull(integrator.getCurrentStepStart());
    }

    /**
     * Test method for {@link AbstractIntegrator#getCurrentSignedStepsize()}.
     */
    @Test
    public void testGetCurrentSignedStepsize() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNull(integrator.getCurrentSignedStepsize());
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
        assertFalse(integrator.resetOccurred);
    }

    /**
     * Test method for {@link AbstractIntegrator#addEventHandler(EventHandler, double, double, int, UnivariateRealSolver)}.
     */
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        EventHandler eventHandler = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler, 1.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(eventHandler));
    }

    /**
     * Test method for {@link AbstractIntegrator#clearEventHandlers()}.
     */
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addEventHandler(mock(EventHandler.class), 1.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#getEventHandlers()}.
     */
    @Test
    public void testGetEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addEventHandler(mock(EventHandler.class), 1.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler));
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithNullEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.integrate(null, 1.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithNegativeStep() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        integrator.setEquations(equations);
        integrator.integrate(equations, -1.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithTooSmallStep() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        integrator.setEquations(equations);
        integrator.sanityChecks(equations, 1.0);
        integrator.integrate(equations, 1.0e-10);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        ExpandableStatefulODE equations = new MockODESystem(1);
        integrator.setEquations(equations);
        integrator.integrate(equations, 1.0, 2.0);
        // no verification needed for this test, since AbstractIntegrator is abstract
    }

    /**
     * Test method for {@link AbstractIntegrator#computeDerivatives(double, double[], double[])}.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testComputeDerivativesWithMaxEvaluationsReached() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setMaxEvaluations(1);
        ExpandableStatefulODE equations = new MockODESystem(1);
        integrator.setEquations(equations);
        double[] y = new double[1];
        double[] yDot = new double[1];
        integrator.computeDerivatives(1.0, y, yDot);
        integrator.computeDerivatives(1.0, y, yDot);
    }

    /**
     * Test method for {@link AbstractIntegrator#setStateInitialized(boolean)}.
     */
    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setStateInitialized(false);
        assertFalse(integrator.isStateInitialized());
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
    }

}

class MockODESystem implements ExpandableStatefulODE {

    /** The number of dimensions. */
    private final int dimension;

    /**
     * Constructor for MockODESystem.
     * @param dimension the number of dimensions
     */
    public MockODESystem(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public void setTime(double time) {
        // no-op
    }

    @Override
    public void setPrimaryState(double[] primaryState) {
        // no-op
    }

    @Override
    public void computeDerivatives(double time, double[] primaryState, double[] derivedState) {
        // no-op
    }

}