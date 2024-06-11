import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.EventState;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;
    private double[] initialCondition;
    private double targetTime;
    private double[] expectedFinalCondition;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty method to avoid actual integration
            }
        };
        equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }
        };
        initialCondition = new double[] {1.0};
        targetTime = 2.0;
        expectedFinalCondition = new double[] {3.0};
    }

    @Test
    public void testIntegrate() throws Exception {
        integrator.initIntegration(0.0, initialCondition, targetTime);
        integrator.integrate(equations, targetTime);
        double[] finalCondition = new double[equations.getDimension()];
        integrator.acceptStep(new AbstractStepInterpolator() {}, finalCondition, new double[equations.getDimension()], targetTime);
        assertEquals(expectedFinalCondition, finalCondition);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() throws Exception {
        integrator.initIntegration(0.0, new double[] {1.0, 2.0}, targetTime);
        integrator.integrate(equations, targetTime);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() throws Exception {
        integrator.setMaxEvaluations(0);
        integrator.initIntegration(0.0, initialCondition, targetTime);
        integrator.integrate(equations, targetTime);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() throws Exception {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double[] events) {
                events[0] = t + 1.0;
            }

            @Override
            public void handleEvent(double t, double[] y) {
                throw new NoBracketingException("Test exception");
            }
        }, 0.0, 0.01, 1, new BracketingNthOrderBrentSolver(0.01, 5));
        integrator.initIntegration(0.0, initialCondition, targetTime);
        integrator.integrate(equations, targetTime);
    }

    @Test
    public void testEventHandler() throws Exception {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double[] events) {
                events[0] = t;
            }

            @Override
            public void handleEvent(double t, double[] y) {
                assertEquals(initialCondition, y);
            }
        }, 0.0, 0.01, 1, new BracketingNthOrderBrentSolver(0.01, 5));
        integrator.initIntegration(0.0, initialCondition, targetTime);
        integrator.integrate(equations, targetTime);
        assertTrue(integrator.resetOccurred);
    }

    @Test
    public void testStepHandlers() throws Exception {
        integrator.addStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                if (lastStep) {
                    fail();
                }
            }
        });
        integrator.initIntegration(0.0, initialCondition, targetTime);
        integrator.integrate(equations, targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        assertFalse(integrator.isLastStep);
    }

    @Test
    public void testMultipleEventHandler() throws Exception {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double[] events) {
                events[0] = t;
            }

            @Override
            public void handleEvent(double t, double[] y) {
                assertEquals(initialCondition, y);
            }
        }, 0.0, 0.01, 1, new BracketingNthOrderBrentSolver(0.01, 5));
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double[] events) {
                events[1] = t + 1.0;
            }

            @Override
            public void handleEvent(double t, double[] y) {
                assertEquals(expectedFinalCondition, y);
            }
        }, 0.0, 0.01, 1, new BracketingNthOrderBrentSolver(0.01, 5));
        integrator.initIntegration(0.0, initialCondition, targetTime);
        integrator.integrate(equations, targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, expectedFinalCondition, new double[equations.getDimension()], targetTime);
        assertFalse(integrator.isLastStep);
    }

    @Test
    public void testMaxEvaluations() throws Exception {
        integrator.setMaxEvaluations(10);
        integrator.initIntegration(0.0, initialCondition, targetTime);
        integrator.integrate(equations, targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        assertEquals(11, integrator.getEvaluations());
        try {
            integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
            fail();
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testStepSize() throws Exception {
        integrator.setMaxEvaluations(10000);
        integrator.initIntegration(0.0, initialCondition, targetTime);
        integrator.integrate(equations, targetTime);
        double currentStepSize = integrator.getCurrentSignedStepsize();
        assertEquals(0.0, currentStepSize, 1e-10);
        integrator.addStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                integrator.setStepSize(1.0);
            }
        });
        integrator.acceptStep(new AbstractStepInterpolator() {}, initialCondition, new double[equations.getDimension()], targetTime);
        double newStepSize = integrator.getCurrentSignedStepsize();
        assertEquals(1.0, newStepSize, 1e-10);
    }

    @Test
    public void testGetEquations() throws Exception {
        integrator.setEquations(equations);
        ExpandableStatefulODE equationsFromIntegrator = integrator.getEquations();
        assertEquals(equations, equationsFromIntegrator);
    }

}