import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    private BaseUnivariateRealOptimizer<FUNC> optimizerMock;
    private MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer;
    private RandomGenerator randomGeneratorMock;

    @Before
    public void setup() {
        optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        randomGeneratorMock = mock(RandomGenerator.class);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, randomGeneratorMock);
    }

    // Test for the constructor
    @Test
    public void testConstructor() {
        Assert.assertNotNull(multiStartOptimizer);
    }

    // Test for setConvergenceChecker and getConvergenceChecker
    @Test
    public void testSetAndGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceCheckerMock = mock(ConvergenceChecker.class);
        multiStartOptimizer.setConvergenceChecker(convergenceCheckerMock);
        Assert.assertEquals(convergenceCheckerMock, multiStartOptimizer.getConvergenceChecker());
    }

    // Test for getMaxEvaluations
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 10;
        when(optimizerMock.getMaxEvaluations()).thenReturn(maxEvaluations);
        when(multiStartOptimizer.getMaxEvaluations()).thenReturn(maxEvaluations);
        Assert.assertEquals(maxEvaluations, multiStartOptimizer.getMaxEvaluations());
    }

    // Test for getEvaluations
    @Test
    public void testGetEvaluations() {
        int evaluations = 5;
        when(optimizerMock.getEvaluations()).thenReturn(evaluations);
        when(multiStartOptimizer.getEvaluations()).thenReturn(evaluations);
        Assert.assertEquals(evaluations, multiStartOptimizer.getEvaluations());
    }

    // Test for setMaxEvaluations
    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 15;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        verify(optimizerMock).setMaxEvaluations(maxEvaluations);
    }

    // Test for getOptima
    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaBeforeOptimize() {
        multiStartOptimizer.getOptima();
    }

    // Test for optimize with a mock UnivariateRealFunction
    @Test
    public void testOptimizeMockFunction() throws Exception {
        UnivariateRealFunction mockFunction = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double expectedOptimum = 2.5;

        when(optimizerMock.optimize(mockFunction, goal, min, max)).thenReturn(new UnivariateRealPointValuePair(3.0, 5.0));
        when(randomGeneratorMock.nextDouble()).thenReturn(0.5);

        UnivariateRealPointValuePair actualOptimum = multiStartOptimizer.optimize(mockFunction, goal, min, max);

        Assert.assertNotNull(actualOptimum);
        Assert.assertEquals(expectedOptimum, actualOptimum.getValue(), 0.001);
        verify(optimizerMock).optimize(mockFunction, goal, anyDouble(), anyDouble());
        for (int i = 0; i < 2; ++i) {
            verify(optimizerMock).setMaxEvaluations(optimizerMock.getMaxEvaluations() - 2);
        }
        verify(randomGeneratorMock, times(3)).nextDouble();
    }

    // Test for optimize with a mock UnivariateRealFunction and a specified start value
    @Test
    public void testOptimizeMockFunctionWithStartValue() throws Exception {
        UnivariateRealFunction mockFunction = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        double expectedOptimum = 2.5;

        when(optimizerMock.optimize(mockFunction, goal, min, max)).thenReturn(new UnivariateRealPointValuePair(3.0, 5.0));
        when(randomGeneratorMock.nextDouble()).thenReturn(0.5);

        UnivariateRealPointValuePair actualOptimum = multiStartOptimizer.optimize(mockFunction, goal, min, max, startValue);

        Assert.assertNotNull(actualOptimum);
        Assert.assertEquals(expectedOptimum, actualOptimum.getValue(), 0.001);
        verify(optimizerMock).optimize(mockFunction, goal, anyDouble(), anyDouble());
        for (int i = 0; i < 3; ++i) {
            verify(optimizerMock).setMaxEvaluations(optimizerMock.getMaxEvaluations() - 2);
        }
        verify(randomGeneratorMock, times(6)).nextDouble();
    }

    // Test for optimize with a mock UnivariateRealFunction when function does not converge
    @Test(expected = ConvergenceException.class)
    public void testOptimizeMockFunctionNoConvergence() throws Exception {
        UnivariateRealFunction mockFunction = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        when(optimizerMock.optimize(mockFunction, goal, min, max)).thenThrow(new ConvergenceException());

        multiStartOptimizer.optimize(mockFunction, goal, min, max);
    }

    // Test for optimize when the optimizer throws a FunctionEvaluationException
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeMockFunctionFunctionEvaluationException() throws Exception {
        UnivariateRealFunction mockFunction = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        when(optimizerMock.optimize(mockFunction, goal, min, max)).thenThrow(new FunctionEvaluationException());

        multiStartOptimizer.optimize(mockFunction, goal, min, max);
    }

    // Test for optimize when the optimizer throws an MathIllegalStateException
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeMockFunctionMathIllegalStateException() throws Exception {
        UnivariateRealFunction mockFunction = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        when(optimizerMock.optimize(mockFunction, goal, min, max)).thenThrow(new MathIllegalStateException());

        multiStartOptimizer.optimize(mockFunction, goal, min, max);
    }
}