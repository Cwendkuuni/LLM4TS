import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetSafety() {
        // Create an instance of EmbeddedRungeKuttaIntegrator
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        
        // Test the getSafety method
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        // Create an instance of EmbeddedRungeKuttaIntegrator
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        
        // Set the safety factor
        integrator.setSafety(0.8);
        
        // Test the setSafety method
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }

    @Test
    public void testGetMinReduction() {
        // Create an instance of EmbeddedRungeKuttaIntegrator
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        
        // Test the getMinReduction method
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        // Create an instance of EmbeddedRungeKuttaIntegrator
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        
        // Set the minimal reduction factor
        integrator.setMinReduction(0.1);
        
        // Test the setMinReduction method
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        // Create an instance of EmbeddedRungeKuttaIntegrator
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        
        // Test the getMaxGrowth method
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        // Create an instance of EmbeddedRungeKuttaIntegrator
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        
        // Set the maximal growth factor
        integrator.setMaxGrowth(15.0);
        
        // Test the setMaxGrowth method
        assertEquals(15.0, integrator.getMaxGrowth(), 0.0);
    }
}