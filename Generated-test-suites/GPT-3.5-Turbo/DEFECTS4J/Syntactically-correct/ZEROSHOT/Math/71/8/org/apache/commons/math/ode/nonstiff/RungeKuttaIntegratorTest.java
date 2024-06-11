import org.junit.Test; 

public class RungeKuttaIntegratorTest {
    @Test
    public void testIntegrate() {
        // create instance of RungeKuttaIntegrator
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestIntegrator", new double[]{0.0, 0.2, 0.3}, new double[][]{{0.0, 0.0, 0.0}, {0.2, 0.0, 0.0}, {0.075, 0.225, 0.0}}, new double[]{0.1, 0.2, 0.3}, new RungeKuttaStepInterpolator(), 0.1);
        
        // create dummy first order differential equations
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }
            
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                yDot[0] = 2.0 * y[0];
                yDot[1] = 3.0 * y[0] + 4.0 * y[1];
            }
        };
        
        // set initial values
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[2];
        
        // run the integrate method
        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (DerivativeException | IntegratorException e) {
            // handle exception
        }
        
        // assert the result
        double[] expected = {Math.exp(2.0), Math.exp(2.0) + 2.0 * Math.exp(2.0)};
        assertArrayEquals(expected, y, 1e-6);
    }
}