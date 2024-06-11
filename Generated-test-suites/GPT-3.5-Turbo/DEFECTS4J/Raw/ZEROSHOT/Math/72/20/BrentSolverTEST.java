import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

	private BrentSolver solver;
	
	@Before
	public void setup() {
		solver = new BrentSolver();
	}
	
	@Test
	public void testFindRoot() throws FunctionException {
		ComputableFunction function = new ComputableFunction() {
			public double valueAt(double x) throws FunctionException {
				return x*x - 4; // f(x) = x^2 - 4
			}
		};
		ConvergenceChecker checker = new ConvergenceChecker() {
			public int converged(double x1, double f1, double x2, double f2) {
				if (Math.abs(f1) < 1e-6) {
					return ConvergenceChecker.LOW;
				} else if (Math.abs(f2) < 1e-6) {
					return ConvergenceChecker.HIGH;
				} else {
					return ConvergenceChecker.NONE;
				}
			}
		};
		int maxIter = 100;
		double x0 = 0;
		double f0 = function.valueAt(x0);
		double x1 = 5;
		double f1 = function.valueAt(x1);
		assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
		assertEquals(2.0, solver.getRoot(), 1e-6);
	}
}