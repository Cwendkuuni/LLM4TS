import org.junit.Test;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;

public class AbstractIntegratorTest {
    
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) {}
        };
        
        StepHandler handler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };
        
        integrator.addStepHandler(handler);
        
        Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
        
        assert(stepHandlers.contains(handler));
        
        integrator.clearStepHandlers();
        
        assert(stepHandlers.isEmpty());
    }
    
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) {}
        };
        
        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public Action eventOccurred(double t, double[] y, boolean increasing) { return Action.CONTINUE; }
            public double g(double t, double[] y) { return 0.0; }
            public void resetState(double t, double[] y) {}
        };
        
        integrator.addEventHandler(handler, 1.0, 0.01, 10);
        
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        
        assert(eventHandlers.contains(handler));
        
        integrator.clearEventHandlers();
        
        assert(eventHandlers.isEmpty());
    }
    
    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) {}
        };
        
        assert(integrator.getEvaluations() == 0);
        
        double[] y = {1.0, 2.0, 3.0};
        
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() { return 3; }
            public void computeDerivatives(double t, double[] y, double[] yDot) {}
        };
        
        try {
            integrator.integrate(equations, 0.0, y, 1.0, y);
        } catch (Exception e) {
            // ignore
        }
        
        assert(integrator.getEvaluations() == 1);
    }
    
    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) {}
        };
        
        integrator.setMaxEvaluations(100);
        
        assert(integrator.getMaxEvaluations() == 100);
    }
    
    @Test
    public void testGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) {}
        };
        
        integrator.integrate(null, 0.0);
        
        assert(integrator.getCurrentStepStart() == Double.NaN);
    }
    
    @Test
    public void testGetCurrentSignedStepsize() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) {}
        };
        
        integrator.integrate(null, 0.0);
        
        assert(integrator.getCurrentSignedStepsize() == Double.NaN);
    }
    
    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) {}
        };
        
        double[] y = {1.0, 2.0, 3.0};
        
        double[] result = new double[3];
        
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() { return 3; }
            public void computeDerivatives(double t, double[] y, double[] yDot) {}
        };
        
        try {
            integrator.integrate(equations, 0.0, y, 1.0, result);
        } catch (Exception e) {
            // ignore
        }
        
        assert(integrator.getCurrentStepStart() == Double.NaN);
    }
    
    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) {}
        };
        
        double[] y = {1.0, 2.0, 3.0};
        
        double[] yDot = new double[3];
        
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() { return 3; }
            public void computeDerivatives(double t, double[] y, double[] yDot) {}
        };
        
        integrator.computeDerivatives(0.0, y, yDot);
    }
}