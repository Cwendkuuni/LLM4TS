import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new TestIntegrator();
        assertEquals("Test Integrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new TestIntegrator();
        StepHandler stepHandler = new TestStepHandler();
        assertEquals(0, integrator.getStepHandlers().size());
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(stepHandler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new TestIntegrator();
        StepHandler stepHandler = new TestStepHandler();
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new TestIntegrator();
        EventHandler eventHandler = new TestEventHandler();
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 100;
        assertEquals(0, integrator.getEventHandlers().size());
        integrator.addEventHandler(eventHandler, maxCheckInterval, convergence, maxIterationCount);
        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new TestIntegrator();
        EventHandler eventHandler = new TestEventHandler();
        integrator.addEventHandler(eventHandler, 0.01, 0.001, 100);
        assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new TestIntegrator();
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new TestIntegrator();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new TestIntegrator();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0, 3.0};
        double t = 1.0;
        integrator.initIntegration(t0, y0, t);
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new TestIntegrator();
        int maxEvaluations = 500;
        integrator.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, integrator.getMaxEvaluations());
    }

    @Test
    public void testIntegrate() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new TestIntegrator();
        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0, 3.0};
        double t = 1.0;
        double[] y = new double[3];
        double result = integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, result, 0.0001);
        for (int i = 0; i < y0.length; i++) {
            assertEquals(y0[i], y[i], 0.0001);
        }
    }

    @Test
    public void testComputeDerivatives() throws MaxCountExceededException, DimensionMismatchException {
        AbstractIntegrator integrator = new TestIntegrator();
        double t = 0.0;
        double[] y = {1.0, 2.0, 3.0};
        double[] yDot = new double[3];
        integrator.computeDerivatives(t, y, yDot);
        for (int i = 0; i < y.length; i++) {
            assertEquals(0.0, yDot[i], 0.0001);
        }
    }

    @Test
    public void testAcceptStep() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
        AbstractIntegrator integrator = new TestIntegrator();
        double[] y = {1.0, 2.0, 3.0};
        double[] yDot = new double[3];
        double tEnd = 1.0;

        AbstractStepInterpolator interpolator = new TestStepInterpolator();
        interpolator.setGlobalPreviousTime(0.0);
        interpolator.setGlobalCurrentTime(1.0);
        interpolator.setForward(true);
        
        double result = integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(1.0, result, 0.0001);
        assertFalse(((TestIntegrator)integrator).getResetOccurred());
        assertFalse(((TestIntegrator)integrator).getIsLastStep());
    }

    @Test
    public void testSanityChecks() throws NumberIsTooSmallException, DimensionMismatchException {
        AbstractIntegrator integrator = new TestIntegrator();
        ExpandableStatefulODE equations = new TestEquations();
        double t = 0.0;
        try {
            integrator.sanityChecks(equations, t);
            fail("NumberIsTooSmallException not caught");
        } catch (NumberIsTooSmallException e) {
            // expected behavior
        }
        t = 1.0;
        integrator.sanityChecks(equations, t);
    }

    private class TestIntegrator extends AbstractIntegrator {
        @Override
        public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // do nothing in test
        }
        
        public boolean getResetOccurred() {
            return resetOccurred;
        }

        public boolean getIsLastStep() {
            return isLastStep;
        }
    }

    private class TestStepHandler implements StepHandler {
        @Override
        public void init(double t0, double[] y0, double t) {
            // do nothing in test
        }

        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLast) {
            // do nothing in test
        }
    }

    private class TestEventHandler implements EventHandler {
        @Override
        public void init(double t0, double[] y0, double t) {
            // do nothing in test
        }

        @Override
        public double g(double t, double[] y) {
            return 0.0; // no events in test
        }

        @Override
        public Action eventOccurred(double t, double[] y, boolean increasing) {
            return Action.CONTINUE; // no events in test
        }

        @Override
        public void resetState(double t, double[] y) {
            // do nothing in test
        }
    }

    private class TestStepInterpolator extends AbstractStepInterpolator {
        @Override
        protected StepInterpolator doCopy() {
            return null; // not used in tests
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            // do nothing in test
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            // do nothing in test
        }

        @Override
        protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
            // do nothing in test
        }
    }

    private class TestEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 3;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
            for (int i = 0; i < y.length; i++) {
                yDot[i] = 0.0; // no derivatives in test
            }
        }
    }
}