import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<PAIR>() {
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

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<PAIR>() {
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

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<PAIR>() {
            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 0;
            }

            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return new ConvergenceChecker<PAIR>() {
                    public boolean converged(int iteration, PAIR previous, PAIR current) {
                        return false;
                    }
                };
            }
        };

        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker.converged(0, null, null));
    }
}