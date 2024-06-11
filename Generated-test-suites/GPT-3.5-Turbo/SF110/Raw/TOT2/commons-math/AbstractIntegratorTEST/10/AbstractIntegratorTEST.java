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
                // Do nothing
            }
            
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {
                // Do nothing
            }
        };
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
    }
    
    // Add more test cases for other methods
    
}