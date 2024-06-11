import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.ode.sampling.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;

public class AbstractIntegratorTest {
    
    private AbstractIntegrator integrator;
    
    @Before
    public void setUp() {
        integrator = new AbstractIntegratorMock();
    }
    
    @Test
    public void testGetName() {
        assertEquals("AbstractIntegratorMock", integrator.getName());
    }
    
    @Test
    public void testAddStepHandler() {
        StepHandler handler = new StepHandlerMock();
        integrator.addStepHandler(handler);
        Collection<StepHandler> handlers = integrator.getStepHandlers();
        assertTrue(handlers.contains(handler));
    }
    
    @Test
    public void testClearStepHandlers() {
        StepHandler handler = new StepHandlerMock();
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        Collection<StepHandler> handlers = integrator.getStepHandlers();
        assertTrue(handlers.isEmpty());
    }
    
    @Test
    public void testAddEventHandler() {
        EventHandler handler = new EventHandlerMock();
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 100;
        integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount);
        Collection<EventHandler> handlers = integrator.getEventHandlers();
        assertTrue(handlers.contains(handler));
    }
    
    @Test
    public void testClearEventHandlers() {
        EventHandler handler = new EventHandlerMock();
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 100;
        integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount);
        integrator.clearEventHandlers();
        Collection<EventHandler> handlers = integrator.getEventHandlers();
        assertTrue(handlers.isEmpty());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 1000;
        integrator.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = 100;
        ((AbstractIntegratorMock) integrator).setEvaluations(evaluations);
        assertEquals(evaluations, integrator.getEvaluations());
    }
    
    @Test
    public void testSetEquations() {
        ExpandableStatefulODE equations = new ExpandableStatefulODEMock();
        integrator.setEquations(equations);
        assertEquals(equations, integrator.getExpandable());
    }
    
    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquationsMock();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        
        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (Exception e) {
            fail("Unexpected Exception: " + e.toString());
        }
        
        assertEquals(1.0, y[0], 1e-6);
        assertEquals(2.0, y[1], 1e-6);
    }
    
    @Test
    public void testComputeDerivatives() {
        ((AbstractIntegratorMock) integrator).setEvaluations(10);

        double t = 0.0;
        double[] y = {1.0, 2.0};
        double[] yDot = new double[y.length];
        
        try {
            integrator.computeDerivatives(t, y, yDot);
        } catch (Exception e) {
            fail("Unexpected Exception: " + e.toString());
        }
        
        assertEquals(10, integrator.getEvaluations());
    }
    
    private class AbstractIntegratorMock extends AbstractIntegrator {
        public AbstractIntegratorMock() {
            super("AbstractIntegratorMock");
        }
        
        public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // do nothing
        }
    }
    
    private class StepHandlerMock implements StepHandler {
        public void handleStep(StepInterpolator interpolator, boolean isLast) {
            // do nothing
        }
        
        public void init(double t0, double[] y0, double t) {
            // do nothing
        }
    }
    
    private class EventHandlerMock implements EventHandler {
        public void init(double t0, double[] y0, double t) {
            // do nothing
        }
        
        public double g(double t, double[] y) {
            return 0.0;
        }
        
        public Action eventOccurred(double t, double[] y, boolean increasing) {
            return Action.CONTINUE;
        }
        
        public void resetState(double t, double[] y) {
            // do nothing
        }
    }
    
    private class ExpandableStatefulODEMock extends ExpandableStatefulODE {
        public ExpandableStatefulODEMock() {
            super(new FirstOrderDifferentialEquationsMock());
        }
    }
    
    private class FirstOrderDifferentialEquationsMock implements FirstOrderDifferentialEquations {
        public int getDimension() {
            return 2;
        }
        
        public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
            // do nothing
        }
    }
}