import org.junit.*;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testConstructorWithName() {
        AbstractIntegrator integrator = new AbstractIntegrator("Test");
        assertEquals("Test", integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
        assertTrue(Double.isNaN(integrator.getCurrentStepStart()));
    }

    @Test
    public void testConstructorWithoutName() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNull(integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
        assertTrue(Double.isNaN(integrator.getCurrentStepStart()));
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler handler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(org.apache.commons.math3.ode.AbstractStepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler handler1 = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(org.apache.commons.math3.ode.AbstractStepInterpolator interpolator, boolean isLast) {}
        };
        StepHandler handler2 = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(org.apache.commons.math3.ode.AbstractStepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        assertEquals(2, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) { return 0; }
            public Action eventOccurred(double t, double[] y, boolean increasing) { return null; }
            public void resetState(double t, double[] y) {}
        };
        integrator.addEventHandler(handler, 1, 0.001, 10);
        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        EventHandler handler1 = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) { return 0; }
            public Action eventOccurred(double t, double[] y, boolean increasing) { return null; }
            public void resetState(double t, double[] y) {}
        };
        EventHandler handler2 = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) { return 0; }
            public Action eventOccurred(double t, double[] y, boolean increasing) { return null; }
            public void resetState(double t, double[] y) {}
        };
        integrator.addEventHandler(handler1, 1, 0.001, 10);
        integrator.addEventHandler(handler2, 1, 0.001, 10);
        assertEquals(2, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) {}
        };
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() { return 2; }
            public void computeDerivatives(double t, double[] y, double[] yDot) {}
        };
        double[] y0 = {0, 0};
        double[] y = new double[2];
        double result = 0;
        try {
            result = integrator.integrate(equations, 0, y0, 1, y);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(0, result, 0);
        assertEquals(2, y.length);
        assertEquals(0, y[0], 0);
        assertEquals(0, y[1], 0);
    }

    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) {}
        };
        double[] y = {0, 0};
        double[] yDot = new double[2];
        try {
            integrator.computeDerivatives(0, y, yDot);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(2, yDot.length);
        assertEquals(0, yDot[0], 0);
        assertEquals(0, yDot[1], 0);
    }
}