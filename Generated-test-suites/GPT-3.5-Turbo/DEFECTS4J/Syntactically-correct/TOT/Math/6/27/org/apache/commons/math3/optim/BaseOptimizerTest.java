import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    //Test case for getMaxEvaluations method
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Integer> baseOptimizer = new BaseOptimizer<Integer>() {
            @Override
            public int getMaxEvaluations() {
                return 100;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<Integer> getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(100, baseOptimizer.getMaxEvaluations());
    }

    //Test case for getEvaluations method
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Integer> baseOptimizer = new BaseOptimizer<Integer>() {
            @Override
            public int getMaxEvaluations() {
                return 100;
            }

            @Override
            public int getEvaluations() {
                return 50;
            }

            @Override
            public ConvergenceChecker<Integer> getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(50, baseOptimizer.getEvaluations());
    }

    //Test case for getConvergenceChecker method
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Integer> baseOptimizer = new BaseOptimizer<Integer>() {
            @Override
            public int getMaxEvaluations() {
                return 100;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<Integer> getConvergenceChecker() {
                return new ConvergenceChecker<Integer>() {
                    @Override
                    public boolean converged(int iteration, PAIR previous, PAIR current) {
                        return false;
                    }
                };
            }
        };

        assertNotNull(baseOptimizer.getConvergenceChecker());
    }
}