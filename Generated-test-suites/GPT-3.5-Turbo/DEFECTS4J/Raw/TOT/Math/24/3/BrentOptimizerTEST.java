import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {

	@Test
	public void testDoOptimize_Minimize() {
		BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
		optimizer.setMaxIteration(1000);
		optimizer.setStartValue(0);
		optimizer.setGoalType(GoalType.MINIMIZE);
		optimizer.setMin(0);
		optimizer.setMax(10);
		UnivariatePointValuePair result = optimizer.optimize();
		
		assertEquals(0, result.getValue(), 1e-10);
		assertEquals(0, result.getPoint(), 1e-10);
	}
	
	@Test
	public void testDoOptimize_Maximize() {
		BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
		optimizer.setMaxIteration(1000);
		optimizer.setStartValue(0);
		optimizer.setGoalType(GoalType.MAXIMIZE);
		optimizer.setMin(0);
		optimizer.setMax(10);
		UnivariatePointValuePair result = optimizer.optimize();
		
		assertEquals(10, result.getValue(), 1e-10);
		assertEquals(10, result.getPoint(), 1e-10);
	}
	
	@Test(expected = NotStrictlyPositiveException.class)
	public void testBrentOptimizer_InvalidAbsoluteThreshold() {
		new BrentOptimizer(1e-10, -1e-10);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBrentOptimizer_InvalidRelativeThreshold() {
		new BrentOptimizer(1e-20, 1e-10);
	}
	
}