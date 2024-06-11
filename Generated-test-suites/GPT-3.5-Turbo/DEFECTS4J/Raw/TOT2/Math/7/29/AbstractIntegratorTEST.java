import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new TestIntegrator("Test Integrator");
        assertEquals("Test Integrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new TestIntegrator();
        StepHandler handler = new TestStepHandler();
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    // Add more test cases for other public methods

}

class TestIntegrator extends AbstractIntegrator {
    public TestIntegrator() {
        super();
    }

    public TestIntegrator(String name) {
        super(name);
    }

    public void integrate(ExpandableStatefulODE equations, double t) {
        // Implementation for testing purposes
    }
}

class TestStepHandler implements StepHandler {
    public void init(double t0, double[] y0, double t) {
        // Implementation for testing purposes
    }

    public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {
        // Implementation for testing purposes
    }
}