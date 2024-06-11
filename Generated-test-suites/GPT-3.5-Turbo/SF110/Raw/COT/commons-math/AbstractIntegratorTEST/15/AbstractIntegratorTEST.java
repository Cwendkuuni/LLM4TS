import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() {
        AbstractIntegrator integrator = new AbstractIntegrator("Test") {};
        assertEquals("Test", integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
        assertEquals(0, integrator.getEventHandlers().size());
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0);
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0);
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {

            }
        };
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {

            }
        };
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        EventHandler handler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }
            @Override
            public double g(double t, double[] y) {
                return 0;
            }
            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }
        };
        integrator.addEventHandler(handler, 0.1, 0.01, 100);
        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testAddEventHandlerWithSolver() {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        EventHandler handler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }
            @Override
            public double g(double t, double[] y) {
                return 0;
            }
            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }
        };
        UnivariateSolver solver = new BracketingNthOrderBrentSolver(0.01, 5);
        integrator.addEventHandler(handler, 0.1, 0.01, 100, solver);
        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        EventHandler handler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }
            @Override
            public double g(double t, double[] y) {
                return 0;
            }
            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }
        };
        integrator.addEventHandler(handler, 0.1, 0.01, 100);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        integrator.integrate(new ExpandableStatefulODE(), 0, new double[0], 1, new double[0]);
        assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        double t = 0;
        double[] y = new double[4];
        double[] yDot = new double[4];
        integrator.computeDerivatives(t, y, yDot);
        assertArrayEquals(new double[4], yDot, 0);
    }
}