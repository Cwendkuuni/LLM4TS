import static org.junit.Assert.*;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
	
	@Test
	public void testSolve() {
		// Typical use cases
		double solution1 = UnivariateRealSolverUtils.solve(new TestFunction(), -10, 10);
		assertEquals(0.0, solution1, 0.0001);
		
		double solution2 = UnivariateRealSolverUtils.solve(new TestFunction(), 0, 5);
		assertEquals(2.5, solution2, 0.0001);
		
		// Edge cases
		double solution3 = UnivariateRealSolverUtils.solve(new TestFunction(), -10, -9);
		assertEquals(-9.5238, solution3, 0.0001);
		
		double solution4 = UnivariateRealSolverUtils.solve(new TestFunction(), 100, 200);
		assertEquals(136.9048, solution4, 0.0001);
		
		// Error scenario
		try {
			UnivariateRealSolverUtils.solve(null, -10, 10);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("function is null", ex.getMessage());
		}
	}
	
	@Test
	public void testSolveWithAccuracy() {
		// Typical use cases
		double solution1 = UnivariateRealSolverUtils.solve(new TestFunction(), -10, 10, 0.001);
		assertEquals(0.0, solution1, 0.001);
		
		double solution2 = UnivariateRealSolverUtils.solve(new TestFunction(), 0, 5, 0.01);
		assertEquals(2.5, solution2, 0.01);
		
		// Edge cases
		double solution3 = UnivariateRealSolverUtils.solve(new TestFunction(), -10, -9, 0.0001);
		assertEquals(-9.5238, solution3, 0.0001);
		
		double solution4 = UnivariateRealSolverUtils.solve(new TestFunction(), 100, 200, 0.00001);
		assertEquals(136.9048, solution4, 0.00001);
		
		// Error scenario
		try {
			UnivariateRealSolverUtils.solve(null, -10, 10, 0.001);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("function is null", ex.getMessage());
		}
	}

	@Test
	public void testBracket() {
		// Typical use cases
		double[] bracket1 = UnivariateRealSolverUtils.bracket(new TestFunction(), 5, 0, 10);
		assertArrayEquals(new double[]{2.0, 10.0}, bracket1, 0.0001);
		
		double[] bracket2 = UnivariateRealSolverUtils.bracket(new TestFunction(), -5, -10, 0);
		assertArrayEquals(new double[]{-10.0, -2.0}, bracket2, 0.0001);
		
		// Edge cases
		double[] bracket3 = UnivariateRealSolverUtils.bracket(new TestFunction(), 0, -5, 5);
		assertArrayEquals(new double[]{-0.4762, 0.4762}, bracket3, 0.0001);
		
		double[] bracket4 = UnivariateRealSolverUtils.bracket(new TestFunction(), 100, 50, 150);
		assertArrayEquals(new double[]{99.8571, 100.1429}, bracket4, 0.0001);
		
		// Error scenarios
		try {
			UnivariateRealSolverUtils.bracket(null, 5, 0, 10);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("function is null", ex.getMessage());
		}
		
		try {
			UnivariateRealSolverUtils.bracket(new TestFunction(), 5, 10, 0);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("invalid bracketing parameters:  lower bound=10.0,  initial=5.0, upper bound=0.0", ex.getMessage());
		}
		
		try {
			UnivariateRealSolverUtils.bracket(new TestFunction(), 5, 0, 10, 0);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("bad value for maximum iterations number: 0", ex.getMessage());
		}
		
		try {
			UnivariateRealSolverUtils.bracket(new TestFunction(), 5, 0, 10, -10);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("bad value for maximum iterations number: -10", ex.getMessage());
		}
	}

	@Test
	public void testMidpoint() {
		// Typical use cases
		double midpoint1 = UnivariateRealSolverUtils.midpoint(0, 10);
		assertEquals(5.0, midpoint1, 0.0001);
		
		double midpoint2 = UnivariateRealSolverUtils.midpoint(-5, 5);
		assertEquals(0.0, midpoint2, 0.0001);
		
		// Edge cases
		double midpoint3 = UnivariateRealSolverUtils.midpoint(0, 0);
		assertEquals(0.0, midpoint3, 0.0001);
	}

	// Test function to be used for testing
	private class TestFunction implements UnivariateRealFunction {
		@Override
		public double value(double x) {
			return x * x - 4;
		}
	}

}