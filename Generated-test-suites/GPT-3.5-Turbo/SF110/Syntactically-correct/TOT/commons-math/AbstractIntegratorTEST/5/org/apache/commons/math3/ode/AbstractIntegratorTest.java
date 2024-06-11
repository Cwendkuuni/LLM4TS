import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setup() {
        integrator = new AbstractIntegratorMock();
    }

    @Test
    public void testGetName() {
        assertEquals("AbstractIntegrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        StepHandler stepHandler = new StepHandlerMock();
        integrator.addStepHandler(stepHandler);
        assertTrue(integrator.getStepHandlers().contains(stepHandler));
    }

    @Test
    public void testClearStepHandlers() {
        StepHandler stepHandler = new StepHandlerMock();
        integrator.addStepHandler(stepHandler);
        integrator.clearStepHandlers();
        assertFalse(integrator.getStepHandlers().contains(stepHandler));
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandlerMock();
        integrator.addEventHandler(eventHandler, 0.1, 0.001, 100);
        assertTrue(integrator.getEventHandlers().contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        EventHandler eventHandler = new EventHandlerMock();
        integrator.addEventHandler(eventHandler, 0.1, 0.001, 100);
        integrator.clearEventHandlers();
        assertFalse(integrator.getEventHandlers().contains(eventHandler));
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        integrator.setMaxEvaluations(100);
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new DifferentialEquationsMock();
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals(1.0, y[0], 1e-6);
        assertEquals(2.0, y[1], 1e-6);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatchException() {
        FirstOrderDifferentialEquations equations = new DifferentialEquationsMock();
        double t0 = 0.0;
        double[] y0 = new double[]{1.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test
    public void testComputeDerivatives() {
        double t = 0.0;
        double[] y = new double[]{1.0, 2.0};
        double[] yDot = new double[y.length];
        try {
            integrator.computeDerivatives(t, y, yDot);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals(2.0, yDot[0], 1e-6);
        assertEquals(4.0, yDot[1], 1e-6);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testComputeDerivativesDimensionMismatchException() {
        double t = 0.0;
        double[] y = new double[]{1.0};
        double[] yDot = new double[y.length];
        integrator.computeDerivatives(t, y, yDot);
    }

    private class AbstractIntegratorMock extends AbstractIntegrator {
        public AbstractIntegratorMock() {
            super("AbstractIntegratorMock");
        }

        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {

        }
    }

    private class StepHandlerMock implements StepHandler {
        public void init(double t0, double[] y0, double t) {

        }

        public void handleStep(StepInterpolator interpolator, boolean isLast) {

        }
    }

    private class EventHandlerMock implements EventHandler {
        public void init(double t0, double[] y0, double t) {

        }

        public double g(double t, double[] y) {
            return 0;
        }

        public Action eventOccurred(double t, double[] y, boolean increasing) {
            return Action.CONTINUE;
        }

        public void resetState(double t, double[] y) {

        }
    }

    private class DifferentialEquationsMock implements FirstOrderDifferentialEquations {
        public int getDimension() {
            return 2;
        }

        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = 2.0 * y[0];
            yDot[1] = 2.0 * y[1];
        }
    }
}