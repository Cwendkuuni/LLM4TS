import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // do nothing
            }
        };

        assertNull(integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // do nothing
            }
        };

        StepHandler handler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }

            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                // do nothing
            }
        };

        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // do nothing
            }
        };

        StepHandler handler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }

            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                // do nothing
            }
        };

        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());

        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // do nothing
            }
        };

        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }

            public double g(double t, double[] y) {
                return 0;
            }

            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return null;
            }

            public void resetState(double t, double[] y) {
                // do nothing
            }
        };

        integrator.addEventHandler(handler, 0.1, 0.01, 100);
        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // do nothing
            }
        };

        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }

            public double g(double t, double[] y) {
                return 0;
            }

            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return null;
            }

            public void resetState(double t, double[] y) {
                // do nothing
            }
        };

        integrator.addEventHandler(handler, 0.1, 0.01, 100);
        assertEquals(1, integrator.getEventHandlers().size());

        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // do nothing
            }
        };

        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // do nothing
            }
        };

        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // do nothing
            }
        };

        double t0 = 0;
        double[] y0 = {1, 2, 3};
        double t = 1;

        integrator.initIntegration(t0, y0, t);
        assertFalse(integrator.getExpandable().isForward());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // do nothing
            }
        };

        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // do nothing
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 3;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // do nothing
            }
        };

        double t0 = 0;
        double[] y0 = {1, 2, 3};
        double t = 1;
        double[] y = new double[3];

        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (Exception e) {
            fail("An exception occurred");
        }
    }

    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // do nothing
            }
        };

        double t = 0;
        double[] y = {1, 2, 3};
        double[] yDot = new double[3];

        try {
            integrator.computeDerivatives(t, y, yDot);
        } catch (Exception e) {
            fail("An exception occurred");
        }
    }
}