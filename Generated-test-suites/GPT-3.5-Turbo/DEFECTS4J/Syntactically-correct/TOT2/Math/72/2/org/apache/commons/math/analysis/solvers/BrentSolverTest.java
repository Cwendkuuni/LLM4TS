import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

	@Test
	public void testConstructor() {
		BrentSolver solver = new BrentSolver();
		assertNotNull(solver);
	}

	@Test
	public void testFindRoot() {
		BrentSolver solver = new BrentSolver();
		double[] values = {0.0, 1.0, 2.0, 3.0};
		ComputableFunction function = new TestFunction(values);
		ConvergenceChecker checker = new TestConvergenceChecker();
		int maxIter = 100;
		double x0 = 0.0;
		double f0 = function.valueAt(x0);
		double x1 = 2.5;
		double f1 = function.valueAt(x1);

		assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
		assertEquals(1.0, solver.getRoot(), 0.0001);
	}

	@Test
	public void testGetRoot() {
		BrentSolver solver = new BrentSolver();
		solver.findRoot(new TestFunction(new double[]{0.0, 1.0, 2.0, 3.0}), new TestConvergenceChecker(), 100, 0.0, 0.0, 1.0, 0.0);
		assertEquals(1.0, solver.getRoot(), 0.0001);
	}

	private class TestFunction implements ComputableFunction {
		private double[] values;

		TestFunction(double[] values) {
			this.values = values;
		}

		@Override
		public double valueAt(double x) {
			return values[(int)x];
		}
	}
	
	private class TestConvergenceChecker implements ConvergenceChecker {
		@Override
		public int converged(double x1, double f1, double x2, double f2) {
			if (f1 == 0.0) {
				return LOW;
			} else if (f2 == 0.0) {
				return HIGH;
			} else {
				return NEITHER;
			}
		}
	}
}