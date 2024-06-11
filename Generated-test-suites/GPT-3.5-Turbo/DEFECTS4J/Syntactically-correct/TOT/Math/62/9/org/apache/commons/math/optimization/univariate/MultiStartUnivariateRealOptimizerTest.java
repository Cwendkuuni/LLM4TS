import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.random.RandomDataGenerator;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private class MockFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x;
        }
    }

    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(
                new MockUnivariateRealOptimizer(), 5, new RandomDataGenerator());
    }

    @After
    public void tearDown() {
        optimizer = null;
    }

    @Test
    public void testOptimize() {
        MockFunction mockFunction = new MockFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        UnivariateRealPointValuePair result = null;
        try {
            result = optimizer.optimize(mockFunction, goalType, min, max);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getPoint() >= min && result.getPoint() <= max);
    }

    @Test
    public void testGetOptima() {
        MockFunction mockFunction = new MockFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        try {
            UnivariateRealPointValuePair[] result = optimizer.getOptima();
            Assert.assertNotNull(result);
            Assert.assertEquals(result.length, 5);
            for (UnivariateRealPointValuePair pair : result) {
                if (pair != null) {
                    Assert.assertTrue(pair.getPoint() >= min && pair.getPoint() <= max);
                    break;
                }
            }
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMaxEvaluations() {
        MockFunction mockFunction = new MockFunction();
        GoalType goalType = GoalType.MINIMIZE;
        int maxEvaluations = 1000;
        optimizer.setMaxEvaluations(maxEvaluations);
        Assert.assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaWithoutOptimize() {
        optimizer.getOptima();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithConvergenceException() {
        MockFunction mockFunction = new MockFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        optimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(
                new MockUnivariateRealOptimizerWithConvergenceException(), 5, new RandomDataGenerator());
        optimizer.optimize(mockFunction, goalType, min, max);
    }
}

class MockUnivariateRealOptimizerWithConvergenceException implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {
    public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
    public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {return null;}
    public int getMaxEvaluations() {return 0;}
    public int getEvaluations() {return 0;}
    public void setMaxEvaluations(int maxEvaluations) {}
    public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goalType, double min, double max)
            throws FunctionEvaluationException {
        throw new ConvergenceException();
    }
}

class MockUnivariateRealOptimizer implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {
    public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
    public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {return null;}
    public int getMaxEvaluations() {return 0;}
    public int getEvaluations() {return 0;}
    public void setMaxEvaluations(int maxEvaluations) {}
    public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goalType, double min, double max)
            throws FunctionEvaluationException {
        return new UnivariateRealPointValuePair(0, f.value(0));
    }
}