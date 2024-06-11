import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEventHandler;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventType;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no integration here
            }
        };
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }
        };
    }

    @Test
    public void testAddEventHandler() throws Exception {
        integrator.addEventHandler(new MyEventHandler(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new MyStepHandler());
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(new MyEventHandler(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new MyStepHandler());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testIntegrate() throws Exception {
        integrator.addEventHandler(new MyEventHandler(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        integrator.setEquations(equations);
        integrator.integrate(equations, 0, new double[] { 1 }, 2, new double[1]);
        fail("Integration should have thrown an exception");
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatch() throws Exception {
        integrator.setEquations(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
                yDot[1] = y[1] * 2;
            }
        });
        integrator.integrate(equations, 0, new double[] { 1, 1 }, 2, new double[1]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateTooSmallStep() throws Exception {
        integrator.addEventHandler(new MyEventHandler(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, 0, new double[] { 1 }, 1, new double[1]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxEvaluationsExceeded() throws Exception {
        integrator.addEventHandler(new MyEventHandler(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0, new double[] { 1 }, 2, new double[1]);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateNoBracketing() throws Exception {
        final MyEvent myEvent = new MyEvent();
        integrator.addEventHandler(myEvent, 0, 1, 1, new BracketingNthOrderBrentSolver(1, 5));
        integrator.setEquations(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }
        });
        integrator.integrate(equations, 0, new double[] { 1 }, 1, new double[1]);
    }

    @Test
    public void testIntegrateWithEventHandler() throws Exception {
        integrator.addEventHandler(new MyEventHandlerWithReset(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        integrator.setEquations(equations);
        final double[] result = new double[1];
        integrator.integrate(equations, 0, new double[] { 1 }, 2, result);
        assertEquals(2, result[0]);
        assertTrue(integrator.resetOccurred);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithEventHandlerDimensionMismatch() throws Exception {
        integrator.addEventHandler(new MyEventHandlerWithResetAndException(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        integrator.setEquations(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
                yDot[1] = y[1] * 2;
            }
        });
        integrator.integrate(equations, 0, new double[] { 1, 1 }, 2, new double[1]);
    }

    @Test
    public void testResetOccurred() throws Exception {
        integrator.addEventHandler(new MyEventHandlerWithReset(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        integrator.setEquations(equations);
        integrator.integrate(equations, 0, new double[] { 1 }, 2, new double[1]);
        assertFalse(integrator.resetOccurred);
        integrator.resetOccurred = false;
        integrator.integrate(equations, 0, new double[] { 1 }, 2.1, new double[1]);
        assertTrue(integrator.resetOccurred);
    }

    @Test
    public void testStep() throws Exception {
        integrator.addEventHandler(new MyEventHandler(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        integrator.addStepHandler(new MyStepHandler());
        integrator.setEquations(equations);
        integrator.integrate(equations, 0, new double[] { 1 }, 2, new double[1]);
        assertEquals(2, integrator.getEvaluations());
    }

    @Test
    public void testStepInterpolator() throws Exception {
        integrator.addEventHandler(new MyEventHandler(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        integrator.addStepHandler(new MyStepHandler());
        integrator.setEquations(equations);
        integrator.integrate(equations, 0, new double[] { 1 }, 2, new double[1]);
        final AbstractStepInterpolator interpolator = integrator.getInterpolator();
        final double stepSize = interpolator.getStepSize();
        final double step = integrator.getLastStep();
        assertEquals(stepSize, step, 1e-14);
    }

    @Test
    public void testStepSize() throws Exception {
        integrator.addEventHandler(new MyEventHandlerWithStepSize(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        integrator.addStepHandler(new MyStepHandler());
        integrator.setEquations(equations);
        integrator.integrate(equations, 0, new double[] { 1 }, 2, new double[1]);
        assertEquals(1.5, integrator.getLastStepSize(), 1e-14);
    }

    @Test
    public void testLastStep() throws Exception {
        integrator.addEventHandler(new MyEventHandler(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        integrator.addStepHandler(new MyStepHandler());
        integrator.setEquations(equations);
        integrator.integrate(equations, 0, new double[] { 1 }, 2, new double[1]);
        assertTrue(integrator.isLastStep());
    }

    @Test
    public void testLastStepFalse() throws Exception {
        integrator.addEventHandler(new MyEventHandler(), 1, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        integrator.addStepHandler(new MyStepHandler());
        integrator.setEquations(equations);
        integrator.integrate(equations, 0, new double[] { 1 }, 1.5, new double[1]);
        assertFalse(integrator.isLastStep());
    }

    @After
    public void tearDown() {
        integrator = null;
        equations = null;
    }

    private static class MyEventHandler implements AbstractEventHandler {
        private final double eventTime;

        public MyEventHandler() {
            this.eventTime = 1.5;
        }

        @Override
        public EventType getEventType() {
            return EventType.CHANGE;
        }

        @Override
        public EventState getState(double t, double[] y, double[] yDot) {
            return new EventState(this, eventTime, 0, 1, new BracketingNthOrderBrentSolver(0, 5));
        }

        @Override
        public void init(double t, double[] y, double tTarget) {
            // empty
        }

        @Override
        public void reinitializeBegin(AbstractStepInterpolator interpolator) {
            // empty
        }

        @Override
        public void stepAccepted(double t, double[] y) {
            // empty
        }

        @Override
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return interpolator.getGlobalCurrentTime() >= eventTime;
        }

        @Override
        public void stepCompleted(double t, double[] y, double[] yDot, int eventCode, double[] eventData) {
            // empty
        }
    }

    private static class MyEventHandlerWithReset implements AbstractEventHandler {
        private final double eventTime;

        public MyEventHandlerWithReset() {
            this.eventTime = 1.5;
        }

        @Override
        public EventType getEventType() {
            return EventType.CHANGE;
        }

        @Override
        public EventState getState(double t, double[] y, double[] yDot) {
            return new EventState(this, eventTime, 1, 1, new BracketingNthOrderBrentSolver(0, 5)) {
                @Override
                public void reset(double t, double[] y) {
                    y[0] = 0;
                }
            };
        }

        @Override
        public void init(double t, double[] y, double tTarget) {
            // empty
        }

        @Override
        public void reinitializeBegin(AbstractStepInterpolator interpolator) {
            // empty
        }

        @Override
        public void stepAccepted(double t, double[] y) {
            // empty
        }

        @Override
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return interpolator.getGlobalCurrentTime() >= eventTime;
        }

        @Override
        public void stepCompleted(double t, double[] y, double[] yDot, int eventCode, double[] eventData) {
            // empty
        }
    }

    private static class MyEventHandlerWithResetAndException implements AbstractEventHandler {
        private final double eventTime;

        public MyEventHandlerWithResetAndException() {
            this.eventTime = 1.5;
        }

        @Override
        public EventType getEventType() {
            return EventType.CHANGE;
        }

        @Override
        public EventState getState(double t, double[] y, double[] yDot) {
            return new EventState(this, eventTime, 2, 2, new BracketingNthOrderBrentSolver(0, 5)) {
                @Override
                public void reset(double t, double[] y) {
                    throw new RuntimeException("Reset exception");
                }
            };
        }

        @Override
        public void init(double t, double[] y, double tTarget) {
            // empty
        }

        @Override
        public void reinitializeBegin(AbstractStepInterpolator interpolator) {
            // empty
        }

        @Override
        public void stepAccepted(double t, double[] y) {
            // empty
        }

        @Override
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return interpolator.getGlobalCurrentTime() >= eventTime;
        }

        @Override
        public void stepCompleted(double t, double[] y, double[] yDot, int eventCode, double[] eventData) {
            // empty
        }
    }

    private static class MyStepHandler implements StepHandler {
        @Override
        public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
            // empty
        }

        @Override
        public void init(double t0, double[] y0, double t) {
            // empty
        }
    }

}