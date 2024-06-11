import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new ConcreteIntegrator("Test Integrator");
        assertEquals("Test Integrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        StepHandler handler = new TestStepHandler();
        AbstractIntegrator integrator = new ConcreteIntegrator();
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        StepHandler handler = new TestStepHandler();
        AbstractIntegrator integrator = new ConcreteIntegrator();
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testGetStepHandlers() {
        StepHandler handler1 = new TestStepHandler();
        StepHandler handler2 = new TestStepHandler();
        AbstractIntegrator integrator = new ConcreteIntegrator();
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        assertEquals(2, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new TestEventHandler();
        AbstractIntegrator integrator = new ConcreteIntegrator();
        integrator.addEventHandler(eventHandler, 0.1, 0.01, 100);
        assertTrue(integrator.getEventHandlers().contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        EventHandler eventHandler = new TestEventHandler();
        AbstractIntegrator integrator = new ConcreteIntegrator();
        integrator.addEventHandler(eventHandler, 0.1, 0.01, 100);
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetCurrentStepStart() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0.0);
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        double t = 0.0;
        double[] y = {1.0, 2.0};
        double[] yDot = new double[2];
        try {
            integrator.computeDerivatives(t, y, yDot);
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException thrown");
        }
    }

    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new ConcreteIntegrator();
        FirstOrderDifferentialEquations equations = new TestDifferentialEquations();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[2];
        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (MathIllegalStateException | MathIllegalArgumentException e) {
            fail("Exception thrown");
        }
    }

}