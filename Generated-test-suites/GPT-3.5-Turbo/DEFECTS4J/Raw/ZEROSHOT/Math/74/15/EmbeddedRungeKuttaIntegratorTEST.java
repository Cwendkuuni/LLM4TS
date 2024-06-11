import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    EmbeddedRungeKuttaIntegrator embeddedRungeKuttaIntegrator;

    @Before
    public void setUp() {
        embeddedRungeKuttaIntegrator = new EmbeddedRungeKuttaIntegrator() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
        };
    }

    @Test
    public void testIntegrate() throws Exception {
        embeddedRungeKuttaIntegrator.integrate(null, 0, null, 0, null);
    }

    @Test
    public void testGetSafety() throws Exception {
        embeddedRungeKuttaIntegrator.getSafety();
    }

    @Test
    public void testSetSafety() throws Exception {
        embeddedRungeKuttaIntegrator.setSafety(0.3);
    }

    @Test
    public void testGetMinReduction() throws Exception {
        embeddedRungeKuttaIntegrator.getMinReduction();
    }

    @Test
    public void testSetMinReduction() throws Exception {
        embeddedRungeKuttaIntegrator.setMinReduction(0.1);
    }

    @Test
    public void testGetMaxGrowth() throws Exception {
        embeddedRungeKuttaIntegrator.getMaxGrowth();
    }

    @Test
    public void testSetMaxGrowth() throws Exception {
        embeddedRungeKuttaIntegrator.setMaxGrowth(2.2);
    }
}