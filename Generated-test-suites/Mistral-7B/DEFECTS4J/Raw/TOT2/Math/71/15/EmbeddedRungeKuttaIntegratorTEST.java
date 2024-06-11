import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Test;
import org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testBasicFunctionality() {
        EmbeddedRungeKuttaIntegrator integrator = new RK4Integrator("RK4", false, RK4.C, RK4.A, RK4.B, new RungeKuttaStepInterpolator(), 0.01, 0.01, new double[]{0.0}, new double[]{0.0}, 0.0, 1.0);
        FirstOrderDifferentialEquations equations = new DifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        double[] result = integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[1]);
        assertEquals(Math.PI / 2.0, result[0], 0.001);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() {
        EmbeddedRungeKuttaIntegrator integrator = new RK4Integrator("RK4", false, RK4.C, RK4.A, RK4.B, new RungeKuttaStepInterpolator(), 0.01, 0.01, new double[]{0.0}, new double[]{0.0}, 0.0, 1.0);
        FirstOrderDifferentialEquations equations = new ThrowingDifferentialEquations();

        integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[1]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() {
        EmbeddedRungeKuttaIntegrator integrator = new RK4Integrator("RK4", false, RK4.C, RK4.A, RK4.B, new RungeKuttaStepInterpolator(), 0.01, 0.01, new double[]{0.0}, new double[]{0.0}, 0.0, 1.0);
        FirstOrderDifferentialEquations equations = new StepRejectingDifferentialEquations();

        integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[1]);
    }

    @Test
    public void testEdgeCases() {
        EmbeddedRungeKuttaIntegrator integrator = new RK4Integrator("RK4", false, RK4.C, RK4.A, RK4.B, new RungeKuttaStepInterpolator(), 0.01, 0.01, new double[]{0.0}, new double[]{0.0}, 0.0, 1.0);

        // Test with initial conditions of zero
        FirstOrderDifferentialEquations equations = new DifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        double[] result = integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[1]);
        assertEquals(Math.PI / 2.0, result[0], 0.001);

        // Test with negative initial conditions
        integrator = new RK4Integrator("RK4", false, RK4.C, RK4.A, RK4.B, new RungeKuttaStepInterpolator(), 0.01, 0.01, new double[]{-0.5}, new double[]{-1.0}, 0.0, 1.0);
        result = integrator.integrate(equations, 0.0, new double[]{-0.5}, 1.0, new double[1]);
        assertEquals(-Math.PI / 2.0, result[0], 0.001);

        // Test with zero step size
        integrator = new RK4Integrator("RK4", false, RK4.C, RK4.A, RK4.B, new RungeKuttaStepInterpolator(), 0.0, 0.0, new double[]{0.0}, new double[]{0.0}, 0.0, 1.0);
        result = integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[1]);
        assertEquals(0.0, result[0], 0.001);
    }
}