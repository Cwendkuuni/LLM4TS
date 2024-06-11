package org.apache.commons.math3.ode;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.events.AbstractEventHandler;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Test cases for {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest {

    /**
     * Test case for {@link AbstractIntegrator#AbstractIntegrator(String)} constructor.
     */
    @Test
    public void testAbstractIntegratorConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        assertEquals("integrator", integrator.getName());
        assertNull(integrator.expandable);
        assertTrue(integrator.stepHandlers.isEmpty());
        assertTrue(integrator.eventsStates.isEmpty());
        assertEquals(0, integrator.evaluations.getCount());
        assertNull(integrator.getEquations());
    }

    /**
     * Test case for {@link AbstractIntegrator#AbstractIntegrator()} constructor.
     */
    @Test
    public void testAbstractIntegratorNoArgConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNull(integrator.getName());
        assertNull(integrator.expandable);
        assertTrue(integrator.stepHandlers.isEmpty());
        assertTrue(integrator.eventsStates.isEmpty());
        assertEquals(0, integrator.evaluations.getCount());
    }

    /**
     * Test case for {@link AbstractIntegrator#getName()} method.
     */
    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        assertEquals("integrator", integrator.getName());
        AbstractIntegrator noNameIntegrator = new AbstractIntegrator();
        assertNull(noNameIntegrator.getName());
    }

    /**
     * Test case for {@link AbstractIntegrator#addStepHandler(StepHandler)} method.
     */
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        StepHandler handler = ODETestUtils.createMockStepHandler();
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.stepHandlers.size());
        assertTrue(integrator.stepHandlers.contains(handler));
    }

    /**
     * Test case for {@link AbstractIntegrator#getStepHandlers()} method.
     */
    @Test
    public void testGetStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        StepHandler handler1 = ODETestUtils.createMockStepHandler();
        integrator.addStepHandler(handler1);
        StepHandler handler2 = ODETestUtils.createMockStepHandler();
        integrator.addStepHandler(handler2);
        Collection<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(2, handlers.size());
        assertTrue(handlers.containsAll(List.of(handler1, handler2)));
    }

    /**
     * Test case for {@link AbstractIntegrator#clearStepHandlers()} method.
     */
    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        StepHandler handler1 = ODETestUtils.createMockStepHandler();
        integrator.addStepHandler(handler1);
        StepHandler handler2 = ODETestUtils.createMockStepHandler();
        integrator.addStepHandler(handler2);
        integrator.clearStepHandlers();
        assertTrue(integrator.stepHandlers.isEmpty());
    }

    /**
     * Test case for {@link AbstractIntegrator#addEventHandler(EventHandler, double, double, int, UnivariateSolver)} method.
     */
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        EventHandler handler = ODETestUtils.createMockEventHandler();
        integrator.addEventHandler(handler, 0.1, 1e-10, 10, null);
        assertEquals(1, integrator.eventsStates.size());
        assertTrue(integrator.eventsStates.contains(handler.getEventState()));
    }

    /**
     * Test case for {@link AbstractIntegrator#getEventHandlers()} method.
     */
    @Test
    public void testGetEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        EventHandler handler1 = ODETestUtils.createMockEventHandler();
        integrator.addEventHandler(handler1, 0.1, 1e-10, 10, null);
        EventHandler handler2 = ODETestUtils.createMockEventHandler();
        integrator.addEventHandler(handler2, 0.1, 1e-10, 10, null);
        Collection<EventHandler> handlers = integrator.getEventHandlers();
        assertEquals(2, handlers.size());
        assertTrue(handlers.containsAll(List.of(handler1, handler2)));
    }

    /**
     * Test case for {@link AbstractIntegrator#clearEventHandlers()} method.
     */
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        EventHandler handler1 = ODETestUtils.createMockEventHandler();
        integrator.addEventHandler(handler1, 0.1, 1e-10, 10, null);
        EventHandler handler2 = ODETestUtils.createMockEventHandler();
        integrator.addEventHandler(handler2, 0.1, 1e-10, 10, null);
        integrator.clearEventHandlers();
        assertTrue(integrator.eventsStates.isEmpty());
    }

    /**
     * Test case for {@link AbstractIntegrator#getCurrentStepStart()} method.
     */
    @Test
    public void testGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        double currentStepStart = integrator.getCurrentStepStart();
        assertTrue(Double.isNaN(currentStepStart));
    }

    /**
     * Test case for {@link AbstractIntegrator#getCurrentSignedStepsize()} method.
     */
    @Test
    public void testGetCurrentSignedStepsize() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        double currentSignedStepsize = integrator.getCurrentSignedStepsize();
        assertTrue(Double.isNaN(currentSignedStepsize));
    }

    /**
     * Test case for {@link AbstractIntegrator#setMaxEvaluations(int)} method.
     */
    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }

    /**
     * Test case for {@link AbstractIntegrator#getMaxEvaluations()} method.
     */
    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
        AbstractIntegrator noMaxEvaluationsIntegrator = new AbstractIntegrator();
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    /**
     * Test case for {@link AbstractIntegrator#getEvaluations()} method.
     */
    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        integrator.setMaxEvaluations(100);
        assertEquals(0, integrator.getEvaluations());
        for (int i = 0; i < 50; i++) {
            integrator.computeDerivatives(0.0, new double[0], new double[0]);
        }
        assertEquals(50, integrator.getEvaluations());
    }

    /**
     * Test case for {@link AbstractIntegrator#initIntegration(double, double[], double)} method.
     */
    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        integrator.initIntegration(t0, y0, t);
        assertEquals(t0, integrator.getCurrentStepStart());
        assertEquals(y0, integrator.expandable.getPrimaryState().clone());
    }

    /**
     * Test case for {@link AbstractIntegrator#setEquations(ExpandableStatefulODE)} method.
     */
    @Test
    public void testSetEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        ExpandableStatefulODE equations = ODETestUtils.createMockEquations();
        integrator.setEquations(equations);
        assertEquals(equations, integrator.expandable);
    }

    /**
     * Test case for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)} method.
     */
    @Test
    public void testIntegrate() {
        // This test case does not make any sense as AbstractIntegrator is an abstract class and does not provide an implementation for the integrate method.
    }

    /**
     * Test case for {@link AbstractIntegrator#computeDerivatives(double, double[], double[])} method.
     */
    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        double t = 0.0;
        double[] y = {1.0, 2.0};
        double[] yDot = new double[2];

        integrator.computeDerivatives(t, y, yDot);

        for (int i = 0; i < yDot.length; i++) {
            assertFalse(Precision.isClose(yDot[i], 0.0, 1e-9));
        }
    }

    /**
     * Test case for {@link AbstractIntegrator#setStateInitialized(boolean)} method.
     */
    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        assertFalse(integrator.isStateInitialized());
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
    }

    /**
     * Test case for {@link AbstractIntegrator#isStateInitialized()} method.
     */
    @Test
    public void testIsStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        assertFalse(integrator.isStateInitialized());
    }

    /**
     * Test case for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)} method.
     */
    @Test
    public void testAcceptStep() throws NumberIsTooSmallException, DimensionMismatchException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        AbstractStepInterpolator interpolator = ODETestUtils.createMockStepInterpolator();
        double[] y = new double[2];
        double[] yDot = new double[2];
        double tEnd = 1.0;

        // Test case for empty event handlers list
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(integrator.getCurrentStepStart(), interpolator.getGlobalPreviousTime());
        assertEquals(integrator.getCurrentStepStart(), interpolator.getGlobalCurrentTime());
        assertFalse(integrator.isLastStep());
        assertEquals(0, integrator.getEvaluations());

        // Test case for non-empty event handlers list
        EventHandler handler = ODETestUtils.createMockEventHandler();
        integrator.addEventHandler(handler, 0.1, 1e-10, 10, null);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(integrator.getCurrentStepStart(), interpolator.getGlobalPreviousTime());
        assertEquals(tEnd, integrator.getCurrentStepStart());
        assertTrue(integrator.isLastStep());
        assertEquals(1, integrator.getEvaluations());
    }

    /**
     * Test case for {@link AbstractIntegrator#sanityChecks(ExpandableStatefulODE, double)} method.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecks() throws NumberIsTooSmallException, DimensionMismatchException {
        AbstractIntegrator integrator = new AbstractIntegrator("integrator");
        ExpandableStatefulODE equations = ODETestUtils.createMockEquations();
        double t = 1e-10;
        integrator.sanityChecks(equations, t);
    }

}