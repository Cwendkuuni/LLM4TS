package org.apache.commons.math3.optim.univariate;

import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;

public class BrentOptimizerTest {

	@Test(expected = NumberIsTooSmallException.class)
	public void testBrentOptimizerConstructorRel() {
		double rel = -0.1;
		double abs = 0.2;
		UnivariateOptimizer optimizer = new BrentOptimizer(rel, abs);
	}

	@Test(expected = NotStrictlyPositiveException.class)
	public void testBrentOptimizerConstructorAbs() {
		double rel = 0.1;
		double abs = -0.2;
		UnivariateOptimizer optimizer = new BrentOptimizer(rel, abs);
	}

	@Test
	public void testBrentOptimizerConstructor() {
		double rel = 0.1;
		double abs = 0.2;
		ConvergenceChecker<UnivariatePointValuePair> checker = null;
		UnivariateOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
		Assert.assertNotNull(optimizer);
	}

	@Test
	public void testDoOptimize() {
		double rel = 0.1;
		double abs = 0.2;
		ConvergenceChecker<UnivariatePointValuePair> checker = null;
		UnivariateOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
		optimizer.setGoalType(GoalType.MINIMIZE);
		optimizer.setMin(0.0);
		optimizer.setMax(10.0);
		optimizer.setStartValue(5.0);
		UnivariatePointValuePair result = optimizer.optimize();
		Assert.assertNotNull(result);
	}

	@Test
	public void testBest() {
		UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
		UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
		boolean isMinim = true;
		BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.2);
		UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
		Assert.assertEquals(1.0, result.getPoint(), 0.0);
		Assert.assertEquals(1.0, result.getValue(), 0.0);
		
		isMinim = false;
		result = optimizer.best(a, b, isMinim);
		Assert.assertEquals(2.0, result.getPoint(), 0.0);
		Assert.assertEquals(2.0, result.getValue(), 0.0);
	}

	@Test
	public void testEquals() {
		double a = 1.0;
		double b = 1.0;
		Assert.assertTrue(Precision.equals(a, b));
	}

}