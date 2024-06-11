import static org.junit.Assert.*;
import org.junit.Test;

public class BaseOptimizerTest {

	@Test
	public void testGetMaxEvaluations() {
		BaseOptimizer<String> optimizer = new BaseOptimizer<>();
		assertEquals(0, optimizer.getMaxEvaluations());
	}

	@Test
	public void testGetEvaluations() {
		BaseOptimizer<String> optimizer = new BaseOptimizer<>();
		assertEquals(0, optimizer.getEvaluations());
	}

	@Test
	public void testGetConvergenceChecker() {
		BaseOptimizer<String> optimizer = new BaseOptimizer<>();
		assertNotNull(optimizer.getConvergenceChecker());
	}

	@Test
	public void testGetEvaluationsAfterOptimizeMethodCall() {
		BaseOptimizer<String> optimizer = new BaseOptimizer<>();
		optimizer.optimize();
		assertEquals(1, optimizer.getEvaluations());
	}

	// Edge case: Using a null type for PAIR
	@Test
	public void testWithNullTypeForPAIR() {
		BaseOptimizer<?> optimizer = new BaseOptimizer<>();
		assertNull(optimizer.getConvergenceChecker());
	}

	// Error scenario: calling getMaxEvaluations with negative PAIR type
	@Test
	public void testNegativePAIRType() {
		BaseOptimizer<Integer> optimizer = new BaseOptimizer<>();
		optimizer.getMaxEvaluations();
	}

	// Error scenario: calling getEvaluations with null PAIR type
	@Test
	public void testNullPAIRType() {
		BaseOptimizer<?> optimizer = new BaseOptimizer<>();
		optimizer.getEvaluations();
	}

	// Typcial use case: Checking default evaluations value
	@Test
	public void testDefaultEvaluationsValue() {
		BaseOptimizer<String> optimizer = new BaseOptimizer<>();
		assertEquals(0, optimizer.getEvaluations());
	}
}