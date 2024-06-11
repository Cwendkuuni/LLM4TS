import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Test;
import org.mockito.Mockito;

public class MultiStartUnivariateRealOptimizerTest {

    BaseUnivariateRealOptimizer<FUNC> optimizerMock = Mockito.mock(BaseUnivariateRealOptimizer.class);
    RandomGenerator generatorMock = Mockito.mock(RandomGenerator.class);
    MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, generatorMock);

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checkerMock = Mockito.mock(ConvergenceChecker.class);
        multiStartOptimizer.setConvergenceChecker(checkerMock);
        Mockito.verify(optimizerMock).setConvergenceChecker(checkerMock);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> expected = Mockito.mock(ConvergenceChecker.class);
        Mockito.when(optimizerMock.getConvergenceChecker()).thenReturn(expected);
        ConvergenceChecker<UnivariateRealPointValuePair> actual = multiStartOptimizer.getConvergenceChecker();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaxEvaluations() {
        int expected = 100;
        Mockito.when(optimizerMock.getMaxEvaluations()).thenReturn(expected);
        int actual = multiStartOptimizer.getMaxEvaluations();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetEvaluations() {
        int expected = 50;
        Mockito.when(optimizerMock.getEvaluations()).thenReturn(expected);
        int actual = multiStartOptimizer.getEvaluations();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 200;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        Mockito.verify(optimizerMock).setMaxEvaluations(maxEvaluations);
    }

    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] expected = new UnivariateRealPointValuePair[3];
        expected[0] = new UnivariateRealPointValuePair(1.0, 0.0);
        expected[1] = new UnivariateRealPointValuePair(2.0, 0.0);
        expected[2] = new UnivariateRealPointValuePair(3.0, 0.0);
        multiStartOptimizer.optima = expected.clone();
        UnivariateRealPointValuePair[] actual = multiStartOptimizer.getOptima();
        assertEquals(expected, actual);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaThrowsException() {
        multiStartOptimizer.getOptima();
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        UnivariateRealFunction f = Mockito.mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        double startValue = 0.0;
        UnivariateRealPointValuePair expected = new UnivariateRealPointValuePair(1.0, 0.0);
        Mockito.when(optimizerMock.optimize(f, goal, min, max, startValue)).thenReturn(expected);
        UnivariateRealPointValuePair actual = multiStartOptimizer.optimize(f, goal, min, max, startValue);
        assertEquals(expected, actual);
        Mockito.verify(generatorMock, Mockito.times(3)).nextDouble();
        Mockito.verify(optimizerMock, Mockito.times(3)).optimize(f, goal, min, max);
    }

    @Test
    public void testOptimizeWithException() throws FunctionEvaluationException {
        UnivariateRealFunction f = Mockito.mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        double startValue = 0.0;
        Mockito.when(optimizerMock.optimize(f, goal, min, max, startValue)).thenThrow(new FunctionEvaluationException());
        try {
            multiStartOptimizer.optimize(f, goal, min, max, startValue);
        } catch (FunctionEvaluationException e) {
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithConvergenceException() throws FunctionEvaluationException {
        UnivariateRealFunction f = Mockito.mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        double startValue = 0.0;
        Mockito.when(optimizerMock.optimize(f, goal, min, max, startValue)).thenThrow(new ConvergenceException());
        multiStartOptimizer.optimize(f, goal, min, max, startValue);
    }

    @Test
    public void testSortPairs() {
        MultiStartUnivariateRealOptimizer<FUNC>.UnivariateRealPointValuePair[] pairs = new MultiStartUnivariateRealOptimizer.UnivariateRealPointValuePair[3];
        pairs[0] = new MultiStartUnivariateRealOptimizer.UnivariateRealPointValuePair(1.0, 0.0);
        pairs[1] = new MultiStartUnivariateRealOptimizer.UnivariateRealPointValuePair(2.0, 0.0);
        pairs[2] = new MultiStartUnivariateRealOptimizer.UnivariateRealPointValuePair(3.0, 0.0);
        GoalType goal = GoalType.MINIMIZE;
        multiStartOptimizer.sortPairs(goal);
        assertEquals(pairs[0], multiStartOptimizer.optima[0]);
        assertEquals(pairs[1], multiStartOptimizer.optima[1]);
        assertEquals(pairs[2], multiStartOptimizer.optima[2]);
    }
}