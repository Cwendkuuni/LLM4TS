import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0, integrator.getOrder());
    }
    
    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0.9, integrator.getSafety(), 0.001);
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
    }
    
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
    }
    
    @Test
    public void testIntegrate() {
        // Add your test code here
    }
    
    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0.2, integrator.getMinReduction(), 0.001);
    }
    
    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.3);
        assertEquals(0.3, integrator.getMinReduction(), 0.001);
    }
    
    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
    }
    
    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(12.0);
        assertEquals(12.0, integrator.getMaxGrowth(), 0.001);
    }
    
    @Test
    public void testEstimateError() {
        // Add your test code here
    }
    
    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        
        public TestEmbeddedRungeKuttaIntegrator() {
            super("test", false, new double[]{0.0}, new double[][]{{0.0}}, new double[]{0.0}, null, 0.0, 1.0, 0.0, 0.0);
        }
        
        @Override
        public int getOrder() {
            return 0;
        }
        
        @Override
        protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
            return 0.0;
        }
        
    }
}