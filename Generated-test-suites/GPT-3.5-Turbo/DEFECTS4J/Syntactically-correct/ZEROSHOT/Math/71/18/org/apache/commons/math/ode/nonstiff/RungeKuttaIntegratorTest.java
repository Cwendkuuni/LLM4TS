import static org.junit.Assert.*;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

	@Test
	public void testIntegrate() throws DerivativeException, IntegratorException {
		// Create a test first order differential equation
		FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
			
			@Override
			public int getDimension() {
				return 2;
			}
			
			@Override
			public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
				yDot[0] = 2 * y[0];
				yDot[1] = 3 * y[1];
			}
		};
		
		// Create RungeKuttaIntegrator object
		RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Test", new double[]{0.0, 0.5, 0.5, 1.0}, new double[][]{{0.0, 0.0, 0.0}, {0.5, 0.0, 0.0}, {0.0, 0.5, 0.0}, {0.0, 0.0, 1.0}}, new double[]{1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0}, null, 0.01);

		// Define initial conditions
		double t0 = 0;
		double[] y0 = new double[]{1.0, 2.0};
		double t = 1.0;
		double[] y = new double[2];

		// Integrate the differential equation
		double stopTime = integrator.integrate(equations, t0, y0, t, y);

		// Verify the result
		assertEquals(1.0 + 2.0 * Math.exp(2), y[0], 1e-10);
		assertEquals(2.0 * Math.exp(3), y[1], 1e-10);
		assertEquals(t, stopTime, 1e-10);
	}
}