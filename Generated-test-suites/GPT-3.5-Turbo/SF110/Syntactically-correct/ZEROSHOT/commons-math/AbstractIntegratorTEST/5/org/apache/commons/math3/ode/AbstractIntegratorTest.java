import org.junit.*;
import java.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("test");
        assertEquals("test", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("test");
        StepHandler handler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(handler);
        Collection<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(1, handlers.size());
        assertTrue(handlers.contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("test");
        StepHandler handler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        Collection<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(0, handlers.size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("test");
        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) throws MaxCountExceededException {}
            public Action eventOccurred(double t, double[] y, boolean increasing) throws MaxCountExceededException {}
            public void resetState(double t, double[] y) {}
        };
        double maxCheckInterval = 1.0;
        double convergence = 0.1;
        int maxIterationCount = 100;
        integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount);
        Collection<EventHandler> handlers = integrator.getEventHandlers();
        assertEquals(1, handlers.size());
        assertTrue(handlers.contains(handler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("test");
        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) throws MaxCountExceededException {}
            public Action eventOccurred(double t, double[] y, boolean increasing) throws MaxCountExceededException {}
            public void resetState(double t, double[] y) {}
        };
        double maxCheckInterval = 1.0;
        double convergence = 0.1;
        int maxIterationCount = 100;
        integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount);
        integrator.clearEventHandlers();
        Collection<EventHandler> handlers = integrator.getEventHandlers();
        assertEquals(0, handlers.size());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("test");
        int maxEvaluations = integrator.getMaxEvaluations();
        assertEquals(-1, maxEvaluations);
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("test");
        int maxEvaluations = 100;
        integrator.setMaxEvaluations(maxEvaluations);
        int actualMaxEvaluations = integrator.getMaxEvaluations();
        assertEquals(maxEvaluations, actualMaxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("test");
        int evaluations = integrator.getEvaluations();
        assertEquals(0, evaluations);
    }

    private class AbstractIntegratorTestImpl extends AbstractIntegrator {
        public AbstractIntegratorTestImpl(String name) {
            super(name);
        }

        public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        }

        public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
        }
    }
}