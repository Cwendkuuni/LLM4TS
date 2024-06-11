import org.junit.Assert;
import org.junit.Test;

public class RungeKuttaIntegratorTest {
    
    @Test
    public void testIntegrate() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator() {
            
            @Override
            public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {
                // TODO Auto-generated method stub
                return 0;
            }
        };

        double[] c = {0.0, 1.0, 2.0};
        double[][] a = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};
        double[] b = {0.0, 0.0, 0.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        double step = 0.1;

        integrator = new RungeKuttaIntegrator("test", c, a, b, prototype, step);
        
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            
            @Override
            public int getDimension() {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                // TODO Auto-generated method stub
                
            }
        };
        
        double t0 = 0.0;
        double[] y0 = {0.0, 0.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        
        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
            Assert.assertEquals(1.0, result, 0.001);
        } catch (DerivativeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IntegratorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}