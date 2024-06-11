package org.apache.commons.math.ode.nonstiff;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.Verifier;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbedgedRungeKuttaIntegratorTest extends TestCase {

    /**
     * Test the classical Runge-Kutta 4th order method.
     */
    public void testRK4() {
        final double a = 0.01;
        final double b = 1.0;
        final double tEnd = 1.0;
        final double y0 = 1.0;
        final double[] y = new double[] {1.0, 0.0};
        final double dt = 0.01;
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4, true);
        final EmbeddedRungeKuttaIntegrator rk4 = new RK4(a, prototype, dt, 0.001, 0.01, null, null);

        rk4.integrate(new SimpleODE(BesselFunctions.besselJ0), y[0], tEnd);
        final double[] expected = new double[] {1.0, 0.0};
        assertEquals("RK4 did not integrate to expected value", expected, y);
    }

    /**
     * Test the Runge-Kutta 4th order method with absolute and relative tolerance.
     */
    public void testRK4WithTol() {
        final double a = 0.01;
        final double b = 1.0;
        final double tEnd = 1.5;
        final double y0 = 1.0;
        final double[] y = new double[] {1.0, 0.0};
        final double dt = 0.05;
        final double absTol = 1e-6;
        final double relTol = 1e-4;
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4, true);
        final EmbeddedRungeKuttaIntegrator rk4 = new RK4(a, prototype, dt, 0.0, tEnd, new double[] {absTol, relTol}, null);

        rk4.integrate(new SimpleODE(BesselFunctions.besselJ0), y[0], tEnd);
        final double[] expected = new double[] {1.2420405484538614, -0.6210202741119307};
        assertEquals("RK4 did not integrate to expected value with tolerance", expected, y, 1e-4);
    }

    /**
     * Test the Runge-Kutta 4th order method with error handling.
     */
    public void testRK4WithError() {
        final double a = 0.01;
        final double b = 1.0;
        final double tEnd = 1.0;
        final double y0 = 1.0 / 0.0;
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4, true);
        final EmbeddedRungeKuttaIntegrator rk4 = new RK4(a, prototype, 0.01, 0.001, 0.01, null, null);

        try {
            rk4.integrate(new SimpleODE(BesselFunctions.besselJ0), y0, tEnd);
            fail("RK4 did not throw MathIllegalArgumentException on singular initial condition");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test the Runge-Kutta 5th order method.
     */
    public void testRK5() {
        final double a = 0.01;
        final double b = 1.0;
        final double tEnd = 1.0;
        final double y0 = 1.0;
        final double[] y = new double[] {1.0, 0.0, 0.0, 0.0, 0.0};
        final double dt = 0.01;
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(5, false);
        final EmbeddedRungeKuttaIntegrator rk5 = new RK5(a, prototype, dt, 0.001, 0.01, null, null);

        rk5.integrate(new SimpleODE(BesselFunctions.besselJ0), y[0], tEnd);
        final double[] expected = new double[] {1.0, 0.0, 0.0, 0.0, 0.0};
        assertEquals("RK5 did not integrate to expected value", expected, y);
    }

    /**
     * Test the Runge-Kutta 5th order method with absolute and relative tolerance.
     */
    public void testRK5WithTol() {
        final double a = 0.01;
        final double b = 1.0;
        final double tEnd = 1.5;
        final double y0 = 1.0;
        final double[] y = new double[] {1.0, 0.0, 0.0, 0.0, 0.0};
        final double dt = 0.05;
        final double absTol = 1e-6;
        final double relTol = 1e-4;
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(5, false);
        final EmbeddedRungeKuttaIntegrator rk5 = new RK5(a, prototype, dt, 0.0, tEnd, new double[] {absTol, relTol}, null);

        rk5.integrate(new SimpleODE(BesselFunctions.besselJ0), y[0], tEnd);
        final double[] expected = new double[] {1.2420405484538614, 0.02215163932499372,
                                                -0.01107581966249686, 0.00558790983124843,
                                                -0.002793954915624215};
        assertEquals("RK5 did not integrate to expected value with tolerance", expected, y, 1e-4);
    }

    /**
     * Test the Runge-Kutta 5th order method with error handling.
     */
    public void testRK5WithError() {
        final double a = 0.01;
        final double b = 1.0;
        final double tEnd = 1.0;
        final double y0 = 1.0 / 0.0;
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(5, false);
        final EmbeddedRungeKuttaIntegrator rk5 = new RK5(a, prototype, 0.01, 0.001, 0.01, null, null);

        try {
            rk5.integrate(new SimpleODE(BesselFunctions.besselJ0), y0, tEnd);
            fail("RK5 did not throw MathIllegalArgumentException on singular initial condition");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test the accuracy of the Runge-Kutta 4th order method.
     */
    public void testRK4Accuracy() {
        final double a = 0.01;
        final double b = 1.0;
        final double tEnd = 1.0;
        final double y0 = 1.0;
        final double[] y = new double[] {1.0, 0.0};
        final double dt = 0.01;
        final double absTol = 1e-11;
        final double relTol = 1e-9;
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4, true);
        final EmbeddedRungeKuttaIntegrator rk4 = new RK4(a, prototype, dt, 0.0, tEnd, new double[] {absTol, relTol}, null);

        final double[] yExact = new double[] {1.0, 0.0};
        Verifier.verify(new SimpleODE(BesselFunctions.besselJ0), yExact, dt, y0, tEnd, 1e-12);
        rk4.integrate(new SimpleODE(BesselFunctions.besselJ0), y[0], tEnd);
        double[] yResult = new double[] {y[0], y[1]};
        Verifier.verify(new SimpleODE(BesselFunctions.besselJ0), yExact, dt, yResult, tEnd, 1e-12);
    }

    /**
     * Test the accuracy of the Runge-Kutta 5th order method.
     */
    public void testRK5Accuracy() {
        final double a = 0.01;
        final double b = 1.0;
        final double tEnd = 1.0;
        final double y0 = 1.0;
        final double[] y = new double[] {1.0, 0.0, 0.0, 0.0, 0.0};
        final double dt = 0.01;
        final double absTol = 1e-11;
        final double relTol = 1e-9;
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(5, false);
        final EmbeddedRungeKuttaIntegrator rk5 = new RK5(a, prototype, dt, 0.0, tEnd, new double[] {absTol, relTol}, null);

        final double[] yExact = new double[] {1.0, 0.0, 0.0, 0.0, 0.0};
        Verifier.verify(new SimpleODE(BesselFunctions.besselJ0), yExact, dt, y[0], tEnd, 1e-12);
        rk5.integrate(new SimpleODE(BesselFunctions.besselJ0), y[0], tEnd);
        double[] yResult = new double[] {y[0], y[1], y[2], y[3], y[4]};
        Verifier.verify(new SimpleODE(BesselFunctions.besselJ0), yExact, dt, yResult, tEnd, 1e-12);
    }

    /**
     * A simple ODE for testing.
     */
    private static class SimpleODE implements ExpandableStatefulODE {
        final org.apache.commons.math3.special.BesselFunctions besselJ0;

        public SimpleODE(org.apache.commons.math3.special.BesselFunctions besselJ0) {
            this.besselJ0 = besselJ0;
        }

        public int getDimension() {
            return 2;
        }

        public int getOrder() {
            return 0;
        }

        public double[] getDerivatives(double t, double[] y) {
            double[] derivatives = new double[y.length];
            for (int i = 0; i < derivatives.length; ++i) {
                derivatives[i] = besselJ0.value(y[i]);
            }
            return derivatives;
        }

        public void setTime(double t) {
            // not used
        }

        public double getTime() {
            return 0.0;
        }

        public void setCompleteState(double[] y) {
            // not used
        }

        public double[] getCompleteState() {
            return new double[] {y[0], y[1]};
        }

        public void setPrimaryMapper(StateMapper mapper) {
            // not used
        }

        public void setSecondaryMappers(StateMapper[] secondaryMappers) {
            // not used
        }

        public void initialize(double t, double[] y, double[] yDot, double[] work) {
            // not used
        }

        public void stateSubroutine(double t, double[] y, double[] yDot, double[] work) {
            for (int i = 0; i < y.length; ++i) {
                yDot[i] = besselJ0.value(y[i]);
            }
        }

        public String getName() {
            return "Simple ODE";
        }
    }

}