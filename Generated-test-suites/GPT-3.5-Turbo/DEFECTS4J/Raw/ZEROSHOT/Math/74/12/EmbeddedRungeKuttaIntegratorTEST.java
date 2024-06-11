import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        // Create a sample EmbeddedRungeKuttaIntegrator for testing
        integrator = new EmbeddedRungeKuttaIntegrator() {
            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
        };
    }

    @Test
    public void testGetOrder() {
        // Test getOrder method
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        // Test getSafety method
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        // Test setSafety method
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }

    @Test
    public void testGetMinReduction() {
        // Test getMinReduction method
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        // Test setMinReduction method
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        // Test getMaxGrowth method
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        // Test setMaxGrowth method
        integrator.setMaxGrowth(20.0);
        assertEquals(20.0, integrator.getMaxGrowth(), 0.0);
    }
}