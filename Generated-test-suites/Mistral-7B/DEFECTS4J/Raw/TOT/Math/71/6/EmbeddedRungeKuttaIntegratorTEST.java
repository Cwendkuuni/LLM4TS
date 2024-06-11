package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.special.E;
import org.apache.commons.math3.special.MathConstants;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testRK2_Gauss_1() {
        EmbeddedRungeKuttaIntegrator rk2 = new RungeKutta2Integrator();
        double[] y0 = {1.0, 1.0};
        double[] y1 = {2.0, 2.0};
        double t0 = 0.0;
        double t = 1.0;
        double[][] equations = new double[][]{{0.0, 1.0}, {1.0, 0.0}};

        assertEquals(t, rk2.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }
        }, t0, y0, t, y1));
    }

    @Test
    public void testRK2_Gauss_2() {
        EmbeddedRungeKuttaIntegrator rk2 = new RungeKutta2Integrator();
        double[] y0 = {Math.E, MathConstants.PI / 2.0};
        double[] y1 = {Math.exp(1.0) * Math.sin(Math.PI / 2.0),
                Math.exp(1.0) * Math.cos(Math.PI / 2.0)};
        double t0 = 0.0;
        double t = Math.PI;
        double[][] equations = new double[][]{{-1.0 * Math.sin(1.0), Math.cos(1.0)},
                {Math.cos(1.0), Math.sin(1.0)}};

        assertEquals(t, rk2.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = -y[1];
                dy[1] = y[0];
            }
        }, t0, y0, t, y1));
    }

    @Test(expected = IntegratorException.class)
    public void testRK2_singular_matrix() {
        EmbeddedRungeKuttaIntegrator rk2 = new RungeKutta2Integrator();
        double[] y0 = {1.0, 0.0};
        double[] y1 = {-1.0, 0.0};
        double t0 = 0.0;
        double t = 1.0;
        double[][] equations = new double[][]{{1.0, -1.0}, {-1.0, 1.0}};

        rk2.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = equations[0][0] * y[0] + equations[0][1] * y[1];
                dy[1] = equations[1][0] * y[0] + equations[1][1] * y[1];
            }
        }, t0, y0, t, y1);
    }

    @Test
    public void testRK4_Gauss_1() {
        EmbeddedRungeKuttaIntegrator rk4 = new RungeKutta4Integrator();
        double[] y0 = {1.0, 1.0};
        double[] y1 = {2.0, 2.0};
        double t0 = 0.0;
        double t = 1.0;
        double[][] equations = new double[][]{{0.0, 1.0}, {1.0, 0.0}};

        assertEquals(t, rk4.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }
        }, t0, y0, t, y1));
    }

    @Test
    public void testRK4_Gauss_2() {
        EmbeddedRungeKuttaIntegrator rk4 = new RungeKutta4Integrator();
        double[] y0 = {Math.E, MathConstants.PI / 2.0};
        double[] y1 = {Math.exp(1.0) * Math.sin(Math.PI / 2.0),
                Math.exp(1.0) * Math.cos(Math.PI / 2.0)};
        double t0 = 0.0;
        double t = Math.PI;
        double[][] equations = new double[][]{{-1.0 * Math.sin(1.0), Math.cos(1.0)},
                {Math.cos(1.0), Math.sin(1.0)}};

        assertEquals(t, rk4.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = -y[1];
                dy[1] = y[0];
            }
        }, t0, y0, t, y1));
    }

    @Test(expected = IntegratorException.class)
    public void testRK4_singular_matrix() {
        EmbeddedRungeKuttaIntegrator rk4 = new RungeKutta4Integrator();
        double[] y0 = {1.0, 0.0};
        double[] y1 = {-1.0, 0.0};
        double t0 = 0.0;
        double t = 1.0;
        double[][] equations = new double[][]{{1.0, -1.0}, {-1.0, 1.0}};

        rk4.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = equations[0][0] * y[0] + equations[0][1] * y[1];
                dy[1] = equations[1][0] * y[0] + equations[1][1] * y[1];
            }
        }, t0, y0, t, y1);
    }

}