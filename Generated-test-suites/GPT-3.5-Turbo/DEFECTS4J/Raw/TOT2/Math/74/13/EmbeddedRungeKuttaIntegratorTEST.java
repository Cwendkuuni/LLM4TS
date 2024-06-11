import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testConstructor() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertNotNull(integrator);
        assertEquals(0.9, integrator.getSafety(), 0.0);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(20.0);
        assertEquals(20.0, integrator.getMaxGrowth(), 0.0);
    }

    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

        public TestEmbeddedRungeKuttaIntegrator() {
            super("Test", true, new double[]{0.0, 0.5, 0.5, 1.0}, new double[][]{{}, {0.5}, {-0.5, 2.0}, {0.0, 0.0, 1.0}},
                    new double[]{1.0 / 6.0, 0.0, 2.0 / 6.0, 1.0 / 6.0}, null, 0.01, 10.0, 1e-8, 1e-8);
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