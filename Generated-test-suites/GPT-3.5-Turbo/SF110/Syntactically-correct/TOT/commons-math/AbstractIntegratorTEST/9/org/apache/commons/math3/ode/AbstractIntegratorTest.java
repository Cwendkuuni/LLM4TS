import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {
    
    private AbstractIntegrator integrator;
    
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE expandableStatefulODE, double v) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                
            }
        };
    }
    
    @Test
    public void testGetName() {
        assertEquals(null, integrator.getName());
    }
    
    @Test
    public void testAddStepHandler() {
        StepHandlerMock stepHandler = new StepHandlerMock();
        integrator.addStepHandler(stepHandler);
        assertTrue(integrator.getStepHandlers().contains(stepHandler));
    }
    
    @Test
    public void testClearStepHandlers() {
        StepHandlerMock stepHandler = new StepHandlerMock();
        integrator.addStepHandler(stepHandler);
        integrator.clearStepHandlers();
        assertFalse(integrator.getStepHandlers().contains(stepHandler));
    }
    
    @Test
    public void testAddEventHandler() {
        EventHandlerMock eventHandler = new EventHandlerMock();
        double maxCheckInterval = 1.0;
        double convergence = 0.1;
        int maxIterationCount = 100;
        integrator.addEventHandler(eventHandler, maxCheckInterval, convergence, maxIterationCount);
        assertEquals(eventHandler, integrator.getEventHandlers().get(0));
    }
    
    @Test
    public void testClearEventHandlers() {
        EventHandlerMock eventHandler = new EventHandlerMock();
        double maxCheckInterval = 1.0;
        double convergence = 0.1;
        int maxIterationCount = 100;
        integrator.addEventHandler(eventHandler, maxCheckInterval, convergence, maxIterationCount);
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        assertEquals(0, integrator.getEvaluations());
    }
    
    @Test
    public void testInitIntegration() {
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        integrator.initIntegration(t0, y0, t);
        assertTrue(integrator.getEvaluationsCounter().getCount() == 0);
    }
    
    @Test
    public void testSetEquations() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        integrator.setEquations(equations);
        assertEquals(equations, integrator.getExpandable());
    }
    
    @Test
    public void testIntegrate() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        FirstOrderDifferentialEquationsMock equations = new FirstOrderDifferentialEquationsMock();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[2];
        double result = integrator.integrate(equations, t0, y0, t, y);
        assertTrue(result >= t0 && result <= t);
    }
    
    @Test
    public void testComputeDerivatives() throws MaxCountExceededException, DimensionMismatchException {
        double t = 0.0;
        double[] y = {1.0, 2.0};
        double[] yDot = new double[2];
        integrator.computeDerivatives(t, y, yDot);
        assertTrue(integrator.getEvaluationsCounter().getCount() > 0);
    }
    
    @Test
    public void testSanityChecks() throws NumberIsTooSmallException, DimensionMismatchException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        double t = 0.0;
        equations.setTime(t);
        exception.expect(NumberIsTooSmallException.class);
        exception.expectMessage(LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL.toString());
        integrator.sanityChecks(equations, t);
    }
    
    private static class EventHandlerMock implements EventHandler {
        public double g(double v, double[] doubles) throws MaxCountExceededException {
            return 0;
        }
        public Action eventOccurred(double v, double[] doubles, boolean b) throws MaxCountExceededException {
            return Action.CONTINUE;
        }
        public void resetState(double v, double[] doubles) throws MaxCountExceededException {
            
        }
    }
    
    private static class StepHandlerMock implements StepHandler {
        public void init(double v, double[] doubles, double v1) {
            
        }
        public void handleStep(StepInterpolator stepInterpolator, boolean b) throws MaxCountExceededException {
            
        }
    }

    private static class FirstOrderDifferentialEquationsMock implements FirstOrderDifferentialEquations {
        public int getDimension() {
            return 2;
        }
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            
        }
    }
}