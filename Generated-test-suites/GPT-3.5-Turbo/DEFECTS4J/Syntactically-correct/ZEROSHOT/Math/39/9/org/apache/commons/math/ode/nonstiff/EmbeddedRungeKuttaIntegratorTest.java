package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.util.FastMath;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() throws MathIllegalStateException, MathIllegalArgumentException {
        double minStep = 0.0001;
        double maxStep = 0.1;
        double scalAbsoluteTolerance = 1e-8;
        double scalRelativeTolerance = 1e-8;

        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("Test", true, null, null, null, null, minStep, maxStep,
                        scalAbsoluteTolerance, scalRelativeTolerance) {

                    @Override
                    public int getOrder() {
                        return 1;
                    }

                    @Override
                    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                        return Math.abs(y1[0] - y0[0]);
                    }
                };

        ExpandableStatefulODE ode = new ExpandableStatefulODE(new DummyODE());

        double[] y = {0};
        ode.setTime(0);
        ode.setPrimaryState(y);

        double t = 1;

        integrator.integrate(ode, t);

        assertEquals(t, ode.getTime(), 0);
        assertEquals(2 * t, ode.getPrimaryState()[0], scalAbsoluteTolerance);
    }

    private static class DummyODE implements ODE {
        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = 2;
        }
    }

}