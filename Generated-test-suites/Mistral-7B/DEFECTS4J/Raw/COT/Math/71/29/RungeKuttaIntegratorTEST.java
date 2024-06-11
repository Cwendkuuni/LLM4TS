import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    @Test
    public void testConstructor() {
        try {
            final double[] c = {0.3, 0.6, 0.1};
            final double[][] a = {{0.0, 0.0, 0.0}, {0.2, 0.4, 0.0}, {0.0, 0.0, 0.2}};
            final double[] b = {0.2, 0.4, 0.2};
            final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 3);
            final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK3", c, a, b, prototype, 0.01);
            Assert.assertNotNull(integrator);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInvalidC() {
        try {
            final double[] c = {0.3, 0.6};
            final double[][] a = {{0.0, 0.0, 0.0}, {0.2, 0.4, 0.0}, {0.0, 0.0, 0.2}};
            final double[] b = {0.2, 0.4, 0.2};
            final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 3);
            new RungeKuttaIntegrator("RK3", c, a, b, prototype, 0.01);
        } catch (Exception e) {
            // expected exception
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInvalidA() {
        try {
            final double[] c = {0.3, 0.6, 0.1};
            final double[][] a = {{0.0, 0.0}, {0.2, 0.4, 0.0}, {0.0, 0.0, 0.2}};
            final double[] b = {0.2, 0.4, 0.2};
            final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 3);
            new RungeKuttaIntegrator("RK3", c, a, b, prototype, 0.01);
        } catch (Exception e) {
            // expected exception
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInvalidB() {
        try {
            final double[] c = {0.3, 0.6, 0.1};
            final double[][] a = {{0.0, 0.0, 0.0}, {0.2, 0.4, 0.0}, {0.0, 0.0, 0.2}};
            final double[] b = {0.2, 0.4};
            final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 3);
            new RungeKuttaIntegrator("RK3", c, a, b, prototype, 0.01);
        } catch (Exception e) {
            // expected exception
        }
    }

    @Test
    public void testIntegrateWithSimpleEquations() {
        try {
            final double[] c = {0.3, 0.6, 0.1};
            final double[][] a = {{0.0, 0.0, 0.0}, {0.2, 0.4, 0.0}, {0.0, 0.0, 0.2}};
            final double[] b = {0.2, 0.4, 0.2};
            final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 3);
            final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK3", c, a, b, prototype, 0.01);
            final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
                @Override
                public int getNumberOfEquations() {
                    return 1;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    yPrime[0] = y[0] * 0.1;
                }
            };
            final double[] initialState = {1.0};
            final double startTime = 0.0;
            final double endTime = 2.0;
            final double[] finalState = integrator.integrate(equations, startTime, initialState, endTime, new double[initialState.length]);
            Assert.assertArrayEquals(new double[]{2.0}, finalState, 0.0);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateWithNonDifferentiableFunctions() {
        try {
            final double[] c = {0.3, 0.6, 0.1};
            final double[][] a = {{0.0, 0.0, 0.0}, {0.2, 0.4, 0.0}, {0.0, 0.0, 0.2}};
            final double[] b = {0.2, 0.4, 0.2};
            final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 3);
            final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK3", c, a, b, prototype, 0.01);
            final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
                @Override
                public int getNumberOfEquations() {
                    return 1;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    if (Math.abs(y[0]) <= 1e-8) {
                        throw new DerivativeException("Non-differentiable function");
                    }
                    yPrime[0] = y[0] * Math.sqrt(Math.abs(y[0]));
                }
            };
            final double[] initialState = {1.0};
            final double startTime = 0.0;
            final double endTime = 2.0;
            integrator.integrate(equations, startTime, initialState, endTime, new double[initialState.length]);
        } catch (Exception e) {
            // expected exception
        }
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithNonInitialConditionSatisfied() {
        try {
            final double[] c = {0.3, 0.6, 0.1};
            final double[][] a = {{0.0, 0.0, 0.0}, {0.2, 0.4, 0.0}, {0.0, 0.0, 0.2}};
            final double[] b = {0.2, 0.4, 0.2};
            final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 3);
            final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK3", c, a, b, prototype, 0.01);
            final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
                @Override
                public int getNumberOfEquations() {
                    return 1;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    yPrime[0] = y[0] * 0.1;
                }
            };
            final double[] initialState = {2.0};
            final double startTime = 0.0;
            final double endTime = 2.0;
            integrator.integrate(equations, startTime, initialState, endTime, new double[initialState.length]);
        } catch (Exception e) {
            // expected exception
        }
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithMaxEvalException() {
        try {
            final double[] c = {0.3, 0.6, 0.1};
            final double[][] a = {{0.0, 0.0, 0.0}, {0.2, 0.4, 0.0}, {0.0, 0.0, 0.2}};
            final double[] b = {0.2, 0.4, 0.2};
            final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 3);
            final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK3", c, a, b, prototype, 0.01);
            final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
                @Override
                public int getNumberOfEquations() {
                    return 1;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    double x = 0.0;
                    for (int i = 0; i < y.length; i++) {
                        x += Math.log(Math.abs(y[i]) + 1e-12);
                    }
                    yPrime[0] = x;
                }
            };
            final double[] initialState = {1.0};
            final double startTime = 0.0;
            final double endTime = 2.0;
            integrator.integrate(equations, startTime, initialState, endTime, new double[initialState.length]);
        } catch (Exception e) {
            // expected exception
        }
    }

}