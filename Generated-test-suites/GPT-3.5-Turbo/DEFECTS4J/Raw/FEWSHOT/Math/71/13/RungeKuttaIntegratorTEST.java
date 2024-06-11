
package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.AbsorbingStepInterpolator;
import org.apache.commons.math.ode.sampling.DummyStepHandler;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.apache.commons.math.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    private static final double EPSILON = 1e-15;

    private static class SimpleEquations implements FirstOrderDifferentialEquations {

        public int getDimension() {
            return 2;
        }

        public void computeDerivatives(double t, double[] y, double[] dot) {
            dot[0] = y[1];
            dot[1] = -y[0];
        }

    }

    private RungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new RungeKuttaIntegrator("Test", null, null, null, null, 0.01) {
            
            @Override
            public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException {
                return 0;
            }
        };
    }

    @Test
    public void testIntegrate() throws Exception {
        double[] y = new double[] { 1.0, 0.0 };
        RungeKuttaStepInterpolator first = (RungeKuttaStepInterpolator) integrator.createInterpolator(false, 1.0, y, y, new SimpleEquations(), first,
                second, scalAbsoluteTolerance, vecAbsoluteTolerance);
        for (double t = 1.0; t < 8.2; t += 0.01) {
            inter.setPreviousTime(t - 0.01);
            inter.setCurrentTime(t);
            double[] i0 = inter.getInterpolatedState();
            double[] i1 = inter.getInterpolatedDerivatives();
            double x = FastMath.cos(t);
            assertEquals(x, i0[0], 3.0e-9);
            assertEquals(-x, i0[1], 2.0e-9);
            assertEquals(-FastMath.sin(t), i1[0], 7.0e-9);
            assertEquals(-FastMath.sin(t), i1[1], 2.0e-9);
        }
    }
}
