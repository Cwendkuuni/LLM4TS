import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;

import static org.junit.Assert.*;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

	// Test case for the constructor with default pre-conditioner
	@Test
	public void testConstructorWithDefaultPreconditioner() {
		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.POLAK_RIBIERE, new SimpleValueChecker());

		assertNotNull(optimizer);
		assertEquals(ConjugateGradientFormula.POLAK_RIBIERE, optimizer.getUpdateFormula());
		assertEquals(new SimpleValueChecker(), optimizer.getConvergenceChecker());
		assertEquals(new BrentSolver(), optimizer.getSolver());
		assertEquals(new NonLinearConjugateGradientOptimizer.IdentityPreconditioner(), optimizer.getPreconditioner());
		assertEquals(1.0, optimizer.getInitialStep(), 0);
	}
	
	// Test case for the constructor with custom pre-conditioner
	@Test
	public void testConstructorWithCustomPreconditioner() {
		NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
		
		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), preconditioner);

		assertNotNull(optimizer);
		assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
		assertEquals(new SimpleValueChecker(), optimizer.getConvergenceChecker());
		assertEquals(new BrentSolver(), optimizer.getSolver());
		assertEquals(preconditioner, optimizer.getPreconditioner());
		assertEquals(1.0, optimizer.getInitialStep(), 0);
	}
	
	// Test case for setInitialStep
	@Test
	public void testSetInitialStep() {
		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
		
		optimizer.setInitialStep(0.5);
		assertEquals(0.5, optimizer.getInitialStep(), 0);
		
		optimizer.setInitialStep(-1.0);
		assertEquals(1.0, optimizer.getInitialStep(), 0);
	}
	
	// Test case for findUpperBound
	@Test
	public void testFindUpperBound() {
		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
		UnivariateFunction lsf = optimizer.new LineSearchFunction(new double[] {1, 2, 3});
		
		double upperBound = optimizer.findUpperBound(lsf, 0, 1);
		assertEquals(1.0, upperBound, 0);
	}

	// Test case for value method of LineSearchFunction
	@Test
	public void testLineSearchFunction() {
		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
		UnivariateFunction lsf = optimizer.new LineSearchFunction(new double[] {1, 2, 3});
		
		assertEquals(-1.0, lsf.value(1), 0);
	}

	// Test case for doOptimize
	@Test
	public void testDoOptimize() {
		NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
				ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());

		UnivariateFunction lsf = new UnivariateFunction() {
			public double value(double x) {
				return Math.pow(x, 2) - 4;
			}
		};

		final double expectedMinValue = 0.0;
		final double expectedMaxValue = 4.0;

		PointValuePair result = optimizer.doOptimize();

		assertNotNull(result);
		
		double[] point = result.getPoint();
		double value = result.getValue();
		
		assertEquals(1, point.length);
		assertTrue(point[0] >= expectedMinValue && point[0] <= expectedMaxValue);
		assertEquals(0, value, 0);
	}

}