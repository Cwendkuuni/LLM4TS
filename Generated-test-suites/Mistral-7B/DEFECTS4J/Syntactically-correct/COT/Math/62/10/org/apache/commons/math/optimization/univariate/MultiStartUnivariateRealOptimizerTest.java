import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    @Test
    public void testSetConvergenceChecker() {
        BaseUnivariateRealOptimizer<FUNC> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 10, new RandomGenerator());

        ConvergenceChecker<UnivariateRealPointValuePair> convergenceCheckerMock = mock(ConvergenceChecker.class);
        multiStartOptimizer.setConvergenceChecker(convergenceCheckerMock);

        Assert.assertEquals(convergenceCheckerMock, multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseUnivariateRealOptimizer<FUNC> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 10, new RandomGenerator());

        ConvergenceChecker<UnivariateRealPointValuePair> convergenceCheckerMock = mock(ConvergenceChecker.class);
        multiStartOptimizer.setConvergenceChecker(convergenceCheckerMock);

        Assert.assertSame(convergenceCheckerMock, multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 10, new RandomGenerator());

        int maxEvaluations = multiStartOptimizer.getMaxEvaluations();
        Assert.assertEquals(optimizerMock.getMaxEvaluations(), maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 10, new RandomGenerator());

        int totalEvaluations = multiStartOptimizer.getEvaluations();
        int optimizerEvaluations = optimizerMock.getEvaluations();
        Assert.assertEquals(totalEvaluations, optimizerEvaluations);
    }

    @Test
    public void testSetMaxEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 10, new RandomGenerator());

        int maxEvaluations = 50;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        int optimizerMaxEvaluations = optimizerMock.getMaxEvaluations();
        Assert.assertEquals(maxEvaluations, optimizerMaxEvaluations);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsNegative() {
        BaseUnivariateRealOptimizer<FUNC> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 10, new RandomGenerator());

        multiStartOptimizer.setMaxEvaluations(-5);
    }

    @Test
    public void testGetOptimaEmpty() {
        BaseUnivariateRealOptimizer<FUNC> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 10, new RandomGenerator());

        Assert.assertNull(multiStartOptimizer.getOptima());
    }

    @Test
    public void testGetOptimaOneStart() {
        BaseUnivariateRealFunction functionMock = mock(BaseUnivariateRealFunction.class);
        BaseUnivariateRealOptimizer<BaseUnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        when(optimizerMock.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0)).thenReturn(new UnivariateRealPointValuePair(0.5, 2.5));
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 1, new RandomGenerator());

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        Assert.assertArrayEquals(new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(0.5, 2.5)}, optima);
    }

    @Test
    public void testGetOptimaMultipleStarts() {
        BaseUnivariateRealFunction functionMock = mock(BaseUnivariateRealFunction.class);
        BaseUnivariateRealOptimizer<BaseUnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        when(optimizerMock.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0)).thenReturn(new UnivariateRealPointValuePair(0.5, 2.5));
        when(optimizerMock.optimize(functionMock, GoalType.MINIMIZE, 1.0, 2.0)).thenReturn(new UnivariateRealPointValuePair(1.5, 3.5));
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 2, new RandomGenerator());

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        Assert.assertArrayEquals(new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(0.5, 2.5), new UnivariateRealPointValuePair(1.5, 3.5)}, optima);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaNotOptimized() {
        BaseUnivariateRealOptimizer<FUNC> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 10, new RandomGenerator());

        Assert.assertNull(multiStartOptimizer.getOptima());
        multiStartOptimizer.optimize(mock(Func.class), GoalType.MINIMIZE, 0.0, 1.0);
        Assert.assertNull(multiStartOptimizer.getOptima());
    }

    @Test
    public void testOptimizeOneStart() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealFunction functionMock = mock(BaseUnivariateRealFunction.class);
        BaseUnivariateRealOptimizer<BaseUnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        when(optimizerMock.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0)).thenReturn(new UnivariateRealPointValuePair(0.5, 2.5));
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 1, new RandomGenerator());

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0);
        Assert.assertEquals(0.5, result.getX(), 0.001);
        Assert.assertEquals(2.5, result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeMultipleStarts() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealFunction functionMock = mock(BaseUnivariateRealFunction.class);
        BaseUnivariateRealOptimizer<BaseUnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        when(optimizerMock.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0)).thenReturn(new UnivariateRealPointValuePair(0.5, 2.5));
        when(optimizerMock.optimize(functionMock, GoalType.MINIMIZE, 1.0, 2.0)).thenReturn(new UnivariateRealPointValuePair(1.5, 3.5));
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 2, new RandomGenerator());

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0);
        Assert.assertEquals(0.5, result.getX(), 0.001);
        Assert.assertEquals(2.5, result.getValue(), 0.001);

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        Assert.assertArrayEquals(new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(0.5, 2.5), new UnivariateRealPointValuePair(1.5, 3.5)}, optima);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealFunction functionMock = mock(BaseUnivariateRealFunction.class);
        when(functionMock.value(0.0)).thenThrow(new FunctionEvaluationException());
        BaseUnivariateRealOptimizer<BaseUnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 1, new RandomGenerator());

        multiStartOptimizer.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeConvergenceException() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealFunction functionMock = mock(BaseUnivariateRealFunction.class);
        when(functionMock.value(0.0)).thenThrow(new ConvergenceException("Mock convergence exception"));
        BaseUnivariateRealOptimizer<BaseUnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 1, new RandomGenerator());

        multiStartOptimizer.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test
    public void testOptimizeMultipleEvaluations() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealFunction functionMock = mock(BaseUnivariateRealFunction.class);
        BaseUnivariateRealOptimizer<BaseUnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        when(optimizerMock.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0)).thenReturn(new UnivariateRealPointValuePair(0.5, 2.5));
        when(optimizerMock.optimize(functionMock, GoalType.MINIMIZE, 1.0, 2.0)).thenReturn(new UnivariateRealPointValuePair(1.5, 3.5));
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 2, new RandomGenerator());

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0);
        Assert.assertEquals(0.5, result.getX(), 0.001);
        Assert.assertEquals(2.5, result.getValue(), 0.001);

        int totalEvaluations = multiStartOptimizer.getEvaluations();
        int optimizerEvaluations = optimizerMock.getEvaluations();
        Assert.assertEquals(totalEvaluations, optimizerEvaluations * 2);
    }
}