package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    private double stepSize;
    private RungeKuttaIntegrator rkIntegrator;

    @Before
    public void setUp() {
        stepSize = 0.01;
        double[] timeSteps = {0.0, 0.5, 0.5};
        double[][] weights = {{0.0}, {0.5}, {0.0, 0.5}};
        double[] bWeights = {0.0, 0.5};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        rkIntegrator = new RungeKuttaIntegrator("Runge-Kutta", timeSteps, weights, bWeights, prototype, stepSize) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yDot) {
                // Do nothing for testing purposes
            }
        };
    }

    @Test
    public void testIntegrate() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 2.0 * y[0];
            }
        };

        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[1];

        rkIntegrator.integrate(equations, t0, y0, t, y);

        assertEquals(2.718281828459045, y[0], 1e-12);
    }

    @Test
    public void testIntegrateWithLargeStepSize() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 2.0 * y[0];
            }
        };

        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 10.0;
        double[] y = new double[1];

        rkIntegrator.integrate(equations, t0, y0, t, y);

        assertEquals(22026.46579480672, y[0], 1e-12);
    }

    @Test(expected = DerivativeException.class)
    public void testComputeDerivatives_throwsDerivativeException() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                throw new DerivativeException("Derivative exception");
            }
        };

        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[1];

        rkIntegrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrate_throwsIntegratorException() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 2.0 * y[0];
            }
        };

        double t0 = 0.0;
        double[] y0 = {1.0, 1.0};
        double t = 1.0;
        double[] y = new double[1];

        rkIntegrator.integrate(equations, t0, y0, t, y);
    }

}