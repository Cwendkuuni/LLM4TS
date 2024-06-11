import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new CustomIntegrator();
        assertEquals("CustomIntegrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new CustomIntegrator();
        StepHandler handler = new CustomStepHandler();
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new CustomIntegrator();
        StepHandler handler = new CustomStepHandler();
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new CustomIntegrator();
        EventHandler handler = new CustomEventHandler();
        integrator.addEventHandler(handler, 0.1, 1e-5, 100);
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new CustomIntegrator();
        EventHandler handler = new CustomEventHandler();
        integrator.addEventHandler(handler, 0.1, 1e-5, 100);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

}