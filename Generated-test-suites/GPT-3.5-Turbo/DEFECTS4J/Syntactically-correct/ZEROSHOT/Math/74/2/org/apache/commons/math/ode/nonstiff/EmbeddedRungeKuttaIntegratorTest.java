import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestIntegrator();
        integrator.setSafety(0.9);
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestIntegrator();
        integrator.setSafety(0.9);
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestIntegrator();
        integrator.setMinReduction(0.2);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestIntegrator();
        integrator.setMinReduction(0.2);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestIntegrator();
        integrator.setMaxGrowth(10.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestIntegrator();
        integrator.setMaxGrowth(10.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    private static class TestIntegrator extends EmbeddedRungeKuttaIntegrator {

        public TestIntegrator() {
            super("Test Integrator", false, new double[0], new double[0][0], new double[0],
                    null, 0.0, 0.0, 0.0, 0.0);
        }

        @Override
        public int getOrder() {
            return 0;
        }

        @Override
        public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 0.0;
        }

        @Override
        protected void computeDerivatives(double t, double[] y, double[] yDot) {
        }
    }
}