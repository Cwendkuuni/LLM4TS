import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Assert;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            @Override
            public int getMaxEvaluations() {
                return 10;
            }
        };
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals("Max evaluations should be 10", 10, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            int evaluations = 0;

            @Override
            public int getEvaluations() {
                return evaluations;
            }

            @Override
            public void optimize(Object... arguments) {
                evaluations++;
            }
        };
        optimizer.optimize(new Object[0]);
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals("Number of evaluations should be 1", 1, evaluations);

        optimizer.optimize(new Object[0]);
        evaluations = optimizer.getEvaluations();
        Assert.assertEquals("Number of evaluations should be 2", 2, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            @Override
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return null;
            }
        };
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNull("Convergence checker should be null", convergenceChecker);

        optimizer = new BaseOptimizer<Object>() {
            @Override
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return new ConvergenceChecker<Object>() {
                    @Override
                    public boolean isConverged(PAIR pair) {
                        return true;
                    }
                };
            }
        };
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull("Convergence checker should not be null", convergenceChecker);
    }
}