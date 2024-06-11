import org.junit.*;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate_WhenEquationsIsNull_ThrowsException() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegratorImpl("test", new double[]{0.1}, new double[][]{{0.2}}, new double[]{0.3}, new RungeKuttaStepInterpolator(), 0.01);
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        try {
            integrator.integrate(null, t0, y0, t, y);
            fail("Expected DerivativeException");
        } catch (DerivativeException e) {
            // exception expected
        } catch (IntegratorException e) {
            fail("Unexpected IntegratorException");
        }
    }
    
    @Test
    public void testIntegrate_WhenY0IsNull_ThrowsException() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegratorImpl("test", new double[]{0.1}, new double[][]{{0.2}}, new double[]{0.3}, new RungeKuttaStepInterpolator(), 0.01);
        double t0 = 0.0;
        double[] y0 = null;
        double t = 1.0;
        double[] y = new double[1];
        try {
            integrator.integrate(new FirstOrderDifferentialEquationsImpl(), t0, y0, t, y);
            fail("Expected DerivativeException");
        } catch (DerivativeException e) {
            // exception expected
        } catch (IntegratorException e) {
            fail("Unexpected IntegratorException");
        }
    }
    
    @Test
    public void testIntegrate_WhenYIsNull_ThrowsException() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegratorImpl("test", new double[]{0.1}, new double[][]{{0.2}}, new double[]{0.3}, new RungeKuttaStepInterpolator(), 0.01);
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = null;
        try {
            integrator.integrate(new FirstOrderDifferentialEquationsImpl(), t0, y0, t, y);
            fail("Expected DerivativeException");
        } catch (DerivativeException e) {
            // exception expected
        } catch (IntegratorException e) {
            fail("Unexpected IntegratorException");
        }
    }
    
    @Test
    public void testIntegrate_WhenTIsLessThanT0_ThrowsException() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegratorImpl("test", new double[]{0.1}, new double[][]{{0.2}}, new double[]{0.3}, new RungeKuttaStepInterpolator(), 0.01);
        double t0 = 1.0;
        double[] y0 = {1.0};
        double t = 0.0;
        double[] y = new double[y0.length];
        try {
            integrator.integrate(new FirstOrderDifferentialEquationsImpl(), t0, y0, t, y);
            fail("Expected IntegratorException");
        } catch (DerivativeException e) {
            fail("Unexpected DerivativeException");
        } catch (IntegratorException e) {
            // exception expected
        }
    }
    
    @Test
    public void testIntegrate_WhenStepIsZero_ThrowsException() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegratorImpl("test", new double[]{0.1}, new double[][]{{0.2}}, new double[]{0.3}, new RungeKuttaStepInterpolator(), 0.0);
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        try {
            integrator.integrate(new FirstOrderDifferentialEquationsImpl(), t0, y0, t, y);
            fail("Expected IntegratorException");
        } catch (DerivativeException e) {
            fail("Unexpected DerivativeException");
        } catch (IntegratorException e) {
            // exception expected
        }
    }
    
    @Test
    public void testIntegrate_WhenIntegrationIsSuccessful_ReturnsStopTime() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegratorImpl("test", new double[]{0.1}, new double[][]{{0.2}}, new double[]{0.3}, new RungeKuttaStepInterpolator(), 0.01);
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        try {
            double stopTime = integrator.integrate(new FirstOrderDifferentialEquationsImpl(), t0, y0, t, y);
            assertEquals(stopTime, t, 0);
        } catch (DerivativeException e) {
            fail("Unexpected DerivativeException");
        } catch (IntegratorException e) {
            fail("Unexpected IntegratorException");
        }
    }

}