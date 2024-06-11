import org.junit.Test;
import org.apache.commons.math3.optimization.BaseOptimizer;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizer() {
            public int getMaxEvaluations() {
                return 100;
            }

            public int getEvaluations() {
                return 0;
            }

            public ConvergenceChecker<Integer> getConvergenceChecker() {
                return null;
            }
        };
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizer() {
            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 10;
            }

            public ConvergenceChecker<Integer> getConvergenceChecker() {
                return null;
            }
        };
        assertEquals(10, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<Integer> convergenceChecker = new ConvergenceChecker<Integer>() {
            public boolean converged(int iteration, PAIR previous, PAIR current) {
                return false;
            }
        };

        BaseOptimizer<Integer> optimizer = new BaseOptimizer() {
            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 0;
            }

            public ConvergenceChecker<Integer> getConvergenceChecker() {
                return convergenceChecker;
            }
        };
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }
}