import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
        assertEquals("test", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandler handler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandler handler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public Action eventOccurred(double t, double[] y, boolean increasing) { return Action.CONTINUE; }
            public void resetState(double t, double[] y) {}
        };
        integrator.addEventHandler(handler, 0.0, 0.0, 0);
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public Action eventOccurred(double t, double[] y, boolean increasing) { return Action.CONTINUE; }
            public void resetState(double t, double[] y) {}
        };
        integrator.addEventHandler(handler, 0.0, 0.0, 0);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        assertEquals(0, integrator.getEvaluations());
    }

    private class AbstractIntegratorImpl extends AbstractIntegrator {
        public AbstractIntegratorImpl(String name) {
            super(name);
        }
        public void integrate(ExpandableStatefulODE p0, double p1) {}
        public void computeDerivatives(double t, double[] y, double[] yDot) {}
    }

}