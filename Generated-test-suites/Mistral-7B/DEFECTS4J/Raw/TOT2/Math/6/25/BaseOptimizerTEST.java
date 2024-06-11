import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertTrue(maxEvaluations == 10);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            private int evaluations = 0;

            @Override
            public int getEvaluations() {
                return evaluations;
            }

            @Override
            public void optimize(Object start) {
                evaluations++;
            }
        };
        optimizer.optimize(new Object());
        int evaluations = optimizer.getEvaluations();
        assertTrue(evaluations == 1);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            @Override
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return new ConvergenceChecker<Object>() {
                    @Override
                    public boolean isConverged(Object point, Object fx) {
                        return true;
                    }
                };
            }
        };
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        assertTrue(convergenceChecker != null);
    }
}