import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testConstructorWithName() {
        AbstractIntegrator integrator = new TestIntegrator("TestIntegrator");

        assertEquals("TestIntegrator", integrator.getName());
    }

    @Test
    public void testConstructorWithoutName() {
        AbstractIntegrator integrator = new TestIntegrator();

        assertNull(integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new TestIntegrator();
        StepHandler handler = new TestStepHandler();

        integrator.addStepHandler(handler);

        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new TestIntegrator();
        StepHandler handler = new TestStepHandler();
        integrator.addStepHandler(handler);

        integrator.clearStepHandlers();

        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new TestIntegrator();
        EventHandler handler = new TestEventHandler();

        integrator.addEventHandler(handler, 0.1, 1e-6, 100);

        assertTrue(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new TestIntegrator();
        EventHandler handler = new TestEventHandler();
        integrator.addEventHandler(handler, 0.1, 1e-6, 100);

        integrator.clearEventHandlers();

        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new TestIntegrator();

        integrator.setMaxEvaluations(100);

        assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new TestIntegrator();

        assertEquals(0, integrator.getEvaluations());
    }
}