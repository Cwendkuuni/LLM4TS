import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<String> optimizer = new BaseOptimizer<String>() {
            public int getMaxEvaluations() {
                return 100;
            }

            public int getEvaluations() {
                return 0;
            }

            public ConvergenceChecker<String> getConvergenceChecker() {
                return null;
            }
        };

        Assert.assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<String> optimizer = new BaseOptimizer<String>() {
            public int getMaxEvaluations() {
                return 100;
            }

            public int getEvaluations() {
                return 50;
            }

            public ConvergenceChecker<String> getConvergenceChecker() {
                return null;
            }
        };

        Assert.assertEquals(50, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<String> optimizer = new BaseOptimizer<String>() {
            public int getMaxEvaluations() {
                return 100;
            }

            public int getEvaluations() {
                return 0;
            }

            public ConvergenceChecker<String> getConvergenceChecker() {
                return new ConvergenceChecker<String>() {
                    public boolean converged(int iteration, String previous, String current) {
                        return true;
                    }
                };
            }
        };

        Assert.assertNotNull(optimizer.getConvergenceChecker());
    }

}