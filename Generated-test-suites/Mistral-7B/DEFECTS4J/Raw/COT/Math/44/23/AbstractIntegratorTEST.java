package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.intervals.Interval;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.stephandler.StepHandler;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest extends AbstractODETest {

    /** The test integrator. */
    private AbstractIntegrator integrator;

    /** The test equations. */
    private FirstOrderDifferentialEquations equations;

    /** The test step interpolator. */
    private AbstractStepInterpolator interpolator;

    /** The test events. */
    private List<Pair<EventHandler, Double>> events;

    /** The test step handlers. */
    private List<StepHandler> stepHandlers;

    /**
     * Initialize test objects.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no integration in unit tests
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot)
                    throws MaxCountExceededException {
                // no derivative computation in unit tests
            }
        };
        equations = createTestEquations();
        interpolator = createTestInterpolator();
        events = new ArrayList<>();
        stepHandlers = new ArrayList<>();
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        assertNotNull(integrator.getName());
        assertTrue(integrator.getStepHandlers().isEmpty());
        assertNull(integrator.getCurrentStepStart());
        assertNull(integrator.getCurrentSignedStepsize());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertNotNull(integrator.eventsStates);
        assertFalse(integrator.statesInitialized);
        assertEquals(0, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
    }

    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        assertEquals("testIntegrator", integrator.getName());
    }

    /**
     * Test addStepHandler method.
     */
    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(createTestStepHandler());
        assertEquals(1, integrator.getStepHandlers().size());
    }

    /**
     * Test getStepHandlers method.
     */
    @Test
    public void testGetStepHandlers() {
        integrator.addStepHandler(createTestStepHandler());
        assertEquals(1, integrator.getStepHandlers().size());
    }

    /**
     * Test clearStepHandlers method.
     */
    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(createTestStepHandler());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    /**
     * Test addEventHandler method.
     */
    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(createTestEventHandler(), 0.0, 1.0e-6, 10);
        assertEquals(1, integrator.getEventHandlers().size());
    }

    /**
     * Test getEventHandlers method.
     */
    @Test
    public void testGetEventHandlers() {
        integrator.addEventHandler(createTestEventHandler(), 0.0, 1.0e-6, 10);
        assertEquals(1, integrator.getEventHandlers().size());
    }

    /**
     * Test clearEventHandlers method.
     */
    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(createTestEventHandler(), 0.0, 1.0e-6, 10);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    /**
     * Test getCurrentStepStart method.
     */
    @Test
    public void testGetCurrentStepStart() {
        assertNull(integrator.getCurrentStepStart());
    }

    /**
     * Test getCurrentSignedStepsize method.
     */
    @Test
    public void testGetCurrentSignedStepsize() {
        assertNull(integrator.getCurrentSignedStepsize());
    }

    /**
     * Test isLastStep method.
     */
    @Test
    public void testIsLastStep() {
        assertFalse(integrator.isLastStep());
    }

    /**
     * Test resetOccurred method.
     */
    @Test
    public void testResetOccurred() {
        assertFalse(integrator.resetOccurred);
    }

    /**
     * Test setMaxEvaluations method.
     */
    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    /**
     * Test getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    /**
     * Test getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        assertEquals(0, integrator.getEvaluations());
    }

    /**
     * Test integrate method.
     * @throws Exception for any error
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIntegrate() throws Exception {
        integrator.integrate(equations, 0.0);
    }

    /**
     * Test computeDerivatives method.
     * @throws Exception for any error
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testComputeDerivatives() throws Exception {
        integrator.computeDerivatives(0.0, new double[3], new double[3]);
    }

    /**
     * Test acceptStep method.
     * @throws Exception for any error
     */
    @Test
    public void testAcceptStep() throws Exception {
        integrator.addEventHandler(createTestEventHandler(), 1.0, 1.0e-6, 10);
        integrator.addStepHandler(createTestStepHandler());
        double t = 0.0;
        integrator.setEquations(equations);

        // test acceptStep with no events
        integrator.acceptStep(interpolator, new double[3], new double[3], t);
        assertTrue(integrator.isLastStep);
        assertArrayEquals(new double[] { 0.0, 0.0, 0.0 }, new double[3]);
        assertEquals(t, integrator.getCurrentStepStart());
        assertEquals(-1.0, integrator.getCurrentSignedStepsize());
        assertFalse(integrator.resetOccurred);

        // test acceptStep with one event
        integrator.eventsStates.add(new EventState(createTestEventHandler(), 0.5, 1.0e-6, 10,
                new BracketingNthOrderBrentSolver(1.0e-6, 5)));
        integrator.eventsStates.get(0).setState(new double[] { 0.0, 0.0, 0.0 }, new double[] { 1.0, 1.0, 1.0 });
        integrator.eventsStates.get(0).setEventTime(0.5);
        integrator.acceptStep(interpolator, new double[3], new double[3], t);
        assertFalse(integrator.isLastStep);
        assertArrayEquals(new double[] { 1.0, 1.0, 1.0 }, new double[3]);
        assertEquals(0.5, integrator.getCurrentStepStart());
        assertEquals(0.0, integrator.getCurrentSignedStepsize());
        assertTrue(integrator.resetOccurred);

        // test acceptStep with one event and one step handler
        integrator.stepHandlers.add(createTestStepHandler());
        integrator.acceptStep(interpolator, new double[3], new double[3], t);
        assertTrue(integrator.isLastStep);
        assertArrayEquals(new double[] { 1.0, 1.0, 1.0 }, new double[3]);
        assertEquals(0.5, integrator.getCurrentStepStart());
        assertEquals(0.0, integrator.getCurrentSignedStepsize());
        assertFalse(integrator.resetOccurred);

        // test acceptStep with one event, one step handler, and reset event
        integrator.eventsStates.get(0).setState(new double[] { 0.0, 0.0, 0.0 }, new double[] { 2.0, 2.0, 2.0 });
        integrator.eventsStates.get(0).setEventTime(0.5);
        integrator.acceptStep(interpolator, new double[3], new double[3], t);
        assertFalse(integrator.isLastStep);
        assertArrayEquals(new double[] { 2.0, 2.0, 2.0 }, new double[3]);
        assertEquals(0.5, integrator.getCurrentStepStart());
        assertEquals(0.0, integrator.getCurrentSignedStepsize());
        assertFalse(integrator.resetOccurred);

        // test acceptStep with one event, one step handler, and reset event at the end of the step
        integrator.eventsStates.get(0).setState(new double[] { 1.0, 1.0, 1.0 }, new double[] { 3.0, 3.0, 3.0 });
        integrator.acceptStep(interpolator, new double[3], new double[3], t);
        assertTrue(integrator.isLastStep);
        assertArrayEquals(new double[] { 3.0, 3.0, 3.0 }, new double[3]);
        assertEquals(0.5, integrator.getCurrentStepStart());
        assertEquals(0.0, integrator.getCurrentSignedStepsize());
        assertFalse(integrator.resetOccurred);

        // test acceptStep with two events, one step handler, and one event at the end of the step
        integrator.eventsStates.add(new EventState(createTestEventHandler(), 1.0, 1.0e-6, 10,
                new BracketingNthOrderBrentSolver(1.0e-6, 5)));
        integrator.eventsStates.get(1).setState(new double[] { 2.0, 2.0, 2.0 }, new double[] { 4.0, 4.0, 4.0 });
        integrator.eventsStates.get(1).setEventTime(1.0);
        integrator.acceptStep(interpolator, new double[3], new double[3], t);
        assertFalse(integrator.isLastStep);
        assertArrayEquals(new double[] { 4.0, 4.0, 4.0 }, new double[3]);
        assertEquals(1.0, integrator.getCurrentStepStart());
        assertEquals(0.0, integrator.getCurrentSignedStepsize());
        assertFalse(integrator.resetOccurred);

        // test acceptStep with two events, one step handler, and both events at the end of the step
        integrator.eventsStates.get(0).setState(new double[] { 3.0, 3.0, 3.0 }, new double[] { 5.0, 5.0, 5.0 });
        integrator.eventsStates.get(1).setState(new double[] { 4.0, 4.0, 4.0 }, new double[] { 6.0, 6.0, 6.0 });
        integrator.acceptStep(interpolator, new double[3], new double[3], t);
        assertTrue(integrator.isLastStep);
        assertArrayEquals(new double[] { 6.0, 6.0, 6.0 }, new double[3]);
        assertEquals(1.0, integrator.getCurrentStepStart());
        assertEquals(0.0, integrator.getCurrentSignedStepsize());
        assertFalse(integrator.resetOccurred);

    }

    /**
     * Test sanityChecks method.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecks() throws NumberIsTooSmallException {
        integrator.setEquations(equations);
        integrator.sanityChecks(equations, 0.0);
    }

}