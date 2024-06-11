import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testSimpleIntegration() {
        double[][] coefficients = {{0.0, 0.3, 0.6, 1.0},
                                   {0.0, 0.27, 0.45, 0.62, 0.74},
                                   {}};
        double[][] weights = {{1.0/6.0, 2.0/3.0, 2.0/3.0, 1.0/6.0},
                             {0.25, 0.4472135955, 0.691116061, 0.25},
                             {}};
        double minStep = 0.001;
        double maxStep = 10.0;
        double scalAbsoluteTolerance = 1.0e-11;
        double scalRelativeTolerance = 1.0e-5;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta method 1", true, coefficients[0], coefficients[0], weights[0], new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.pow(Precision.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE("timestepper test equation") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void calculateDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 2.0;
            }
        };

        integrator.integrate(equations, 0.0);
        double[] y0 = equations.getCompleteState();

        integrator.integrate(equations, 1.0);
        double[] y1 = equations.getCompleteState();

        double[] expected = new double[]{1.0, 2.0};
        assertEquals("Integration failed: initial and final states do not match expected values", expected, y1);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalArgumentException() {
        double[][] coefficients = {{0.0, 1.0}, {0.0, 0.3, 0.6, 1.0}, {}};
        double[][] weights = {{1.0/6.0, 2.0/3.0, 2.0/3.0, 1.0/6.0},
                             {0.25, 0.4472135955, 0.691116061, 0.25},
                             {}};
        double minStep = 0.001;
        double maxStep = 10.0;
        double scalAbsoluteTolerance = 1.0e-11;
        double scalRelativeTolerance = 1.0e-5;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta method 1", true, coefficients[0], coefficients[0], weights[0], new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        ExpandableStatefulODE equations = new ExpandableStatefulODE("timestepper test equation") {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void calculateDerivatives(double t, double[] y, double[] yPrime) {
                // Intentionally left empty
            }
        };

        integrator.integrate(equations, 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIllegalStateException() {
        double[][] coefficients = {{0.0, 0.3, 0.6, 1.0},
                                   {0.0, 0.27, 0.45, 0.62, 0.74},
                                   {}};
        double[][] weights = {{1.0/6.0, 2.0/3.0, 2.0/3.0, 1.0/6.0},
                             {0.25, 0.4472135955, 0.691116061, 0.25},
                             {}};
        double minStep = 0.001;
        double maxStep = 10.0;
        double scalAbsoluteTolerance = 1.0e-11;
        double scalRelativeTolerance = 1.0e-5;
        double[] initialState = {1.0, 1.0};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta method 1", true, coefficients[0], coefficients[0], weights[0], new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        ExpandableStatefulODE equations = new ExpandableStatefulODE("timestepper test equation") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void calculateDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 2.0;
            }
        };

        integrator.integrate(equations, 0.0);
        equations.setCompleteState(initialState);

        integrator.integrate(equations, Double.POSITIVE_INFINITY);
    }

}