import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(15.0);
        assertEquals(15.0, integrator.getMaxGrowth(), 0.0);
    }

    private static class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        public TestEmbeddedRungeKuttaIntegrator() {
            super("Test", true, new double[]{1.0}, new double[][]{{1.0}}, new double[]{1.0},
                    null, 0.1, 1.0, 0.01, 0.01);
        }

        @Override
        public int getOrder() {
            return 1;
        }

        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 0.0;
        }

        @Override
        protected void computeDerivatives(double t, double[] y, double[] yDot) {
            // Do nothing for testing purposes
        }

        @Override
        protected double initializeStep(FirstOrderDifferentialEquations equations, boolean forward, int order, double[] scale, double t, double[] y, double[] yDot0, double[] yTmp, double[] yDot1) {
            return 0.1;
        }
    }

}