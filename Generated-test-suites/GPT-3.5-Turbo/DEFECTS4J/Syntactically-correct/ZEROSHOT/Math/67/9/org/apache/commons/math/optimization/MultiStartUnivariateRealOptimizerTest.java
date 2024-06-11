import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
	
	@Test
	public void testGetFunctionValue() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		double result = optimizer.getFunctionValue();
		assertEquals(0.0, result, 0.0);
	}
	
	@Test
	public void testGetResult() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		double result = optimizer.getResult();
		assertEquals(0.0, result, 0.0);
	}
	
	@Test
	public void testGetAbsoluteAccuracy() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		double result = optimizer.getAbsoluteAccuracy();
		assertEquals(0.0, result, 0.0);
	}
	
	@Test
	public void testGetIterationCount() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		int result = optimizer.getIterationCount();
		assertEquals(0, result);
	}
	
	@Test
	public void testGetMaximalIterationCount() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		int result = optimizer.getMaximalIterationCount();
		assertEquals(Integer.MAX_VALUE, result);
	}
	
	@Test
	public void testGetMaxEvaluations() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		int result = optimizer.getMaxEvaluations();
		assertEquals(Integer.MAX_VALUE, result);
	}
	
	@Test
	public void testGetEvaluations() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		int result = optimizer.getEvaluations();
		assertEquals(0, result);
	}
	
	@Test
	public void testGetRelativeAccuracy() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		double result = optimizer.getRelativeAccuracy();
		assertEquals(0.0, result, 0.0);
	}
	
	@Test
	public void testResetAbsoluteAccuracy() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		optimizer.resetAbsoluteAccuracy();
		assertEquals(0.0, optimizer.getAbsoluteAccuracy(), 0.0);
	}
	
	@Test
	public void testResetMaximalIterationCount() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		optimizer.resetMaximalIterationCount();
		assertEquals(Integer.MAX_VALUE, optimizer.getMaximalIterationCount());
	}
	
	@Test
	public void testResetRelativeAccuracy() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		optimizer.resetRelativeAccuracy();
		assertEquals(0.0, optimizer.getRelativeAccuracy(), 0.0);
	}
	
	@Test
	public void testSetAbsoluteAccuracy() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		optimizer.setAbsoluteAccuracy(0.1);
		assertEquals(0.1, optimizer.getAbsoluteAccuracy(), 0.0);
	}
	
	@Test
	public void testSetMaximalIterationCount() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		optimizer.setMaximalIterationCount(100);
		assertEquals(100, optimizer.getMaximalIterationCount());
	}
	
	@Test
	public void testSetMaxEvaluations() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		optimizer.setMaxEvaluations(1000);
		assertEquals(1000, optimizer.getMaxEvaluations());
	}
	
	@Test
	public void testSetRelativeAccuracy() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		optimizer.setRelativeAccuracy(0.01);
		assertEquals(0.01, optimizer.getRelativeAccuracy(), 0.0);
	}
	
	@Test
	public void testGetOptima() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		try {
			double[] optima = optimizer.getOptima();
			assertArrayEquals(new double[] {Double.NaN}, optima, 0.0);
		} catch (IllegalStateException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testGetOptimaValues() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		try {
			double[] optimaValues = optimizer.getOptimaValues();
			assertArrayEquals(new double[] {Double.NaN}, optimaValues, 0.0);
		} catch (IllegalStateException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testOptimize() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		try {
			double result = optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0);
			assertEquals(0.0, result, 0.0);
		} catch (ConvergenceException | FunctionEvaluationException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testOptimizeWithStartValue() {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		try {
			double result = optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
			assertEquals(0.0, result, 0.0);
		} catch (ConvergenceException | FunctionEvaluationException e) {
			fail(e.toString());
		}
	}
	
	@Test(expected = OptimizationException.class)
	public void testOptimizeWithNoConvergence() throws OptimizationException {
		UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
		optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0);
	}
}