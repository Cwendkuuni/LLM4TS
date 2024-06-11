import org.junit.Test;
import org.apache.commons.math3.optimization.BaseOptimizer;

import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            @Override
            public int getMaxEvaluations() {
                return 100;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(100, optimizer.getMaxEvaluations());
    }
  
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            @Override
            public int getMaxEvaluations() {
                return 0;
            }

            @Override
            public int getEvaluations() {
                return 10;
            }

            @Override
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(10, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<Object> checker = new ConvergenceChecker<Object>() {
            @Override
            public boolean converged(int iteration, Object previous, Object current) {
                return false;
            }
        };

        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            @Override
            public int getMaxEvaluations() {
                return 0;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return checker;
            }
        };

        assertSame(checker, optimizer.getConvergenceChecker());
    }
}