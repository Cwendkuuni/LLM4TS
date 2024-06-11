import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;

public class RungeKuttaIntegratorTest {

	private RungeKuttaIntegrator integrator;

	@Before
	public void setup() {
		integrator = new RungeKuttaIntegrator();
	}

	@Test
	public void testIntegrate() {
		FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
			public int getDimension() {
				return 2;
			}

			public void computeDerivatives(double t, double[] y, double[] yDot) {
				yDot[0] = y[0] + 2 * y[1];
				yDot[1] = 3 * y[0] + 4 * y[1];
			}
		};

		double t0 = 0.0;
		double[] y0 = {1.0, 2.0};
		double t = 1.0;
		double[] y = new double[2];

		try {
			double result = integrator.integrate(equations, t0, y0, t, y);
			assertEquals(10.0, result, 1E-6);
			assertEquals(9.0, y[0], 1E-6);
			assertEquals(18.0, y[1], 1E-6);
		} catch (DerivativeException e) {
			e.printStackTrace();
		} catch (IntegratorException e) {
			e.printStackTrace();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIntegrateWithNullEquations() throws DerivativeException, IntegratorException {
		double t0 = 0.0;
		double[] y0 = {1.0, 2.0};
		double t = 1.0;
		double[] y = new double[2];

		integrator.integrate(null, t0, y0, t, y);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIntegrateWithInvalidY0Size() throws DerivativeException, IntegratorException {
		FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
			public int getDimension() {
				return 2;
			}

			public void computeDerivatives(double t, double[] y, double[] yDot) {
				yDot[0] = y[0] + 2 * y[1];
				yDot[1] = 3 * y[0] + 4 * y[1];
			}
		};

		double t0 = 0.0;
		double[] y0 = {1.0};
		double t = 1.0;
		double[] y = new double[2];

		integrator.integrate(equations, t0, y0, t, y);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIntegrateWithInvalidYSize() throws DerivativeException, IntegratorException {
		FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
			public int getDimension() {
				return 2;
			}

			public void computeDerivatives(double t, double[] y, double[] yDot) {
				yDot[0] = y[0] + 2 * y[1];
				yDot[1] = 3 * y[0] + 4 * y[1];
			}
		};

		double t0 = 0.0;
		double[] y0 = {1.0, 2.0};
		double t = 1.0;
		double[] y = new double[3];

		integrator.integrate(equations, t0, y0, t, y);
	}


}