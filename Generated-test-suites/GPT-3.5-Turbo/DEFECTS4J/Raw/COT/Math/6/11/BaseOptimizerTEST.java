

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;

public class BaseOptimizerTest {

    private BaseOptimizer<PAIR> optimizer;

    @Before
    public void setUp() {
        optimizer = new BaseOptimizer<PAIR>() {
            @Override
            public int getMaxEvaluations() {
                return 0;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return null;
            }
        };
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNull(convergenceChecker);
    }

}

