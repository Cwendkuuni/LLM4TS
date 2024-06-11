import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.ConvergenceException;
import org.apache.commons.math.optimization.GoalType;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testSetConvergenceChecker() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<>();
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<>();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 2, new RandomGenerator());
        multiStartOptimizer.setConvergenceChecker(checker);
        Assert.assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<>();
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<>();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 2, new RandomGenerator());
        optimizer.setConvergenceChecker(checker);
        Assert.assertEquals(checker, multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<>();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 2, new RandomGenerator());
        optimizer.setMaxEvaluations(100);
        Assert.assertEquals(100, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<>();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 2, new RandomGenerator());
        optimizer.setMaxEvaluations(100);
        MultiStartUnivariateRealOptimizer.optimize(optimizer, goal, min, max);
        Assert.assertEquals(100, multiStartOptimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<>();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 2, new RandomGenerator());
        multiStartOptimizer.setMaxEvaluations(200);
        Assert.assertEquals(200, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetOptima() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<>();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 2, new RandomGenerator());
        MultiStartUnivariateRealOptimizer.optimize(optimizer, goal, min, max);
        Assert.assertNotNull(multiStartOptimizer.getOptima());
    }

    @Test(expected = Exception.class)
    public void testNoOptimumComputed() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<>();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 2, new RandomGenerator());
        multiStartOptimizer.getOptima();
    }

    @Test
    public void testOptimize() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<>();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = null;
        try {
            multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 2, new RandomGenerator());
        } catch (ConvergenceException ce) {
            Assert.fail();
        }
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, GoalType.MINIMIZE, 0, 100, 0);
        Assert.assertNotNull(result);
    }
}