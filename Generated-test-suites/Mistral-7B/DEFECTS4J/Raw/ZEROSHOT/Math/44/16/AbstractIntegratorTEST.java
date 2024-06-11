package org.apache.commons.math.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.stepper.DormandPrince54;
import org.apache.commons.math3.ode.stepper.StepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest {

    private static final double EPSILON = 1.0e-10;
    private static final double TOLERANCE = 1.0e-6;

    /** ODE system to integrate. */
    private FirstOrderDifferentialEquations equations;

    /** Integrator under test. */
    private AbstractIntegrator integrator;

    /** Stepper to perform integration. */
    private StepInterpolator stepper;

    /** Step handler. */
    private StepHandler stepHandler;

    /** Event handler. */
    private EventHandler eventHandler;

    /** Event state. */
    private EventState eventState;

    /**
     * Set up before each test.
     */
    @Before
    public void setUp() {
        equations = new ExpandableStatefulODE(new PolynomialFunction(x -> 2 * x * x - 1)) {
            private static final long serialVersionUID = 1L;

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 4.0 * y[0];
            }

        };
        integrator = new DormandPrince54();
        stepper = integrator;
        stepHandler = new StepHandlerAdapter() {
            private static final long serialVersionUID = 1L;

            @Override
            public void handleStep(double t, double[] y, double[] yDot, double stepSize, double[] work) {
                assertEquals("y[" + 0 + "]", y[0], 1.0, EPSILON);
            }
        };
        eventHandler = new EventHandler() {
            private static final long serialVersionUID = 1L;

            @Override
            public EventState evaluateStep(AbstractStepInterpolator interpolator) {
                final double t = interpolator.getGlobalCurrentTime();
                final double y = interpolator.getInterpolatedState()[0];
                if (Math.abs(y - 1.0) <= 0.5 * EPSILON) {
                    eventState = new EventState(this, 0.001, 0.1, 10,
                            new SecantSolver());
                    eventState.setReset(true);
                    return eventState;
                }
                return null;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                assertEquals("y[" + 0 + "]", y[0], 2.0, EPSILON);
            }

            @Override
            public boolean stop() {
                return true;
            }

            @Override
            public String getId() {
                return "TestEvent";
            }
        };
        integrator.addEventHandler(eventHandler, 0.0, 0.1, 10, new BrentSolver());
        integrator.addStepHandler(stepHandler);
    }

    /**
     * Test integration with no events or step handlers.
     * @throws Exception if integration fails
     */
    @Test
    public void testIntegrationNoEventsOrHandlers()
        throws Exception {
        integrator.setEquations(equations);
        double[] y = new double[1];
        double t = 2.0;
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 2.0, EPSILON);
    }

    /**
     * Test integration with an event that triggers a state reset.
     * @throws Exception if integration fails
     */
    @Test
    public void testIntegrationWithEventHandler()
        throws Exception {
        integrator.setEquations(equations);
        double[] y = new double[1];
        double t = 1.5;
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 1.0, EPSILON);
        integrator.reset();
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 2.0, EPSILON);
    }

    /**
     * Test integration with a step handler.
     * @throws Exception if integration fails
     */
    @Test
    public void testIntegrationWithStepHandler()
        throws Exception {
        integrator.setEquations(equations);
        double[] y = new double[1];
        double t = 2.0;
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 2.0, EPSILON);
        double oldStepSize = integrator.getCurrentSignedStepsize();
        integrator.addStepHandler(stepHandler);
        integrator.reset();
        double[] y2 = new double[1];
        integrator.integrate(equations, 0.0, y2, t);
        assertEquals("y[" + 0 + "]", y2[0], 1.0, EPSILON);
        assertEquals("old step size", oldStepSize, integrator.getCurrentSignedStepsize(), EPSILON);
    }

    /**
     * Test integration with events and step handlers.
     * @throws Exception if integration fails
     */
    @Test
    public void testIntegrationWithEventsAndHandlers()
        throws Exception {
        integrator.setEquations(equations);
        double[] y = new double[1];
        double t = 2.0;
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 2.0, EPSILON);
        integrator.reset();
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 1.0, EPSILON);
        integrator.reset();
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 2.0, EPSILON);
    }

    /**
     * Test integration with events and step handlers, with events
     * causing integration to stop.
     * @throws Exception if integration fails
     */
    @Test
    public void testIntegrationWithEventsAndHandlersStopping()
        throws Exception {
        integrator.setEquations(equations);
        double[] y = new double[1];
        double t = 2.5;
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 2.0, EPSILON);
        integrator.reset();
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 1.0, EPSILON);
        integrator.reset();
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 2.0, EPSILON);
        integrator.reset();
        integrator.integrate(equations, 0.0, y, t);
        assertTrue("integration should have stopped", integrator.isLastStep());
        assertFalse("resetOccurred should not be true", integrator.getResetOccurred());
    }

    /**
     * Test integration with events and step handlers, with events
     * causing integration to reset.
     * @throws Exception if integration fails
     */
    @Test
    public void testIntegrationWithEventsAndHandlersResetting()
        throws Exception {
        integrator.setEquations(equations);
        double[] y = new double[1];
        double t = 2.5;
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 2.0, EPSILON);
        integrator.reset();
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 1.0, EPSILON);
        integrator.reset();
        integrator.integrate(equations, 0.0, y, t);
        assertEquals("y[" + 0 + "]", y[0], 2.0, EPSILON);
        integrator.reset();
        integrator.integrate(equations, 0.0, y, t);
        assertTrue("integration should have stopped", integrator.isLastStep());
        assertTrue("resetOccurred should be true", integrator.getResetOccurred());
    }

    /**
     * Test integration with very small integration span.
     * @throws Exception if integration fails
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrationWithVerySmallIntegrationSpan()
        throws Exception {
        integrator.setEquations(equations);
        integrator.sanityChecks(equations, equations.getTime() - 1.0e-12);
    }

}