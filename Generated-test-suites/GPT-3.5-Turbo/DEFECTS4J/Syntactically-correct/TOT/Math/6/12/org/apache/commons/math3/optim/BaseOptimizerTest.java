import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    // Test case for getMaxEvaluations()
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer<PAIR>() {
            public int getMaxEvaluations() {
                return 100;
            }

            public int getEvaluations() {
                return 0;
            }

            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(100, optimizer.getMaxEvaluations());
    }

    // Test case for getEvaluations()
    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer<PAIR>() {
            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 50;
            }

            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(50, optimizer.getEvaluations());
    }

    // Test case for getConvergenceChecker()
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> convergenceChecker = new ConvergenceChecker<PAIR>() {
            public boolean converged(int iteration, PAIR previous, PAIR current) {
                return true;
            }
        };

        BaseOptimizer optimizer = new BaseOptimizer<PAIR>() {
            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 0;
            }

            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return convergenceChecker;
            }
        };

        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

}