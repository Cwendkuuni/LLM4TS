
import static org.junit.Assert.*;
import org.junit.Test;

public class RungeKuttaIntegratorTest {
    
    @Test
    public void testIntegrate() {
        // Initialize test case
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = {0.0};
        
        // Create instance of RungeKuttaIntegrator
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator();
        
        // Call the method to be tested
        double result = integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                yDot[0] = -t * y[0];
            }
        }, t0, y0, t, y);
        
        // Check if the result is as expected
        assertEquals(Math.exp(-t), y[0], 1e-6);
        assertEquals(t, result, 1e-6);
    }
}
