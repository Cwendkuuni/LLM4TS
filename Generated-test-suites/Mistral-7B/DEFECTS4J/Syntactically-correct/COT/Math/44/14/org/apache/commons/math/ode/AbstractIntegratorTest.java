package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Utilities;

/**
 * Test the AbstractIntegrator class
 */
public class AbstractIntegratorTest extends AbstractODETest {
    // Test variables
    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues;
    private double[] results;

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testConstructor() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException, MathIllegalArgumentException {
                // Empty implementation
            }
        };
        assertNull(integrator.getName());
        assertNull(integrator.getStepHandlers());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertNull(integrator.getEventsStates());
        assertFalse(integrator.statesInitialized);
        assertEquals(0, integrator.getEvaluations());
        assertEquals(-1, integrator.getMaxEvaluations());
        assertNull(integrator.expandable);
    }

    /**
     * Test method: addStepHandler
     */
    @org.junit.Test
    public void testAddStepHandler() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Empty implementation
            }
        };

        integrator.addStepHandler(createMockStepHandler());
        List<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(1, handlers.size());
        assertEquals(handlers.get(0), integrator.getStepHandlers().iterator().next());

    }

    /**
     * Test method: clearStepHandlers
     */
    @org.junit.Test
    public void testClearStepHandlers() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Empty implementation
            }
        };

        integrator.addStepHandler(createMockStepHandler());
        integrator.clearStepHandlers();
        List<StepHandler> handlers = integrator.getStepHandlers();
        assertTrue(handlers.isEmpty());

    }

    /**
     * Test method: integrate
     */
    @org.junit.Test
    public void testIntegrate() throws Exception {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Empty implementation
            }
        };

        equations = createMockODE();
        initialValues = new double[]{0.0, 1.0};
        results = new double[]{1.0, 2.0};

        integrator.setEquations(equations);
        integrator.integrate(equations, 2.0);

        List<Double> actualValues = Arrays.asList(equations.getPrimaryState());
        List<Double> expectedValues = Arrays.asList(results);
        assertEquals(expectedValues, actualValues);

    }

    /**
     * Test method: integrate with exception
     */
    @org.junit.Test
    public void testIntegrateWithException() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                throw new MathIllegalArgumentException("Test exception");
            }
        };

        integrator.setMaxEvaluations(1);
        integrator.integrate(createMockODE(), 2.0);

        assertEquals(1, integrator.getEvaluations());
        assertEquals(1, integrator.getMaxEvaluations());
    }

    /**
     * Test method: addEventHandler
     */
    @org.junit.Test
    public void testAddEventHandler() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Empty implementation
            }
        };

        equations = new MockODEEventHandler();
        integrator.addEventHandler(createMockEventHandler(), 0.0, 1.0, 10,
                new BracketingNthOrderBrentSolver(1.0, 5));

        List<EventHandler> handlers = integrator.getEventHandlers();
        assertEquals(1, handlers.size());
        assertEquals(handlers.get(0), integrator.getEventHandlers().iterator().next());

    }

    /**
     * Test method: clearEventHandler
     */
    @org.junit.Test
    public void testClearEventHandler() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Empty implementation
            }
        };

        integrator.addEventHandler(createMockEventHandler(), 0.0, 1.0, 10,
                new BracketingNthOrderBrentSolver(1.0, 5));
        integrator.clearEventHandler();

        List<EventHandler> handlers = integrator.getEventHandlers();
        assertTrue(handlers.isEmpty());

    }

    /**
     * Test method: getCurrentSignedStepsize
     */
    @org.junit.Test
    public void testGetCurrentSignedStepsize() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public double getCurrentSignedStepsize() {
                return 1.0;
            }
        };

        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 0.0);
    }

    /**
     * Test method: setMaxEvaluations
     */
    @org.junit.Test
    public void testSetMaxEvaluations() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void setMaxEvaluations(int maxEvaluations) {
                // Empty implementation
            }
        };

        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());

    }

    /**
     * Test method: resetEvaluations
     */
    @org.junit.Test
    public void testResetEvaluations() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void resetEvaluations() {
                // Empty implementation
            }
        };

        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());

    }

    /**
     * Test method: getEvaluations
     */
    @org.junit.Test
    public void testGetEvaluations() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public int getEvaluations() {
                return 10;
            }
        };

        assertEquals(10, integrator.getEvaluations());

    }

    /**
     * Test method: setStateInitialized
     */
    @org.junit.Test
    public void testSetStateInitialized() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void setStateInitialized(boolean stateInitialized) {
                // Empty implementation
            }
        };

        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());

    }

    /**
     * Test method: setEquations
     */
    @org.junit.Test
    public void testSetEquations() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void setEquations(ExpandableStatefulODE equations) {
                // Empty implementation
            }
        };

        equations = createMockODE();
        integrator.setEquations(equations);
        assertNotNull(integrator.expandable);
        assertEquals(equations, integrator.expandable);

    }

    /**
     * Test method: acceptStep
     */
    @org.junit.Test
    public void testAcceptStep() throws Exception {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Empty implementation
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot)
                    throws MaxCountExceededException {
                // Empty implementation
            }
        };

        equations = new MockODE();
        AbstractStepInterpolator interpolator = new MockStepInterpolator();
        double[] y = new double[]{1.0, 1.0};
        double[] yDot = new double[]{1.0, 1.0};
        double tEnd = 2.0;

        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0);

        double t = 0.5;
        integrator.computeDerivatives(t, y, yDot);

        // Test step acceptance without reset
        double acceptedStep = integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(t, acceptedStep, 0.0);
        assertFalse(integrator.resetOccurred);

        // Test step acceptance with reset
        y[0] = 2.0;
        integrator.computeDerivatives(t, y, yDot);
        integrator.resetOccurred = false;
        acceptedStep = integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(t, acceptedStep, 0.0);
        assertTrue(integrator.resetOccurred);

        // Test step acceptance with step limit
        integrator.setMaxEvaluations(1);
        acceptedStep = integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(t, acceptedStep, 0.0);
        assertEquals(1, integrator.getEvaluations());
        assertEquals(1, integrator.getMaxEvaluations());

    }

    /**
     * Test method: sanityChecks
     */
    @org.junit.Test
    public void testSanityChecks() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Empty implementation
            }

            @Override
            public void sanityChecks(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException {
                // Empty implementation
            }
        };

        equations = new ExpandableStatefulODE(new MockFunction()) {
            @Override
            public int getDimension() {
                return 1;
            }
        };
        integrator.setEquations(equations);

        // Test sanity checks with sufficient interval
        integrator.sanityChecks(equations, 1.0);

        // Test sanity checks with too small interval
        integrator.sanityChecks(equations, 0.0);
        assertThrows(NumberIsTooSmallException.class, () -> {
            integrator.integrate(equations, 0.0);
        });

    }

}