
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer() {
            @Override
            public int getMaxEvaluations() {
                return 100;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer() {
            @Override
            public int getMaxEvaluations() {
                return 0;
            }

            @Override
            public int getEvaluations() {
                return 10;
            }

            @Override
            public ConvergenceChecker getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(10, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer() {
            @Override
            public int getMaxEvaluations() {
                return 0;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker getConvergenceChecker() {
                return new ConvergenceChecker() {
                  @Override
                  public boolean converged(int iteration, PAIR previous, PAIR current) {
                      return false;
                  }
                };
            }
        };

        assertNotNull(optimizer.getConvergenceChecker());
    }
}

