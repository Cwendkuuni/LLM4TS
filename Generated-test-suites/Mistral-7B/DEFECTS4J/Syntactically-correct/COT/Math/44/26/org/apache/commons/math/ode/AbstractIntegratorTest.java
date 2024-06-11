package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.test.MockStepInterpolator;
import org.apache.commons.math3.ode.test.MockStepHandler;
import org.apache.commons.math3.ode.test.MockEventHandler;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link org.apache.commons.math.ode.AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    /** The abstract integrator to test. */
    private AbstractIntegrator integrator;

    /** A dummy step interpolator. */
    private AbstractStepInterpolator interpolator;

    /** A dummy step handler. */
    private MockStepHandler stepHandler;

    /** A list of dummy event handlers. */
    private List<MockEventHandler> eventHandlers;

    /** A dummy first order differential equations. */
    private FirstOrderDifferentialEquations equations;

    /** A dummy expandable stateful ODE. */
    private ExpandableStatefulODE expandable;

    /** The name of the integrator. */
    private static final String NAME = "AbstractIntegratorTest";

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator(NAME) {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no-op
            }
        };
        interpolator = new MockStepInterpolator();
        stepHandler = new MockStepHandler();
        eventHandlers = new ArrayList<MockEventHandler>();
        equations = new ExpandableStatefulODE(new UnivariateFunction() {
            @Override
            public double value(double t) {
                return 0;
            }

            @Override
            public int getDimension() {
                return 1;
            }
        }) {
            @Override
            public double getTime() {
                return 0;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[0];
            }

            @Override
            public void setTime(double time) {
                // no-op
            }

            @Override
            public void setPrimaryState(double[] state) {
                // no-op
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // no-op
            }
        };
        integrator.setEquations(equations);
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        assertNull(integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), Precision.SAFE_EPSILON);
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), Precision.SAFE_EPSILON);
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());
        assertEquals(0, integrator.getEvaluations());
    }

    /**
     * Tests the {@link #addStepHandler(StepHandler)} method.
     */
    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(stepHandler);
        integrator.addEventHandler(new MockEventHandler(), 0.5, 1e-10, 5);
        integrator.addEventHandler(new MockEventHandler(), 1.0, 1e-10, 5);
        assertEquals(2, integrator.getStepHandlers().size());
        assertEquals(2, integrator.getEventHandlers().size());
    }

    /**
     * Tests the {@link #clearEventHandlers()} method.
     */
    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(new MockEventHandler(), 0.5, 1e-10, 5);
        integrator.addEventHandler(new MockEventHandler(), 1.0, 1e-10, 5);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    /**
     * Tests the {@link #clearStepHandlers()} method.
     */
    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(stepHandler);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    /**
     * Tests the {@link #integrate(FirstOrderDifferentialEquations, double, double[], double, double[], int)} method.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateIllegalArgument() {
        integrator.integrate(equations, 0, new double[1], Double.NaN, new double[1], 10);
    }

    /**
     * Tests the {@link #integrate(FirstOrderDifferentialEquations, double, double[], double, double[], int)} method.
     */
    @Test
    public void testIntegrateSuccess() {
        integrator.integrate(equations, 0, new double[1], 1, new double[1], 10);
        assertEquals(1, integrator.getEvaluations());
        assertTrue(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());
        assertEquals(1, integrator.getMaxEvaluations());
        assertEquals(1, integrator.getEvaluations());
        assertEquals(1.0, integrator.getCurrentStepStart(), Precision.SAFE_EPSILON);
        assertEquals(0, integrator.getCurrentSignedStepsize(), Precision.SAFE_EPSILON);
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());
        assertEquals(0, integrator.getEvaluations());
    }

    /**
     * Tests the {@link #resetEvaluations()} method.
     */
    @Test
    public void testResetEvaluations() {
        integrator.integrate(equations, 0, new double[1], 1, new double[1], 10);
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

}

class MockEventHandler implements EventHandler {

    /** The event time. */
    private double eventTime;

    /** The event handler. */
    private final Object eventHandler;

    /**
     * Constructs a new mock event handler.
     * @param eventHandler the event handler to wrap
     */
    public MockEventHandler(Object eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * {@inheritDoc}
     */
    public Object getEventHandler() {
        return eventHandler;
    }

    /**
     * {@inheritDoc}
     */
    public double getEventTime() {
        return eventTime;
    }

    /**
     * {@inheritDoc}
     */
    public void reinitializeBegin(AbstractStepInterpolator interpolator) {
        // no-op
    }

    /**
     * {@inheritDoc}
     */
    public boolean evaluateStep(AbstractStepInterpolator interpolator) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void stepAccepted(double eventTime, double[] eventY) {
        this.eventTime = eventTime;
    }

    /**
     * {@inheritDoc}
     */
    public boolean stop() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void reset(double eventTime, double[] eventY) {
        // no-op
    }

}

class MockStepHandler implements StepHandler {

    /** The number of calls. */
    private int calls;

    /**
     * Constructs a new mock step handler.
     */
    public MockStepHandler() {
        this.calls = 0;
    }

    /**
     * {@inheritDoc}
     */
    public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
        calls++;
    }

    /**
     * Returns the number of calls.
     * @return the number of calls
     */
    public int getCalls() {
        return calls;
    }

}

class MockStepInterpolator extends AbstractStepInterpolator {

    /** The global previous time. */
    private double globalPreviousTime;

    /** The global current time. */
    private double globalCurrentTime;

    /**
     * {@inheritDoc}
     */
    public double getGlobalPreviousTime() {
        return globalPreviousTime;
    }

    /**
     * {@inheritDoc}
     */
    public double getGlobalCurrentTime() {
        return globalCurrentTime;
    }

    /**
     * {@inheritDoc}
     */
    public double getInterpolatedTime() {
        return globalCurrentTime;
    }

    /**
     * {@inheritDoc}
     */
    public double[] getInterpolatedState() {
        return new double[0];
    }

    /**
     * {@inheritDoc}
     */
    public void setSoftPreviousTime(double time) {
        globalPreviousTime = time;
    }

    /**
     * {@inheritDoc}
     */
    public void setSoftCurrentTime(double time) {
        globalCurrentTime = time;
    }

}