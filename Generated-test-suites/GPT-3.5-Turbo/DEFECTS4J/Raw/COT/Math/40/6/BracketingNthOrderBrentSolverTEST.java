import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

	@Test
	public void testDoSolve() {
		BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
		double result = solver.doSolve();
		assertEquals(0.0, result, 1e-6);
	}

	@Test
	public void testGetMaximalOrder() {
		BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
		int result = solver.getMaximalOrder();
		assertEquals(5, result);
	}

	@Test
	public void testSolve1() {
		BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
		int maxEval = 100;
		UnivariateFunction f = new UnivariateFunction() {
			public double value(double x) {
				return x*x - 4.0;
			}
		};
		double min = 0.0;
		double max = 10.0;
		BracketingNthOrderBrentSolver.AllowedSolution allowedSolution = BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE;
		double result = solver.solve(maxEval, f, min, max, allowedSolution);
		assertEquals(2.0, result, 1e-6);
	}

	@Test
	public void testSolve2() {
		BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
		int maxEval = 100;
		UnivariateFunction f = new UnivariateFunction() {
			public double value(double x) {
				return x*x - 4.0;
			}
		};
		double min = 0.0;
		double max = 10.0;
		double startValue = 1.0;
		BracketingNthOrderBrentSolver.AllowedSolution allowedSolution = BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE;
		double result = solver.solve(maxEval, f, min, max, startValue, allowedSolution);
		assertEquals(2.0, result, 1e-6);
	}

}