import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sample.InterpolatedValue;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Test with a simple function, y' = sin(x)
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta45Integrator(
                1.0, true,
                new double[]{0.0, 0.25, 0.4, 0.6, 0.8, 1.0},
                new double[][]{
                        {0.0, 0.25, 0.25, 0.5, 0.5, 0.5, 0.5, 0.5},
                        {0.0, 0.373605635, 0.0, 0.687196837, 0.316227766, 0.316227766, 0.316227766, 0.316227766}
                },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public InterpolatedValue computeInterpolatedValue(double x, double[] y) {
                        double yp = Math.sin(x);
                        return new InterpolatedValue(x, y, yp);
                    }
                },
                0.01, 0.01, null, null);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{0.0};
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getPrimaryMapper() {
                return new double[]{0.0};
            }

            @Override
            public double[][] getSecondaryMappers() {
                return new double[][]{{}};
            }

            @Override
            public void setTime(double time) {
                // empty
            }

            @Override
            public void setCompleteState(double[] state) {
                // empty
            }

            @Override
            public void setDerivatives(double[] derivatives, double[] state, double time) {
                derivatives[0] = Math.sin(state[0]);
            }
        };

        // Test with initial conditions x(0) = 0
        integrator.integrate(equations, 2.0);
        assertEquals(Math.sin(2.0), equations.getCompleteState()[0], 1e-8);

        // Test with initial conditions x(0) = 1
        equations.setCompleteState(new double[]{1.0});
        integrator.integrate(equations, 2.0);
        assertEquals(Math.sin(1.0) + Math.sin(2.0), equations.getCompleteState()[0], 1e-8);

        // Test with initial conditions x(0) = 0, but with an error in the function
        equations.setCompleteState(new double[]{0.0});
        integrator.integrate(equations, 2.0);
        double sin2 = Math.sin(2.0);
        double expected = Math.sin(0.0) + sin2;
        assertEquals(expected, equations.getCompleteState()[0], 1e-8);

        // Test with initial conditions x(0) = 1, but with an error in the function
        equations.setCompleteState(new double[]{1.0});
        integrator.integrate(equations, 2.0);
        double testResult = equations.getCompleteState()[0];
        double expectedValue = Math.sin(1.0) + sin2;
        assertEquals(expectedValue, testResult, 1e-8);

        // Test with initial conditions x(0) = -1, but with an error in the function
        equations.setCompleteState(new double[]{-1.0});
        integrator.integrate(equations, 2.0);
        double testResult2 = equations.getCompleteState()[0];
        double expectedValue2 = Math.sin(-1.0) + sin2;
        assertEquals(expectedValue2, testResult2, 1e-8);

        // Test with initial conditions x(0) = 0, but with an error in the step size
        integrator.setMinStep(0.1);
        integrator.integrate(equations, 2.0);
        double testResult3 = equations.getCompleteState()[0];
        double expectedValue3 = Math.sin(0.0) + sin2;
        assertEquals(expectedValue3, testResult3, 1e-8);

        // Test with initial conditions x(0) = 1, but with an error in the step size
        equations.setCompleteState(new double[]{1.0});
        integrator.integrate(equations, 2.0);
        double testResult4 = equations.getCompleteState()[0];
        double expectedValue4 = Math.sin(1.0) + sin2;
        assertEquals(expectedValue4, testResult4, 1e-8);

        // Test with initial conditions x(0) = -1, but with an error in the step size
        equations.setCompleteState(new double[]{-1.0});
        integrator.integrate(equations, 2.0);
        double testResult5 = equations.getCompleteState()[0];
        double expectedValue5 = Math.sin(-1.0) + sin2;
        assertEquals(expectedValue5, testResult5, 1e-8);

        // Test with initial conditions x(0) = 0, and a large error in the tolerance
        integrator.setMinAbsoluteError(1e10);
        integrator.integrate(equations, 2.0);
        double testResult6 = equations.getCompleteState()[0];
        double expectedValue6 = Math.sin(0.0) + sin2;
        assertEquals(expectedValue6, testResult6, 1e-8);

        // Test with initial conditions x(0) = 1, and a large error in the tolerance
        equations.setCompleteState(new double[]{1.0});
        integrator.setMinAbsoluteError(1e10);
        integrator.integrate(equations, 2.0);
        double testResult7 = equations.getCompleteState()[0];
        double expectedValue7 = Math.sin(1.0) + sin2;
        assertEquals(expectedValue7, testResult7, 1e-8);

        // Test with initial conditions x(0) = -1, and a large error in the tolerance
        equations.setCompleteState(new double[]{-1.0});
        integrator.setMinAbsoluteError(1e10);
        integrator.integrate(equations, 2.0);
        double testResult8 = equations.getCompleteState()[0];
        double expectedValue8 = Math.sin(-1.0) + sin2;
        assertEquals(expectedValue8, testResult8, 1e-8);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorEdgeCases() {
        // Test a discontinuous function
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta45Integrator(
                1.0, true,
                new double[]{0.0, 0.25, 0.4, 0.6, 0.8, 1.0},
                new double[][]{
                        {0.0, 0.25, 0.25, 0.5, 0.5, 0.5, 0.5, 0.5},
                        {0.0, 0.373605635, 0.0, 0.687196837, 0.316227766, 0.316227766, 0.316227766, 0.316227766}
                },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public InterpolatedValue computeInterpolatedValue(double x, double[] y) {
                        if (x > 0) {
                            return new InterpolatedValue(x, y, Math.sin(x));
                        } else {
                            return new InterpolatedValue(x, y, Math.cos(x));
                        }
                    }
                },
                0.01, 0.01, null, null);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{0.0};
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public void setTime(double time) {
                // empty
            }

            @Override
            public void setCompleteState(double[] state) {
                // empty
            }

            @Override
            public void setDerivatives(double[] derivatives, double[] state, double time) {
                derivatives[0] = (time > 0) ? Math.sin(state[0]) : Math.cos(state[0]);
            }
        };

        integrator.integrate(equations, 1.0);
        double x = equations.getCompleteState()[0];
        double y = (x > 0) ? Math.sin(x) : Math.cos(x);
        assertTrue(x > 0 || y > 0);

        // Test a singular function
        integrator = new EmbeddedRungeKutta45Integrator(
                1.0, true,
                new double[]{0.0, 0.25, 0.4, 0.6, 0.8, 1.0},
                new double[][]{
                        {0.0, 0.25, 0.25, 0.5, 0.5, 0.5, 0.5, 0.5},
                        {0.0, 0.373605635, 0.0, 0.687196837, 0.316227766, 0.316227766, 0.316227766, 0.316227766}
                },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public InterpolatedValue computeInterpolatedValue(double x, double[] y) {
                        return new InterpolatedValue(x, y, 1.0 / Erf.erf(x));
                    }
                },
                0.01, 0.01, null, null);

        equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{0.0};
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public void setTime(double time) {
                // empty
            }

            @Override
            public void setCompleteState(double[] state) {
                // empty
            }

            @Override
            public void setDerivatives(double[] derivatives, double[] state, double time) {
                derivatives[0] = 1.0 / (Math.PI * Math.sqrt(2.0) * Erf.erf(state[0]));
            }
        };

        integrator.integrate(equations, 3.0);
        x = equations.getCompleteState()[0];
        y = 1.0 / (Math.PI * Math.sqrt(2.0) * Erf.erf(x));
        assertTrue(x > 0);
    }
}