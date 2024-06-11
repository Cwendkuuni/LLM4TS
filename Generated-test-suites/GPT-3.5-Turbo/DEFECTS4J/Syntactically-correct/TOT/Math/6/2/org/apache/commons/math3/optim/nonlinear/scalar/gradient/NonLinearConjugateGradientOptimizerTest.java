import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

	@Test
	public void testDoOptimize() {
		MultivariateFunction function = new MultivariateFunction() {

			public double value(double[] point) {
				double x = point[0];
				double y = point[1];
				return (x - 2) * (x - 2) + (y - 3) * (y - 3);
			}

		};

		double[] startPoint = { 1.0, 1.0 };

		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.POLAK_RIBIERE);
		optimizer.setMaxEvaluations(100);

		try {
			PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
			assertEquals(2.0, result.getPoint()[0], 0.00001);
			assertEquals(3.0, result.getPoint()[1], 0.00001);
		} catch (MathIllegalStateException e) {
			fail("Optimization failed: " + e.getMessage());
		}
	}

	@Test
	public void testDoOptimizeNonConverging() {
		MultivariateFunction function = new MultivariateFunction() {

			public double value(double[] point) {
				return point[0];
			}

		};

		double[] startPoint = { 1.0 };

		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.POLAK_RIBIERE);
		optimizer.setMaxEvaluations(100);

		try {
			optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
			fail("Optimization should have thrown an exception");
		} catch (MathIllegalStateException e) {
			// Expected behavior
		}
	}

	@Test
	public void testDoOptimizeMaxIterations() {
		MultivariateFunction function = new MultivariateFunction() {

			public double value(double[] point) {
				return point[0];
			}

		};

		double[] startPoint = { 1.0 };

		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.POLAK_RIBIERE);
		optimizer.setMaxEvaluations(1);

		try {
			optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
			fail("Optimization should have thrown an exception");
		} catch (MathIllegalStateException e) {
			// Expected behavior
		}
	}

	@Test
	public void testDoOptimizeMaxIterationsNoException() {
		MultivariateFunction function = new MultivariateFunction() {

			public double value(double[] point) {
				return point[0];
			}

		};

		double[] startPoint = { 1.0 };

		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.POLAK_RIBIERE);
		optimizer.setMaxEvaluations(1);
		optimizer.setConvergenceChecker(null); // Set a null convergence checker to avoid exception

		try {
			PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
			assertEquals(0.0, result.getPoint()[0], 0.00001);
		} catch (MathIllegalStateException e) {
			fail("Optimization failed: " + e.getMessage());
		}
	}

	@Test
	public void testDoOptimizeEdgeCaseStartPoint() {
		MultivariateFunction function = new MultivariateFunction() {

			public double value(double[] point) {
				return point[0];
			}

		};

		double[] startPoint = { 0.0 };

		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.POLAK_RIBIERE);
		optimizer.setMaxEvaluations(100);

		try {
			PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
			assertEquals(0.0, result.getPoint()[0], 0.00001);
		} catch (MathIllegalStateException e) {
			fail("Optimization failed: " + e.getMessage());
		}
	}

	@Test
	public void testDoOptimizeEdgeCaseFunction() {
		MultivariateFunction function = new MultivariateFunction() {

			public double value(double[] point) {
				return 0.0;
			}

		};

		double[] startPoint = { 1.0 };

		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.POLAK_RIBIERE);
		optimizer.setMaxEvaluations(100);

		try {
			PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
			assertEquals(1.0, result.getPoint()[0], 0.00001);
		} catch (MathIllegalStateException e) {
			fail("Optimization failed: " + e.getMessage());
		}
	}

	@Test
	public void testDoOptimizeErrorCaseInvalidDelta() {
		MultivariateFunction function = new MultivariateFunction() {

			public double value(double[] point) {
				return point[0];
			}

		};

		double[] startPoint = { 1.0 };

		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.POLAK_RIBIERE);
		optimizer.setMaxEvaluations(100);
		optimizer.setInitialStep(0.0); // Invalid delta

		try {
			PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
			fail("Optimization should have thrown an exception");
		} catch (MathIllegalStateException e) {
			// Expected behavior
		}
	}

}