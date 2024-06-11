import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() {
        
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorTestImpl();
        
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquationsTestImpl();
        
        double t0 = 0.0;
        double[] y0 = {1.0, 1.0, 1.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        
        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
            assertEquals(t, result, 1e-15);
        } catch (DerivativeException | IntegratorException e) {
            fail(e.getMessage());
        }
        
    }

    private static class EmbeddedRungeKuttaIntegratorTestImpl extends EmbeddedRungeKuttaIntegrator {
        
        public EmbeddedRungeKuttaIntegratorTestImpl() {
            super("Embedded Runge-Kutta Integrator Test", true);
        }

        @Override
        public int getOrder() {
            return 4;
        }

        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 0.0;
        }

        @Override
        protected void computeDerivatives(double t, double[] y, double[] yDot) {
            // do nothing
        }
        
    }
    
    private static class FirstOrderDifferentialEquationsTestImpl implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 3;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            // do nothing
        }
        
    }
}