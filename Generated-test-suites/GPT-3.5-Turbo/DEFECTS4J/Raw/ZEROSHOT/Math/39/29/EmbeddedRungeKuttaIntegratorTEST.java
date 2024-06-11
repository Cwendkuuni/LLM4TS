import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    // Create test object
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator();

    @Test
    public void testGetSafety() {
        double safety = integrator.getSafety();
        assertEquals(0.9, safety, 0.0001);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.8);
        double safety = integrator.getSafety();
        assertEquals(0.8, safety, 0.0001);
    }

    @Test
    public void testGetMinReduction() {
        double minReduction = integrator.getMinReduction();
        assertEquals(0.2, minReduction, 0.0001);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.1);
        double minReduction = integrator.getMinReduction();
        assertEquals(0.1, minReduction, 0.0001);
    }

    @Test
    public void testGetMaxGrowth() {
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(10.0, maxGrowth, 0.0001);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(20.0);
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(20.0, maxGrowth, 0.0001);
    }
}