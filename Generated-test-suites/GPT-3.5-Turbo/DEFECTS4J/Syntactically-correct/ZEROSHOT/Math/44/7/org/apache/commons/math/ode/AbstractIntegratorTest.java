import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        assertEquals("AbstractIntegratorImpl", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandlerImpl handler = new StepHandlerImpl();
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandlerImpl handler = new StepHandlerImpl();
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        assertFalse(integrator.getStepHandlers().contains(handler));
     }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        EventHandlerImpl handler = new EventHandlerImpl();
        integrator.addEventHandler(handler, 0, 0, 0);
        assertTrue(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        EventHandlerImpl handler = new EventHandlerImpl();
        integrator.addEventHandler(handler, 0, 0, 0);
        integrator.clearEventHandlers();
        assertFalse(integrator.getEventHandlers().contains(handler));
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

    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        double[] y = new double[2];
        double[] yOut = new double[2];
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };
        double t = 1.0;
        double h = 0.1;
        y[0] = 0.0;
        y[1] = 1.0;
        integrator.integrate(equations, 0, y, t, yOut);
        assertEquals(Math.sin(t), yOut[0], 0.0001);
        assertEquals(Math.cos(t), yOut[1], 0.0001);
    }

    private class AbstractIntegratorImpl extends AbstractIntegrator {
        public void integrate(ExpandableStatefulODE equations, double t) {
        }
    }

    private class StepHandlerImpl implements StepHandler {
        public void init(double t0, double[] y0, double t) {
        }
        public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {
        }
    }

    private class EventHandlerImpl implements EventHandler {
        public double g(double t, double[] y) {
            return 0;
        }
        public int eventOccurred(double t, double[] y, boolean increasing) {
            return 0;
        }
        public double getMaxCheckInterval() {
            return 0;
        }
        public double getEventTime() {
            return 0;
        }
        public int getConvergence() {
            return 0;
        }
        public int getMaxIterationCount() {
            return 0;
        }
        public UnivariateRealSolver getSolver() {
            return null;
        }
        public boolean stop() {
            return false;
        }
        public void stepAccepted(double t, double[] y) {
        }
    }

}