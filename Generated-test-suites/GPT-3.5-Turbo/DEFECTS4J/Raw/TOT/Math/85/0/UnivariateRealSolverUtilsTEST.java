import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.XxxFunction;

public class UnivariateRealSolverUtilsTest {

	private UnivariateRealFunction function;

	@Before
	public void setup() {
		function = new XxxFunction();
	}

	@Test
	public void testSolveDefault() throws FunctionEvaluationException, ConvergenceException {
		double result = UnivariateRealSolverUtils.solve(function, 0.0, 1.0);
		Assert.assertEquals(0.0, result, 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSolveDefaultNullFunction() throws FunctionEvaluationException, ConvergenceException {
		UnivariateRealSolverUtils.solve(null, 0.0, 1.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSolveDefaultInvalidInterval() throws FunctionEvaluationException, ConvergenceException {
		UnivariateRealSolverUtils.solve(function, 1.0, 0.0);
	}

	@Test
	public void testSolveWithAccuracy() throws FunctionEvaluationException, ConvergenceException {
		double result = UnivariateRealSolverUtils.solve(function, 0.0, 1.0, 0.0001);
		Assert.assertEquals(0.0, result, 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSolveWithAccuracyNullFunction() throws FunctionEvaluationException, ConvergenceException {
		UnivariateRealSolverUtils.solve(null, 0.0, 1.0, 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSolveWithAccuracyInvalidInterval() throws FunctionEvaluationException, ConvergenceException {
		UnivariateRealSolverUtils.solve(function, 1.0, 0.0, 0.0001);
	}

	@Test
	public void testBracket() throws FunctionEvaluationException, ConvergenceException {
		double[] result = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 100);
		Assert.assertEquals(-1.0, result[0], 0.0001);
		Assert.assertEquals(1.0, result[1], 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBracketNullFunction() throws FunctionEvaluationException, ConvergenceException {
		UnivariateRealSolverUtils.bracket(null, 0.0, -1.0, 1.0, 100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBracketInvalidInterval() throws FunctionEvaluationException, ConvergenceException {
		UnivariateRealSolverUtils.bracket(function, 1.0, 0.0, -1.0, 100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBracketInvalidMaximumIterations() throws FunctionEvaluationException, ConvergenceException {
		UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 0);
	}

	@Test
	public void testMidpoint() {
		double result = UnivariateRealSolverUtils.midpoint(0.0, 1.0);
		Assert.assertEquals(0.5, result, 0.0001);
	}

	@Test
	public void testSetup() {
		UnivariateRealSolverUtils.setup(function);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetupNullFunction() {
		UnivariateRealSolverUtils.setup(null);
	}

	@Test
	public void testPrivateConstructor() {
		UnivariateRealSolverUtils utils = new UnivariateRealSolverUtils();
	}

}