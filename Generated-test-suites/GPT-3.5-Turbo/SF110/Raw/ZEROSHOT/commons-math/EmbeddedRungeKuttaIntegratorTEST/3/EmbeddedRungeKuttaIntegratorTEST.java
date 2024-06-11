import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double safety = integrator.getSafety();
        assertEquals(0.9, safety, 0.0);
    }
    
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        double safety = integrator.getSafety();
        assertEquals(0.8, safety, 0.0);
    }
    
    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double minReduction = integrator.getMinReduction();
        assertEquals(0.2, minReduction, 0.0);
    }
    
    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.3);
        double minReduction = integrator.getMinReduction();
        assertEquals(0.3, minReduction, 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(10.0, maxGrowth, 0.0);
    }
    
    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(15.0);
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(15.0, maxGrowth, 0.0);
    }

    @Test
    public void testOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        int order = integrator.getOrder();
        assertEquals(0, order);
    }

    @Test
    public void testEstimateError() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double[][] yDotK = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] y2 = {1.0, 2.0};
        double[] yTmp = {3.0, 4.0};
        double stepSize = 0.1;
        double error = integrator.estimateError(yDotK, y2, yTmp, stepSize);
        assertEquals(0.0, error, 0.0);
    }

    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

        public TestEmbeddedRungeKuttaIntegrator() {
            super("Test Integrator", true, new double[]{0.1, 0.2}, new double[][]{{0.3, 0.4}, {0.5, 0.6}}, new double[]{0.7, 0.8}, null, 0.0, 0.0, 0.0, 0.0);
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