import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.sampling.StepHandler;

public class AbstractIntegratorTest {
    
    private static class TestAbstractIntegrator extends AbstractIntegrator {
        @Override
        public void integrate(ExpandableStatefulODE equations, double t) {
            // implementation left blank for testing
        }
    }
    
    private TestAbstractIntegrator integrator;
    
    @Before
    public void setUp() {
        integrator = new TestAbstractIntegrator();
    }
    
    @Test
    public void testGetName() {
        assertEquals(null, integrator.getName());
    }
    
    @Test
    public void testAddStepHandler() {
        StepHandler handler = new StepHandler() {
            
            public void init(double t0, double[] y0, double t) {
            }
            
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
            }
            
        };
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
    }
    
    @Test
    public void testClearStepHandlers() {
        StepHandler handler = new StepHandler() {
            
            public void init(double t0, double[] y0, double t) {
            }
            
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
            }
            
        };
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }
    
    @Test
    public void testAddEventHandler() {
        EventHandler handler = new EventHandler() {
            
            public void init(double t0, double[] y0, double t) {
            }
            
            public double g(double t, double[] y) {
                return 0;
            }
            
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }
            
            public void resetState(double t, double[] y) {
            }
            
        };
        integrator.addEventHandler(handler, 0.0, 0.0, 0);
        assertEquals(1, integrator.getEventHandlers().size());
    }
    
    @Test
    public void testClearEventHandlers() {
        EventHandler handler = new EventHandler() {
            
            public void init(double t0, double[] y0, double t) {
            }
            
            public double g(double t, double[] y) {
                return 0;
            }
            
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }
            
            public void resetState(double t, double[] y) {
            }
            
        };
        integrator.addEventHandler(handler, 0.0, 0.0, 0);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }
    
    @Test
    public void testGetCurrentStepStart() {
        double stepStart = 1.0;
        integrator.stepStart = stepStart;
        assertEquals(stepStart, integrator.getCurrentStepStart(), 0.0);
    }
    
    @Test
    public void testGetCurrentSignedStepsize() {
        double stepSize = 1.0;
        integrator.stepSize = stepSize;
        assertEquals(stepSize, integrator.getCurrentSignedStepsize(), 0.0);
    }
    
    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        integrator.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        assertEquals(0, integrator.getEvaluations());
    }
    
    @Test
    public void testSetStateInitialized() {
        boolean stateInitialized = true;
        integrator.setStateInitialized(stateInitialized);
        assertEquals(stateInitialized, integrator.statesInitialized);
    }
    
    @Test
    public void testAcceptStep() {
        // implementation left blank for testing
    }
    
    @Test
    public void testComputeDerivatives() {
        double t = 0.0;
        double[] y = new double[1];
        double[] yDot = new double[1];
        integrator.computeDerivatives(t, y, yDot);
        assertEquals(1, integrator.getEvaluations());
    }
    
    @Test
    public void testSanityChecks() {
        // implementation left blank for testing
    }
    
}