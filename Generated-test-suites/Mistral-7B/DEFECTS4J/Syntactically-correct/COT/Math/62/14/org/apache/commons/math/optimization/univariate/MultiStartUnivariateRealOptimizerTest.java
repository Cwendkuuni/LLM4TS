import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.optim.nonlinear.LocalSearchOptimizer;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @SuppressWarnings("unchecked")
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> getDummyOptimizer() {
        return new LocalSearchOptimizer(new GoalType[]{GoalType.MINIMIZE}, new MaxEval(100));
    }

    @Test
    public void testConstructor() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getDummyOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerator());
        Assert.assertNotNull(msOptimizer.getOptima());
        Assert.assertEquals(10, msOptimizer.getOptima().length);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getDummyOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerator());
        Assert.assertSame(optimizer.getConvergenceChecker(), msOptimizer.getConvergenceChecker());
    }

    @Test
    public void testSetConvergenceChecker() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getDummyOptimizer();
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = new ConvergenceChecker<UnivariateRealPointValuePair>() {
            @Override
            public boolean converged(UnivariateRealPointValuePair current, UnivariateRealPointValuePair previous, int iteration) {
                return false;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerator());
        msOptimizer.setConvergenceChecker(convergenceChecker);
        Assert.assertSame(convergenceChecker, msOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getDummyOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerator());
        int maxEvaluations = msOptimizer.getMaxEvaluations();
        Assert.assertNotSame(optimizer.getMaxEvaluations(), maxEvaluations);
    }

    @Test
    public void testSetMaxEvaluations() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getDummyOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerator());
        msOptimizer.setMaxEvaluations(100);
        Assert.assertEquals(100, msOptimizer.getMaxEvaluations());
        optimizer.setMaxEvaluations(50);
        Assert.assertEquals(100, msOptimizer.getMaxEvaluations());
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_NoConvergence() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getDummyOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerator());
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test exception");
            }
        };
        UnivariateRealPointValuePair result = msOptimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test
    public void testOptimize_Convergence() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getDummyOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerator());
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateRealPointValuePair result = msOptimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
        Assert.assertNotNull(result);
        Assert.assertEquals(0.0, result.getPoint(), 1e-6);
    }

    @Test
    public void testGetOptima() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getDummyOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerator());
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateRealPointValuePair result = msOptimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
        UnivariateRealPointValuePair[] optima = msOptimizer.getOptima();
        Assert.assertNotNull(optima);
        Assert.assertEquals(10, optima.length);
        Assert.assertNotNull(result);
        Assert.assertEquals(0.0, result.getPoint(), 1e-6);
        Assert.assertNotNull(optima[0]);
        Assert.assertEquals(0.0, optima[0].getPoint(), 1e-6);
        for (int i = 1; i < optima.length; ++i) {
            Assert.assertNull(optima[i]);
        }
    }
}