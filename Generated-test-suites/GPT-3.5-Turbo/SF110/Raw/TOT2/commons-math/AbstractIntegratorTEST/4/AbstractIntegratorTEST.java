import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        // Test typical case
        AbstractIntegrator integrator = new ConcreteIntegrator("Test Integrator");
        assertEquals("Test Integrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        // Test adding a step handler
        AbstractIntegrator integrator = new ConcreteIntegrator();
        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                // Custom implementation for testing
            }
        };
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    // Additional test cases for other methods...

}