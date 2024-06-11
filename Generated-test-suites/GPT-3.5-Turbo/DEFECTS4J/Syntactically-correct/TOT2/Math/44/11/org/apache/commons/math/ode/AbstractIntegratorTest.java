import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        assertEquals("ConcreteIntegrator", integrator.getName());
    }

    // Other test cases for public methods...

    @Test
    public void testMaxEvaluations() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testInvalidEvaluations() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        try {
            integrator.setMaxEvaluations(-10);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    // More edge cases and exception handling test cases...

}