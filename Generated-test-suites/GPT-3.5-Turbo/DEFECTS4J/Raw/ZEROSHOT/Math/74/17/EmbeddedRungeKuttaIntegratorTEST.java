import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testIntegration() throws DerivativeException, IntegratorException {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator() {
            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                
            }
        };
        
        double t0 = 0;
        double[] y0 = {1, 2, 3};
        double t = 1;
        double[] y = new double[3];
        double result = integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                
            }
        }, t0, y0, t, y);
        
        assertEquals(t, result, 0.0);
    }
}