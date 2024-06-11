import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractIntegratorTest {
    
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandler handler = new StepHandlerImpl();
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(handler));
    }
    
    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandler handler = new StepHandlerImpl();
        integrator.addStepHandler(handler);
        
        integrator.clearStepHandlers();
        
        assertEquals(0, integrator.getStepHandlers().size());
    }
    
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        EventHandler handler = new EventHandlerImpl();
        double maxCheckInterval = 1.0;
        double convergence = 0.001;
        int maxIterationCount = 100;
        UnivariateSolver solver = new BracketingNthOrderBrentSolver(convergence, 5);
        
        integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount, solver);
        
        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(handler));
    }
    
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        EventHandler handler = new EventHandlerImpl();
        double maxCheckInterval = 1.0;
        double convergence = 0.001;
        int maxIterationCount = 100;
        UnivariateSolver solver = new BracketingNthOrderBrentSolver(convergence, 5);
        
        integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount, solver);
        
        integrator.clearEventHandlers();
        
        assertEquals(0, integrator.getEventHandlers().size());
    }
    
    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        int maxEvaluations = 100;
        
        integrator.setMaxEvaluations(maxEvaluations);
        
        assertEquals(maxEvaluations, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        Incrementor evaluations = integrator.getEvaluationsCounter();
        
        assertEquals(0, integrator.getEvaluations());
        
        evaluations.incrementCount();
        
        assertEquals(1, integrator.getEvaluations());
    }
    
    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        FirstOrderDifferentialEquations equations = new DifferentialEquationsImpl();
        double t0 = 0.0;
        double[] y0 = {0.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        
        try {
            double endTime = integrator.integrate(equations, t0, y0, t, y);
            
            assertEquals(t, endTime, 0.0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    private class AbstractIntegratorImpl extends AbstractIntegrator {
        public void integrate(ExpandableStatefulODE equations, double t) {}
    }
    
    private class StepHandlerImpl implements StepHandler {
        public void init(double t0, double[] y0, double t) {}
        public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {}
    }
    
    private class EventHandlerImpl implements EventHandler {
        public void init(double t0, double[] y0, double t) {}
        public double g(double t, double[] y) { return 0.0; }
        public Action eventOccurred(double t, double[] y, boolean increasing) { return Action.CONTINUE; }
        public void resetState(double t, double[] y) {}
    }
    
    private class DifferentialEquationsImpl implements FirstOrderDifferentialEquations {
        public int getDimension() { return 1; }
        public void computeDerivatives(double t, double[] y, double[] yDot) {}
    }    
}