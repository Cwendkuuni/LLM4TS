import static org.junit.Assert.*;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerMock();
        int expected = 100;
        int actual = optimizer.getMaxEvaluations();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerMock();
        int expected = 0;
        int actual = optimizer.getEvaluations();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizerMock();
        ConvergenceChecker expected = new ConvergenceCheckerMock();
        ConvergenceChecker actual = optimizer.getConvergenceChecker();
        assertSame(expected, actual);
    }
}

class BaseOptimizerMock implements BaseOptimizer {

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
        return new ConvergenceCheckerMock();
    }
}

class ConvergenceCheckerMock implements ConvergenceChecker {

    @Override
    public boolean converged(int iteration, Pair previous, Pair current) {
        return false;
    }
}