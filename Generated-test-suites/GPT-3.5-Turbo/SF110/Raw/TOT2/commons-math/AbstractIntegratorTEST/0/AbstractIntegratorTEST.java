import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator("Test Integrator");
        assertEquals("Test Integrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // Implementation not relevant for this test
            }
            
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {
                // Implementation not relevant for this test
            }
        };
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }
    
    // More test cases for other public methods can be added here

}