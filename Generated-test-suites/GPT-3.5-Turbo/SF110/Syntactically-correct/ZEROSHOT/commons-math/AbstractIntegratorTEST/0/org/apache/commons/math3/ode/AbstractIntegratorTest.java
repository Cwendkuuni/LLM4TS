import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;

public class AbstractIntegratorTest {
    private AbstractIntegrator abstractIntegrator;
    
    @Before
    public void setUp() {
        abstractIntegrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };
    }
    
    @Test
    public void testGetName() {
        assertEquals(null, abstractIntegrator.getName());
    }
    
    @Test
    public void testAddStepHandler() {
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }
            
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                // do nothing
            }
        };
        abstractIntegrator.addStepHandler(handler);
        Collection<StepHandler> stepHandlers = abstractIntegrator.getStepHandlers();
        assertTrue(stepHandlers.contains(handler));
    }
    
    @Test
    public void testClearStepHandlers() {
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }
            
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                // do nothing
            }
        };
        abstractIntegrator.addStepHandler(handler);
        abstractIntegrator.clearStepHandlers();
        Collection<StepHandler> stepHandlers = abstractIntegrator.getStepHandlers();
        assertEquals(0, stepHandlers.size());
    }
    
    @Test
    public void testAddEventHandler() {
        EventHandler handler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) throws MaxCountExceededException {
                // do nothing
            }
            
            @Override
            public double g(double t, double[] y) throws MaxCountExceededException {
                return 0.0;
            }
            
            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) throws MaxCountExceededException {
                return Action.CONTINUE;
            }
            
            @Override
            public void resetState(double t, double[] y) throws MaxCountExceededException {
                // do nothing
            }
        };
        abstractIntegrator.addEventHandler(handler, 0.1, 0.01, 100);
        Collection<EventHandler> eventHandlers = abstractIntegrator.getEventHandlers();
        assertTrue(eventHandlers.contains(handler));
    }
    
    @Test
    public void testClearEventHandlers() {
        EventHandler handler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) throws MaxCountExceededException {
                // do nothing
            }
            
            @Override
            public double g(double t, double[] y) throws MaxCountExceededException {
                return 0.0;
            }
            
            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) throws MaxCountExceededException {
                return Action.CONTINUE;
            }
            
            @Override
            public void resetState(double t, double[] y) throws MaxCountExceededException {
                // do nothing
            }
        };
        abstractIntegrator.addEventHandler(handler, 0.1, 0.01, 100);
        abstractIntegrator.clearEventHandlers();
        Collection<EventHandler> eventHandlers = abstractIntegrator.getEventHandlers();
        assertEquals(0, eventHandlers.size());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(-1, abstractIntegrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        assertEquals(0, abstractIntegrator.getEvaluations());
    }
    
    @Test
    public void testSetEquations() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }
            
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // do nothing
            }
        });
        abstractIntegrator.setEquations(equations);
        assertEquals(equations, abstractIntegrator.getExpandable());
    }
    
    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }
            
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // do nothing
            }
        };
        double t0 = 0.0;
        double[] y0 = new double[] {0.0, 1.0};
        double t = 10.0;
        double[] y = new double[2];
        try {
            abstractIntegrator.integrate(equations, t0, y0, t, y);
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException should not be thrown");
        } catch (NumberIsTooSmallException e) {
            fail("NumberIsTooSmallException should not be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not be thrown");
        }
    }
    
    @Test
    public void testComputeDerivatives() {
        double t = 0.0;
        double[] y = new double[] {0.0, 1.0};
        double[] yDot = new double[2];
        try {
            abstractIntegrator.computeDerivatives(t, y, yDot);
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException should not be thrown");
        }
    }
}