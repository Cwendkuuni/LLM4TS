package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.EventState;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.Integrator;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.TestFunctions;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private static final double EPSILON = 1e-10;

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // do nothing
            }
        };
        equations = new TestFunctions.RosslerSystem();
    }

    @Test
    public void testConstructor() {
        assertNotNull(integrator);
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                assertTrue("Is last step not set correctly", isLastStep);
            }
        });
        assertNotNull(integrator.getStepHandlers().iterator().next());
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                assertTrue("Is last step not set correctly", isLastStep);
            }
        });
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testGetEventHandlers() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(StepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                // do nothing
            }

            @Override
            public double getEventTime() {
                return 1.0;
            }

            @Override
            public boolean stop() {
                return false;
            }

            @Override
            public void reinitializeBegin(StepInterpolator interpolator) {
                // do nothing
            }
        }, 1.0, 0.001, 5, new BracketingNthOrderBrentSolver(0.001, 5));
        List<EventHandler> handlers = integrator.getEventHandlers();
        assertEquals(1, handlers.size());
        assertEquals(handlers.get(0).getClass(), EventHandler.class);
    }

    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(StepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                // do nothing
            }

            @Override
            public double getEventTime() {
                return 1.0;
            }

            @Override
            public boolean stop() {
                return false;
            }

            @Override
            public void reinitializeBegin(StepInterpolator interpolator) {
                // do nothing
            }
        }, 1.0, 0.001, 5, new BracketingNthOrderBrentSolver(0.001, 5));
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testIntegrate_success() {
        integrator.setEquations(equations);
        double[] initialValues = new double[] { 1.0, 1.0, 1.0 };
        double finalTime = 5.0;
        double[] result = new double[equations.getDimension()];
        integrator.integrate(equations, finalTime, initialValues, result);
        double expectedTime = equations.getTime(result);
        assertEquals(expectedTime, finalTime, EPSILON);
    }

    @Test
    public void testIntegrate_maxEvaluations() {
        integrator.setMaxEvaluations(0);
        assertThrows(MaxCountExceededException.class, () -> integrator.integrate(equations, 1.0, new double[] { 0.0 }, 1.0));
    }

    @Test
    public void testIntegrate_dimensionMismatch() {
        integrator.setEquations(equations);
        double[] initialValues = new double[] { 1.0, 1.0, 1.0, 1.0 };
        assertThrows(DimensionMismatchException.class, () -> integrator.integrate(equations, 1.0, initialValues, new double[equations.getDimension() - 1]));
    }

    @Test
    public void testIntegrate_illegalArgument() {
        integrator.setEquations(equations);
        assertThrows(MathIllegalArgumentException.class, () -> integrator.integrate(equations, Double.NaN, new double[] { 1.0 }, new double[equations.getDimension()]));
    }

    @Test
    public void testIntegrate_stateNotInitialized() {
        integrator.setEquations(equations);
        double[] initialValues = new double[equations.getDimension()];
        integrator.integrate(equations, 1.0, initialValues, new double[equations.getDimension()]);
        assertFalse(integrator.statesInitialized);
    }

    @Test
    public void testComputeDerivatives() {
        integrator.setEquations(equations);
        double[] initialValues = new double[equations.getDimension()];
        double[] result = new double[equations.getDimension()];
        integrator.integrate(equations, 1.0, initialValues, initialValues);
        integrator.computeDerivatives(1.0, initialValues, result);
        for (int i = 0; i < equations.getDimension(); i++) {
            assertTrue(Precision.isClose(equations.getDerivatives(initialValues, result)[i], result[i], EPSILON));
        }
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(1);
        integrator.setMaxEvaluations(2);
        assertEquals(2, integrator.getMaxEvaluations());
    }

    @Test
    public void testResetEvaluations() {
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 1.0, new double[] { 0.0 }, new double[equations.getDimension()]);
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

}