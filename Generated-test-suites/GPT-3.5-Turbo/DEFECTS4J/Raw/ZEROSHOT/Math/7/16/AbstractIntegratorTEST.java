import org.junit.Assert;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        Assert.assertNull(integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorTestHelper() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        AbstractIntegratorTestHelper.StepHandlerMock handler = new AbstractIntegratorTestHelper.StepHandlerMock();
        integrator.addStepHandler(handler);

        Assert.assertEquals(1, integrator.getStepHandlers().size());
        Assert.assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorTestHelper() {
            public void integrate(ExpandableStatefulODE equations, double t) {}
        };
        
        AbstractIntegratorTestHelper.StepHandlerMock handler = new AbstractIntegratorTestHelper.StepHandlerMock();
        integrator.addStepHandler(handler);
        
        Assert.assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        Assert.assertEquals(0, integrator.getStepHandlers().size());
    }

    private static class AbstractIntegratorTestHelper extends AbstractIntegrator {
        public void integrate(ExpandableStatefulODE equations, double t) {}

        private static class StepHandlerMock implements StepHandler {
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {}
            public void init(double t0, double[] y0, double t) {}
        }
    }

}