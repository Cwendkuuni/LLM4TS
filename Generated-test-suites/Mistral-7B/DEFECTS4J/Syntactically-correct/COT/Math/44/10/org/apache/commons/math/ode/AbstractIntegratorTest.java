package org.apache.commons.math.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private static final double TOLERANCE = 1e-9;

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // This method is not tested here, as it is abstract.
            }
        };
        equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new MyEventHandler(), 0.1, 1e-6, 10);
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearEventHandler() {
        integrator.addEventHandler(new MyEventHandler(), 0.1, 1e-6, 10);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testIntegrate() {
        integrator.setEquations(equations);
        double t0 = 0.0;
        double[] y0 = new double[] { 1.0 };
        double t = 1.0;
        double[] y = new double[1];

        try {
            integrator.integrate(equations, t, y0, y);
            assertEquals(1.1, y[0], TOLERANCE);
        } catch (MathIllegalStateException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidInitialCondition() {
        integrator.setEquations(equations);
        double t = 1.0;
        double[] y = new double[] { 1.0, 2.0 };

        integrator.integrate(equations, t, y, y);
        fail("Expected an exception");
    }

    @Test
    public void testAcceptStep() throws MathIllegalStateException {
        integrator.addEventHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                assertTrue(isLastStep);
            }
        }, 0.0);

        integrator.setEquations(equations);
        double t0 = 0.0;
        double[] y0 = new double[] { 1.0 };
        double t = 1.0;
        double[] y = new double[1];

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double integrate(double y, double yDot, double step) {
                return 0.0;
            }
        };

        try {
            integrator.acceptStep(interpolator, y0, y, t);
            assertTrue(integrator.isLastStep());
        } catch (MathIllegalStateException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MathIllegalStateException.class)
    public void testAcceptStepWithInvalidStepHandler() {
        integrator.addEventHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                throw new RuntimeException();
            }
        }, 0.0);
        integrator.setEquations(equations);
        double t0 = 0.0;
        double[] y0 = new double[] { 1.0 };
        double t = 1.0;
        double[] y = new double[1];

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double integrate(double y, double yDot, double step) {
                return 0.0;
            }
        };

        integrator.acceptStep(interpolator, y0, y, t);
        fail("Expected an exception");
    }

}

class MyEventHandler implements EventHandler {

    @Override
    public boolean eventReached(double t, double[] y) {
        return false;
    }

    @Override
    public void handleEvent(double t, double[] y) {
        // Empty
    }
}