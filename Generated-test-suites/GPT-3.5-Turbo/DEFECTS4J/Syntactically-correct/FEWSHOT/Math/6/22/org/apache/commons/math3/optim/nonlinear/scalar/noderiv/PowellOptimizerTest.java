import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PowellOptimizerTest {
	
	private PowellOptimizer optimizer;
	
	@Before
	public void setUp() {
		optimizer = new PowellOptimizer(0.001, 0.0001);
	}
	
	@Test
	public void testDoOptimize() {
		double[] startPoint = {1, 2, 3};
		double result = optimizer.doOptimize(startPoint);
		assertEquals(0, result, 0.001);
	}
	
	@Test
	public void testNewPointAndDirection() {
		double[] p = {1, 2, 3};
		double[] d = {4, 5, 6};
		double optimum = 0.5;
		double[][] result = optimizer.newPointAndDirection(p, d, optimum);
		double[] newPoint = {3, 4.5, 6};
		double[] newDirection = {2, 2.5, 3};
		assertArrayEquals(newPoint, result[0], 0.001);
		assertArrayEquals(newDirection, result[1], 0.001);
	}
	
	@Test
	public void testLineSearch() {
		double[] p = {1, 2, 3};
		double[] d = {4, 5, 6};
		double result = optimizer.line.search(p, d);
		assertEquals(0.5, result, 0.001);
	}
	
	@Test
	public void testSearch() {
		final double x[] = { 0.0, 1.0 };
		final double y[] = { 0.8, 1.0 };
		final double coefficients[] = PolynomialCurveFitter.create().fit(x, y);
			
		// Create a polynomial function with the fitted coefficients
		PolynomialFunction fittedFunction = new PolynomialFunction(coefficients);
			
		// Use PowellOptimizer to find the minimum of the fitted function
		PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-10);
		double[] startPoint = { 0.1, 0.1 };
		double minimum = optimizer.doOptimize(startPoint, fittedFunction);
			
		assertEquals(0.8, minimum, 1e-5);
	}
}