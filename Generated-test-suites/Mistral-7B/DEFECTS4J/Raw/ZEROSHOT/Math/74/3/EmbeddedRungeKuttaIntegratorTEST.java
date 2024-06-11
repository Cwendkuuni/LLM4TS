package org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueHolder;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.RelativetimeEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testRK4() {
        EmbeddedRungeKuttaIntegrator rk4 = new RK4Method();
        FirstOrderDifferentialEquations equations = new DummyEquations();

        double[] y0 = {0.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;

        try {
            double t = rk4.integrate(equations, t0, y0, t1, new double[y0.length]);
            assertEquals(t1, t, 1e-14);
            assertArrayEquals(new double[]{1.0, 0.0}, y0, 1e-14);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRK4WithEvents() {
        EmbeddedRungeKuttaIntegrator rk4 = new RK4Method();
        FirstOrderDifferentialEquations equations = new DummyEquationsWithEvent();

        double[] y0 = {0.0, 1.0};
        double t0 = 0.0;
        double t1 = 2.0;

        try {
            double t = rk4.integrate(equations, t0, y0, t1, new double[y0.length]);
            assertEquals(t1, t, 1e-14);
            assertArrayEquals(new double[]{1.36268, -1.36268}, y0, 1e-14);
        } catch (DerivativeException | IntegratorException | MaxCountExceededException | NumberIsTooSmallException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRKF54() {
        EmbeddedRungeKuttaIntegrator rkf54 = new RKF54Method();
        FirstOrderDifferentialEquations equations = new DummyEquations();

        double[] y0 = {0.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;

        try {
            double t = rkf54.integrate(equations, t0, y0, t1, new double[y0.length]);
            assertEquals(t1, t, 1e-14);
            assertArrayEquals(new double[]{1.0, 0.0}, y0, 1e-14);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRKF54WithEvents() {
        EmbeddedRungeKuttaIntegrator rkf54 = new RKF54Method();
        FirstOrderDifferentialEquations equations = new DummyEquationsWithEvent();

        double[] y0 = {0.0, 1.0};
        double t0 = 0.0;
        double t1 = 2.0;

        try {
            double t = rkf54.integrate(equations, t0, y0, t1, new double[y0.length]);
            assertEquals(t1, t, 1e-14);
            assertArrayEquals(new double[]{1.36268, -1.36268}, y0, 1e-14);
        } catch (DerivativeException | IntegratorException | MaxCountExceededException | NumberIsTooSmallException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    static class DummyEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = -y[1];
            yPrime[1] = y[0];
        }

        @Override
        public String getName() {
            return "DummyEquations";
        }
    }

    static class DummyEquationsWithEvent implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = -y[1];
            yPrime[1] = y[0];
        }

        @Override
        public String getName() {
            return "DummyEquationsWithEvent";
        }

        @Override
        public Event[] getEvents(double t, double[] y) {
            return new Event[]{new RelativetimeEvent(1.0, new DefaultValueHolder(new double[]{1.0, 1.0}))};
        }

        @Override
        public int getEventIndex(Event event) {
            return 0;
        }

        @Override
        public double eventTime(int index) {
            return 1.0;
        }
    }

}

class RK4Method extends EmbeddedRungeKuttaIntegrator {

    public RK4Method() {
        super("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{
                        {0.0, 0.0, 0.0, 0.0},
                        {0.25, 0.25, 0.25, 0.25},
                        {0.0, 0.25, 0.5, 0.25},
                        {0.0, 0.0, 0.5, 0.5},
                        {0.0, 0.0, 0.5, 0.5}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), 1e-9, 1e-9, null, null);
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y, double[] yTmp, double h) {
        double error = 0.0;
        for (int i = 0; i < y.length; i++) {
            error += Math.abs(yDotK[stages - 1][i] - yDotK[0][i]);
        }
        return error;
    }
}

class RKF54Method extends EmbeddedRungeKuttaIntegrator {

    public RKF54Method() {
        super("RKF54", true, new double[]{0.166666667, 0.333333333, 0.333333333, 0.25},
                new double[][]{
                        {0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                        {0.2, 0.0, 0.0, 0.2, 0.0, 0.2},
                        {0.2, 0.2, 0.0, 0.4, 0.0, 0.4},
                        {0.2, 0.2, 0.2, 0.6, 0.0, 0.6},
                        {0.2, 0.2, 0.2, 0.6, 0.2, 0.8},
                        {0.2, 0.2, 0.2, 0.6, 0.2, 0.8}},
                new double[]{0.2, 0.2, 0.2, 0.2, 0.2, 0.2},
                new RungeKuttaStepInterpolator(), 1e-9, 1e-9, null, null);
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y, double[] yTmp, double h) {
        double error = 0.0;
        for (int i = 0; i < y.length; i++) {
            double ej = yDotK[stages - 1][i] - yDotK[0][i];
            error += Math.abs(ej);
        }
        return error;
    }
}