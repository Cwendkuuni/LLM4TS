import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[] c = {0.0, 0.2, 0.3, 0.6};
        double[][] a = {{0.0, 0.0, 0.0, 0.0},
                        {0.2, 0.4472, 0.0, 0.0},
                        {0.3, 0.0, 0.689, 0.0},
                        {0.6, -1.216, 1.356, 1.0}};
        double[] b = {0.0, 0.235, 0.354, 0.5};
        double minStep = 0.001;
        double maxStep = 0.01;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaMethod", true, c, a, b,
                new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += FastMath.pow(FastMath.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE("testEquations") {
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
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0 + y[0];
            }
        };

        double t = 0.0;
        integrator.integrate(equations, t);
        double[] y = equations.getCompleteState();
        assertEquals(1.0, y[0], 1e-6);

        t = 1.0;
        integrator.integrate(equations, t);
        y = equations.getCompleteState();
        assertEquals(2.0, y[0], 1e-6);

        t = 2.0;
        integrator.integrate(equations, t);
        y = equations.getCompleteState();
        assertEquals(3.0, y[0], 1e-6);
    }

}