package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.events.MockEventState;
import org.apache.commons.math3.ode.step.AbstractStepHandler;
import org.apache.commons.math3.ode.step.MockStepHandler;
import org.apache.commons.math3.ode.step.MockStepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for the {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest {

    /** The test integrator. */
    private AbstractIntegrator integrator;

    /** The test equations. */
    private FirstOrderDifferentialEquations equations;

    /** The test integrator with step handlers and event handlers. */
    private AbstractIntegrator integratorWithHandlers;

    /** The test equations with step handlers and event handlers. */
    private ExpandableStatefulODE expandableEquations;

    /** The test event handler. */
    private MockEventHandler eventHandler;

    /** The test event state. */
    private MockEventState eventState;

    /** The test step handler. */
    private MockStepHandler stepHandler;

    /** The test step interpolator. */
    private MockStepInterpolator stepInterpolator;

    /** The test integrator step handlers. */
    private List<AbstractStepHandler> integratorStepHandlers;

    /** The test integrator event handlers. */
    private List<EventHandler> integratorEventHandlers;

    /** The test integrator step interpolator. */
    private AbstractStepInterpolator integratorStepInterpolator;

    /** The test integrator maximum evaluations. */
    private int integratorMaxEvaluations;

    /** The test initial time. */
    private double initialTime;

    /** The test initial state. */
    private double[] initialState;

    /** The test target time. */
    private double targetTime;

    /** The test target state. */
    private double[] targetState;

    /** The test current state. */
    private double[] currentState;

    /** The test current derivatives. */
    private double[] currentDerivatives;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Empty integration method for the test
            }
        };
        integratorMaxEvaluations = integrator.getMaxEvaluations();
        resetEvaluations(integrator);

        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 1;
            }
        };
        expandableEquations = new ExpandableStatefulODE(equations);
        expandableEquations.setTime(initialTime);
        expandableEquations.setPrimaryState(initialState);

        eventHandler = new MockEventHandler();
        eventState = new MockEventState();

        integratorWithHandlers = new AbstractIntegrator("TestIntegratorWithHandlers") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Empty integration method for the test
            }

            @Override
            public void addEventHandler(EventHandler handler, double maxCheckInterval,
                                         double convergence, int maxIterationCount,
                                         UnivariateRealSolver solver) {
                super.addEventHandler(handler, maxCheckInterval, convergence,
                                      maxIterationCount, solver);
                integratorEventHandlers.add(handler);
            }

            @Override
            public void addStepHandler(StepHandler handler) {
                super.addStepHandler(handler);
                integratorStepHandlers.add(handler);
            }

            @Override
            public AbstractStepInterpolator getInterpolator() {
                if (integratorStepInterpolator == null) {
                    integratorStepInterpolator = new MockStepInterpolator();
                }
                return integratorStepInterpolator;
            }

            @Override
            public int getMaxEvaluations() {
                return integratorMaxEvaluations + 10;
            }
        };
        integratorWithHandlers.addEventHandler(eventHandler, 0.1, 1.0e-10, 1000, null);
        integratorWithHandlers.addStepHandler(stepHandler);

        integratorStepInterpolator = new MockStepInterpolator();

        initialTime = 0.0;
        initialState = new double[] {0.0};
        targetTime = 1.0;
        targetState = new double[] {1.0};
        currentState = new double[1];
        currentDerivatives = new double[1];
    }

    /**
     * Test basic functionality of the integrator.
     * @throws MathIllegalStateException if the integrator cannot perform integration
     * @throws MathIllegalArgumentException if integration parameters are wrong
     */
    @Test
    public void testIntegrateBasic() throws MathIllegalStateException, MathIllegalArgumentException {
        integrator.integrate(expandableEquations, targetTime);
        assertEquals(targetTime, integrator.getCurrentStepStart());
        assertArrayEquals(targetState, integrator.getState(), 1e-10);
    }

    /**
     * Test integrator maximum evaluations limit.
     */
    @Test
    public void testIntegrateMaxEvaluations() {
        integrator.integrate(expandableEquations, targetTime);
        assertEquals(integratorMaxEvaluations, integrator.getEvaluations());
        assertThrows(MaxCountExceededException.class, () -> integrator.integrate(expandableEquations, targetTime));
    }

    /**
     * Test integrator with event handler.
     * @throws MathIllegalStateException if the integrator cannot perform integration
     * @throws MathIllegalArgumentException if integration parameters are wrong
     */
    @Test
    public void testIntegrateWithEventHandler()
            throws MathIllegalStateException, MathIllegalArgumentException {

        integratorWithHandlers.integrate(expandableEquations, targetTime);
        assertEquals(targetTime, integratorWithHandlers.getCurrentStepStart());
        assertArrayEquals(targetState, integratorWithHandlers.getState(), 1e-10);
        assertTrue(integratorWithHandlers.hasEventOccurred());
        assertEquals(eventState.getEventType(), integratorWithHandlers.getLastEventType());
        assertArrayEquals(eventState.getEventState(), integratorWithHandlers.getLastEventState(), 1e-10);

    }

    /**
     * Test integrator with step handler.
     * @throws MathIllegalStateException if the integrator cannot perform integration
     * @throws MathIllegalArgumentException if integration parameters are wrong
     */
    @Test
    public void testIntegrateWithStepHandler()
            throws MathIllegalStateException, MathIllegalArgumentException {

        integratorWithHandlers.integrate(expandableEquations, targetTime);
        assertEquals(targetTime, integratorWithHandlers.getCurrentStepStart());
        assertArrayEquals(targetState, integratorWithHandlers.getState(), 1e-10);
        assertTrue(stepHandler.isLastStepCalled());

    }

    /**
     * Test integrator with step interpolator.
     * @throws MathIllegalStateException if the integrator cannot perform integration
     * @throws MathIllegalArgumentException if integration parameters are wrong
     */
    @Test
    public void testIntegrateWithStepInterpolator()
            throws MathIllegalStateException, MathIllegalArgumentException {

        integratorWithHandlers.integrate(expandableEquations, targetTime);
        assertEquals(targetTime, integratorWithHandlers.getCurrentStepStart());
        assertArrayEquals(targetState, integratorWithHandlers.getState(), 1e-10);
        assertEquals(targetTime, integratorStepInterpolator.getGlobalCurrentTime());

    }

    /**
     * Test integrator with event and step handlers.
     * @throws MathIllegalStateException if the integrator cannot perform integration
     * @throws MathIllegalArgumentException if integration parameters are wrong
     */
    @Test
    public void testIntegrateWithEventAndStepHandlers()
            throws MathIllegalStateException, MathIllegalArgumentException {

        integratorWithHandlers.integrate(expandableEquations, targetTime);
        assertEquals(targetTime, integratorWithHandlers.getCurrentStepStart());
        assertArrayEquals(targetState, integratorWithHandlers.getState(), 1e-10);
        assertTrue(integratorWithHandlers.hasEventOccurred());
        assertEquals(eventState.getEventType(), integratorWithHandlers.getLastEventType());
        assertArrayEquals(eventState.getEventState(), integratorWithHandlers.getLastEventState(), 1e-10);
        assertTrue(stepHandler.isLastStepCalled());

    }

    /**
     * Test integrator with invalid parameters.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithInvalidParameters() {
        integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // Empty implementation for the test
            }
        }, targetTime);
    }

    /**
     * Test integrator with negative maximum evaluations limit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNegativeMaxEvaluations() {
        integrator.setMaxEvaluations(-1);
        integrator.integrate(expandableEquations, targetTime);
    }

    /**
     * Test integrator with too small integration span.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithTooSmallIntegrationSpan() {
        integrator.integrate(expandableEquations, initialTime);
    }

    /**
     * Test reset evaluations.
     */
    public void resetEvaluations(AbstractIntegrator integrator) {
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

}