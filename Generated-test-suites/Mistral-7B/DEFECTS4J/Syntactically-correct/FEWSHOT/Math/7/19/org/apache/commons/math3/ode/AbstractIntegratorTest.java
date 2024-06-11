package org.apache.commons.math3.ode;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEventHandler;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.stepper.StepHandler;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail();
            }
        };
        integrator.initIntegration(0.0, new double[0], 1.0);
        assertNull(integrator.getEquations());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.isStateInitialized());
        assertNull(integrator.getStepHandlers());
        assertNull(integrator.getEventHandlers());
        assertNaN(integrator.getCurrentStepStart());
        assertNaN(integrator.getCurrentSignedStepsize());
        assertEquals(0, integrator.getEvaluations());
        assertEquals(0, integrator.getMaxEvaluations());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail();
            }
        };
        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(AbstractIntegrator integrator, boolean lastStep) {
                integrator.setLastStep(lastStep);
            }
        };
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail();
            }
        };
        EventHandler eventHandler = new AbstractEventHandler() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return true;
            }

            @Override
            public void stepAccepted(double time, double[] state) {
                integrator.setLastStep(true);
            }
        };
        integrator.addEventHandler(eventHandler, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        assertTrue(integrator.getEventHandlers().size() == 1);
        assertEquals(eventHandler, integrator.getEventHandlers().get(0));
    }

    @Test
    public void testGetEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail();
            }
        };
        EventHandler eventHandler1 = new AbstractEventHandler() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return true;
            }

            @Override
            public void stepAccepted(double time, double[] state) {
                integrator.setLastStep(true);
            }
        };
        integrator.addEventHandler(eventHandler1, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        EventHandler eventHandler2 = new AbstractEventHandler() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }
        };
        integrator.addEventHandler(eventHandler2, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testComputeDerivatives() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail();
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot)
                    throws MaxCountExceededException, DimensionMismatchException {
                System.arraycopy(y, 0, yDot, 0, y.length);
            }
        };
        integrator.setEquations(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }
        }));
        double[] y = new double[1];
        double[] yDot = new double[1];
        integrator.computeDerivatives(0.0, y, yDot);
        assertEquals(y[0] * 2, yDot[0], 1e-10);
    }

    @Test
    public void testAcceptStep() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail();
            }

            @Override
            public void setLastStep(boolean lastStep) {
                this.lastStep = lastStep;
            }

            @Override
            public boolean isLastStep() {
                return lastStep;
            }

            @Override
            public void setStateInitialized(boolean stateInitialized) {
                this.stateInitialized = stateInitialized;
            }

            @Override
            public boolean isStateInitialized() {
                return stateInitialized;
            }

            @Override
            public double getCurrentStepStart() {
                return currentStepStart;
            }

            @Override
            public double getCurrentSignedStepsize() {
                return currentSignedStepsize;
            }

            @Override
            public int getEvaluations() {
                return evaluations;
            }

            @Override
            public void clearEventHandlers() {
                this.eventHandlers.clear();
            }

            @Override
            public Collection<EventHandler> getEventHandlers() {
                return Collections.unmodifiableCollection(eventHandlers);
            }

            @Override
            public Collection<StepHandler> getStepHandlers() {
                return Collections.unmodifiableCollection(stepHandlers);
            }

            @Override
            public void addEventHandler(EventHandler handler, double maxCheckInterval,
                    double convergence, int maxIterationCount, UnivariateSolver solver) {
                eventHandlers.add(handler);
            }

            @Override
            public void addStepHandler(StepHandler handler) {
                stepHandlers.add(handler);
            }

            @Override
            public void setEquations(ExpandableStatefulODE equations) {
                this.equations = equations;
            }

            @Override
            public ExpandableStatefulODE getEquations() {
                return equations;
            }

            private boolean lastStep;
            private boolean stateInitialized;
            private ExpandableStatefulODE equations;
            private List<EventHandler> eventHandlers = new ArrayList<>();
            private List<StepHandler> stepHandlers = new ArrayList<>();
            private double currentStepStart;
            private double currentSignedStepsize;
            private int evaluations;
        };
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractIntegrator integrator, boolean lastStep) {
                integrator.setLastStep(lastStep);
            }
        };
        integrator.addStepHandler(stepHandler);
        EventHandler eventHandler = new AbstractEventHandler() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return true;
            }

            @Override
            public void stepAccepted(double time, double[] state) {
                integrator.setLastStep(true);
            }
        };
        integrator.addEventHandler(eventHandler, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.5;
            }

            @Override
            public double getCurrentTime() {
                return 0.5;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public void setInterpolatedTime(double time) {
                integrator.setTime(time);
            }

            @Override
            public double[] getInterpolatedState() {
                return integrator.getState();
            }
        };
        double[] y = new double[1];
        double[] yDot = new double[1];
        integrator.initIntegration(0.0, y, 1.0);
        integrator.setEquations(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }
        }));
        integrator.acceptStep(interpolator, y, yDot, 0.5);
        assertTrue(integrator.isLastStep());
        assertFalse(integrator.isStateInitialized());
        assertEquals(0.5, integrator.getCurrentStepStart(), 1e-10);
        assertEquals(0.5, integrator.getCurrentSignedStepsize(), 1e-10);
        assertEquals(1, integrator.getEvaluations());
        assertEquals(1, integrator.getMaxEvaluations());
        assertFalse(integrator.getEventHandlers().isEmpty());
        assertFalse(integrator.getStepHandlers().isEmpty());
        reset(integrator);
        integrator.acceptStep(interpolator, y, yDot, 0.5);
        assertTrue(integrator.isLastStep());
        assertTrue(integrator.isStateInitialized());
        assertEquals(0.5, integrator.getCurrentStepStart(), 1e-10);
        assertEquals(0.5, integrator.getCurrentSignedStepsize(), 1e-10);
        assertEquals(2, integrator.getEvaluations());
        assertEquals(1, integrator.getMaxEvaluations());
        reset(integrator);
        integrator.acceptStep(interpolator, y, yDot, 0.5);
        assertFalse(integrator.isLastStep());
        assertTrue(integrator.isStateInitialized());
        assertEquals(0.5, integrator.getCurrentStepStart(), 1e-10);
        assertEquals(-0.5, integrator.getCurrentSignedStepsize(), 1e-10);
        assertEquals(3, integrator.getEvaluations());
        assertEquals(1, integrator.getMaxEvaluations());
    }

    private void reset(AbstractIntegrator integrator) {
        integrator.setLastStep(false);
        integrator.setEquations(null);
        integrator.setState(new double[0]);
        integrator.setTime(Double.NaN);
        integrator.clearEventHandlers();
        integrator.clearStepHandlers();
        integrator.evaluations.resetCount();
        integrator.initIntegration(0.0, new double[0], 1.0);
    }

}