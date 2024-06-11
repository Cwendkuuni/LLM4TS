import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testIntegrate() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(){
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
        };
        
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations(){
            @Override
            public int getDimension() {
                return 0;
            }
            
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot){
                
            }
        };
        
        double t0 = 0;
        double[] y0 = new double[0];
        double t = 0;
        double[] y = new double[0];
        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown");
        } catch (DerivativeException e) {
            fail("DerivativeException thrown");
        } catch (IntegratorException e) {
            fail("IntegratorException thrown");
        }
    }
}