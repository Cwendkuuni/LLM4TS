package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        double[] c = {0.2, 0.3, 0.4, 0.5};
        double[][] a = {{0.2}, {0.3, 0.4}, {0.5, 0.6, 0.7}, {0.8, 0.9, 1.0, 1.1}};
        double[] b = {1.2, 1.3, 1.4, 1.5, 1.6};
        RungeKuttaStepInterpolator prototype = mock(RungeKuttaStepInterpolator.class);
        double step = 0.1;
        integrator = new RungeKuttaIntegratorImpl("test", c, a, b, prototype, step);
    }

    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {0.1, 0.2, 0.3};
        double t = 1.0;
        double[] y = new double[y0.length];

        double result = integrator.integrate(equations, t0, y0, t, y);

        // Verify that the integrator calls the necessary methods
        verify(equations, times(4)).computeDerivatives(anyDouble(), any(double[].class), any(double[].class));

        assertEquals(t, result, 0.0);
    }

    @Test
    public void testIntegrateWithNullEquations() {
        double t0 = 0.0;
        double[] y0 = {0.1, 0.2, 0.3};
        double t = 1.0;
        double[] y = new double[y0.length];

        try {
            integrator.integrate(null, t0, y0, t, y);
        } catch (Exception e) {
            assertNull(equations);
        }
    }

    @Test
    public void testIntegrateWithNegativeStep() {
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {0.1, 0.2, 0.3};
        double t = 1.0;
        double[] y = new double[y0.length];
        integrator = new RungeKuttaIntegratorImpl("test", equations, t0, y0, -0.1);

        double result = integrator.integrate(equations, t0, y0, t, y);

        // Verify that the integrator calls the necessary methods
        verify(equations, times(4)).computeDerivatives(anyDouble(), any(double[].class), any(double[].class));

        assertEquals(t, result, 0.0);
    }

    @Test
    public void testIntegrateWithStepSizeZero() {
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {0.1, 0.2, 0.3};
        double t = 1.0;
        double[] y = new double[y0.length];
        integrator = new RungeKuttaIntegratorImpl("test", equations, t0, y0, 0.0);

        double result = integrator.integrate(equations, t0, y0, t, y);

        // Verify that the integrator does not call the computeDerivatives method
        verify(equations, never()).computeDerivatives(anyDouble(), any(double[].class), any(double[].class));

        assertEquals(t, result, 0.0);
    }

    @Test
    public void testIntegrateWithEvents() {
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {0.1, 0.2, 0.3};
        double t = 1.0;
        double[] y = new double[y0.length];
        StepHandler handler = mock(StepHandler.class);
        integrator.addStepHandler(handler);
        CombinedEventsManager manager = mock(CombinedEventsManager.class);
        integrator.setEventsHandlersManager(manager);

        double result = integrator.integrate(equations, t0, y0, t, y);

        // Verify that the integrator calls the necessary methods
        verify(equations, times(4)).computeDerivatives(anyDouble(), any(double[].class), any(double[].class));
        verify(handler, times(5)).handleStep(any(DummyStepInterpolator.class), anyBoolean());
        verify(manager, times(1)).reset(anyDouble(), any(double[].class));

        assertEquals(t, result, 0.0);
    }

    // Private inner class that extends RungeKuttaIntegrator to be able to create an instance for testing
    private class RungeKuttaIntegratorImpl extends RungeKuttaIntegrator {

        public RungeKuttaIntegratorImpl(String name, double[] c, double[][] a, double[] b,
                RungeKuttaStepInterpolator prototype, double step) {
            super(name, c, a, b, prototype, step);
        }

        public RungeKuttaIntegratorImpl(String name, FirstOrderDifferentialEquations equations,
                double t0, double[] y0, double step) {
            super(name, new double[0], new double[0][0], new double[0], null, step);
        }

        @Override
        protected boolean requiresDenseOutput() {
            return false;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public int getIterations() {
            return 0;
        }

        @Override
        public void resetEvaluations() {
        }

        @Override
        protected void computeDerivatives(double t, double[] y, double[] yDot) {
        }

    }
}