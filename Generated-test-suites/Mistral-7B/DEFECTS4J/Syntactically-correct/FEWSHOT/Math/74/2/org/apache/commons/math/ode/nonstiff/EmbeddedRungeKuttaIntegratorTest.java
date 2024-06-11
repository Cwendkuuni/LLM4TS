package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.noxtools.DummyDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[] c = {0.0, 1.0 / 3.0, 1.0 / 2.0, 1.0};
        double[][] a = {{1.0 / 3.0, 1.0 / 3.0}, {0.25, 0.64375}, {0, 0.6}, {1.0, 0}};
        double[] b = {0.6, 0.25, 0.6, 1.0};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta78Integrator(
                "Embedded Runge-Kutta Fehlberg 7(8) method", true, c, a, b,
                new RungeKuttaStepInterpolator(7), 0.001, 0.01, null, null);

        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        double t = 0.2;

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                System.out.println("Step " + stepNumber + " accepted at time " + interpolator.getTime());
            }
        };

        integrator.addStepHandler(stepHandler);

        double result = integrator.integrate(new DummyDifferentialEquations(), t0, y0, t, null);
        assertEquals(t, result, 1e-9);

        // check interpolation points
        StepInterpolator interpolator = (StepInterpolator) integrator.getInterpolator();
        double[] interpolationPoints = interpolator.getInterpolationPoints();
        assertArrayEquals(new double[]{t0, t0 + 0.1 * c[0], t0 + 0.2 * c[0], t0 + 0.3 * c[0], t0 + 0.6 * c[0], t}, interpolationPoints, 1e-9);
    }
}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0];
    }
}