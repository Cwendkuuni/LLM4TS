import org.apache.commons.math.ode.*;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new DummyIntegrator();
    }

    @Test
    public void testAddStepHandler() {
        StepHandler handler = new DummyStepHandler();
        integrator.addStepHandler(handler);
        Collection<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(1, handlers.size());
        assertTrue(handlers.contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        StepHandler handler = new DummyStepHandler();
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        Collection<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(0, handlers.size());
        assertFalse(handlers.contains(handler));
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new DummyEventHandler();
        double maxCheckInterval = 0.1;
        double convergence = 1e-12;
        int maxIterationCount = 100;
        integrator.addEventHandler(eventHandler, maxCheckInterval, convergence, maxIterationCount);
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        EventHandler eventHandler = new DummyEventHandler();
        double maxCheckInterval = 0.1;
        double convergence = 1e-12;
        int maxIterationCount = 100;
        integrator.addEventHandler(eventHandler, maxCheckInterval, convergence, maxIterationCount);
        integrator.clearEventHandlers();
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(0, eventHandlers.size());
        assertFalse(eventHandlers.contains(eventHandler));
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 1000;
        integrator.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        int maxEvaluations = 1000;
        integrator.setMaxEvaluations(maxEvaluations);
        int evaluations = 500;
        for (int i = 0; i < evaluations; i++) {
            integrator.computeDerivatives(0.0, new double[]{}, new double[]{});
        }
        assertEquals(evaluations, integrator.getEvaluations());
    }

    private static class DummyIntegrator extends AbstractIntegrator {

        public DummyIntegrator() {
            super("Dummy");
        }

        @Override
        public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
            
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot)
                throws MaxCountExceededException {
            
        }
    }

    private static class DummyStepHandler implements StepHandler {

        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
        }
    }

    private static class DummyEventHandler implements EventHandler {

        @Override
        public void init(double t0, double[] y0, double t) {
        }

        @Override
        public double g(double t, double[] y) {
            return 0;
        }

        @Override
        public Action eventOccurred(double t, double[] y, boolean increasing) {
            return Action.STOP;
        }

        @Override
        public void resetState(double t, double[] y) {
        }
    }

}