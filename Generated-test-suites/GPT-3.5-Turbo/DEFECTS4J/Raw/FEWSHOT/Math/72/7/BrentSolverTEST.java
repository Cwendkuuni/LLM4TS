import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

	@Test
	public void testFindRoot() throws FunctionException {
		ComputableFunction function = new ComputableFunction() {
			@Override
			public double valueAt(double x) {
				return x * x - 4;       // f(x) = x^2 - 4
			}
		};
		
		ConvergenceChecker checker = new ConvergenceChecker() {
			@Override
			public int converged(double xLow, double fLow, double xHigh, double fHigh) {
				if (Math.abs(fLow) <= 0.01) {
					return ConvergenceChecker.LOW;
				}
				if (Math.abs(fHigh) <= 0.01) {
					return ConvergenceChecker.HIGH;
				}
				return ConvergenceChecker.NOT_CONVERGED;
			}
		};
		
		BrentSolver solver = new BrentSolver();
		boolean found = solver.findRoot(function, checker, 100, 1, -3, 3, 5);
		assertTrue(found);
		assertEquals(2.0, solver.getRoot(), 0.01);
	}

}