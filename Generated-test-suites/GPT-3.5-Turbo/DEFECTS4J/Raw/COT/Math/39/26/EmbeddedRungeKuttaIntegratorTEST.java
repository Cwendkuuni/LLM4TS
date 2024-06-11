import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        // Create an instance of the class to be tested
        integrator = new EmbeddedRungeKuttaIntegratorImpl();
    }

    @Test
    public void testIntegrate() {
        // Test the integrate method
        assertEquals(0.0, integrator.integrate(0.0), 0.0001);
        assertEquals(1.0, integrator.integrate(1.0), 0.0001);
        assertEquals(2.0, integrator.integrate(2.0), 0.0001);
    }

    @Test
    public void testGetOrder() {
        // Test the getOrder method
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        // Test the getSafety method
        assertEquals(0.9, integrator.getSafety(), 0.0001);
    }

    @Test
    public void testSetSafety() {
        // Test the setSafety method
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0001);
    }

    @Test
    public void testGetMinReduction() {
        // Test the getMinReduction method
        assertEquals(0.2, integrator.getMinReduction(), 0.0001);
    }

    @Test
    public void testSetMinReduction() {
        // Test the setMinReduction method
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0001);
    }

    @Test
    public void testGetMaxGrowth() {
        // Test the getMaxGrowth method
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0001);
    }

    @Test
    public void testSetMaxGrowth() {
        // Test the setMaxGrowth method
        integrator.setMaxGrowth(20.0);
        assertEquals(20.0, integrator.getMaxGrowth(), 0.0001);
    }

}

