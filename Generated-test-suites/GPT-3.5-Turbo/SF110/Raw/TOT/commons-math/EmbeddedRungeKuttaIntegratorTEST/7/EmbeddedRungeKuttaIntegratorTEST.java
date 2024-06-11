import org.junit.Test;
import org.apache.commons.math3.ode.nonstiff.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        int result = integrator.getOrder();
        assertEquals(4, result);
    }
    
    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double result = integrator.getSafety();
        assertEquals(0.9, result, 0.0001);
    }
    
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        double result = integrator.getSafety();
        assertEquals(0.8, result, 0.0001);
    }
    
    @Test
    public void testIntegrate() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new TestFirstOrderDifferentialEquations());
        double t = 10.0;
        try {
            integrator.integrate(equations, t);
            double result = equations.getTime();
            assertEquals(t, result, 0.0001);
        } catch(Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrate_NumberIsTooSmallException() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new TestFirstOrderDifferentialEquations());
        double t = 0.0;
        integrator.integrate(equations, t);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_DimensionMismatchException() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new TestFirstOrderDifferentialEquationsInvalidDimension());
        double t = 10.0;
        integrator.integrate(equations, t);
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testIntegrate_MaxCountExceededException() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new TestFirstOrderDifferentialEquations());
        double t = 10.0;
        equations.setMaximalStep(1.0);
        integrator.integrate(equations, t);
    }
    
    @Test(expected = NoBracketingException.class)
    public void testIntegrate_NoBracketingException() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new TestFirstOrderDifferentialEquationsNoBracketing());
        double t = 10.0;
        integrator.integrate(equations, t);
    }
    
    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double result = integrator.getMinReduction();
        assertEquals(0.2, result, 0.0001);
    }
    
    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        double result = integrator.getMinReduction();
        assertEquals(0.1, result, 0.0001);
    }
    
    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double result = integrator.getMaxGrowth();
        assertEquals(10.0, result, 0.0001);
    }
    
    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(20.0);
        double result = integrator.getMaxGrowth();
        assertEquals(20.0, result, 0.0001);
    }
    
    @Test
    public void testEstimateError() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double[][] p0 = {{1.0, 2.0}, {3.0, 4.0}};
        double[] p1 = {5.0, 6.0};
        double[] p2 = {7.0, 8.0};
        double p3 = 0.1;
        double result = integrator.estimateError(p0, p1, p2, p3);
        assertEquals(0.0, result, 0.0001);
    }
}


// Test classes used for testing specific scenarios

class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
    
    public TestEmbeddedRungeKuttaIntegrator() {
        super("TestEmbeddedRungeKuttaIntegrator", true, new double[]{1.0, 2.0}, new double[][]{{3.0, 4.0}, {5.0, 6.0}}, new double[]{7.0, 8.0}, null, 0.1, 0.2, 0.3, 0.4);
    }
    
    public int getOrder() {
        return 4;
    }
    
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        // implement dummy computeDerivatives method
    }
    
    public RungeKuttaStepInterpolator prototype() {
        return null;
    }
    
    protected double initializeStep(boolean forward, int order, double[] scale, double t0, double[] y0, double[] yDot0, double[] y1, double[] yDot1) {
        return 0.1;
    }
    
    protected double filterStep(double stepSize, boolean forward, boolean acceptSmall) {
        return stepSize;
    }
    
    protected double stepSize;
    protected double stepStart;
    protected boolean isLastStep;
}

class TestFirstOrderDifferentialEquations implements FirstOrderDifferentialEquations {
    
    public int getDimension() {
        return 2;
    }
    
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -y[0];
    }
}

class TestFirstOrderDifferentialEquationsInvalidDimension implements FirstOrderDifferentialEquations {
    
    public int getDimension() {
        return 3;
    }
    
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -y[0];
    }
}

class TestFirstOrderDifferentialEquationsNoBracketing implements FirstOrderDifferentialEquations {
    
    public int getDimension() {
        return 2;
    }
    
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        throw new RuntimeException("NoBracketingException");
    }
}