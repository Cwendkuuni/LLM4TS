import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

	@Test
	public void testFindRoot() throws FunctionException {
		BrentSolver solver = new BrentSolver();
		ComputableFunction function = new ComputableFunction() {
			@Override
			public double valueAt(double x) {
				return x * x - 4;
			}
		};
		ConvergenceChecker checker = new ConvergenceChecker() {
			@Override
			public int converged(double x1, double f1, double x2, double f2) {
				if (Math.abs(f1) < 1e-6) {
					return ConvergenceChecker.LOW;
				} else if (Math.abs(f2) < 1e-6) {
					return ConvergenceChecker.HIGH;
				} else {
					return ConvergenceChecker.NOT_CONVERGED;
				}
			}
		};
		int maxIter = 100;
		double x0 = 0.0;
		double f0 = function.valueAt(x0);
		double x1 = 10.0;
		double f1 = function.valueAt(x1);
		
		boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
		assertTrue(rootFound);
		assertEquals(2.0, solver.getRoot(), 1e-6);
	}

	@Test
	public void testGetRoot() {
		BrentSolver solver = new BrentSolver();
		solver.root = 3.0;
		assertEquals(3.0, solver.getRoot(), 0.0);
	}

}