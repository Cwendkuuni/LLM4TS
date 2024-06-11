import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = mock(AbstractIntegrator.class);
        when(integrator.getName()).thenReturn("Test Integrator");
        
        assertEquals("Test Integrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler handler = mock(StepHandler.class);
        
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler handler = mock(StepHandler.class);
        
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    // Add more test cases for other methods

}