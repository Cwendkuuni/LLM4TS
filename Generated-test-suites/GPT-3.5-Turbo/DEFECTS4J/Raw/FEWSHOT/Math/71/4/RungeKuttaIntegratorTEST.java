

package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate_InitialParameters_ReturnsCorrectResults() throws DerivativeException, IntegratorException {
        // Setup
        double[] c = {1.0, 2.0};
        double[][] a = {{1.0, 1.0}, {1.0, 1.0}};
        double[] b = {1.0, 1.0};
        RungeKuttaStepInterpolator prototype = mock(RungeKuttaStepInterpolator.class);
        double step = 0.1;
        String name = "Test Integrator";
        RungeKuttaIntegrator integrator = new RungeKuttaIntegratorImpl(name, c, a, b, prototype, step);

        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[2];
        
        when(prototype.copy()).thenReturn(prototype);
        when(prototype.reinitialize(isA(RungeKuttaIntegrator.class), any(double[].class), any(double[][].class), anyBoolean())).thenReturn(prototype);
        when(equations.getDimension()).thenReturn(2);
        
        // Run the test
        double result = integrator.integrate(equations, t0, y0, t, y);

        // Verify the results
        verify(prototype, atLeastOnce()).copy();
        verify(prototype).reinitialize(integrator, y, new double[3][2], true);
        verify(equations, atLeastOnce()).computeDerivatives(eq(t0), eq(y0), any(double[].class));
        verify(equations, atLeastOnce()).computeDerivatives(eq(t0 + 1.0), any(double[].class), any(double[].class));
        verify(prototype).storeTime(t0);
        verify(prototype).shift();
        verify(equations, atLeastOnce()).computeDerivatives(eq(t0 + 1.0), any(double[].class), any(double[].class));
        verify(prototype).storeTime(t0 + 0.1);
        verify(prototype, atLeastOnce()).getSolverRelativeTolerance();
        verify(prototype, atLeastOnce()).isLastStep();
        verify(equations, atLeastOnce()).computeDerivatives(eq(t0 + 0.1), any(double[].class), any(double[].class));
        verify(prototype).storeTime(t0 + 0.2);
        verify(prototype, atLeastOnce()).resetOccurred();
        verify(equations, atLeastOnce()).computeDerivatives(eq(t0 + 0.2), any(double[].class), any(double[].class));
        verify(prototype).storeTime(t0 + 0.3);
        verify(equations, atLeastOnce()).computeDerivatives(eq(t0 + 0.3), any(double[].class), any(double[].class));
        verify(prototype).storeTime(t0 + 0.4);
        verify(equations, atLeastOnce()).computeDerivatives(eq(t0 + 0.4), any(double[].class), any(double[].class));
        verify(prototype).storeTime(t0 + 0.5);
        verify(equations, atLeastOnce()).computeDerivatives(eq(t0 + 0.5), any(double[].class), any(double[].class));
        verify(prototype).storeTime(t);
        verify(equations, atLeastOnce()).computeDerivatives(eq(t), any(double[].class), any(double[].class));
        verify(equations, atLeastOnce()).computeDerivatives(eq(t), any(double[].class), any(double[].class));
        verify(equations, atLeastOnce()).computeDerivatives(eq(t), any(double[].class), any(double[].class));
        verify(prototype).getCurrentTime();
        verify(prototype, atLeastOnce()).setStepStart(eq(0.0));
        verify(prototype, atLeastOnce()).setStepSize(eq(0.1));
        verify(prototype).reinitialize(integrator, y, new double[3][2], true);
        verify(equations).getDimension();
        verify(prototype, atLeastOnce()).resetOccurred();
        verify(prototype).reinitialize(integrator, y, new double[3][2], true);
        verify(equations).getDimension();
        verify(prototype).shift();
        verify(equations, atLeastOnce()).computeDerivatives(eq(t0), eq(y0), any(double[].class));
        verify(prototype).storeTime(t0);
        verify(prototype).shift();
        verify(equations, atLeastOnce()).computeDerivatives(eq(t0 + 1.0), any(double[].class), any(double[].class));
        verify(prototype).storeTime(t0 + 0.1);
        verify(prototype, atLeastOnce()).getSolverRelativeTolerance();
        verify(prototype).storeTime(t0);
        verify(prototype, atLeastOnce()).setStepStart(eq(0.0));
        verify(prototype, atLeastOnce()).setStepSize(eq(0.1));
        verify(prototype).reinitialize(integrator, y, new double[3][2], true);
        verify(equations, atLeastOnce()).computeDerivatives(eq(t0), eq(y0), any(double[].class));
        verify(prototype).storeTime(t0);
        verify(prototype).getSolverRelativeTolerance();

        assertEquals(1.0, result, 0.0001);
        assertArrayEquals(new double[] {1.0, 2.0}, y, 0.0001);
    }

    @SuppressWarnings("serial")
    private class RungeKuttaIntegratorImpl extends RungeKuttaIntegrator {

        public RungeKuttaIntegratorImpl(String name, double[] c, double[][] a, double[] b,
                RungeKuttaStepInterpolator prototype, double step) {
            super(name, c, a, b, prototype, step);
        }

        protected void computeDerivatives(double t, double[] y, double[] yDot)
                throws DerivativeException {

        }

        @Override
        protected boolean requiresDenseOutput() {
            return false;
        }

        @Override
        protected StepHandler getStepHandler() {
            return null;
        }

        @Override
        protected void sanityChecks(FirstOrderDifferentialEquations equations,
                double t0, double[] y0, double t, double[] y)
                throws IllegalArgumentException {
            
        }

        @Override
        protected boolean isLastStep() {
            return true;
        }

    }
}
