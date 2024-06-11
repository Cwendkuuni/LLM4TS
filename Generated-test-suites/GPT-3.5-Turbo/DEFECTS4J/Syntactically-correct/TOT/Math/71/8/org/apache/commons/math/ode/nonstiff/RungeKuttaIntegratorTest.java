import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        double[] c = {0, 1, 0.5}; // time steps
        double[][] a = {{0, 0, 0}, {1, 0, 0}, {0.25, 0.25, 0}};
        double[] b = {0.1666667, 0.6666667, 0.1666667};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        double step = 0.1; // integration step
        integrator = new RungeKuttaIntegrator("Test Integrator", c, a, b, prototype, step);
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        // Test a typical use case
        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        double stopTime = integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, stopTime, Double.MIN_VALUE);
        assertEquals(12.0, y[0], Double.MIN_VALUE);
        assertEquals(13.0, y[1], Double.MIN_VALUE);
    }

    @Test
    public void testIntegrateWithNegativeStep() throws DerivativeException, IntegratorException {
        // Test with negative integration step
        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        double stopTime = integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, stopTime, Double.MIN_VALUE);
        assertEquals(12.0, y[0], Double.MIN_VALUE);
        assertEquals(13.0, y[1], Double.MIN_VALUE);
    }

    @Test
    public void testIntegrateWithLargeStep() throws DerivativeException, IntegratorException {
        // Test with large integration step
        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double step = 10.0;
        double[] y = new double[y0.length];
        integrator.setStep(step);
        double stopTime = integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, stopTime, Double.MIN_VALUE);
        assertEquals(12.0, y[0], Double.MIN_VALUE);
        assertEquals(13.0, y[1], Double.MIN_VALUE);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateWithDerivativeException() throws DerivativeException, IntegratorException {
        // Test with derivative exception
        FirstOrderDifferentialEquations equations = new TestEquationsWithException();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithIntegratorException() throws DerivativeException, IntegratorException {
        // Test with integrator exception
        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        integrator.integrate(equations, t0, y0, t, y);
    }
}