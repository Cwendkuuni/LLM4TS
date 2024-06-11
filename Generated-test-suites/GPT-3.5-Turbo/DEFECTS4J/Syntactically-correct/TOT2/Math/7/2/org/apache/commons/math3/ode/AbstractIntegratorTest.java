import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new TestIntegrator();
        assertEquals("Test Integrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new TestIntegrator();
        StepHandler stepHandler = new TestStepHandler();
        integrator.addStepHandler(stepHandler);
        assertTrue(integrator.getStepHandlers().contains(stepHandler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new TestIntegrator();
        StepHandler stepHandler = new TestStepHandler();
        integrator.addStepHandler(stepHandler);
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new TestIntegrator();
        EventHandler eventHandler = new TestEventHandler();
        integrator.addEventHandler(eventHandler, 0.1, 0.01, 10);
        assertTrue(integrator.getEventHandlers().contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new TestIntegrator();
        EventHandler eventHandler = new TestEventHandler();
        integrator.addEventHandler(eventHandler, 0.1, 0.01, 10);
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
        integrator.setMaxEvaluations(100);
        integrator.integrate(new TestEquations(), 0.0, new double[]{1.0}, 1.0, new double[1]);
        assertEquals(101, integrator.getEvaluations());
    }

    @Test
    public void testIntegration() {
        AbstractIntegrator integrator = new TestIntegrator();
        FirstOrderDifferentialEquations equations = new TestEquations();
        double[] y0 = {1.0};
        double[] y = new double[1];
        double t = integrator.integrate(equations, 0.0, y0, 1.0, y);
        assertEquals(1.0, t, 0.0);
        assertEquals(2.0, y[0], 0.0);
    }

    private static class TestIntegrator extends AbstractIntegrator {
        public TestIntegrator() {
            super("Test Integrator");
        }
        public void integrate(ExpandableStatefulODE equations, double t) {
            double[] y = equations.getPrimaryState();
            y[0] = 2.0;
        }
    }

    private static class TestStepHandler implements StepHandler {
        public void init(double t0, double[] y0, double t) {}
        public void handleStep(StepInterpolator interpolator, boolean isLast) {}
    }

    private static class TestEventHandler implements EventHandler {
        public void init(double t0, double[] y0, double t) {}
        public Action eventOccurred(double t, double[] y, boolean increasing) {
            return Action.CONTINUE;
        }
        public double g(double t, double[] y) {
            return 0.0;
        }
    }

    private static class TestEquations implements FirstOrderDifferentialEquations {
        public int getDimension() {
            return 1;
        }
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = 1.0;
        }
    }

}