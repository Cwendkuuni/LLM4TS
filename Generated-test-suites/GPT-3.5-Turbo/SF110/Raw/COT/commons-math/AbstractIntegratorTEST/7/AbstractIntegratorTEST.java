import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.exception.*;

public class AbstractIntegratorTest {
    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new ConcreteIntegrator("test");
        Assert.assertEquals("test", integrator.getName());
    }
    
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        StepHandler handler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(handler);
        Assert.assertEquals(1, integrator.getStepHandlers().size());
    }
    
    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        StepHandler handler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        Assert.assertEquals(0, integrator.getStepHandlers().size());
    }
    
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) { return 0; }
        };
        integrator.addEventHandler(handler, 0.1, 1.0e-6, 10);
        Assert.assertEquals(1, integrator.getEventHandlers().size());
    }
    
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) { return 0; }
        };
        integrator.addEventHandler(handler, 0.1, 1.0e-6, 10);
        integrator.clearEventHandlers();
        Assert.assertEquals(0, integrator.getEventHandlers().size());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        Assert.assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() { return 1; }
            public void computeDerivatives(double t, double[] y, double[] yDot) {}
        };
        double t0 = 0.0;
        double[] y0 = new double[]{1.0};
        double t = 10.0;
        double[] y = new double[1];
        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (Exception e) {
            Assert.fail("An exception should not be thrown");
        }
    }
    
    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        double t = 0.0;
        double[] y = new double[]{1.0};
        double[] yDot = new double[1];
        try {
            integrator.computeDerivatives(t, y, yDot);
        } catch (Exception e) {
            Assert.fail("An exception should not be thrown");
        }
    }
    
    private class ConcreteIntegrator extends AbstractIntegrator {
        public ConcreteIntegrator(String name) { super(name); }
        public ConcreteIntegrator() { super(); }
        
        public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {}
    }
}
