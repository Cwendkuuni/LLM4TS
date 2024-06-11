import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.9);
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.2);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(10.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    // Add more test cases for other methods...

    private class CustomEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        public CustomEmbeddedRungeKuttaIntegrator() {
            super("Custom", true, new double[]{0.5}, new double[][]{{0.5}}, new double[]{1.0}, null, 0.001, 1.0, 1e-6, 1e-6);
        }

        @Override
        public int getOrder() {
            return 4;
        }

        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 0.0;
        }
    }
}