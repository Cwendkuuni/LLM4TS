import org.junit.Test;
import org.junit.Assert;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new ConcreteIntegrator("Test Integrator");
        Assert.assertEquals("Test Integrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        StepHandler handler = new StepHandler();
        integrator.addStepHandler(handler);
        Assert.assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        StepHandler handler = new StepHandler();
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        Assert.assertTrue(integrator.getStepHandlers().isEmpty());
    }

    // More test cases for other methods should be added here

}