import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new TestIntegrator("Test");
        assertEquals("Test", integrator.getName());
    }
    
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new TestIntegrator("Test");
        StepHandler handler = new TestStepHandler();
        integrator.addStepHandler(handler);
        
        assertEquals(1, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(handler));
    }
    
    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new TestIntegrator("Test");
        StepHandler handler = new TestStepHandler();
        integrator.addStepHandler(handler);
        
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }
    
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new TestIntegrator("Test");
        EventHandler eventHandler = new TestEventHandler();
        integrator.addEventHandler(eventHandler, 0.1, 0.01, 10);
        
        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(eventHandler));
    }
    
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new TestIntegrator("Test");
        EventHandler eventHandler = new TestEventHandler();
        integrator.addEventHandler(eventHandler, 0.1, 0.01, 10);
        
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new TestIntegrator("Test");
        integrator.setMaxEvaluations(100);
        
        assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new TestIntegrator("Test");
        integrator.integrate(new TestEquations(), 0, new double[2], 10, new double[2]);
        
        assertEquals(1, integrator.getEvaluations());
    }
    
    @Test
    public void testSetEquations() {
        AbstractIntegrator integrator = new TestIntegrator("Test");
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new TestEquations());
        integrator.setEquations(equations);
        
        assertEquals(equations, integrator.getExpandable());
    }
    
    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new TestIntegrator("Test");
        double result = integrator.integrate(new TestEquations(), 0, new double[2], 10, new double[2]);
        
        assertEquals(10, result, 1e-15);
    }

    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new TestIntegrator("Test");
        double[] y = new double[2];
        double[] yDot = new double[2];
        integrator.computeDerivatives(0, y, yDot);
        
        assertArrayEquals(new double[]{0, 0}, yDot, 1e-15);
    }
    
    @Test
    public void testAcceptStep() {
        AbstractIntegrator integrator = new TestIntegrator("Test");
        double[] y = new double[2];
        double[] yDot = new double[2];
        AbstractStepInterpolator interpolator = new TestStepInterpolator();
        double result = integrator.acceptStep(interpolator, y, yDot, 10);
        
        assertEquals(10, result, 1e-15);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecks() {
        AbstractIntegrator integrator = new TestIntegrator("Test");
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new TestEquations());
        equations.setTime(0);
        double t = 1e-16;
        integrator.sanityChecks(equations, t);
    }
    
    private class TestIntegrator extends AbstractIntegrator {
        public TestIntegrator(String name) {
            super(name);
        }

        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // some implementation here
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
            // some implementation here
        }
    }
    
    private class TestStepHandler implements StepHandler {
        @Override
        public void init(double t0, double[] y0, double t) {
            // some implementation here
        }

        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLast) {
            // some implementation here
        }
    }
    
    private class TestEventHandler implements EventHandler {
        @Override
        public void init(double t0, double[] y0, double t) {
            // some implementation here
        }

        @Override
        public double g(double t, double[] y) {
            return 0;
        }

        @Override
        public Action eventOccurred(double t, double[] y, boolean increasing) {
            return Action.CONTINUE;
        }

        @Override
        public void resetState(double t, double[] y) {
            // some implementation here
        }
    }
    
    private class TestEquations implements FirstOrderDifferentialEquations {
        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            // some implementation here
        }

        @Override
        public int getDimension() {
            return 2;
        }
    }
    
    private class TestStepInterpolator implements AbstractStepInterpolator {
        @Override
        public double[] getInterpolatedState() {
            return new double[]{0, 0};
        }

        @Override
        public void setInterpolatedTime(double time) {
            // some implementation here
        }

        @Override
        protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
            // some implementation here
        }
    }
}