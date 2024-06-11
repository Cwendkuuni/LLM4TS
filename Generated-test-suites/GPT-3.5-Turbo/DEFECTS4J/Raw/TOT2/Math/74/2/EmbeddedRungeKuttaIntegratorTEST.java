import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        assertEquals(4, integrator.getOrder());
    }
    
    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }
    
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.7);
        assertEquals(0.7, integrator.getSafety(), 0.0);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testNegativeSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(-0.1);
    }
    
    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }
    
    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.2);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testNegativeMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(-0.1);
    }
    
    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(2.0);
        assertEquals(2.0, integrator.getMaxGrowth(), 0.0);
    }
    
    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(3.0);
        assertEquals(3.0, integrator.getMaxGrowth(), 0.0);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testNegativeMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(-0.1);
    }

    private static class FakeEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
      
        public FakeEmbeddedRungeKuttaIntegrator() {
            super("Fake", false, new double[1], new double[1][1], new double[1], null, 1.0, 2.0, 0.1, 0.01);
        }

        @Override
        public int getOrder() {
            return 4;
        }

        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 0.0;
        }

        @Override
        protected void computeDerivatives(double t, double[] y, double[] yDot) {
            // do nothing
        }

        @Override
        protected double initializeStep(FirstOrderDifferentialEquations equations, boolean forward, int order, double[] scale, 
                                         double t, double[] y, double[] yDot0, double[] yTmp, double[] yDot1) {
            return 0.1;
        }
    }
}