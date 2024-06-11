import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.solvers.BisectionSolver;



public class BisectionSolverTest {
	private BisectionSolver solver;
	
	@Test
	public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
		UnivariateRealFunction function = new UnivariateRealFunction() {
			public double value(double x) throws FunctionEvaluationException {
				return x * x - 4; // sample function
			}
		};
		
		solver = new BisectionSolver(function);
		double result = solver.solve(0, 10);
		Assert.assertEquals(2.000, result, 0.001); // expected result is 2.000

		result = solver.solve(-5, 5);
		Assert.assertEquals(2.000, result, 0.001); // expected result is 2.000
		
		result = solver.solve(0, 2);
		Assert.assertEquals(2.000, result, 0.001); // expected result is 2.000
		
		result = solver.solve(-2, 0);
		Assert.assertEquals(-2.000, result, 0.001); // expected result is -2.000
	}

	@Test(expected = MaxIterationsExceededException.class)
	public void testMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
		UnivariateRealFunction function = new UnivariateRealFunction() {
			public double value(double x) throws FunctionEvaluationException {
				return x * x - 4; // sample function
			}
		};
		
		solver = new BisectionSolver(function);
		solver.setMaximalIterationCount(1);
		
		solver.solve(0, 10); // this should throw MaxIterationsExceededException
	}

	@Test(expected = FunctionEvaluationException.class)
	public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
		UnivariateRealFunction function = new UnivariateRealFunction() {
			public double value(double x) throws FunctionEvaluationException {
				throw new FunctionEvaluationException(x);
			}
		};
		
		solver = new BisectionSolver(function);
		
		solver.solve(0, 10); // this should throw FunctionEvaluationException
	}

	@Test
	public void testDefaultConstructor() {
		solver = new BisectionSolver();
		
		Assert.assertEquals(100, solver.getMaximalIterationCount());
		Assert.assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0.001);
	}
}

