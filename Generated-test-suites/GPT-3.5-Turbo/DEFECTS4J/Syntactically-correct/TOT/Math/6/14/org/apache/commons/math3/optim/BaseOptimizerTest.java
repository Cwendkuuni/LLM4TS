import org.apache.commons.math3.optimization.BaseOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {

    private BaseOptimizer<String> optimizer;

    @Before
    public void setUp() {
        optimizer = new BaseOptimizer<String>() {
            @Override
            public int getMaxEvaluations() {
                return 100;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<String> getConvergenceChecker() {
                return null;
            }
        };
    }

    @Test
    public void testGetMaxEvaluations() {
        Assert.assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        Assert.assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        Assert.assertNull(optimizer.getConvergenceChecker());
    }
}