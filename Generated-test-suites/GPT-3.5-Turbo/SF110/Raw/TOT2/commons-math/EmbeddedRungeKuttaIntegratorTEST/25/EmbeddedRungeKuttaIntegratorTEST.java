import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestableEmbeddedRungeKuttaIntegrator();
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestableEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestableEmbeddedRungeKuttaIntegrator();
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestableEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestableEmbeddedRungeKuttaIntegrator();
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestableEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(15.0);
        assertEquals(15.0, integrator.getMaxGrowth(), 0.0);
    }

    private static class TestableEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        @Override
        public int getOrder() {
            return 4;
        }

        @Override
        protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
            return 0.0;
        }
    }
}