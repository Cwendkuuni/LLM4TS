import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {
    
    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        assertEquals(null, integrator.getName());
        
        AbstractIntegrator integrator2 = new AbstractIntegrator("name") {
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        assertEquals("name", integrator2.getName());
    }
    
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        assertEquals(0, integrator.getStepHandlers().size());
        
        integrator.addStepHandler(new StepHandler() {
            public void init(double t0, double[] y0, double t) {
            }
            
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
            }
        });
        
        assertEquals(1, integrator.getStepHandlers().size());
    }
    
    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        StepHandler stepHandler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {
            }
            
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
            }
        };
        
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
        
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }
    
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        assertEquals(0, integrator.getEventHandlers().size());
        
        EventHandler eventHandler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {
            }
            
            public double g(double t, double[] y) {
                return 0.0;
            }
            
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }
            
            public void resetState(double t, double[] y) {
            }
        };
        
        integrator.addEventHandler(eventHandler, 1.0, 0.001, 100);
        
        assertEquals(1, integrator.getEventHandlers().size());
    }
    
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        EventHandler eventHandler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {
            }
            
            public double g(double t, double[] y) {
                return 0.0;
            }
            
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }
            
            public void resetState(double t, double[] y) {
            }
        };
        
        integrator.addEventHandler(eventHandler, 1.0, 0.001, 100);
        assertEquals(1, integrator.getEventHandlers().size());
        
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        integrator.setMaxEvaluations(100);
        
        assertEquals(100, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        assertEquals(0, integrator.getEvaluations());
    }
    
    @Test
    public void testGetExpandable() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        assertNull(integrator.getExpandable());
    }
    
    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        double[] y0 = new double[2];
        double[] y = new double[2];
        
        try {
            integrator.integrate(new FirstOrderDifferentialEquations() {
                public int getDimension() {
                    return 2;
                }
                
                public void computeDerivatives(double t, double[] y, double[] yDot) {
                }
            }, 0.0, y0, 1.0, y);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        double[] y = new double[2];
        double[] yDot = new double[2];
        
        try {
            integrator.computeDerivatives(0.0, y, yDot);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
}