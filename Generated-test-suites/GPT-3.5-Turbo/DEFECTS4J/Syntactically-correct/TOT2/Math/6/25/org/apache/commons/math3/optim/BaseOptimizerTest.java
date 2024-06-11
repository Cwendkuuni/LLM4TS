import static org.junit.Assert.*;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        // Create an instance of BaseOptimizer
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
                return 100;
            }

            @Override
            public int getEvaluations() {
                return 50;
            }

            @Override
            public ConvergenceChecker getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(50, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public boolean converged(int iteration, Pair previous, Pair current) {
                return false;
            }
        };

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
                return checker;
            }
        };

        assertEquals(checker, optimizer.getConvergenceChecker());
    }
}