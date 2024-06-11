package org.apache.commons.math3.ode;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.Integrator;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private Integrator integrator;
    private ExpandableStatefulODE equations;
    private double[] initialState;
    private double[] solution;
    private double[] derivatives;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };
        equations = new ExpandableStatefulODE(new MyDifferentialEquations()) {
            @Override
            public int getDimension() {
                return 1;
            }
        };
        initialState = new double[] { 1.0 };
        derivatives = new double[1];
        integrator.initIntegration(0.0, initialState, 10.0);
        integrator.setEquations(equations);
    }

    @Test
    public void testAddStepHandler() {
        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                // do nothing
            }
        };
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
        assertSame(handler, integrator.getStepHandlers().iterator().next());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler handler = new EventHandler() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double t, double[] state) {
                // do nothing
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }
        };
        integrator.addEventHandler(handler, 1.0, 1.0E-6, 10, new BracketingNthOrderBrentSolver(1.0E-6, 5));
        assertEquals(1, integrator.getEventHandlers().size());
        assertSame(handler, integrator.getEventHandlers().iterator().next());
    }

    @Test
    public void testClearStepHandlers() {
        StepHandler handler1 = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                // do nothing
            }
        };
        StepHandler handler2 = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                // do nothing
            }
        };
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        assertEquals(2, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearEventHandlers() {
        EventHandler handler1 = new EventHandler() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double t, double[] state) {
                // do nothing
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }
        };
        EventHandler handler2 = new EventHandler() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double t, double[] state) {
                // do nothing
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }
        };
        integrator.addEventHandler(handler1, 1.0, 1.0E-6, 10, new BracketingNthOrderBrentSolver(1.0E-6, 5));
        integrator.addEventHandler(handler2, 1.0, 1.0E-6, 10, new BracketingNthOrderBrentSolver(1.0E-6, 5));
        assertEquals(2, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetCurrentStepStart() {
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0.0);
        integrator.setCurrentStepStart(0.0);
        assertEquals(0.0, integrator.getCurrentStepStart(), 0.0);
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0.0);
        integrator.setCurrentSignedStepsize(1.0);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 0.0);
    }

    @Test
    public void testIsLastStep() {
        assertFalse(integrator.isLastStep());
        integrator.setLastStep(true);
        assertTrue(integrator.isLastStep());
    }

    @Test
    public void testResetOccurred() {
        assertFalse(integrator.resetOccurred());
        integrator.setResetOccurred(true);
        assertTrue(integrator.resetOccurred());
    }

    @Test
    public void testGetEventsStates() {
        List<EventState> eventsStates = integrator.getEventsStates();
        assertNotNull(eventsStates);
        assertEquals(0, eventsStates.size());
    }

    @Test
    public void testIsStatesInitialized() {
        assertFalse(integrator.isStatesInitialized());
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStatesInitialized());
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
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        integrator.setEvaluations(0);
        assertEquals(0, integrator.getEvaluations());
        integrator.incrementEvaluations();
        assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() {
        MyDifferentialEquations equations = (MyDifferentialEquations) integrator.getEquations();
        try {
            integrator.integrate(equations, 1.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
        integrator.setMaxEvaluations(Integer.MAX_VALUE);
        try {
            integrator.integrate(equations, 1.0);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // expected
        }
        integrator.setMaxEvaluations(10);
        integrator.setStepSize(0.1);
        integrator.initIntegration(0.0, initialState, 1.0);
        integrator.integrate(equations, 1.0);
        assertArrayEquals(solution, integrator.getState(), 0.0);
        assertEquals(1.0, integrator.getTime(), 0.0);
    }

    @Test
    public void testAcceptStep() throws NumberIsTooSmallException, DimensionMismatchException,
            MaxCountExceededException, NoBracketingException {
        MyDifferentialEquations equations = (MyDifferentialEquations) integrator.getEquations();
        integrator.setMaxEvaluations(10);
        integrator.setStepSize(0.1);
        integrator.initIntegration(0.0, initialState, 1.0);
        AbstractStepInterpolator interpolator = new StepInterpolator();
        interpolator.setGlobalPreviousTime(0.0);
        interpolator.setGlobalCurrentTime(0.1);
        double tEnd = 0.2;
        integrator.acceptStep(interpolator, derivatives, tEnd);
        assertEquals(0.1, integrator.getTime(), 0.0);
        assertArrayEquals(new double[] { 1.105126830469236 }, derivatives, 0.0);
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());

        interpolator.setGlobalPreviousTime(0.1);
        interpolator.setGlobalCurrentTime(0.2);
        integrator.acceptStep(interpolator, derivatives, tEnd);
        assertEquals(0.2, integrator.getTime(), 0.0);
        assertArrayEquals(new double[] { 1.2103920587606753 }, derivatives, 0.0);
        assertTrue(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());

        interpolator.setGlobalPreviousTime(0.2);
        interpolator.setGlobalCurrentTime(0.2);
        integrator.acceptStep(interpolator, derivatives, tEnd);
        assertEquals(0.2, integrator.getTime(), 0.0);
        assertArrayEquals(new double[] { 1.2103920587606753 }, derivatives, 0.0);
        assertTrue(integrator.isLastStep());
        assertTrue(integrator.resetOccurred());
    }

    @Test
    public void testSanityChecks() throws NumberIsTooSmallException, DimensionMismatchException {
        MyDifferentialEquations equations = new MyDifferentialEquations();
        integrator.initIntegration(0.0, initialState, 1.0);
        integrator.setEquations(equations);
        integrator.sanityChecks(equations, 1.0);
    }

    @After
    public void tearDown() {
        integrator = null;
        equations = null;
        initialState = null;
        solution = null;
        derivatives = null;
    }

    private static class StepInterpolator extends AbstractStepInterpolator {
        @Override
        public void init(double t0, double[] y0, double t) {
            // do nothing
        }

        @Override
        public void setInterpolatedTime(double t) {
            super.setInterpolatedTime(t);
        }

        @Override
        public double[] getInterpolatedState() {
            return new double[] { integrator.getState()[0] };
        }

        @Override
        public double[] getDerivativeVector(int index) {
            return new double[] { integrator.getDerivative()[index] };
        }
    }

    private static class MyDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] state, double[] derivatives) {
            derivatives[0] = state[0] * 0.1;
        }

        @Override
        public double timeDerivative(double t, double[] state) {
            return state[0] * 0.1;
        }

        @Override
        public double[] getPrimaryState() {
            return new double[] { 1.0 };
        }

        @Override
        public double[] getInitialState() {
            return new double[] { 1.0 };
        }

        @Override
        public double[] getBoundState(int index) {
            return new double[] { 0.0 };
        }

        @Override
        public double[] getBoundDerivative(int index) {
            return new double[] { 0.0 };
        }

        @Override
        public double[] getInitialDerivative() {
            return new double[] { 0.0 };
        }

        @Override
        public void setTime(double t) {
            // do nothing
        }

        @Override
        public void setPrimaryState(double[] state) {
            this.state = state;
        }

        private double[] state;
    }

}