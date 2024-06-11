import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {
    
    private AbstractIntegrator integrator;
    
    @Before
    public void setUp() {
        this.integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };
    }
    
    @Test
    public void testName() {
        assertEquals(null, this.integrator.getName());
    }
    
    @Test
    public void testAddStepHandler() {
        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {
                // do nothing
            }
            @Override
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }
        };
        this.integrator.addStepHandler(handler);
        Collection<StepHandler> handlers = this.integrator.getStepHandlers();
        assertTrue(handlers.contains(handler));
    }
    
    @Test
    public void testClearStepHandlers() {
        StepHandler handler1 = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {
                // do nothing
            }
            @Override
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }
        };
        StepHandler handler2 = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {
                // do nothing
            }
            @Override
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }
        };
        this.integrator.addStepHandler(handler1);
        this.integrator.addStepHandler(handler2);
        this.integrator.clearStepHandlers();
        Collection<StepHandler> handlers = this.integrator.getStepHandlers();
        assertTrue(handlers.isEmpty());
    }
    
    @Test
    public void testAddEventHandler() {
        EventHandler handler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }
            @Override
            public double g(double t, double[] y) {
                return 0.0;
            }
            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }
            @Override
            public void resetState(double t, double[] y) {
                // do nothing
            }
        };
        this.integrator.addEventHandler(handler, 0.1, 0.01, 10);
        Collection<EventHandler> handlers = this.integrator.getEventHandlers();
        assertTrue(handlers.contains(handler));
    }
    
    @Test
    public void testClearEventHandlers() {
        EventHandler handler1 = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }
            @Override
            public double g(double t, double[] y) {
                return 0.0;
            }
            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }
            @Override
            public void resetState(double t, double[] y) {
                // do nothing
            }
        };
        EventHandler handler2 = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }
            @Override
            public double g(double t, double[] y) {
                return 0.0;
            }
            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }
            @Override
            public void resetState(double t, double[] y) {
                // do nothing
            }
        };
        this.integrator.addEventHandler(handler1, 0.1, 0.01, 10);
        this.integrator.addEventHandler(handler2, 0.1, 0.01, 10);
        this.integrator.clearEventHandlers();
        Collection<EventHandler> handlers = this.integrator.getEventHandlers();
        assertTrue(handlers.isEmpty());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(-1, this.integrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        assertEquals(0, this.integrator.getEvaluations());
    }
    
    @Test
    public void testSetEquations() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // do nothing
            }
        });
        this.integrator.setEquations(equations);
        assertEquals(equations, this.integrator.getExpandable());
    }
    
    @Test
    public void testIntegrate() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // do nothing
            }
        };
        double[] y0 = {1.0};
        double[] y = new double[1];
        double t0 = 0.0;
        double t = 0.1;
        double result = this.integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, result, 0.0);
        assertEquals(y0[0], y[0], 0.0);
    }
    
    @Test
    public void testComputeDerivatives() throws MaxCountExceededException, DimensionMismatchException {
        double t = 0.0;
        double[] y = {1.0};
        double[] yDot = new double[1];
        this.integrator.computeDerivatives(t, y, yDot);
        assertEquals(1, this.integrator.getEvaluations());
    }
    
    @Test
    public void testSanityChecks() throws NumberIsTooSmallException, DimensionMismatchException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // do nothing
            }
        });
        equations.setTime(0.0);
        double t = 0.1;
        this.integrator.sanityChecks(equations, t);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecksException() throws NumberIsTooSmallException, DimensionMismatchException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // do nothing
            }
        });
        equations.setTime(0.0);
        double t = 0.0;
        this.integrator.sanityChecks(equations, t);
    }
}