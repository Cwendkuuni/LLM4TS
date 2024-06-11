import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sample.OdeTestUtils;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Create a test equation
        ExpandableStatefulODE equation = new ExpandableStatefulODE() {
            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0];
            }

            @Override
            public int getDimensions() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public double getTime() {
                return 0;
            }
        };

        // Initialize the integrator with a test Butcher array
        double[] c = {0.0, 0.236926885, 0.678724126, 0.0};
        double[][] a = {{0.0, 0.236926885, 0.678724126, 0.0},
                        {0.236926885, 0.678724126, 0.865063889, 0.236926885},
                        {0.678724126, 0.865063889, 1.0, 0.678724126}};
        double[] b = {0.236926885, 0.678724126, 1.0};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b,
                new RungeKuttaStepInterpolator(), 1e-8, 1e-8, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(FastMath.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        // Set up test data
        double[] initialState = {1.0};
        double startTime = 0.0;
        double endTime = 1.0;

        // Test the integrator
        OdeTestUtils.testIntegrator(equation, integrator, initialState, startTime, endTime);

        // Test the integrator with a smaller step size
        integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b,
                new RungeKuttaStepInterpolator(), 1e-9, 1e-9, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(FastMath.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };
        OdeTestUtils.testIntegrator(equation, integrator, initialState, startTime, endTime);

        // Test the integrator with a larger step size
        integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b,
                new RungeKuttaStepInterpolator(), 1e-7, 1e-7, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(FastMath.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };
        OdeTestUtils.testIntegrator(equation, integrator, initialState, startTime, endTime);
    }
}