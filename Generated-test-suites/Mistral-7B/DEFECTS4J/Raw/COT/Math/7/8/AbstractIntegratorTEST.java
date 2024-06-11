package org.apache.commons.math3.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.mock.MockEventHandler;
import org.apache.commons.math3.ode.test.mock.MockStepHandler;
import org.apache.commons.math3.ode.util.DoubleField;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest {

    /** Test integrator instance. */
    private AbstractIntegrator integrator;

    /** Test equations instance. */
    private FirstOrderDifferentialEquations equations;

    /** Test step handler instance. */
    private StepHandler stepHandler;

    /** Test event handler instance. */
    private MockEventHandler eventHandler;

    /** Test integration time. */
    private double t;

    /** Test initial state. */
    private double[] y0;

    /** Test final state. */
    private double[] y;

    /** Test step interpolator. */
    private AbstractStepInterpolator interpolator;

    /** Test event states. */
    private List<EventState> eventStates;

    /** Test events initialized flag. */
    private boolean statesInitialized;

    /** Test step counter. */
    private int evaluations;

    /** Test name. */
    private String name;

    /** Test step handlers. */
    private List<StepHandler> stepHandlers;

    /** Test event handlers. */
    private List<MockEventHandler> eventHandlers;

    /** Test integration result. */
    private double result;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };
        equations = new ExpandableStatefulODE(new MockDifferentialEquations(1)) {
            @Override
            public double[] getPrimaryState() {
                return getState();
            }
        };
        y0 = new double[] {0.1};
        t = 1.0;
        y = new double[] {1.0};
        interpolator = new AbstractStepInterpolator() {
            @Override
            public double interpolate(double t, double[] y, double[] yDot) {
                return t;
            }
        };
        eventHandler = new MockEventHandler();
        stepHandler = new MockStepHandler();
        stepHandlers = Collections.singletonList(stepHandler);
        eventHandlers = Collections.emptyList();
        integrator.setEquations(equations);
        integrator.addEventHandler(eventHandler, 0.0, 1.0E-10, 10, null);
        integrator.addStepHandler(stepHandler);
        integrator.initIntegration(0.0, y0, t);
        eventStates = integrator.getEventHandlers();
        statesInitialized = integrator.isStateInitialized();
        evaluations = integrator.getEvaluations();
        name = integrator.getName();
    }

    @Test
    public void testGetName() {
        assertEquals("TestIntegrator", name);
    }

    @Test
    public void testGetStepHandlers() {
        assertEquals(1, stepHandlers.size());
        assertNotNull(stepHandlers.get(0));
    }

    @Test
    public void testGetEventHandlers() {
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        assertFalse(Double.isNaN(integrator.getCurrentSignedStepsize()));
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() throws Exception {
        integrator.integrate(equations, t);
        assertEquals(t, result);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithWrongDimension() throws Exception {
        equations = new ExpandableStatefulODE(new MockDifferentialEquations(2)) {
            @Override
            public double[] getPrimaryState() {
                return getState();
            }
        };
        integrator.setEquations(equations);
        integrator.integrate(equations, t);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketingException() throws Exception {
        MockEventHandler eventHandler = new MockEventHandler();
        integrator.addEventHandler(eventHandler, 0.5, 1.0E-10, 10, null);
        integrator.integrate(equations, t);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxCountExceededException() throws Exception {
        MockEventHandler eventHandler = new MockEventHandler();
        MockStepHandler stepHandler = new MockStepHandler();
        integrator.addEventHandler(eventHandler, 0.0, 1.0E-10, 1, null);
        integrator.addStepHandler(stepHandler);
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, t);
    }

    @Test
    public void testAcceptStep() throws Exception {
        result = integrator.acceptStep(interpolator, y, y);
        assertEquals(t, result);
    }

    @Test
    public void testAcceptStepWithEvent() throws Exception {
        eventHandler.setEventTime(0.5);
        result = integrator.acceptStep(interpolator, y, y);
        assertEquals(0.5, result);
    }

    @Test
    public void testAcceptStepWithEventAndStepHandler() throws Exception {
        eventHandler.setEventTime(0.5);
        stepHandler.setAccept(true);
        result = integrator.acceptStep(interpolator, y, y);
        assertEquals(0.5, result);
        assertTrue(stepHandler.isAccepted());
    }

    @Test
    public void testAcceptStepWithEventAndStepHandlerAndReset() throws Exception {
        eventHandler.setEventTime(0.5);
        stepHandler.setAccept(true);
        stepHandler.setReset(true);
        result = integrator.acceptStep(interpolator, y, y);
        assertEquals(0.5, result);
        assertTrue(stepHandler.isAccepted());
        assertTrue(stepHandler.isReset());
    }

    @Test
    public void testAcceptStepWithEventAndStepHandlerAndNoReset() throws Exception {
        eventHandler.setEventTime(0.5);
        stepHandler.setAccept(true);
        result = integrator.acceptStep(interpolator, y, y);
        assertEquals(0.5, result);
        assertTrue(stepHandler.isAccepted());
        assertFalse(stepHandler.isReset());
    }

    @Test
    public void testAcceptStepWithEventAndStepHandlerAndNoEvent() throws Exception {
        stepHandler.setAccept(true);
        result = integrator.acceptStep(interpolator, y, y);
        assertEquals(t, result);
        assertTrue(stepHandler.isAccepted());
    }

    @Test
    public void testAcceptStepWithEventAndStepHandlerAndMultipleEvents() throws Exception {
        eventHandlers = Arrays.asList(new MockEventHandler(), new MockEventHandler());
        integrator.addEventHandler(eventHandlers.get(0), 0.1, 1.0E-10, 1, null);
        integrator.addEventHandler(eventHandlers.get(1), 0.5, 1.0E-10, 1, null);
        integrator.integrate(equations, t);
        assertEquals(0.1, eventHandlers.get(0).getEventTime());
        assertEquals(0.5, eventHandlers.get(1).getEventTime());
    }

    @Test
    public void testAcceptStepWithEventAndStepHandlerAndEventReset() throws Exception {
        eventHandlers = Arrays.asList(new MockEventHandler(), new MockEventHandler());
        integrator.addEventHandler(eventHandlers.get(0), 0.1, 1.0E-10, 1, null);
        integrator.addEventHandler(eventHandlers.get(1), 0.5, 1.0E-10, 1, null);
        integrator.integrate(equations, t);
        eventHandlers.get(1).setReset(true);
        result = integrator.acceptStep(interpolator, y, y);
        assertEquals(0.5, result);
        assertTrue(eventHandlers.get(1).isReset());
    }

    @Test
    public void testAcceptStepWithEventAndStepHandlerAndNoEventReset() throws Exception {
        eventHandlers = Arrays.asList(new MockEventHandler(), new MockEventHandler());
        integrator.addEventHandler(eventHandlers.get(0), 0.1, 1.0E-10, 1, null);
        integrator.addEventHandler(eventHandlers.get(1), 0.5, 1.0E-10, 1, null);
        integrator.integrate(equations, t);
        result = integrator.acceptStep(interpolator, y, y);
        assertEquals(t, result);
        assertFalse(eventHandlers.get(1).isReset());
    }

    @Test
    public void testAcceptStepWithNoEvent() throws Exception {
        result = integrator.acceptStep(interpolator, y, y);
        assertEquals(t, result);
    }

    @Test
    public void testAcceptStepWithNoEventAndStepHandler() throws Exception {
        stepHandler.setAccept(true);
        result = integrator.acceptStep(interpolator, y, y);
        assertEquals(t, result);
        assertTrue(stepHandler.isAccepted());
    }

    @Test
    public void testAcceptStepWithNoEventAndStepHandlerAndReset() throws Exception {
        stepHandler.setAccept(true);
        stepHandler.setReset(true);
        result = integrator.acceptStep(interpolator, y, y);
        assertEquals(t, result);
        assertTrue(stepHandler.isReset());
    }

    @Test
    public void testAcceptStepWithNoEventAndStepHandlerAndNoReset() throws Exception {
        stepHandler.setAccept(true);
        result = integrator.acceptStep(interpolator, y, y);
        assertEquals(t, result);
        assertFalse(stepHandler.isReset());
    }

    @Test
    public void testSanityChecks() throws Exception {
        integrator.sanityChecks(equations, t);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecksWithTooSmallIntegrationInterval() throws Exception {
        DoubleField df = new DoubleField(0.001);
        integrator.sanityChecks(equations, df.getDoubleValue() + t * 0.001);
    }

    @Test
    public void testIsStateInitialized() {
        assertFalse(statesInitialized);
        integrator.acceptStep(interpolator, y, y);
        assertTrue(statesInitialized);
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, integrator.getEvaluations());
        integrator.computeDerivatives(t, y, new double[y.length]);
        assertEquals(1, integrator.getEvaluations());
    }

}

class MockEventHandler implements EventHandler<FirstOrderDifferentialEquations> {

    /** Event time. */
    private double eventTime;

    /** Reset flag. */
    private boolean reset;

    /** Accept flag. */
    private boolean accept;

    /** {@inheritDoc} */
    public void init(double t, double[] y, double tEnd) {
        // Empty implementation
    }

    /** {@inheritDoc} */
    public double eventTime() {
        return eventTime;
    }

    /** {@inheritDoc} */
    public void reset(double t, double[] y) {
        reset = true;
    }

    /** {@inheritDoc} */
    public boolean stop() {
        return accept;
    }

    /** {@inheritDoc} */
    public void evaluateStep(AbstractStepInterpolator interpolator) {
        accept = true;
    }

    /** {@inheritDoc} */
    public void stepAccepted(double t, double[] y) {
        eventTime = t;
    }

    /** {@inheritDoc} */
    public void setEventTime(double eventTime) {
        this.eventTime = eventTime;
    }

    /** {@inheritDoc} */
    public void setReset(boolean reset) {
        this.reset = reset;
    }

}

class MockStepHandler implements StepHandler {

    /** Accept flag. */
    private boolean accept;

    /** Reset flag. */
    private boolean reset;

    /** {@inheritDoc} */
    public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
        accept = isLastStep;
    }

    /** {@inheritDoc} */
    public void reset() {
        reset = true;
    }

    /** {@inheritDoc} */
    public boolean isAccepted() {
        return accept;
    }

    /** {@inheritDoc} */
    public boolean isReset() {
        return reset;
    }

}

class MockDifferentialEquations implements FirstOrderDifferentialEquations {

    /** Dimension. */
    private int dimension;

    /** {@inheritDoc} */
    public int getDimension() {
        return dimension;
    }

    /** {@inheritDoc} */
    public void setTime(double time) {
        // Empty implementation
    }

    /** {@inheritDoc} */
    public double[] getState() {
        return new double[] {0.1};
    }

    /** {@inheritDoc} */
    public double[] computePrimaryDerivative(double time, double[] state) {
        return new double[] {state[0]};
    }

}