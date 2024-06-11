import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BaseOptimizerTest {

    private BaseOptimizer optimizer;
    private ConvergenceChecker convergenceChecker;

    @Before
    public void setUp() {
        optimizer = new BaseOptimizerImpl();
        convergenceChecker = Mockito.mock(ConvergenceChecker.class);
        optimizer.setConvergenceChecker(convergenceChecker);
    }

    @Test
    public void testGetMaxEvaluations() {
        int expectedMaxEvaluations = 1000;
        optimizer.setMaxEvaluations(expectedMaxEvaluations);
        int actualMaxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals(expectedMaxEvaluations, actualMaxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        int expectedEvaluations = 10;
        ((BaseOptimizerImpl)optimizer).setEvaluations(expectedEvaluations);
        int actualEvaluations = optimizer.getEvaluations();
        Assert.assertEquals(expectedEvaluations, actualEvaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker expectedConvergenceChecker = Mockito.mock(ConvergenceChecker.class);
        ConvergenceChecker actualConvergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertEquals(expectedConvergenceChecker, actualConvergenceChecker);
    }

    @Test
    public void testDefaultGetEvaluations() {
        int expectedEvaluations = 0;
        int actualEvaluations = optimizer.getEvaluations();
        Assert.assertEquals(expectedEvaluations, actualEvaluations);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker expectedConvergenceChecker = Mockito.mock(ConvergenceChecker.class);
        optimizer.setConvergenceChecker(expectedConvergenceChecker);
        ConvergenceChecker actualConvergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertEquals(expectedConvergenceChecker, actualConvergenceChecker);
    }

    private class BaseOptimizerImpl implements BaseOptimizer<Integer> {
        private int maxEvaluations;
        private int evaluations;
        private ConvergenceChecker<Integer> convergenceChecker;

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return evaluations;
        }

        @Override
        public ConvergenceChecker<Integer> getConvergenceChecker() {
            return convergenceChecker;
        }

        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        public void setEvaluations(int evaluations) {
            this.evaluations = evaluations;
        }

        public void setConvergenceChecker(ConvergenceChecker<Integer> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }
    }

}