//Test for NonLinearConjugateGradientOptimizer class

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Before;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

	private NonLinearConjugateGradientOptimizer optimizer;
	private double[] startPoint;
	private double[] objectiveResult;
	private double[] gradientResult;

	@Before
	public void setup() {
		optimizer = new NonLinearConjugateGradientOptimizer();
		startPoint = new double[] { 0, 0 };
		objectiveResult = new double[] { 0, 0 };
		gradientResult = new double[] { 0, 0 };
	}


	@Test
	public void testDoOptimize() {
		optimizer.setGoalType(GoalType.MINIMIZE);
		optimizer.setMaxEvaluations(1000);
		optimizer.setStartPoint(startPoint);
		PointValuePair result = optimizer.doOptimize();
		assertNotNull(result);
		assertEquals(objectiveResult, result);
	}

	@Test(expected=MathIllegalStateException.class)
	public void testFindUpperBound() {
		optimizer.findUpperBound(new UnivariateFunction() {

			@Override
			public double value(double arg0) {
				return 0;
			}
		}, 0, 0.1);
	}

	@Test(expected=MathIllegalStateException.class)
	public void testFindUpperBoundNegative() {
		optimizer.findUpperBound(new UnivariateFunction() {

			@Override
			public double value(double arg0) {
				return -1;
			}
		}, 0, 0.1);
	}

	@Test
	public void testLineSearchFunctionValue() {
		NonLinearConjugateGradientOptimizer.LineSearchFunction lsf = optimizer.new LineSearchFunction(new double[] { 0, 0 });
		assertEquals(0.0, lsf.value(0.0), 0.0);
	}

	@Test
	public void testIdentityPreconditioner() {
		NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
		double[] variables = new double[] { 1, 2 };
		double[] r = new double[] { 3, 4 };
		double[] result = preconditioner.precondition(variables, r);
		assertEquals(r, result);
	}

}