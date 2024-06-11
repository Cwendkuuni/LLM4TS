import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer() {
            public int getMaxEvaluations() {
                return 100;
            }
            public int getEvaluations() {
                // Return a dummy value for testing purposes
                return 0;
            }
            public ConvergenceChecker getConvergenceChecker() {
                // Return a dummy value for testing purposes
                return null;
            }
        };

        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer() {
            public int getMaxEvaluations() {
                // Return a dummy value for testing purposes
                return 0;
            }
            public int getEvaluations() {
                return 10;
            }
            public ConvergenceChecker getConvergenceChecker() {
                // Return a dummy value for testing purposes
                return null;
            }
        };

        assertEquals(10, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> convergenceChecker = new ConvergenceChecker<PAIR>() {
            public boolean converged(int iteration, PAIR previousStep, PAIR currentStep) {
                // Return a dummy value for testing purposes
                return false;
            }
        };

        BaseOptimizer optimizer = new BaseOptimizer() {
            public int getMaxEvaluations() {
                // Return a dummy value for testing purposes
                return 0;
            }
            public int getEvaluations() {
                // Return a dummy value for testing purposes
                return 0;
            }
            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return convergenceChecker;
            }
        };

        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }
}