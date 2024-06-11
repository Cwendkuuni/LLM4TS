import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };
        String name = "Test Integrator";
        integrator = new AbstractIntegrator(name) {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };

        assertEquals(name, integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double v, double[] doubles, double v1) {

            }

            @Override
            public void handleStep(StepInterpolator stepInterpolator, boolean b) {

            }
        };

        integrator.addStepHandler(handler);

        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double v, double[] doubles, double v1) {

            }

            @Override
            public void handleStep(StepInterpolator stepInterpolator, boolean b) {

            }
        };

        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();

        assertFalse(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };
        EventHandler handler = new EventHandler() {
            @Override
            public void init(double v, double[] doubles, double v1) throws MaxCountExceededException {

            }

            @Override
            public double g(double v, double[] doubles) throws MaxCountExceededException {
                return 0;
            }

            @Override
            public Action eventOccurred(double v, double[] doubles, boolean b) throws MaxCountExceededException {
                return null;
            }

            @Override
            public void resetState(double v, double[] doubles) throws MaxCountExceededException {

            }
        };

        integrator.addEventHandler(handler, 0.1, 0.01, 10);

        assertTrue(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };
        EventHandler handler = new EventHandler() {
            @Override
            public void init(double v, double[] doubles, double v1) throws MaxCountExceededException {

            }

            @Override
            public double g(double v, double[] doubles) throws MaxCountExceededException {
                return 0;
            }

            @Override
            public Action eventOccurred(double v, double[] doubles, boolean b) throws MaxCountExceededException {
                return null;
            }

            @Override
            public void resetState(double v, double[] doubles) throws MaxCountExceededException {

            }
        };

        integrator.addEventHandler(handler, 0.1, 0.01, 10);
        integrator.clearEventHandlers();

        assertFalse(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };
        int maxEvaluations = 100;
        integrator.setMaxEvaluations(maxEvaluations);

        assertEquals(maxEvaluations, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };
        int evaluations = 10;
        for(int i=0; i<evaluations; i++) {
            integrator.getEvaluationsCounter().incrementCount();
        }

        assertEquals(evaluations, integrator.getEvaluations());
    }

    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };
        boolean stateInitialized = true;
        integrator.setStateInitialized(stateInitialized);

        assertEquals(stateInitialized, integrator.isStatesInitialized());
    }

    @Test
    public void testAcceptStep() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            protected StepInterpolator doCopy() {
                return null;
            }
        };
        double[] y = new double[2];
        double[] yDot = new double[2];
        double tEnd = 10.0;

        try {
            double acceptedTime = integrator.acceptStep(interpolator, y, yDot, tEnd);

            assertTrue(acceptedTime <= tEnd);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testSanityChecks() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        double t = 10.0;

        try {
            integrator.sanityChecks(equations, t);
        } catch (Exception e) {
            fail();
        }
    }
}