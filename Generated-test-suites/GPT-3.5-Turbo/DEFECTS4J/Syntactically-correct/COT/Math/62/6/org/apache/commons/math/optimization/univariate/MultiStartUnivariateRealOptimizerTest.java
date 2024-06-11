import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

	private MultiStartUnivariateRealOptimizer optimizer;
	private MockUnivariateRealFunction function;

	@Before
	public void setUp() {
		BaseUnivariateRealOptimizer<FUNC> baseOptimizer = new MockUnivariateRealOptimizer();
		RandomGenerator generator = new MockRandomGenerator();
		optimizer = new MultiStartUnivariateRealOptimizer(baseOptimizer, 3, generator);
		function = new MockUnivariateRealFunction();
	}

	@Test
	public void testOptimize() throws FunctionEvaluationException {
		double min = 0;
		double max = 10;
		GoalType goal = GoalType.MINIMIZE;
		double expectedValue = 42.0;
		double expectedPoint = 5.0;

		function.setGoal(goal);
		function.setExpectedResult(expectedValue);

		UnivariateRealPointValuePair result = optimizer.optimize(function, goal, min, max);

		Assert.assertEquals(expectedValue, result.getValue(), 0.0001);
		Assert.assertEquals(expectedPoint, result.getPoint(), 0.0001);
	}

	@Test(expected = ConvergenceException.class)
	public void testOptimizeConvergenceException() throws FunctionEvaluationException {
		double min = 0;
		double max = 10;
		GoalType goal = GoalType.MINIMIZE;

		function.setThrowConvergenceException(true);

		optimizer.optimize(function, goal, min, max);
	}

	@Test(expected = MathIllegalStateException.class)
	public void testGetOptimaMathIllegalStateException() {
		optimizer.getOptima();
	}

	private class MockUnivariateRealOptimizer implements BaseUnivariateRealOptimizer<FUNC> {
		private int maxEvaluations;
		private int evaluations;

		public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
		}

		public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
			return null;
		}

		public int getMaxEvaluations() {
			return maxEvaluations;
		}

		public int getEvaluations() {
			return evaluations;
		}

		public void setMaxEvaluations(int maxEvaluations) {
			this.maxEvaluations = maxEvaluations;
		}

		public UnivariateRealPointValuePair optimize(FUNC f, GoalType goal, double min, double max)
				throws FunctionEvaluationException {
			evaluations++;
			if (f instanceof MockUnivariateRealFunction) {
				return ((MockUnivariateRealFunction) f).evaluate();
			}
			return null;
		}
	}

	private class MockRandomGenerator implements RandomGenerator {
		public void setSeed(int seed) {
		}

		public void setSeed(int[] seed) {
		}

		public void setSeed(long seed) {
		}

		public void nextBytes(byte[] bytes) {
		}

		public int nextInt() {
			return 0;
		}

		public int nextInt(int n) {
			return 0;
		}

		public long nextLong() {
			return 0;
		}

		public boolean nextBoolean() {
			return false;
		}

		public float nextFloat() {
			return 0;
		}

		public double nextDouble() {
			return 0;
		}

		public double nextGaussian() {
			return 0;
		}
	}

	private class MockUnivariateRealFunction implements UnivariateRealFunction {
		private GoalType goal;
		private double expectedResult;
		private boolean throwConvergenceException;

		public void setGoal(GoalType goal) {
			this.goal = goal;
		}

		public void setExpectedResult(double expectedResult) {
			this.expectedResult = expectedResult;
		}

		public void setThrowConvergenceException(boolean throwConvergenceException) {
			this.throwConvergenceException = throwConvergenceException;
		}

		public UnivariateRealPointValuePair evaluate() throws FunctionEvaluationException {
			if (throwConvergenceException) {
				throw new ConvergenceException();
			}
			return new UnivariateRealPointValuePair(expectedResult, expectedResult, goal);
		}

		public double value(double point) throws FunctionEvaluationException {
			return expectedResult;
		}
	}
}