
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.util.FastMath;
import org.junit.Assert;


public class MultiStartUnivariateRealOptimizerTest {
	@Test
	public void testSetConvergenceChecker() {
		// create a dummy optimizer to test
		BaseUnivariateRealOptimizer<FUNC> dummyOptimizer = new BaseUnivariateRealOptimizer<FUNC>() {
			// implementation of inherited abstract methods
		};
		
		// create an instance of MultiStartUnivariateRealOptimizer
		MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(dummyOptimizer, 3, generator);
		
		// create a convergence checker to be set
		ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>() {
			// implementation of inherited abstract methods
		};
		
		// set the convergence checker
		optimizer.setConvergenceChecker(checker);
		
		// check if the checker is set correctly
		ConvergenceChecker<UnivariateRealPointValuePair> actual = optimizer.getConvergenceChecker();
		Assert.assertEquals(checker, actual);
	}
	
	@Test
	public void testGetConvergenceChecker() {
		// create a dummy optimizer to test
		BaseUnivariateRealOptimizer<FUNC> dummyOptimizer = new BaseUnivariateRealOptimizer<FUNC>() {
			// implementation of inherited abstract methods
		};
		
		// create an instance of MultiStartUnivariateRealOptimizer
		MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(dummyOptimizer, 3, generator);
		
		// create a convergence checker to use
		ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>() {
			// implementation of inherited abstract methods
		};
		
		// set the convergence checker
		optimizer.setConvergenceChecker(checker);
		
		// get the convergence checker
		ConvergenceChecker<UnivariateRealPointValuePair> actual = optimizer.getConvergenceChecker();
		
		// check if the returned checker is the same as the one set
		Assert.assertEquals(checker, actual);
	}
	
	@Test
	public void testGetMaxEvaluations() {
		// create a dummy optimizer to test
		BaseUnivariateRealOptimizer<FUNC> dummyOptimizer = new BaseUnivariateRealOptimizer<FUNC>() {
			// implementation of inherited abstract methods
		};
		
		// create an instance of MultiStartUnivariateRealOptimizer
		MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(dummyOptimizer, 3, generator);
		
		// set the maximum number of evaluations
		int maxEvaluations = 100;
		optimizer.setMaxEvaluations(maxEvaluations);
		
		// get the maximum number of evaluations
		int actual = optimizer.getMaxEvaluations();
		
		// check if the returned value is the same as the one set
		Assert.assertEquals(maxEvaluations, actual);
	}
	
	@Test
	public void testGetEvaluations() {
		// create a dummy optimizer to test
		BaseUnivariateRealOptimizer<FUNC> dummyOptimizer = new BaseUnivariateRealOptimizer<FUNC>() {
			// implementation of inherited abstract methods
		};
		
		// create an instance of MultiStartUnivariateRealOptimizer
		MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(dummyOptimizer, 3, generator);
		
		// get the number of evaluations
		int actual = optimizer.getEvaluations();
		
		// check if the initial number of evaluations is zero
		Assert.assertEquals(0, actual);
	}
	
	@Test
	public void testSetMaxEvaluations() {
		// create a dummy optimizer to test
		BaseUnivariateRealOptimizer<FUNC> dummyOptimizer = new BaseUnivariateRealOptimizer<FUNC>() {
			// implementation of inherited abstract methods
		};
		
		// create an instance of MultiStartUnivariateRealOptimizer
		MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(dummyOptimizer, 3, generator);
		
		// set the maximum number of evaluations
		int maxEvaluations = 100;
		optimizer.setMaxEvaluations(maxEvaluations);
		
		// get the maximum number of evaluations
		int actual = optimizer.getMaxEvaluations();
		
		// check if the returned value is the same as the one set
		Assert.assertEquals(maxEvaluations, actual);
	}
	
	@Test
	public void testGetOptima() {
		// create a dummy optimizer to test
		BaseUnivariateRealOptimizer<FUNC> dummyOptimizer = new BaseUnivariateRealOptimizer<FUNC>() {
			// implementation of inherited abstract methods
		};
		
		// create an instance of MultiStartUnivariateRealOptimizer
		MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(dummyOptimizer, 3, generator);
		
		// optimize the function
		FUNC f = new UnivariateRealFunction() {
			// implementation of inherited abstract methods
		};
		GoalType goal = GoalType.MINIMIZE;
		double min = -10.0;
		double max = 10.0;
		dummyOptimizer.optimize(f, goal, min, max, 0);
		
		// get the optima
		UnivariateRealPointValuePair[] optima = optimizer.getOptima();
		
		// check if the optima array is not null
		Assert.assertNotNull(optima);
	}
	
	@Test(expected = MathIllegalStateException.class)
	public void testGetOptima_NotOptimized() throws MathIllegalStateException {
		// create a dummy optimizer to test
		BaseUnivariateRealOptimizer<FUNC> dummyOptimizer = new BaseUnivariateRealOptimizer<FUNC>() {
			// implementation of inherited abstract methods
		};
		
		// create an instance of MultiStartUnivariateRealOptimizer
		MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(dummyOptimizer, 3, generator);
		
		// get the optima without optimizing the function
		optimizer.getOptima();
	}
	
	@Test
	public void testOptimize() throws FunctionEvaluationException {
		// create a dummy optimizer to test
		BaseUnivariateRealOptimizer<FUNC> dummyOptimizer = new BaseUnivariateRealOptimizer<FUNC>() {
			// implementation of inherited abstract methods
		};
		
		// create an instance of MultiStartUnivariateRealOptimizer
		MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(dummyOptimizer, 3, generator);
		
		// create a function to optimize
		FUNC f = new UnivariateRealFunction() {
			// implementation of inherited abstract methods
		};
		
		// set the maximum number of evaluations
		int maxEvaluations = 100;
		optimizer.setMaxEvaluations(maxEvaluations);
		
		// set the goal type
		GoalType goal = GoalType.MINIMIZE;
		
		// set the interval bounds
		double min = -10.0;
		double max = 10.0;
		
		// optimize the function
		UnivariateRealPointValuePair optima = optimizer.optimize(f, goal, min, max);
		
		// check if the optima is not null
		Assert.assertNotNull(optima);
	}
}
