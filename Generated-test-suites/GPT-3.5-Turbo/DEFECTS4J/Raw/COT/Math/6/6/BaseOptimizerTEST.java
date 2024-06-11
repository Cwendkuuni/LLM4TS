import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        // Create an instance of BaseOptimizer
        BaseOptimizer<Double> optimizer = new BaseOptimizer<Double>() {
            // Implement the interface methods
            public int getMaxEvaluations() { return 100; }
            public int getEvaluations() { return 0; }
            public ConvergenceChecker<Double> getConvergenceChecker() { return null; }
        };

        // Test for the correct maximal number of function evaluations
        Assert.assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // Create an instance of BaseOptimizer
        BaseOptimizer<Double> optimizer = new BaseOptimizer<Double>() {
            // Implement the interface methods
            public int getMaxEvaluations() { return 100; }
            public int getEvaluations() { return 50; }
            public ConvergenceChecker<Double> getConvergenceChecker() { return null; }
        };

        // Test for the correct number of evaluations
        Assert.assertEquals(50, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        // Create a custom convergence checker
        ConvergenceChecker<Double> convergenceChecker = new ConvergenceChecker<Double>() {
            public boolean converged(int iteration, Pair<Double, Double> previous, Pair<Double, Double> current) {
                return false;
            }
        };

        // Create an instance of BaseOptimizer
        BaseOptimizer<Double> optimizer = new BaseOptimizer<Double>() {
            // Implement the interface methods
            public int getMaxEvaluations() { return 100; }
            public int getEvaluations() { return 0; }
            public ConvergenceChecker<Double> getConvergenceChecker() { return convergenceChecker; }
        };

        // Test for the correct convergence checker
        Assert.assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }
}