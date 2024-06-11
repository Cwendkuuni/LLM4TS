import org.junit.Test;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math.ode.sampling.StepHandler;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() { return 2; }
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };
        
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false,
                                                                                   new double[]{0.5, 1.0},
                                                                                   new double[][]{{0.0}, {0.5}},
                                                                                   new double[]{0.5, 0.5},
                                                                                   null,
                                                                                   0.001, 0.1,
                                                                                   1e-8, 1e-8);
        
        StepHandler handler = new StepHandler() {
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                // Handle step
            }
            public void init(double t0, double[] y0, double t) {
                // Initialize
            }
            public void reset() {
                // Reset
            }
        };
        
        integrator.addStepHandler(handler);
        
        double t0 = 0.0;
        double[] y0 = new double[] {1.0, 0.0};
        double t = 2 * Math.PI;
        double[] y = new double[2];
        
        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (DerivativeException | IntegratorException e) {
            // Handle exception
        }
        
        // Assert results
        
    }

}