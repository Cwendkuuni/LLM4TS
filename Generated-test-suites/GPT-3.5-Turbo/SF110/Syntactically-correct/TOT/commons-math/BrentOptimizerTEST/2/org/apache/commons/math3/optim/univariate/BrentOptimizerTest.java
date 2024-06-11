import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.SimpleBounds;

public class BrentOptimizerTest {

	@Test
	public void testDoOptimize() {
		// Typical use case
		BrentOptimizer optimizer1 = new BrentOptimizer(1e-10, 1e-10);
		UnivariatePointValuePair result1 = optimizer1.doOptimize();
		assertEquals(0.0, result1.getPoint(), 1e-10);
		assertEquals(0.0, result1.getValue(), 1e-10);

		// Edge case
		BrentOptimizer optimizer2 = new BrentOptimizer(0.0, 0.0);
		UnivariatePointValuePair result2 = optimizer2.doOptimize();
		assertEquals(Double.POSITIVE_INFINITY, result2.getPoint(), 0.0);
		assertEquals(Double.POSITIVE_INFINITY, result2.getValue(), 0.0);

		// Error scenario
		BrentOptimizer optimizer3 = new BrentOptimizer(1e-10, 1e-10);
		UnivariatePointValuePair result3 = optimizer3.doOptimize();
		assertNull(result3); // Assuming there is an error scenario that can result in a null result
	}

	@Test
	public void testBest() {
		// Typical use case - minimizing function
		BrentOptimizer optimizer1 = new BrentOptimizer(1e-10, 1e-10);
		UnivariatePointValuePair a1 = new UnivariatePointValuePair(0.0, 10.0);
		UnivariatePointValuePair b1 = new UnivariatePointValuePair(1.0, 5.0);
		UnivariatePointValuePair best1 = optimizer1.best(a1, b1, true);
		assertEquals(1.0, best1.getPoint(), 1e-10);
		assertEquals(5.0, best1.getValue(), 1e-10);

		// Typical use case - maximizing function
		BrentOptimizer optimizer2 = new BrentOptimizer(1e-10, 1e-10);
		UnivariatePointValuePair a2 = new UnivariatePointValuePair(0.0, 10.0);
		UnivariatePointValuePair b2 = new UnivariatePointValuePair(1.0, 5.0);
		UnivariatePointValuePair best2 = optimizer2.best(a2, b2, false);
		assertEquals(0.0, best2.getPoint(), 1e-10);
		assertEquals(10.0, best2.getValue(), 1e-10);
	}

	@Test
	public void testConstructor() {
		// Typical use case
		BrentOptimizer optimizer1 = new BrentOptimizer(1e-10, 1e-10);
		assertEquals(1e-10, optimizer1.relativeThreshold, 1e-10);
		assertEquals(1e-10, optimizer1.absoluteThreshold, 1e-10);
		assertNull(optimizer1.getConvergenceChecker());
		
		// Edge case
		BrentOptimizer optimizer2 = new BrentOptimizer(Double.MIN_VALUE, Double.MIN_VALUE);
		assertEquals(Double.MIN_VALUE, optimizer2.relativeThreshold, 1e-10);
		assertEquals(Double.MIN_VALUE, optimizer2.absoluteThreshold, 1e-10);
		assertNull(optimizer2.getConvergenceChecker());
		
		// Error scenario
		try {
			BrentOptimizer optimizer3 = new BrentOptimizer(0.0, 0.0);
		} catch (Exception e) {
			assertTrue(e instanceof NotStrictlyPositiveException);
		}
	}
}