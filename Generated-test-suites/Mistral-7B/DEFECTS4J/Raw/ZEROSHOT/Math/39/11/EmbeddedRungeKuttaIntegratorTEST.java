import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.VectorFunctions;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;

    @Before
    public void setUp() {
        double[] c = {0.0, 0.25, 0.5, 0.875};
        double[][] a = {{0.0, 0.25, 0.5, 0.875}, {0.0, 0.416666666666667, 0.625, 0.9375},
                {0.0, 0.25, 0.5, 0.875}, {0.0, 0.416666666666667, 0.625, 0.9375}};
        double[] b = {0.25, 0.416666666666667, 0.625, 0.9375};
        double[] vecRelativeTolerance = {1e-5, 1e-5};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(2);

        integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaTest", false, c, a, b, prototype,
                0.001, 0.01, null, vecRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y1.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                error = Math.sqrt(error);
                return error;
            }
        };

        equations = new ExpandableStatefulODE("Test equation") {
            private double x;

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return integrator.getOrder();
            }

            @Override
            public int getStateDimension() {
                return getDimension();
            }

            @Override
            public void computePrimaryDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * Math.exp(-1.0) * Math.sin(state[0]);
            }

            @Override
            public void computeSecondaryDerivatives(double time, double[] state, double[] derivatives,
                    int secondary) {
                if (secondary == 0) {
                    derivatives[0] = state[0] * Math.cos(state[0]);
                } else {
                    derivatives[0] = -state[0] * Math.exp(-1.0) * Math.cos(state[0]);
                }
            }

            @Override
            public double getTime() {
                return x;
            }

            @Override
            public void setTime(double time) {
                x = time;
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{x};
            }

            @Override
            public void setCompleteState(double[] state) {
                x = state[0];
            }
        };
    }

    @Test
    public void testIntegration() throws Exception {
        double tStart = 0.0;
        double tEnd   = 10.0;

        integrator.integrate(equations, tStart);

        double tolerance = 1e-8;
        double t         = tStart + (tEnd - tStart) / 2.0;
        double[] state   = equations.getCompleteState();
        double expected = VectorFunctions.expIntegral1E(t);

        double error = Math.abs(state[0] - expected);
        assertEquals("Step 1 error: " + error, error, tolerance);

        integrator.resetInternalState();
        integrator.integrate(equations, tEnd);

        error = Math.abs(equations.getCompleteState()[0] - expected);
        assertEquals("Step 2 error: " + error, error, tolerance);
    }

}