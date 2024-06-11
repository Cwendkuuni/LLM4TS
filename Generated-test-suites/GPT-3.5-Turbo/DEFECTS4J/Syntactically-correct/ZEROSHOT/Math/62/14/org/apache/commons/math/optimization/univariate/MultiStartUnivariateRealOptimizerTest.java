import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

import org.junit.Assert;

public class MultiStartUnivariateRealOptimizerTest {
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(new BaseUnivariateRealOptimizerMock(), 5);
    }

    @Test
    public void testGetMaxEvaluations() {
        optimizer.setMaxEvaluations(100);
        Assert.assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        Assert.assertEquals(0, optimizer.getEvaluations());

        optimizer.optimize(new UnivariateRealFunctionMock(), GoalType.MINIMIZE, 0.0, 1.0);
        optimizer.optimize(new UnivariateRealFunctionMock(), GoalType.MAXIMIZE, 0.0, 1.0);

        Assert.assertEquals(20, optimizer.getEvaluations());
    }

    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();

        Assert.assertEquals(5, optima.length);
        for (UnivariateRealPointValuePair opt : optima) {
            Assert.assertNull(opt);
        }

        optimizer.optimize(new UnivariateRealFunctionMock(), GoalType.MINIMIZE, 0.0, 1.0);

        optima = optimizer.getOptima();

        Assert.assertEquals(5, optima.length);
        for (int i = 1; i < optima.length; i++) {
            Assert.assertNull(optima[i]);
        }
    }

    @Test
    public void testOptimize() {
        UnivariateRealPointValuePair opt = optimizer.optimize(new UnivariateRealFunctionMock(), GoalType.MINIMIZE, 0.0, 1.0);

        Assert.assertNotNull(opt);
        Assert.assertEquals(0.0, opt.getPoint(), 0.00001);
        Assert.assertEquals(0.0, opt.getValue(), 0.00001);
    }

    private class BaseUnivariateRealOptimizerMock implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {
        private int maxEvaluations;
        private int evaluations;

        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}

        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return null;
        }

        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        public int getEvaluations() {
            return evaluations;
        }

        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) {
            evaluations++;
            return new UnivariateRealPointValuePairMock();
        }

        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) {
            evaluations++;
            return new UnivariateRealPointValuePairMock();
        }
    }

    private class UnivariateRealFunctionMock implements UnivariateRealFunction {
        public double value(double x) {
            return FastMath.pow(x, 2);
        }
    }

    private class UnivariateRealPointValuePairMock implements UnivariateRealPointValuePair {
        public double getPoint() {
            return 0.0;
        }

        public double getValue() {
            return 0.0;
        }
    }
}