package org.apache.commons.math3.ode;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepInterpolatorAdapter;
import org.apache.commons.math3.ode.util.ExpandableStatefulODE;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {

            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }

        };
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandlerAdapter() {

            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                // no-op
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // no-op
            }

        });
        assertSame(integrator, integrator.getIntegrator());
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new StepHandlerAdapter() {

            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                // no-op
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // no-op
            }

        });
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testAddEventHandler() {
        final StepInterpolator interpolator = new StepInterpolatorAdapter(1.0);
        final StepHandler stepHandler = new StepHandlerAdapter() {

            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                // no-op
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // no-op
            }

        };
        integrator.addEventHandler(new EventHandler() {

            @Override
            public void init(double t0, double[] y0, double t) {
                stepHandler.init(t0, y0, t);
            }

            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double eventTime, double[] eventY) {
                stepHandler.handleStep(interpolator, true);
            }

            @Override
            public void handleEvent(AbstractEvent event) {
                // no-op
            }

        }, 1.0, 1.0, 1);
        integrator.addEventHandler(new EventHandler() {

            @Override
            public void init(double t0, double[] y0, double t) {
                // no-op
            }

            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double eventTime, double[] eventY) {
                // no-op
            }

            @Override
            public void handleEvent(AbstractEvent event) {
                // no-op
            }

        }, 2.0, 2.0, 2);
        assertEquals(2, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearEventHandlers() {
        final StepInterpolator interpolator = new StepInterpolatorAdapter(1.0);
        integrator.addEventHandler(new EventHandler() {

            @Override
            public void init(double t0, double[] y0, double t) {
                // no-op
            }

            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double eventTime, double[] eventY) {
                // no-op
            }

            @Override
            public void handleEvent(AbstractEvent event) {
                // no-op
            }

        }, 1.0, 1.0, 1);
        integrator.addEventHandler(new EventHandler() {

            @Override
            public void init(double t0, double[] y0, double t) {
                // no-op
            }

            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double eventTime, double[] eventY) {
                // no-op
            }

            @Override
            public void handleEvent(AbstractEvent event) {
                // no-op
            }

        }, 2.0, 2.0, 2);
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetCurrentStepStart() {
        double stepStart = 0.0;
        integrator.setCurrentStepStart(stepStart);
        assertEquals(stepStart, integrator.getCurrentStepStart(), 0.0);
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        double stepSize = 1.0;
        integrator.setCurrentSignedStepsize(stepSize);
        assertEquals(stepSize, integrator.getCurrentSignedStepsize(), 0.0);
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());
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
        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0, 2.0 };
        final double t = 3.0;
        final double[] y = new double[y0.length];
        integrator.initIntegration(t0, y0, t);
        integrator.setEquations(new ExpandableStatefulODE() {

            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void setTime(double t) {
                // no-op
            }

            @Override
            public void setPrimaryState(double[] state) {
                // no-op
            }

            @Override
            public void computeDerivatives(double t, double[] state, double[] derivatives) {
                for (int i = 0; i < state.length; ++i) {
                    derivatives[i] = state[i] * 2.0;
                }
            }

        });
        integrator.integrate(new ExpandableStatefulODE[] { integrator.getEquations() }, t);
        integrator.computeDerivatives(t, y, null);
        assertArrayEquals(new double[] { 3.0, 6.0 }, y, 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatch() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0, 2.0 };
        final double t = 3.0;
        final double[] y = new double[1];
        integrator.initIntegration(t0, y0, t);
        integrator.setEquations(new ExpandableStatefulODE() {

            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void setTime(double t) {
                // no-op
            }

            @Override
            public void setPrimaryState(double[] state) {
                // no-op
            }

            @Override
            public void computeDerivatives(double t, double[] state, double[] derivatives) {
                for (int i = 0; i < state.length; ++i) {
                    derivatives[i] = state[i] * 2.0;
                }
            }

        });
        integrator.integrate(new ExpandableStatefulODE[] { integrator.getEquations() }, t);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxEvaluations() throws Exception {
        final int maxEvaluations = 1;
        integrator.setMaxEvaluations(maxEvaluations);
        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0, 2.0 };
        final double t = 3.0;
        final double[] y = new double[y0.length];
        integrator.initIntegration(t0, y0, t);
        integrator.setEquations(new ExpandableStatefulODE() {

            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void setTime(double t) {
                // no-op
            }

            @Override
            public void setPrimaryState(double[] state) {
                // no-op
            }

            @Override
            public void computeDerivatives(double t, double[] state, double[] derivatives) {
                for (int i = 0; i < state.length; ++i) {
                    derivatives[i] = state[i] * 2.0;
                }
            }

        });
        integrator.integrate(new ExpandableStatefulODE[] { integrator.getEquations() }, t);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateNoBracketing() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0, 2.0 };
        final double t = 3.0;
        final double[] y = new double[y0.length];
        integrator.initIntegration(t0, y0, t);
        integrator.setEquations(new ExpandableStatefulODE() {

            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void setTime(double t) {
                // no-op
            }

            @Override
            public void setPrimaryState(double[] state) {
                // no-op
            }

            @Override
            public void computeDerivatives(double t, double[] state, double[] derivatives) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double getTime() {
                return t0;
            }

            @Override
            public double[] getPrimaryState() {
                return y0;
            }

        });
        integrator.integrate(new ExpandableStatefulODE[] { integrator.getEquations() }, t);
    }

    @Test
    public void testAcceptStep() throws Exception {
        final double[] y = new double[1];
        final double[] yDot = new double[1];
        final StepInterpolator interpolator = new StepInterpolatorAdapter(1.0);
        integrator.setStepSize(1.0);
        integrator.acceptStep(interpolator, y, yDot, 1.0);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 0.0);
        assertFalse(integrator.isLastStep());
        assertArrayEquals(new double[] { 1.0 }, y, 0.0);
        assertNull(integrator.getResetOccurred());
    }

    @Test
    public void testAcceptStepLastStep() throws Exception {
        final double[] y = new double[1];
        final double[] yDot = new double[1];
        final StepInterpolator interpolator = new StepInterpolatorAdapter(1.0);
        integrator.setStepSize(1.0);
        integrator.setLastStep(true);
        integrator.acceptStep(interpolator, y, yDot, 1.0);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 0.0);
        assertTrue(integrator.isLastStep());
        assertArrayEquals(new double[] { 1.0 }, y, 0.0);
        assertNull(integrator.getResetOccurred());
    }

    @Test
    public void testAcceptStepReset() throws Exception {
        final double[] y = new double[1];
        final double[] yDot = new double[1];
        final StepInterpolator interpolator = new StepInterpolatorAdapter(1.0);
        integrator.setStepSize(1.0);
        integrator.setResetOccurred(new Object());
        integrator.acceptStep(interpolator, y, yDot, 1.0);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 0.0);
        assertFalse(integrator.isLastStep());
        assertArrayEquals(new double[] { 0.0 }, y, 0.0);
        assertSame(integrator.getResetOccurred(), integrator.getResetOccurred());
    }

}