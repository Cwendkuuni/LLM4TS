package org.apache.commons.math3.ode;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Incrementor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class AbstractIntegratorTest {

    private static class TestIntegrator extends AbstractIntegrator {

        public TestIntegrator() {
            super("Test Integrator");
        }

        @Override
        public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {

        }
    }

    @Test
    public void testGetName() {
        TestIntegrator integrator = new TestIntegrator();
        assertEquals("Test Integrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        TestIntegrator integrator = new TestIntegrator();
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {

            }
        };

        assertEquals(0, integrator.getStepHandlers().size());
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        TestIntegrator integrator = new TestIntegrator();
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {

            }
        };

        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        TestIntegrator integrator = new TestIntegrator();

        EventHandler handler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }

            @Override
            public double[] computeDerivatives(double t, double[] y) {
                return new double[0];
            }

            @Override
            public double g(double t, double[] y) {
                return 0;
            }

            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return null;
            }

            @Override
            public void resetState(double t, double[] y) {

            }
        };

        assertEquals(0, integrator.getEventHandlers().size());
        integrator.addEventHandler(handler, 0.01, 0.001, 100);
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearEventHandlers() {
        TestIntegrator integrator = new TestIntegrator();

        EventHandler handler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }

            @Override
            public double[] computeDerivatives(double t, double[] y) {
                return new double[0];
            }

            @Override
            public double g(double t, double[] y) {
                return 0;
            }

            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return null;
            }

            @Override
            public void resetState(double t, double[] y) {

            }
        };

        integrator.addEventHandler(handler, 0.01, 0.001, 100);
        assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetMaxEvaluations() {
        TestIntegrator integrator = new TestIntegrator();

        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        TestIntegrator integrator = new TestIntegrator();

        assertEquals(0, integrator.getEvaluations());

        Incrementor evaluations = integrator.getEvaluationsCounter();
        evaluations.incrementCount();
        assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testSetEquations() {
        TestIntegrator integrator = new TestIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        integrator.setEquations(equations);

        assertEquals(equations, integrator.getExpandable());
    }

    @Test
    public void testIntegrate() {
        TestIntegrator integrator = new TestIntegrator();
        final double[] y0 = new double[3];
        final double[] y = new double[3];

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {

            }
        };

        try {
            integrator.integrate(equations, 0.0, y0, 1.0, y);
        } catch (Exception e) {
        }
    }

    @Test
    public void testComputeDerivatives() {
        TestIntegrator integrator = new TestIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        integrator.setEquations(equations);

        try {
            integrator.computeDerivatives(0.0, new double[3], new double[3]);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSanityChecks() {
        TestIntegrator integrator = new TestIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        equations.setTime(0.0);

        try {
            integrator.sanityChecks(equations, 1.0);
        } catch (Exception e) {
        }

        try {
            integrator.sanityChecks(equations, 0.0);
        } catch (Exception e) {
        }
    }
}