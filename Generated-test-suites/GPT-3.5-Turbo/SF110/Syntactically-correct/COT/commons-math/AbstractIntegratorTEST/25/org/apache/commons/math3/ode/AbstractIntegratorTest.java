import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractIntegratorTest {
    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        String name = integrator.getName();
        assertEquals(null, name);
    }
    
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        StepHandler stepHandler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
    }
    
    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        StepHandler stepHandler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(stepHandler);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }
    
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        EventHandler eventHandler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) { return 0; }
            public Action eventOccurred(double t, double[] y, boolean increasing) { return Action.CONTINUE; }
            public void resetState(double t, double[] y) {}
        };
        integrator.addEventHandler(eventHandler, 0, 0, 0);
        assertEquals(1, integrator.getEventHandlers().size());
    }
    
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        EventHandler eventHandler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) { return 0; }
            public Action eventOccurred(double t, double[] y, boolean increasing) { return Action.CONTINUE; }
            public void resetState(double t, double[] y) {}
        };
        integrator.addEventHandler(eventHandler, 0, 0, 0);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }
    
    @Test
    public void testGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        double currentStepStart = integrator.getCurrentStepStart();
        assertEquals(Double.NaN, currentStepStart, 0);
    }
    
    @Test
    public void testGetCurrentSignedStepsize() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        double currentSignedStepsize = integrator.getCurrentSignedStepsize();
        assertEquals(Double.NaN, currentSignedStepsize, 0);
    }
    
    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        int evaluations = integrator.getEvaluations();
        assertEquals(0, evaluations);
    }
    
    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        double[] y = {1.0};
        double[] yDot = {0.0};
        try {
            integrator.computeDerivatives(0, y, yDot);
        } catch (MaxCountExceededException | DimensionMismatchException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        integrator.setStateInitialized(true);
        assertTrue(integrator.statesInitialized);
    }
    
    @Test
    public void testSanityChecks() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            public int getDimension() { return 1; }
            public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
                yDot[0] = 0;
            }
        });
        double t = 1.0;
        try {
            integrator.sanityChecks(equations, t);
        } catch (NumberIsTooSmallException | DimensionMismatchException e) {
            fail(e.getMessage());
        }
    }
}