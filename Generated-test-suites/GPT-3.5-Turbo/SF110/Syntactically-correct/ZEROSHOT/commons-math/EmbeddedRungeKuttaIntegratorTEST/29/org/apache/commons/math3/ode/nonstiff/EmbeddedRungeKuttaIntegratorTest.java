import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorStub();
        assertEquals(0, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorStub();
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorStub();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }

    @Test
    public void testIntegrate() {
        // TODO: Write integration test
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorStub();
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorStub();
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorStub();
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorStub();
        integrator.setMaxGrowth(20.0);
        assertEquals(20.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testEstimateError() {
        // TODO: Write error estimation test
    }

    private class EmbeddedRungeKuttaIntegratorStub extends EmbeddedRungeKuttaIntegrator {

        public EmbeddedRungeKuttaIntegratorStub() {
            super("Stub", false, null, null, null, null, 0.0, 0.0, 0.0, 0.0);
        }

        @Override
        public int getOrder() {
            return 0;
        }

        @Override
        protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
            return 0.0;
        }

        @Override
        protected void computeDerivatives(double v, double[] doubles, double[] doubles1) {
            
        }
    }
}