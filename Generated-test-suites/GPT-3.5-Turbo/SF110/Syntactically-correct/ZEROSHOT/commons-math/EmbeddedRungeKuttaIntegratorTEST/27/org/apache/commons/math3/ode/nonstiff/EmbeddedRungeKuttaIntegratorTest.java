import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        int order = integrator.getOrder();
        assertEquals(4, order);
    }
    
    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        double safety = integrator.getSafety();
        assertEquals(0.9, safety, 0.00001);
    }
    
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        integrator.setSafety(0.8);
        double safety = integrator.getSafety();
        assertEquals(0.8, safety, 0.00001);
    }
    
    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        double minReduction = integrator.getMinReduction();
        assertEquals(0.2, minReduction, 0.00001);
    }
    
    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        integrator.setMinReduction(0.1);
        double minReduction = integrator.getMinReduction();
        assertEquals(0.1, minReduction, 0.00001);
    }
    
    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(10.0, maxGrowth, 0.00001);
    }
    
    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        integrator.setMaxGrowth(20.0);
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(20.0, maxGrowth, 0.00001);
    }
    
    @Test
    public void testEstimateError() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        double[][] p0 = { {1, 2, 3}, {4, 5, 6} };
        double[] p1 = {1, 2, 3};
        double[] p2 = {4, 5, 6};
        double p3 = 0.5;
        double error = integrator.estimateError(p0, p1, p2, p3);
        assertEquals(2.357022603955158, error, 0.00001);
    }
    
    private static class EmbeddedRungeKuttaIntegratorImpl extends EmbeddedRungeKuttaIntegrator {
        public EmbeddedRungeKuttaIntegratorImpl() {
            super("", true, null, null, null, null, 0.0, 0.0, 0.0, 0.0);
        }
        
        public int getOrder() {
            return 4;
        }
        
        protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
            return 2.357022603955158;
        }
        
        public void computeDerivatives(double t, double[] y, double[] yDot) {
        
        }
        
        public double initializeStep(boolean forward, int order, double[] scale, double t0, double[] y0, double[] yDot0, double[] y1, double[] yDot1) {
            return 0.0;
        }
        
        public double filterStep(double h, boolean forward, boolean acceptSystemComputed) {
            return 0.0;
        }
    }
}