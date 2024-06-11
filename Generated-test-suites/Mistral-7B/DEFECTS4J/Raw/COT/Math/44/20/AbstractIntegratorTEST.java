import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class AbstractIntegratorTest {

    private static final double EPSILON = 1e-12;
    private static final double TOLERANCE = 1e-12;

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;

    @Before
    public void setUp() {
        equations = mock(ExpandableStatefulODE.class);
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Do nothing
            }
        };
        integrator.setEquations(equations);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("TestIntegrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(mock(StepHandler.class));
        Assert.assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddStepHandlerNull() {
        integrator.addStepHandler(null);
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(mock(StepHandler.class));
        integrator.clearStepHandlers();
        Assert.assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(mock(EventHandler.class), 1.0, 1.0, 10);
        Assert.assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerNullEventHandler() {
        integrator.addEventHandler(null, 1.0, 1.0, 10);
    }

    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(mock(EventHandler.class), 1.0, 1.0, 10);
        integrator.clearEventHandlers();
        Assert.assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetCurrentStepStart() {
        integrator.setCurrentStepStart(1.0);
        Assert.assertEquals(1.0, integrator.getCurrentStepStart(), EPSILON);
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        integrator.setCurrentSignedStepsize(1.0);
        Assert.assertEquals(1.0, integrator.getCurrentSignedStepsize(), EPSILON);
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(-1);
        Assert.assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        Assert.assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        integrator.setMaxEvaluations(10);
        integrator.resetEvaluations();
        integrator.evaluate(1.0, new double[1], new double[1]);
        integrator.evaluate(2.0, new double[1], new double[1]);
        integrator.evaluate(3.0, new double[1], new double[1]);
        Assert.assertEquals(3, integrator.getEvaluations());
    }

    @Test
    public void testResetEvaluations() {
        integrator.setMaxEvaluations(10);
        integrator.resetEvaluations();
        integrator.evaluate(1.0, new double[1], new double[1]);
        integrator.evaluate(2.0, new double[1], new double[1]);
        integrator.evaluate(3.0, new double[1], new double[1]);
        integrator.resetEvaluations();
        Assert.assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() throws Exception {
        final double[] y0 = new double[]{0.0};
        final double t0 = 0.0;
        final double t = 1.0;
        final double[] y = new double[]{1.0};
        integrator.integrate(equations, t);
        integrator.integrate(equations, t, y0, y);
        Assert.assertEquals(1.0, y[0], EPSILON);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateNoEquations() throws Exception {
        integrator.integrate(null, 1.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateNullInitialState() throws Exception {
        integrator.integrate(equations, 1.0, null, new double[1]);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateNullFinalState() throws Exception {
        integrator.integrate(equations, 1.0, new double[1], null);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateNullState() throws Exception {
        integrator.integrate(equations, 1.0, null, null);
    }

    @Test
    public void testComputeDerivatives() throws Exception {
        final double t = 0.0;
        final double[] y = new double[]{0.0};
        final double[] yDot = new double[1];
        integrator.computeDerivatives(t, y, yDot);
        Assert.assertArrayEquals(new double[]{0.0}, yDot, EPSILON);

        final FirstOrderDifferentialEquations differentialEquations = mock(FirstOrderDifferentialEquations.class);
        when(differentialEquations.getDimension()).thenReturn(1);
        integrator.setEquations(differentialEquations);
        integrator.computeDerivatives(0.0, new double[]{0.0}, yDot);
        Assert.assertArrayEquals(new double[]{0.0}, yDot, EPSILON);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testComputeDerivativesMaxEvaluations() throws Exception {
        final int maxEvaluations = 0;
        integrator.setMaxEvaluations(maxEvaluations);
        final double t = 0.0;
        final double[] y = new double[]{0.0};
        integrator.computeDerivatives(t, y, new double[1]);
        integrator.computeDerivatives(t, y, new double[1]);
        integrator.computeDerivatives(t, y, new double[1]);
        integrator.computeDerivatives(t, y, new double[1]);
        integrator.computeDerivatives(t, y, new double[1]);
    }

    @Test
    public void testSetStateInitialized() {
        integrator.setStateInitialized(true);
        Assert.assertTrue(integrator.isStateInitialized());

        integrator.setStateInitialized(false);
        Assert.assertFalse(integrator.isStateInitialized());
    }

    @Test
    public void testAcceptStep() throws Exception {
        final double[] y = new double[]{0.0};
        final double[] yDot = new double[1];
        final AbstractStepInterpolator interpolator = mock(AbstractStepInterpolator.class);
        when(interpolator.getGlobalPreviousTime()).thenReturn(0.0);
        when(interpolator.getGlobalCurrentTime()).thenReturn(1.0);

        integrator.acceptStep(interpolator, y, yDot, 1.0);
        verify(interpolator).setInterpolatedTime(eq(1.0));

        final EventHandler eventHandler = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler, 0.5, 0.1, 5);
        when(eventHandler.evaluateStep(interpolator)).thenReturn(true);
        when(eventHandler.stepAccepted(anyDouble(), any(double[].class))).thenReturn(true);
        when(eventHandler.stop()).thenReturn(true);

        integrator.acceptStep(interpolator, y, yDot, 0.5);
        verify(interpolator, times(2)).setInterpolatedTime(eq(0.5));
        verify(eventHandler).stepAccepted(eq(0.5), isA(double[].class));
        verify(eventHandler).stop();

        when(eventHandler.evaluateStep(interpolator)).thenReturn(false);
        integrator.acceptStep(interpolator, y, yDot, 0.5);
        verify(interpolator, times(3)).setInterpolatedTime(eq(0.5));
        verify(eventHandler, never()).stepAccepted(anyDouble(), any(double[].class));

        when(eventHandler.evaluateStep(interpolator)).thenReturn(true);
        integrator.acceptStep(interpolator, y, yDot, 1.0);
        verify(interpolator, times(4)).setInterpolatedTime(eq(1.0));
        verify(eventHandler).stepAccepted(eq(1.0), isA(double[].class));
        verify(eventHandler).stop();

        when(eventHandler.evaluateStep(interpolator)).thenReturn(false);
        integrator.acceptStep(interpolator, y, yDot, 1.0);
        verify(interpolator, times(5)).setInterpolatedTime(eq(1.0));
        verify(eventHandler, never()).stepAccepted(anyDouble(), any(double[].class));
    }

    @Test
    public void testSanityChecks() throws Exception {
        integrator.sanityChecks(equations, 0.0);
        verifyNoMoreInteractions(equations);

        final double threshold = 1e-15;
        integrator.sanityChecks(equations, 1e-20);
        verify(equations).sanityChecks(eq(1e-20), anyDouble(), anyDouble());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecksTooSmallIntegrationInterval() throws Exception {
        integrator.sanityChecks(equations, 1e-21);
    }

    @Test
    public void testResetOccurred() throws Exception {
        final double[] y = new double[]{0.0};
        integrator.acceptStep(mock(AbstractStepInterpolator.class), y, new double[1], 1.0);
        integrator.reset();
        Assert.assertFalse(integrator.isLastStep());
        integrator.acceptStep(mock(AbstractStepInterpolator.class), y, new double[1], 1.0);
        Assert.assertTrue(integrator.isLastStep());
        Assert.assertTrue(integrator.resetOccurred);
    }

}