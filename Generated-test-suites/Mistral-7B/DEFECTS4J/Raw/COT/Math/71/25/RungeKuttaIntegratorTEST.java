package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialCondition;
    private double[] finalCondition;
    private double initialTime;
    private double finalTime;
    private double stepSize;

    @Before
    public void setUp() {
        integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
        equations = new DummyDifferentialEquations();
        initialCondition = new double[] {0.0, 1.0};
        finalCondition = new double[] {1.0, 0.0};
        initialTime = 0.0;
        finalTime = 1.0;
        stepSize = 0.1;
    }

    @Test
    public void testIntegrate() throws Exception {
        double result = integrator.integrate(equations, initialTime, initialCondition, finalTime, finalCondition);
        assertEquals(finalTime, result, 1e-10);
        assertArrayEquals(finalCondition, integrator.getSolution(finalTime), 1e-10);
    }

    @Test(expected = DeriveException.class)
    public void testIntegrateWithDeriveException() throws Exception {
        final FirstOrderDifferentialEquations equationsWithException = new DifferentialEquationsWithException();
        integrator.integrate(equationsWithException, initialTime, initialCondition, finalTime, finalCondition);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithIntegratorException() throws Exception {
        final FirstOrderDifferentialEquations equationsWithException = new DifferentialEquationsWithIntegratorException();
        integrator.integrator = new ExceptionThrowingIntegrator();
        integrator.integrate(equationsWithException, initialTime, initialCondition, finalTime, finalCondition);
    }

    @Test
    public void testIntegrateWithStepHandlers() throws Exception {
        integrator = new RungeKuttaIntegrator("RK45", new RK45Constants(), new RK45StepInterpolator(), new RK45StepInterpolator(), new RK45StepInterpolator()) {
            @Override
            protected void setUpStepHandlers() {
                addStepHandler(new StepCounter());
                addStepHandler(new StepPrinter());
            }
        };
        integrator.setStepSize(0.02);
        integrator.integrate(equations, initialTime, initialCondition, finalTime, finalCondition);
        assertTrue(stepHandlers.size() == 2);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidStepSize() throws Exception {
        integrator.setStepSize(-0.1);
        integrator.integrate(equations, initialTime, initialCondition, finalTime, finalCondition);
    }

    @Test
    public void testIntegrateWithEvents() throws Exception {
        final double eventTime = 0.5;
        final Event event = new When(eventTime) {
            @Override
            public boolean isSatisfied(double t, double[] y) {
                return y[0] > 0.5;
            }
        };
        integrator.eventsHandlersManager.addEvent(event);
        integrator.integrate(equations, initialTime, initialCondition, finalTime, finalCondition);
        assertEquals(eventTime, integrator.getLastStepTime(), 1e-10);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithEventException() throws Exception {
        final FirstOrderDifferentialEquations equationsWithException = new DifferentialEquationsWithEventException();
        final Event event = new When(0.5) {
            @Override
            public void handleEvent(double t, double[] y) throws Exception {
                throw new IntegratorException("An event exception occurred.");
            }
        };
        integrator.eventsHandlersManager.addEvent(event);
        integrator.integrate(equationsWithException, initialTime, initialCondition, finalTime, finalCondition);
    }

    @Test
    public void testResetEvaluations() {
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluationsDone());
    }

    @Test
    public void testSanityChecks() {
        FirstOrderDifferentialEquations equationsWithNull = null;
        try {
            integrator.sanityChecks(equationsWithNull, initialTime, initialCondition, finalTime, finalCondition);
            assertFalse("Sanity checks should fail with NullPointerException", true);
        } catch (NullPointerException e) {
            // expected
        }
    }

    private static class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }

    }

    private static class DifferentialEquationsWithException implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            throw new DeriveException("A derivative exception occurred.");
        }

    }

    private static class DifferentialEquationsWithIntegratorException implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            integrator.integrator = new ExceptionThrowingIntegrator();
            integrator.integrate(this, 0.0, new double[] {0.0}, 1.0, yDot);
        }

    }

    private static class DifferentialEquationsWithEventException implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            final Event event = new When(0.5) {
                @Override
                public void handleEvent(double t, double[] y) throws Exception {
                    throw new IntegratorException("An event exception occurred.");
                }
            };
            integrator.eventsHandlersManager.addEvent(event);
            integrator.integrate(this, 0.0, new double[] {0.0}, 1.0, yDot);
        }

    }

    private static class ExceptionThrowingIntegrator extends AbstractIntegrator {

        @Override
        public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                throws DeriveException, IntegratorException {
            throw new IntegratorException("An integrator exception occurred.");
        }

    }

    private static class RK45Constants {
        static final double[] c = {0.25, 0.45, 0.55, 0.55, 0.45, 0.25};
        static final double[][] a = {
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.25, 0.25, 0.0, 0.0, 0.0, 0.0},
            {0.0, 0.5555555555555556, 0.5555555555555556, 0.0, 0.0, 0.0},
            {0.0, 0, 0.8789045154925261, 0.6220066751235275, 0.0, 0.0},
            {0.0, 0, 0, 0.9238095238095238, 0.9, 0.0},
            {0.0, 0, 0, 0, 0.9623001105763497, 1.0}
        };
        static final double[] b = {0.25, 0.45, 0.55, 0.55, 0.45, 0.25};

    }

    private static class RK45StepInterpolator implements RungeKuttaStepInterpolator {

        @Override
        public int getOrder() {
            return 5;
        }

        @Override
        public double getDensity(double t, double[] y) {
            return 1.0;
        }

        @Override
        public double[] getInterpolatedValues(double t, double[] y, double[][] yDot, int index) {
            return yDot[index];
        }

    }

    private static class StepCounter implements StepHandler {

        private int count = 0;

        @Override
        public void handleStep(RungeKuttaIntegrator integrator, int index) {
            count++;
        }

        @Override
        public void reset() {
            count = 0;
        }

        public int getCount() {
            return count;
        }

    }

    private static class StepPrinter implements StepHandler {

        @Override
        public void handleStep(RungeKuttaIntegrator integrator, int index) {
            System.out.println("Step " + index);
        }

        @Override
        public void reset() {
            // empty
        }

    }

}