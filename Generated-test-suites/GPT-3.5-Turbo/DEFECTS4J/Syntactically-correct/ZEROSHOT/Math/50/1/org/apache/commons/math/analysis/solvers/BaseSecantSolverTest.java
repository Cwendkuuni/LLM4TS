import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

public class BaseSecantSolverTest {

	@Test
	public void testSolve() {
		UnivariateRealFunction f = new UnivariateRealFunction() {
			public double value(double x) {
				return Math.sin(x);
			}
		};

		BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
		double solution = solver.solve(100, f, 0, 2 * Math.PI, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
		
		assertEquals(3.14159, solution, 0.00001);
	}
	
	@Test
	public void testSolveWithAllowedSolutionAnySide() {
		UnivariateRealFunction f = new UnivariateRealFunction() {
			public double value(double x) {
				return x * x - 4;
			}
		};

		BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
		double solution = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
		
		assertEquals(2, solution, 0.00001);
	}
	
	@Test
	public void testSolveWithAllowedSolutionLeftSide() {
		UnivariateRealFunction f = new UnivariateRealFunction() {
			public double value(double x) {
				return x * x - 4;
			}
		};

		BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
		double solution = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
		
		assertEquals(-2, solution, 0.00001);
	}
	
	@Test
	public void testSolveWithAllowedSolutionRightSide() {
		UnivariateRealFunction f = new UnivariateRealFunction() {
			public double value(double x) {
				return x * x - 4;
			}
		};

		BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
		double solution = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
		
		assertEquals(2, solution, 0.00001);
	}
	
	@Test
	public void testSolveWithAllowedSolutionBelowSide() {
		UnivariateRealFunction f = new UnivariateRealFunction() {
			public double value(double x) {
				return x * x - 4;
			}
		};

		BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
		double solution = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
		
		assertEquals(-2, solution, 0.00001);
	}
	
	@Test
	public void testSolveWithAllowedSolutionAboveSide() {
		UnivariateRealFunction f = new UnivariateRealFunction() {
			public double value(double x) {
				return x * x - 4;
			}
		};

		BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
		double solution = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
		
		assertEquals(2, solution, 0.00001);
	}
	
	@Test(expected = MathInternalError.class)
	public void testSolveWithException() {
		UnivariateRealFunction f = new UnivariateRealFunction() {
			public double value(double x) {
				if (x == 0) {
					throw new ArithmeticException();
				} else {
					return x;
				}
			}
		};

		BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
		solver.solve(100, f, -10, 10);
	}

}