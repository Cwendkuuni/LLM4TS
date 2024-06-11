import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.optimization.*;

public class MultiStartUnivariateRealOptimizerTest {

    private static final double EPSILON = 1e-9;

    @Test
    public void testGetFunctionValue() throws FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.optimize(x -> x * x, GoalType.MINIMIZE, -10, 10);
        Assert.assertEquals(0.0, optimizer.getFunctionValue(), EPSILON);
    }

    @Test
    public void testGetResult() throws FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = createOptimizer();
        double result = optimizer.optimize(x -> x * x, GoalType.MINIMIZE, -10, 10);
        Assert.assertEquals(0.0, result, EPSILON);
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.setAbsoluteAccuracy(0.1);
        Assert.assertEquals(0.1, optimizer.getAbsoluteAccuracy(), EPSILON);
    }

    @Test
    public void testGetIterationCount() throws FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.optimize(x -> x * x, GoalType.MINIMIZE, -10, 10);
        Assert.assertTrue(optimizer.getIterationCount() > 0);
    }

    @Test
    public void testGetMaximalIterationCount() {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.setMaximalIterationCount(100);
        Assert.assertEquals(100, optimizer.getMaximalIterationCount());
    }

    @Test
    public void testGetMaxEvaluations() {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.setMaxEvaluations(1000);
        Assert.assertEquals(1000, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() throws FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.optimize(x -> x * x, GoalType.MINIMIZE, -10, 10);
        Assert.assertTrue(optimizer.getEvaluations() > 0);
    }

    @Test
    public void testGetRelativeAccuracy() {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.setRelativeAccuracy(0.01);
        Assert.assertEquals(0.01, optimizer.getRelativeAccuracy(), EPSILON);
    }

    @Test
    public void testResetAbsoluteAccuracy() {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.setAbsoluteAccuracy(0.1);
        optimizer.resetAbsoluteAccuracy();
        Assert.assertEquals(1e-6, optimizer.getAbsoluteAccuracy(), EPSILON);
    }

    @Test
    public void testResetMaximalIterationCount() {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.setMaximalIterationCount(100);
        optimizer.resetMaximalIterationCount();
        Assert.assertEquals(Integer.MAX_VALUE, optimizer.getMaximalIterationCount());
    }

    @Test
    public void testResetRelativeAccuracy() {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.setRelativeAccuracy(0.01);
        optimizer.resetRelativeAccuracy();
        Assert.assertEquals(1e-13, optimizer.getRelativeAccuracy(), EPSILON);
    }

    @Test
    public void testSetAbsoluteAccuracy() {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.setAbsoluteAccuracy(0.1);
        Assert.assertEquals(0.1, optimizer.getAbsoluteAccuracy(), EPSILON);
    }

    @Test
    public void testSetMaximalIterationCount() {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.setMaximalIterationCount(100);
        Assert.assertEquals(100, optimizer.getMaximalIterationCount());
    }

    @Test
    public void testSetMaxEvaluations() {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.setMaxEvaluations(1000);
        Assert.assertEquals(1000, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSetRelativeAccuracy() {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.setRelativeAccuracy(0.01);
        Assert.assertEquals(0.01, optimizer.getRelativeAccuracy(), EPSILON);
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealOptimizer optimizer = createOptimizer();
        double result = optimizer.optimize(x -> x * x, GoalType.MINIMIZE, -10, 10);
        Assert.assertEquals(0.0, result, EPSILON);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizeThrowsConvergenceException() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealOptimizer optimizer = createOptimizer();
        optimizer.optimize(x -> 1 / x, GoalType.MINIMIZE, -1, 1);
    }

    @Test
    public void testOptimizeWithStartValue() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealOptimizer optimizer = createOptimizer();
        double result = optimizer.optimize(x -> x * x, GoalType.MINIMIZE, -10, 10, 5.0);
        Assert.assertEquals(0.0, result, EPSILON);
    }

    private UnivariateRealOptimizer createOptimizer() {
        return new MultiStartUnivariateRealOptimizer(new BrentOptimizer(), 5, new RandomGenerator());
    }
}