import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator abstractIntegrator;

    @Before
    public void setUp() {
        abstractIntegrator = new AbstractIntegratorStub();
    }

    @Test
    public void testGetName() {
        assertEquals("AbstractIntegrator", abstractIntegrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        StepHandler stepHandler = new StepHandlerStub();
        abstractIntegrator.addStepHandler(stepHandler);
        Collection<StepHandler> stepHandlers = abstractIntegrator.getStepHandlers();
        assertTrue(stepHandlers.contains(stepHandler));
    }

    @Test
    public void testClearStepHandlers() {
        StepHandler stepHandler = new StepHandlerStub();
        abstractIntegrator.addStepHandler(stepHandler);
        abstractIntegrator.clearStepHandlers();
        Collection<StepHandler> stepHandlers = abstractIntegrator.getStepHandlers();
        assertFalse(stepHandlers.contains(stepHandler));
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandlerStub();
        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 100;
        abstractIntegrator.addEventHandler(eventHandler, maxCheckInterval, convergence, maxIterationCount);
        Collection<EventHandler> eventHandlers = abstractIntegrator.getEventHandlers();
        assertTrue(eventHandlers.contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        EventHandler eventHandler = new EventHandlerStub();
        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 100;
        abstractIntegrator.addEventHandler(eventHandler, maxCheckInterval, convergence, maxIterationCount);
        abstractIntegrator.clearEventHandlers();
        Collection<EventHandler> eventHandlers = abstractIntegrator.getEventHandlers();
        assertFalse(eventHandlers.contains(eventHandler));
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 1000;
        abstractIntegrator.setMaxEvaluations(maxEvaluations);
        int result = abstractIntegrator.getMaxEvaluations();
        assertEquals(maxEvaluations, result);
    }

    @Test
    public void testGetEvaluations() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        double t0 = 0;
        double[] y0 = {1, 2, 3};
        double t = 1;
        double[] y = new double[3];
        abstractIntegrator.integrate(new DifferentialEquationsStub(), t0, y0, t, y);
        int evaluations = abstractIntegrator.getEvaluations();
        assertEquals(3, evaluations);
    }

    // Stub classes for testing
    private static class AbstractIntegratorStub extends AbstractIntegrator {
        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // Do nothing in the stub
        }

        @Override
        protected void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
            // Do nothing in the stub
        }
    }

    private static class DifferentialEquationsStub implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 3;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            // Do nothing in the stub
        }
    }

    private static class EventHandlerStub implements EventHandler {
        @Override
        public void init(double t0, double[] y0, double t) {
            // Do nothing in the stub
        }

        @Override
        public double g(double t, double[] y) {
            // Do nothing in the stub
            return 0;
        }

        @Override
        public Action eventOccurred(double t, double[] y, boolean increasing) {
            // Do nothing in the stub
            return null;
        }

        @Override
        public void resetState(double t, double[] y) {
            // Do nothing in the stub
        }
    }

    private static class StepHandlerStub implements StepHandler {
        @Override
        public void init(double t0, double[] y0, double t) {
            // Do nothing in the stub
        }

        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLast) {
            // Do nothing in the stub
        }
    }

}