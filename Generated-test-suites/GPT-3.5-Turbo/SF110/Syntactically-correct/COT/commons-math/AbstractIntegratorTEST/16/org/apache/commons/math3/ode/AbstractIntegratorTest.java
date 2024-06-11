import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testName() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("Test");
        assertEquals("Test", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandler handler = new StepHandlerImpl();
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandler handler = new StepHandlerImpl();
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    // ... repeat the test cases for all the other methods
}

