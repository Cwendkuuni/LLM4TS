import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("TestIntegrator");
        assertEquals("TestIntegrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("TestIntegrator");
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {}
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    // More test cases for other methods...

}

class AbstractIntegratorImpl extends AbstractIntegrator {
    public AbstractIntegratorImpl(String name) {
        super(name);
    }
}