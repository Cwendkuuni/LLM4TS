package org.apache.commons.math3.ode;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.stephandler.MockStepHandler;
import org.apache.commons.math3.ode.util.UnivariateSolver;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator_constructor() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNull(integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
        assertTrue(Double.isNaN(integrator.getCurrentStepStart()));
        assertTrue(Double.isNaN(integrator.getCurrentSignedStepsize()));
        assertEquals(0, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testAbstractIntegrator_constructorWithName() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        assertEquals("TestIntegrator", integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
        assertTrue(Double.isNaN(integrator.getCurrentStepStart()));
        assertTrue(Double.isNaN(integrator.getCurrentSignedStepsize()));
        assertEquals(0, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractIntegrator_negativeMaxEvaluations() {
        new AbstractIntegrator(-1);
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        EventHandler eventHandler = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler, 1.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        assertEquals(1, integrator.getEventHandlers().size());
        assertEquals(eventHandler, integrator.getEventHandlers().iterator().next());
    }

    @Test
    public void testGetEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        EventHandler eventHandler1 = mock(EventHandler.class);
        EventHandler eventHandler2 = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler1, 1.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        integrator.addEventHandler(eventHandler2, 1.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        assertEquals(2, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(eventHandler1));
        assertTrue(integrator.getEventHandlers().contains(eventHandler2));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        EventHandler eventHandler1 = mock(EventHandler.class);
        EventHandler eventHandler2 = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler1, 1.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        integrator.addEventHandler(eventHandler2, 1.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler stepHandler1 = mock(StepHandler.class);
        StepHandler stepHandler2 = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler1);
        integrator.addStepHandler(stepHandler2);
        assertEquals(2, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(stepHandler1));
        assertTrue(integrator.getStepHandlers().contains(stepHandler2));
    }

    @Test
    public void testGetStepHandlers_unmodifiableCollection() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler stepHandler1 = mock(StepHandler.class);
        StepHandler stepHandler2 = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler1);
        integrator.addStepHandler(stepHandler2);
        assertEquals(2, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(stepHandler1));
        assertTrue(integrator.getStepHandlers().contains(stepHandler2));
        assertFalse(integrator.getStepHandlers().isEmpty());
        assertFalse(integrator.getStepHandlers().contains(null));
        assertFalse(integrator.getStepHandlers().add(mock(StepHandler.class)));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler stepHandler1 = mock(StepHandler.class);
        StepHandler stepHandler2 = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler1);
        integrator.addStepHandler(stepHandler2);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testIntegrate() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        integrator.setEquations(expandable);
        double t0 = 0.0;
        double[] y0 = new double[]{0.0};
        double t = 1.0;
        integrator.initIntegration(t0, y0, t);
        integrator.integrate(expandable, t);
        double y = integrateMock(expandable, t);
        assertEquals(y, integrator.getState()[0], 1e-6);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_dimensionMismatch() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        integrator.setEquations(expandable);
        double t0 = 0.0;
        double[] y0 = new double[]{0.0, 0.0};
        double t = 1.0;
        integrator.initIntegration(t0, y0, t);
        integrator.integrate(expandable, t);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrate_numberTooSmall() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        integrator.setEquations(expandable);
        double t0 = 0.0;
        double[] y0 = new double[]{0.0};
        double t = 1.0e-1000;
        integrator.initIntegration(t0, y0, t);
        integrator.integrate(expandable, t);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrate_maxCountExceeded() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        integrator.setEquations(expandable);
        double t0 = 0.0;
        double[] y0 = new double[]{0.0};
        double t = 1.0;
        integrator.initIntegration(t0, y0, t);
        integrator.setMaxEvaluations(1);
        integrator.integrate(expandable, t);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrate_noBracketing() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        integrator.setEquations(expandable);
        double t0 = 0.0;
        double[] y0 = new double[]{0.0};
        double t = 1.0;
        integrator.initIntegration(t0, y0, t);
        when(expandable.getTime()).thenReturn(t0);
        when(expandable.getPrimaryState()).thenReturn(y0);
        doThrow(NoBracketingException.class).when(expandable).computeDerivatives(anyDouble(), anyArray(), anyArray());
        integrator.integrate(expandable, t);
    }

    @Test
    public void testAcceptStep() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler stepHandler = new MockStepHandler();
        integrator.addStepHandler(stepHandler);
        AbstractStepInterpolator interpolator = mock(AbstractStepInterpolator.class);
        double[] y = new double[]{1.0};
        double yDot = 1.5;
        double tEnd = 2.0;
        when(interpolator.getGlobalPreviousTime()).thenReturn(1.0);
        when(interpolator.getGlobalCurrentTime()).thenReturn(1.5);
        when(interpolator.getSoftCurrentTime()).thenReturn(1.5);
        when(interpolator.getSoftPreviousTime()).thenReturn(1.5);
        when(interpolator.getInterpolatedState()).thenReturn(y);
        when(interpolator.getInterpolatedTime()).thenReturn(1.5);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertTrue(stepHandler.isLastStep());
        stepHandler.reset();
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertFalse(stepHandler.isLastStep());
    }

    @Test
    public void testAcceptStep_resetOccurred() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler stepHandler = new MockStepHandler();
        integrator.addStepHandler(stepHandler);
        AbstractStepInterpolator interpolator = mock(AbstractStepInterpolator.class);
        double[] y = new double[]{1.0};
        double yDot = 1.5;
        double tEnd = 2.0;
        when(interpolator.getGlobalPreviousTime()).thenReturn(1.0);
        when(interpolator.getGlobalCurrentTime()).thenReturn(1.5);
        when(interpolator.getSoftCurrentTime()).thenReturn(1.5);
        when(interpolator.getSoftPreviousTime()).thenReturn(1.5);
        when(interpolator.getInterpolatedState()).thenReturn(y);
        when(interpolator.getInterpolatedTime()).thenReturn(1.5);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertTrue(stepHandler.isLastStep());
        stepHandler.reset();
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertFalse(stepHandler.isLastStep());

        EventHandler eventHandler = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler, 1.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        when(eventHandler.reset(anyDouble(), anyArray())).thenReturn(true);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertTrue(integrator.resetOccurred);
        integrator.resetOccurred = false;
    }

    @Test
    public void testComputeDerivatives() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        integrator.setEquations(expandable);
        double t = 1.0;
        double[] y = new double[]{1.0};
        double[] yDot = new double[]{1.5};
        integrator.computeDerivatives(t, y, yDot);
        verify(expandable).computeDerivatives(t, y, yDot);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testComputeDerivatives_maxCountExceeded() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        integrator.setEquations(expandable);
        double t = 1.0;
        double[] y = new double[]{1.0};
        double[] yDot = new double[]{1.5};
        integrator.setMaxEvaluations(0);
        integrator.computeDerivatives(t, y, yDot);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testComputeDerivatives_dimensionMismatch() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(2);
        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        integrator.setEquations(expandable);
        double t = 1.0;
        double[] y = new double[]{1.0, 2.0};
        double[] yDot = new double[]{1.5, 3.0};
        integrator.computeDerivatives(t, y, yDot);
    }

    private double integrateMock(ExpandableStatefulODE expandable, double t) {
        double y = 0.0;
        expandable.setTime(t);
        expandable.setPrimaryState(new double[]{y});
        expandable.computeDerivatives(t, new double[]{y}, new double[]{y + 0.1});
        y += expandable.getPrimaryState()[0];
        return t;
    }

}