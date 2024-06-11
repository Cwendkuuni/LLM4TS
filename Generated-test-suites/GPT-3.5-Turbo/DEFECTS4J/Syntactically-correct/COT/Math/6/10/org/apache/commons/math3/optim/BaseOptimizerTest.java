import org.apache.commons.math3.optimization.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

	@Test
	public void testGetMaxEvaluations() {
		BaseOptimizer optimizer = new BaseOptimizer() {
			public int getMaxEvaluations() { return 100; }
			public int getEvaluations() { return 0; }
			public ConvergenceChecker<PAIR> getConvergenceChecker() { return null; }
		};

		assertEquals(100, optimizer.getMaxEvaluations());
	}

	@Test
	public void testGetEvaluations() {
		BaseOptimizer optimizer = new BaseOptimizer() {
			public int getMaxEvaluations() { return 0; }
			public int getEvaluations() { return 10; }
			public ConvergenceChecker<PAIR> getConvergenceChecker() { return null; }
		};

		assertEquals(10, optimizer.getEvaluations());
	}

	@Test
	public void testGetConvergenceChecker() {
		ConvergenceChecker mockChecker = new ConvergenceChecker() {
			public boolean converged(int iteration, PAIR previous, PAIR current) { return false; }
		};

		BaseOptimizer optimizer = new BaseOptimizer() {
			public int getMaxEvaluations() { return 0; }
			public int getEvaluations() { return 0; }
			public ConvergenceChecker<PAIR> getConvergenceChecker() { return mockChecker; }
		};

		assertEquals(mockChecker, optimizer.getConvergenceChecker());
	}
}