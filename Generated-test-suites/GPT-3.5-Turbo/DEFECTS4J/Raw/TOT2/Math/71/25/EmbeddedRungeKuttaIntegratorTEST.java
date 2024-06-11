import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        double expectedSafety = 0.9;
        assertEquals(expectedSafety, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        double newSafety = 0.85;
        integrator.setSafety(newSafety);
        assertEquals(newSafety, integrator.getSafety(), 0.0);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        double expectedMinReduction = 0.2;
        assertEquals(expectedMinReduction, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        double newMinReduction = 0.15;
        integrator.setMinReduction(newMinReduction);
        assertEquals(newMinReduction, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        double expectedMaxGrowth = 10.0;
        assertEquals(expectedMaxGrowth, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        double newMaxGrowth = 12.0;
        integrator.setMaxGrowth(newMaxGrowth);
        assertEquals(newMaxGrowth, integrator.getMaxGrowth(), 0.0);
    }
}