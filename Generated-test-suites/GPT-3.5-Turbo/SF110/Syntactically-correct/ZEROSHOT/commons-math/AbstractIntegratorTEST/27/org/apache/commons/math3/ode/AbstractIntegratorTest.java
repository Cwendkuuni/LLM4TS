import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {
    
    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {}
        };
        
        assertNull(integrator.getName());
        
        AbstractIntegrator integratorWithName = new AbstractIntegrator("test") {
            public void integrate(ExpandableStatefulODE p0, double p1) {}
        };
        
        assertEquals("test", integratorWithName.getName());
    }
    
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {}
        };
        
        StepHandler handler1 = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {}
        };
        StepHandler handler2 = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {}
        };
        
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        
        Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertEquals(2, stepHandlers.size());
        assertTrue(stepHandlers.contains(handler1));
        assertTrue(stepHandlers.contains(handler2));
    }
    
    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {}
        };
        
        StepHandler handler1 = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {}
        };
        StepHandler handler2 = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {}
        };
        
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        
        integrator.clearStepHandlers();
        
        Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertTrue(stepHandlers.isEmpty());
    }
    
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {}
        };
        
        EventHandler handler1 = new EventHandler() {
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
        EventHandler handler2 = new EventHandler() {
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
        
        integrator.addEventHandler(handler1, 1, 0.01, 100);
        integrator.addEventHandler(handler2, 2, 0.02, 200);
        
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(handler1));
        assertTrue(eventHandlers.contains(handler2));
    }
    
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {}
        };
        
        EventHandler handler1 = new EventHandler() {
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
        EventHandler handler2 = new EventHandler() {
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
        
        integrator.addEventHandler(handler1, 1, 0.01, 100);
        integrator.addEventHandler(handler2, 2, 0.02, 200);
        
        integrator.clearEventHandlers();
        
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertTrue(eventHandlers.isEmpty());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {}
        };
        
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {}
        };
        
        integrator.setMaxEvaluations(100);
        
        assertEquals(100, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {}
        };
        
        assertEquals(0, integrator.getEvaluations());
        
        integrator.computeDerivatives(0, new double[]{0, 0}, new double[]{0, 0});
        assertEquals(1, integrator.getEvaluations());
        
        integrator.computeDerivatives(0, new double[]{0, 0}, new double[]{0, 0});
        assertEquals(2, integrator.getEvaluations());
    }
    
    @Test
    public void testSetEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {}
        };
        
        ExpandableStatefulODE equations = new ExpandableStatefulODE(null);
        
        integrator.setEquations(equations);
        
        assertEquals(equations, integrator.getExpandable());
    }
    
    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {}
        };
        
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
                yDot[0] = 0;
                yDot[1] = 0;
            }
        };
        
        double[] y0 = {0, 0};
        double[] y = new double[2];
        
        assertThrows(DimensionMismatchException.class, () -> {
            integrator.integrate(equations, 0, y0, 0, new double[3]);
        });
        
        assertThrows(DimensionMismatchException.class, () -> {
            integrator.integrate(equations, 0, y0, 0, new double[1]);
        });
        
        assertDoesNotThrow(() -> {
            integrator.integrate(equations, 0, y0, 0, y);
        });
    }
    
    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            protected void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {}
            public void integrate(ExpandableStatefulODE p0, double p1) {}
        };
        
        double t = 0;
        double[] y = {0, 0};
        double[] yDot = new double[2];
        
        assertDoesNotThrow(() -> {
            integrator.computeDerivatives(t, y, yDot);
        });
    }
}