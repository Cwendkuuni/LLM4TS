import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testConstructorWithScalTolerance() {
        // Test the constructor with scalar tolerance values
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator("Test", true,
                new double[]{0.2, 0.3}, new double[][]{{0.2}, {0.3, 0.4}}, new double[]{0.5, 0.6},
                new TestRungeKuttaStepInterpolator(), 1.0, 10.0, 0.01, 0.001);

        assertEquals("Test", integrator.getName());
        assertEquals(0.9, integrator.getSafety(), 0.0);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testGetOrder() {
        // Test the getOrder method
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator("Test", true,
                new double[]{0.2, 0.3}, new double[][]{{0.2}, {0.3, 0.4}}, new double[]{0.5, 0.6},
                new TestRungeKuttaStepInterpolator(), 1.0, 10.0, 0.01, 0.001);

        assertEquals(2, integrator.getOrder());
    }

    // Add more test cases for other methods in the EmbeddedRungeKuttaIntegrator class

    // Test edge cases and exceptions handling scenarios

}