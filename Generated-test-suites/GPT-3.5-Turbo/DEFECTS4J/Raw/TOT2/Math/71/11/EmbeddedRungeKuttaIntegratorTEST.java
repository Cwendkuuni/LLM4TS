import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        // Test getOrder method
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator(); // CustomEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator
        assertEquals(5, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        // Test getSafety method
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        // Test setSafety method
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }

    @Test
    public void testGetMinReduction() {
        // Test getMinReduction method
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        // Test setMinReduction method
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        // Test getMaxGrowth method
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        // Test setMaxGrowth method
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(12.0);
        assertEquals(12.0, integrator.getMaxGrowth(), 0.0);
    }

    // Add more test cases for other methods and edge cases

}