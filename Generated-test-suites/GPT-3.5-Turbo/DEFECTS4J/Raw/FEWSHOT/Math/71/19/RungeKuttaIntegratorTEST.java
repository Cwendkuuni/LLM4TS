package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math.ode.*;
import org.junit.Test;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Test Integrator", new double[]{0.0, 0.5, 0.5, 1.0}, new double[][]{{}, {0.5}, {0.0, 0.5}, {0.0, 0.0, 1.0}}, new double[]{1.0 / 6.0, 0.0, 2.0 / 6.0, 2.0 / 6.0, 1.0 / 6.0}, new RungeKuttaStepInterpolator(), 1.0);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };
        double t0 = 0.0;
        double[] y0 = new double[]{0.0, 1.0};
        double t = 10.0;
        double[] y = new double[2];
        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
        } catch (DerivativeException e) {
            e.printStackTrace();
        } catch (IntegratorException e) {
            e.printStackTrace();
        }
    }

}