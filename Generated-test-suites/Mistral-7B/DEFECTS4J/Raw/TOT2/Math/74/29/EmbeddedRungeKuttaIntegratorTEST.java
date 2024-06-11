package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKutta5_4Integrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.ODETestConfig;
import org.apache.commons.math3.ode.test.TestODE;
import org.apache.commons.math3.util.Precision;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#initializeStep(FirstOrderDifferentialEquations, boolean, int, double[], double[], double[], double[], double[], double[])}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testInitializeStep() throws Exception {
        final double[] vecAbsoluteTolerance = new double[] {1.0e-14, 1.0e-14};
        final double[] vecRelativeTolerance = new double[] {1.0e-9, 1.0e-9};
        EmbeddedRungeKuttaIntegrator integrator = new RungeKutta5_4Integrator("RK5(4)", true, new double[] {0.3, 0.4, 0}, new double[][] {{0.5, 0.5, 0.5}, {0.55, 0.5, 0.54}, {0.6, 0.4, 0.6}, {0.7, 0.2, 0.7}, {1.0, 0.1, 1.0}}, new double[] {0.5, 0.5, 0.5, 0.5, 0.5}, new double[] {0.236926885056682}, RungeKuttaStepInterpolator.class, 1.0e-8, 1.0e-6, vecAbsoluteTolerance, vecRelativeTolerance);
        FirstOrderDifferentialEquations equations = new TestODE(integrator);

        // Test with default values
        double h = integrator.initializeStep(equations, true, integrator.getOrder(), null, null, null, null, null, null);
        assertEquals(1.0e-8, h, 1.0e-12);

        // Test with minimal step
        h = integrator.initializeStep(equations, true, integrator.getOrder(), null, null, null, null, new double[] {1.0e-12}, null);
        assertEquals(1.0e-12, h, 1.0e-14);

        // Test with maximal step
        h = integrator.initializeStep(equations, true, integrator.getOrder(), null, null, null, null, null, new double[] {1.0e12});
        assertEquals(1.0e12, h, 1.0e12);

        // Test with absolute tolerance
        h = integrator.initializeStep(equations, true, integrator.getOrder(), null, null, null, null, null, new double[] {1.0e-16});
        assertEquals(1.0e-16, h, 1.0e-16);

        // Test with relative tolerance
        h = integrator.initializeStep(equations, true, integrator.getOrder(), null, null, null, null, null, new double[] {1.0e-14, 1.0e-16});
        assertEquals(1.0e-14, h, 1.0e-14);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double[], double, double[])}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIntegrate() throws Exception {
        final double[] vecAbsoluteTolerance = new double[] {1.0e-14, 1.0e-14};
        final double[] vecRelativeTolerance = new double[] {1.0e-9, 1.0e-9};
        EmbeddedRungeKuttaIntegrator integrator = new RungeKutta5_4Integrator("RK5(4)", true, new double[] {0.3, 0.4, 0}, new double[][] {{0.5, 0.5, 0.5}, {0.55, 0.5, 0.54}, {0.6, 0.4, 0.6}, {0.7, 0.2, 0.7}, {1.0, 0.1, 1.0}}, new double[] {0.5, 0.5, 0.5, 0.5, 0.5}, new double[] {0.236926885056682}, RungeKuttaStepInterpolator.class, 1.0e-8, 1.0e-6, vecAbsoluteTolerance, vecRelativeTolerance);
        FirstOrderDifferentialEquations equations = new TestODE(integrator);

        // Test with default tolerances
        double t = 1.0;
        double[] y0 = new double[] {0.0, 0.0};
        double[] y = new double[] {1.0, 1.0};
        double result = integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(t, result, 1.0e-12);

        // Test with minimal tolerances
        t = 1.0;
        y0 = new double[] {1.0e-12, 1.0e-12};
        y = new double[] {1.0, 1.0};
        result = integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(t, result, 1.0e-12);

        // Test with maximal tolerances
        t = 1.0;
        y0 = new double[] {1.0e12, 1.0e12};
        y = new double[] {1.0, 1.0};
        result = integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(t, result, 1.0e12);

        // Test with absolute tolerance only
        t = 1.0;
        y0 = new double[] {1.0e-14, 1.0e-14};
        y = new double[] {1.0, 1.0};
        result = integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(t, result, 1.0e-14);

        // Test with relative tolerance only
        t = 1.0;
        y0 = new double[] {1.0, 1.0};
        y = new double[] {1.0e-9, 1.0e-9};
        result = integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(t, result, 1.0e-9);

        // Test with events
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(integrator, new double[] {0.5}, new double[] {0.5}, StepHandler.CLASSICAL);
        equations = new TestODE(integrator, new When(0.5, new TestEvent()));
        t = 1.0;
        y0 = new double[] {0.0, 0.0};
        y = new double[] {1.0, 1.0};
        result = integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(0.5, result, 1.0e-12);
        assertEquals(1.0, interpolator.getStep(), 1.0e-12);
    }

    /**
     * Test event.
     */
    static class TestEvent extends Event {

        /**
         * Constructor.
         */
        TestEvent() {
            super(Double.POSITIVE_INFINITY, null, null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void evaluateLhs(double t, double[] y) {
            fail("Should not be called");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void evaluateRhs(double t, double[] y, double[] dydt) {
            dydt[0] = 1.0;
            dydt[1] = 1.0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName() {
            return "TestEvent";
        }

    }

    /**
     * Test ODE.
     */
    static class TestODE implements FirstOrderDifferentialEquations {

        /** Integrator. */
        private final EmbeddedRungeKuttaIntegrator integrator;

        /** Event handler manager. */
        private final CombinedEventsManager eventsHandlerManager;

        /**
         * Constructor.
         * @param integrator integrator
         */
        TestODE(EmbeddedRungeKuttaIntegrator integrator) {
            this.integrator = integrator;
            this.eventsHandlerManager = new CombinedEventsManager();
        }

        /**
         * Constructor.
         * @param integrator integrator
         * @param events events
         */
        TestODE(EmbeddedRungeKuttaIntegrator integrator, Event... events) {
            this.integrator = integrator;
            for (Event event : events) {
                this.eventsHandlerManager.addRelated(event);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            integrator.computeDerivatives(t, y);
            System.arraycopy(integrator.getYDot(), 0, yPrime, 0, yPrime.length);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName() {
            return integrator.getName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isAutoTimeStepAdjustable() {
            return integrator.isAutoTimeStepAdjustable();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double getMinStep() {
            return integrator.getMinStep();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double getMaxStep() {
            return integrator.getMaxStep();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double getStepSize() {
            return integrator.getStepSize();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setStepSize(double stepSize) {
            integrator.setStepSize(stepSize);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void addEvent(Event event) {
            eventsHandlerManager.addRelated(event);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeEvent(Event event) {
            eventsHandlerManager.removeRelated(event);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void clearEvents() {
            eventsHandlerManager.clear();
        }
    }

}