import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.95);
        assertEquals(0.95, integrator.getSafety(), 0.0);
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
        integrator.setMaxGrowth(12.0);
        assertEquals(12.0, integrator.getMaxGrowth(), 0.0);
    }

}

class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
    public TestEmbeddedRungeKuttaIntegrator() {
        super("Test Integrator", false, new double[]{0.0, 1.0}, new double[][]{{0.0}, {0.5, 0.5}}, new double[]{1.0, 0.0},
                null, 0.0, 1.0, 0.01, 0.01);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        return 0.0;
    }
}