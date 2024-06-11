import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        assertEquals("Test Integrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        StepHandler handler = new TestStepHandler();
        integrator.addStepHandler(handler);
        assertEquals(handler, integrator.getStepHandlers().iterator().next());
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        StepHandler handler = new TestStepHandler();
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }
    
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        EventHandler handler = new TestEventHandler();
        integrator.addEventHandler(handler, 0.1, 0.01, 100);
        assertEquals(handler, integrator.getEventHandlers().iterator().next());
    }
    
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        EventHandler handler = new TestEventHandler();
        integrator.addEventHandler(handler, 0.1, 0.01, 100);
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        assertEquals(-1, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0, 3.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        
        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
            assertEquals(t, result, 0.000001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testIntegrateDimensionMismatchException() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        
        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
            fail("Expected DimensionMismatchException not thrown");
        } catch (DimensionMismatchException e) {
            assertEquals("Dimension mismatch: expected 2, got 3", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testIntegrateNumberIsTooSmallException() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0, 3.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        
        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
            fail("Expected NumberIsTooSmallException not thrown");
        } catch (NumberIsTooSmallException e) {
            assertEquals("Integration interval is too small: expected > 0.0, was 0.0", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        double t = 0.0;
        double[] y = {1.0, 2.0, 3.0};
        double[] yDot = new double[y.length];
        
        try {
            integrator.computeDerivatives(t, y, yDot);
            assertArrayEquals(new double[] {-1.0, -2.0, -3.0}, yDot, 0.000001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSanityChecks() {
        AbstractIntegrator integrator = new TestAbstractIntegrator("Test Integrator");
        ExpandableStatefulODE equations = new TestExpandableStatefulODE();
        double t = 0.0;
        
        try {
            integrator.sanityChecks(equations, t);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    private class TestAbstractIntegrator extends AbstractIntegrator {

        public TestAbstractIntegrator(String name) {
            super(name);
        }

        @Override
        public void integrate(ExpandableStatefulODE equations, double t) {
            // implementation for testing purposes
        }
    }

    private class TestStepHandler implements StepHandler {

        @Override
        public void init(double t0, double[] y0, double t) {
        }

        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLast) {
        }
    }
    
    private class TestEventHandler implements EventHandler {

        @Override
        public void init(double t0, double[] y0, double t) {
        }

        @Override
        public double g(double t, double[] y) {
            return 0.0;
        }

        @Override
        public Action eventOccurred(double t, double[] y, boolean increasing) {
            return null;
        }

        @Override
        public void resetState(double t, double[] y) {
        }
    }
    
    private class TestEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 3;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            for (int i = 0; i < y.length; i++) {
                yDot[i] = -y[i];
            }
        }
    }
    
    private class TestExpandableStatefulODE extends ExpandableStatefulODE {

        public TestExpandableStatefulODE() {
            super(new TestEquations());
        }
    }
}