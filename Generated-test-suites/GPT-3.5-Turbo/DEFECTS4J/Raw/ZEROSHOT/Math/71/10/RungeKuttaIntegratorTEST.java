import org.junit.Test;

public class RungeKuttaIntegratorTest {

	@Test
	public void testIntegrate() {
		double[] c = { 0, 1, 2, 3 };
		double[][] a = { { 1 }, { 2, 3 }, { 4, 5, 6 } };
		double[] b = { 1, 2, 3, 4 };

		RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
		double step = 1.0;

		RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Test", c, a, b, prototype, step);

		FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquationsImpl();
		double t0 = 0;
		double[] y0 = { 1, 2, 3 };
		double t = 1;
		double[] y = new double[y0.length];

		try {
			integrator.integrate(equations, t0, y0, t, y);
		} catch (DerivativeException | IntegratorException e) {
			e.printStackTrace();
		}

		// Assertion code here
	}

	private class FirstOrderDifferentialEquationsImpl implements FirstOrderDifferentialEquations {

		public int getDimension() {
			return 3;
		}

		public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
			// Compute derivatives here
		}
	}
}