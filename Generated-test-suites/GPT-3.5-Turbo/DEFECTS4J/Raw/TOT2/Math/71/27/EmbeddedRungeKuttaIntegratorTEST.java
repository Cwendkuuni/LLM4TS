import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    // Test case for getOrder()
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        assertEquals(4, integrator.getOrder());
    }

    // Test case for getSafety() and setSafety()
    @Test
    public void testGetSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0);
    }

    // Test case for getMinReduction() and setMinReduction()
    @Test
    public void testGetSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0);
    }

    // Test case for getMaxGrowth() and setMaxGrowth()
    @Test
    public void testGetSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0);
    }

    // Add more test cases for other methods...

}