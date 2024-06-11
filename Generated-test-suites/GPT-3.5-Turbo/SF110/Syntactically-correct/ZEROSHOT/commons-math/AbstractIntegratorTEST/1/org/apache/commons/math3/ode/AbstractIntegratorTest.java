import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;

public class AbstractIntegratorTest {
    
    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    
    @Before
    public void setUp() {
        equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }
            
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + y[1];
                yDot[1] = -y[0];
            }
        };
        
        integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // dummy integration method
            }
        };
    }
    
    @Test
    public void testGetName() {
        Assert.assertNull(integrator.getName());
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
        Assert.assertEquals(1, integrator.getStepHandlers().size());
        Assert.assertTrue(integrator.getStepHandlers().contains(handler));
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
        Assert.assertEquals(0, integrator.getStepHandlers().size());
        Assert.assertFalse(integrator.getStepHandlers().contains(handler));
    }
    
    @Test
    public void testAddEventHandler() {
        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {
            }
            
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
            }
            
            public double g(double t, double[] y) {
                return t - y[0];
            }
            
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }
        };
        double maxCheckInterval = 5;
        double convergence = 1e-6;
        int maxIterationCount = 100;
        integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount);
        Assert.assertEquals(1, integrator.getEventHandlers().size());
        Assert.assertTrue(integrator.getEventHandlers().contains(handler));
    }
    
    @Test
    public void testClearEventHandlers() {
        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {
            }
            
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
            }
            
            public double g(double t, double[] y) {
                return t - y[0];
            }
            
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }
        };
        double maxCheckInterval = 5;
        double convergence = 1e-6;
        int maxIterationCount = 100;
        integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount);
        integrator.clearEventHandlers();
        Assert.assertEquals(0, integrator.getEventHandlers().size());
        Assert.assertFalse(integrator.getEventHandlers().contains(handler));
    }
    
    @Test
    public void testGetMaxEvaluations() {
        Assert.assertEquals(-1, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        Assert.assertEquals(0, integrator.getEvaluations());
    }
    
    @Test
    public void testInitIntegration() {
        double t0 = 0;
        double[] y0 = new double[]{0, 1};
        double t = 1;
        
        integrator.initIntegration(t0, y0, t);
        
        Assert.assertEquals(0, integrator.getEvaluations());
    }
    
    @Test
    public void testIntegrate() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        double t0 = 0;
        double[] y0 = new double[]{0, 1};
        double t = 1;
        double[] y = new double[2];
        
        double result = integrator.integrate(equations, t0, y0, t, y);
        
        Assert.assertEquals(t, result, 0.001);
    }
    
    @Test
    public void testComputeDerivatives() throws DimensionMismatchException, MaxCountExceededException {
        double t = 0;
        double[] y = new double[]{0, 1};
        double[] yDot = new double[2];
        
        integrator.computeDerivatives(t, y, yDot);
        
        Assert.assertEquals(1, yDot[0], 0.001);
        Assert.assertEquals(-0, yDot[1], 0.001);
    }
    
    @Test
    public void testSetStateInitialized() {
        integrator.setStateInitialized(true);
        
        Assert.assertEquals(true, integrator.getStateInitialized());
    }
    
    @Test
    public void testAcceptStep() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            public double getGlobalPreviousTime() {
                return 0;
            }
            
            public double getGlobalCurrentTime() {
                return 1;
            }
            
            public boolean isForward() {
                return true;
            }
            
            public double getInterpolatedTime() {
                return 0;
            }
            
            public double[] getInterpolatedState() throws MaxCountExceededException {
                return new double[]{0, 1};
            }
            
            public double getSoftPreviousTime() {
                return 0;
            }
            
            public double getSoftCurrentTime() {
                return 1;
            }
        };
        interpolator.setInterpolatedTime(0.5);
        double[] y = new double[]{0, 1};
        double[] yDot = new double[2];
        double tEnd = 1;
        
        double result = integrator.acceptStep(interpolator, y, yDot, tEnd);
        
        Assert.assertEquals(1, result, 0.001);
    }
    
    @Test
    public void testSanityChecks() throws NumberIsTooSmallException, DimensionMismatchException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(this.equations) {
            public double getTime() {
                return 0;
            }
        };
        double t = 1;
        
        integrator.sanityChecks(equations, t);
    }
}