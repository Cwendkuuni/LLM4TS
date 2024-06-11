import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
	
    @Test
    public void testIntegrate() {
        // create an instance of the class to be tested
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator() {
        	
        	public int getOrder() {
        		return 4;
        	}
        	
        	public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        		return 0.0;
        	}
        	
        };
        
        // create test inputs
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
        	
        	public int getDimension() {
        		return 2;
        	}
        	
        	public void computeDerivatives(double t, double[] y, double[] yDot)
                throws DerivativeException {
        		
        	}
        	
        };
        
        double t0 = 0.0;
        double[] y0 = {0.0, 0.0};
        double t = 10.0;
        double[] y = new double[2];
        
        // call the method to be tested
        try {
        	integrator.integrate(equations, t0, y0, t, y);
        } catch (Exception e) {
        	// handle any exceptions
        }
        
        // assert expected behavior
        // ...
    }
}