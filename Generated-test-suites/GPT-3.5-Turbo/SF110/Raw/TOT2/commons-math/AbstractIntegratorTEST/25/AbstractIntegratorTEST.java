import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {
    
    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        assertEquals("TestIntegrator", integrator.getName());
    }
    
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // Initialization code
            }
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                // Step handling code
            }
        };
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    // Add more test cases for other methods
    
}