package org.apache.commons.math.ode.test;

import static org.apache.commons.math3.util.Precision.equals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.SampleIntegratorTest;
import org.apache.commons.math3.ode.test.TestIntegrator;
import org.apache.commons.math3.ode.test.TestVectorField;
import org.junit.Test;

import org.apache.commons.math.ode.AbstractIntegrator;

/**
 * Test class for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest extends SampleIntegratorTest {

    /** Integrator to test. */
    private AbstractIntegrator integrator;

    /**
     * Constructor.
     */
    public AbstractIntegratorTest() {
        super();
        integrator = new TestIntegrator("TestIntegrator", 0.1, 1000);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        integrator = new AbstractIntegrator();
        assertNull(integrator.getName());
        assertNotNull(integrator.getStepHandlers());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertFalse(integrator.statesInitialized);
        assertEquals(0, integrator.getEvaluations());
        assertNull(integrator.expandable);
    }

    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        assertEquals("TestIntegrator", integrator.getName());
    }

    /**
     * Test addStepHandler method.
     */
    @Test
    public void testAddAndClearStepHandlers() {
        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(final StepInterpolator interpolator, final boolean isLast) {
                // no-op
            }
        };
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        assertNull(integrator.getStepHandlers().iterator().next());
    }

    /**
     * Test getStepHandlers method.
     */
    @Test
    public void testGetStepHandlers() {
        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(final StepInterpolator interpolator, final boolean isLast) {
                // no-op
            }
        };
        integrator.addStepHandler(handler);
        List<StepHandler> handlers = new ArrayList<StepHandler>(integrator.getStepHandlers());
        assertEquals(1, handlers.size());
        assertEquals(handler, handlers.get(0));
        integrator.clearStepHandlers();
        assertNull(integrator.getStepHandlers().iterator().next());
    }

    /**
     * Test integration of a simple differential equation.
     */
    @Test
    public void testSimpleIntegration() throws Exception {
        // prepare equations
        FirstOrderDifferentialEquations equations = new TestVectorField(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        });
        integrator.setEquations(equations);

        // integration test
        double[] y = new double[equations.getDimension()];
        double t = 2.0;
        double[] result = integrator.integrate(equations, 0.0, y, t);
        assertEquals(t, result, 1e-14);
        assertEquals(Arrays.asList(new Double[]{2.0}), Arrays.asList(y));
    }

    /**
     * Test integration with events.
     */
    @Test
    public void testIntegrationWithEvents() throws Exception {
        // prepare equations with events
        FirstOrderDifferentialEquations equations = new TestVectorField(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        });
        EventHandler eventHandler = new TestEventHandler();
        integrator.addEventHandler(eventHandler, 1.0, 0.1, 10);
        integrator.setEquations(equations);

        // integration test
        double[] y = new double[equations.getDimension()];
        double t = 2.0;
        double[] result = integrator.integrate(equations, 0.0, y, t);
        assertEquals(t, result, 1e-14);
        assertEquals(Arrays.asList(new Double[]{2.0}), Arrays.asList(y));
        assertEquals(1, eventHandler.getCount());
        assertNotNull(eventHandler.getEvent());
    }

    /**
     * Test integration with maximum number of evaluations.
     */
    @Test
    public void testIntegrationWithMaxEvaluations() throws Exception {
        // prepare equations
        FirstOrderDifferentialEquations equations = new TestVectorField(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        });
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(10);

        // integration test
        double[] y = new double[equations.getDimension()];
        double t = 2.0;
        assertThrows(MaxCountExceededException.class, () -> {
            integrator.integrate(equations, 0.0, y, t);
        });
    }

    /**
     * Test integration with negative maximum number of evaluations.
     */
    @Test
    public void testIntegrationWithNegativeMaxEvaluations() throws Exception {
        // prepare equations
        FirstOrderDifferentialEquations equations = new TestVectorField(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        });
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(-10);
        integrator.integrate(equations, 0.0, new double[equations.getDimension()], 2.0);
    }

    /**
     * Test integration with negative time.
     */
    @Test
    public void testIntegrationWithNegativeTime() throws Exception {
        // prepare equations
        FirstOrderDifferentialEquations equations = new TestVectorField(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        });
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[equations.getDimension()], -2.0);
        assertThrows(MathIllegalArgumentException.class, () -> {
            integrator.integrate(equations, 0.0, new double[equations.getDimension()], -1.0);
        });
    }

    /**
     * Test integration with too small time difference.
     */
    @Test
    public void testIntegrationWithTooSmallTimeDifference() throws Exception {
        // prepare equations
        FirstOrderDifferentialEquations equations = new TestVectorField(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        });
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(1000);
        integrator.integrate(equations, 0.0, new double[equations.getDimension()], 1e-15, new double[equations.getDimension()]);
        assertThrows(NumberIsTooSmallException.class, () -> {
            integrator.integrate(equations, 0.0, new double[equations.getDimension()], 1e-16, new double[equations.getDimension()]);
        });
    }

    /**
     * Test integration with reset.
     */
    @Test
    public void testIntegrationWithReset() throws Exception {
        // prepare equations with events
        FirstOrderDifferentialEquations equations = new TestVectorField(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        });
        EventHandler eventHandler = new TestEventHandler();
        integrator.addEventHandler(eventHandler, 1.0, 0.1, 10);
        integrator.setEquations(equations);

        // integration test
        double[] y = new double[equations.getDimension()];
        double t = 2.0;
        integrator.integrate(equations, 0.0, y, t);
        assertEquals(t, integrator.getCurrentStepStart(), 1e-14);
        assertEquals(1, eventHandler.getCount());
        assertNotNull(eventHandler.getEvent());
        assertFalse(integrator.resetOccurred);

        integrator.reset();
        integrator.integrate(equations, 0.0, y, t);
        assertEquals(t, integrator.getCurrentStepStart(), 1e-14);
        assertEquals(1, eventHandler.getCount());
        assertNotNull(eventHandler.getEvent());
        assertFalse(integrator.resetOccurred);

        integrator.resetOccurred = true;
        integrator.integrate(equations, 0.0, y, t);
        assertEquals(t, integrator.getCurrentStepStart(), 1e-14);
        assertEquals(1, eventHandler.getCount());
        assertNotNull(eventHandler.getEvent());
        assertTrue(integrator.resetOccurred);

    }

}

class TestIntegrator extends AbstractIntegrator {

    /** Constructor. */
    public TestIntegrator(final String name, final double maxStepSize, final int maxNumSteps) {
        super(name, maxStepSize, maxNumSteps, new StepInterpolator());
    }

    /** {@inheritDoc} */
    @Override
    protected void integrate(ExpandableStatefulODE equations, double t)
        throws MathIllegalStateException, MathIllegalArgumentException {
        super.integrate(equations, t);
        sanityChecks(equations, t);
    }

}

class TestEventHandler implements EventHandler {

    /** Number of events. */
    private int count = 0;

    /** Last event. */
    private EventState event;

    /** {@inheritDoc} */
    public void stepAccepted(final double t, final double[] y) {
        if (event == null) {
            event = new EventState(this, 1.0, 0.1, 10);
        }
        event.setTime(t);
        event.setState(y);
    }

    /** {@inheritDoc} */
    public void reset() {
        count = 0;
        event = null;
    }

    /** {@inheritDoc} */
    public int getCount() {
        return count;
    }

    /** {@inheritDoc} */
    public EventState getEvent() {
        return event;
    }

}

class TestVectorField implements FirstOrderDifferentialEquations {

    /** Constructor. */
    public TestVectorField(UnivariateFunction vectorFunction) {
        this.vectorFunction = vectorFunction;
    }

    /** Vector function. */
    private final UnivariateFunction vectorFunction;

    /** {@inheritDoc} */
    public int getDimension() {
        return 1;
    }

    /** {@inheritDoc} */
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = vectorFunction.value(y[0]);
    }

}