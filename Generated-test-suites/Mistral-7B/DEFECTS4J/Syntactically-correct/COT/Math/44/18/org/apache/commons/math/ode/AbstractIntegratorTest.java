package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.IVP;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.stepper.ClassicalRungeKuttaStepper;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setup() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Do nothing, as we only test the methods of the AbstractIntegrator class
            }
        };
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // Do nothing
            }
        });
        assertNotNull(integrator.getStepHandlers());
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // Do nothing
            }
        });
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double[] yPrime, double[] ySecondPrime,
                    double[] equationConstants) {
                // Do nothing
            }

            @Override
            public void handleEvent(double t, double[] y, double[] yDot) {
                // Do nothing
            }
        }, 0.0, 0.0, 1, new BracketingNthOrderBrentSolver(0.01, 5));
        assertNotNull(integrator.getEventHandlers());
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearEventHandler() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double[] yPrime, double[] ySecondPrime,
                    double[] equationConstants) {
                // Do nothing
            }

            @Override
            public void handleEvent(double t, double[] y, double[] yDot) {
                // Do nothing
            }
        }, 0.0, 0.0, 1, new BracketingNthOrderBrentSolver(0.01, 5));
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        integrator.setMaxEvaluations(1);
        integrator.setMaxSteps(1);
        ExpandableStatefulODE equations = new IVP(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.5;
            }
        }, 0.0, new double[] { 1.0 }, 1.0, new ClassicalRungeKuttaStepper());
        integrator.setEquations(equations);
        integrator.integrate(equations, 1.0);
        double stepsize = integrator.getCurrentSignedStepsize();
        assertEquals(stepsize, 1.0, 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetCurrentSignedStepsizeWhenIntegrationIsNotCompleted() {
        ExpandableStatefulODE equations = new IVP(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.5;
            }
        }, 0.0, new double[] { 1.0 }, 1.0, new ClassicalRungeKuttaStepper());
        integrator.setEquations(equations);
        integrator.integrate(equations, 1.0, new double[] { 1.0 }, 1.1);
        integrator.getCurrentSignedStepsize();
    }

    @Test
    public void testGetEvaluations() {
        integrator.setMaxEvaluations(1);
        ExpandableStatefulODE equations = new IVP(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.5;
            }
        }, 0.0, new double[] { 1.0 }, 1.0, new ClassicalRungeKuttaStepper());
        integrator.setEquations(equations);
        integrator.integrate(equations, 1.0, new double[] { 1.0 }, 1.1);
        int evaluations = integrator.getEvaluations();
        assertEquals(evaluations, 1);
    }

    @Test
    public void testResetEvaluations() {
        integrator.setMaxEvaluations(1);
        integrator.resetEvaluations();
        int evaluations = integrator.getEvaluations();
        assertEquals(evaluations, 0);
    }

    @Test
    public void testIntegrateWithEventHandler() {
        final int numSteps = 3;
        integrator.setMaxEvaluations(numSteps * 2);
        integrator.setMaxSteps(numSteps);

        ExpandableStatefulODE equations = new IVP(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.5;
            }
        }, 0.0, new double[] { 1.0 }, 1.0, new ClassicalRungeKuttaStepper());
        integrator.setEquations(equations);
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double[] yPrime, double[] ySecondPrime,
                    double[] equationConstants) {
                if (t >= 0.5) {
                    throw new MathIllegalArgumentException(
                            LocalizedFormats.EVENT_EVALUATION_FAILED, t);
                }
            }

            @Override
            public void handleEvent(double t, double[] y, double[] yDot) {
                // Do nothing
            }
        }, 0.5, 0.0, 1, new BracketingNthOrderBrentSolver(0.01, 5));

        integrator.integrate(equations, 1.0, new double[] { 1.0 }, 2.0);
        assertEquals(integrator.getEvaluations(), numSteps * 2);
        assertFalse(integrator.isLastStep());
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidEventHandler() {
        integrator.setMaxEvaluations(1);
        integrator.setMaxSteps(1);

        ExpandableStatefulODE equations = new IVP(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.5;
            }
        }, 0.0, new double[] { 1.0 }, 1.0, new ClassicalRungeKuttaStepper());
        integrator.setEquations(equations);
        integrator.addEventHandler(null, 0.0, 0.0, 1, new BracketingNthOrderBrentSolver(0.01, 5));
        integrator.integrate(equations, 0.0, new double[] { 1.0 }, 1.0);
    }

}