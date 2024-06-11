import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Comparator;

import static org.mockito.Mockito.*;


public class MultiStartUnivariateRealOptimizerTest {
    private BaseUnivariateRealOptimizer optimizer;
    private RandomGenerator generator;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;

    @Before
    public void setUp() {
        optimizer = Mockito.mock(BaseUnivariateRealOptimizer.class);
        generator = Mockito.mock(RandomGenerator.class);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, generator);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = mock(ConvergenceChecker.class);
        multiStartOptimizer.setConvergenceChecker(checker);

        verify(optimizer).setConvergenceChecker(checker);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(checker);

        assertEquals(multiStartOptimizer.getConvergenceChecker(), checker);
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 100;
        when(optimizer.getMaxEvaluations()).thenReturn(maxEvaluations);

        assertEquals(multiStartOptimizer.getMaxEvaluations(), maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        int totalEvaluations = 50;
        when(optimizer.getEvaluations()).thenReturn(totalEvaluations);

        assertEquals(multiStartOptimizer.getEvaluations(), totalEvaluations);
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);

        verify(optimizer).setMaxEvaluations(maxEvaluations);
    }

    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[3];
        optima[0] = new UnivariateRealPointValuePair(1.0, 0.1);
        optima[1] = new UnivariateRealPointValuePair(2.0, 0.2);
        optima[2] = null;
        multiStartOptimizer.optima = optima.clone();

        UnivariateRealPointValuePair[] result = multiStartOptimizer.getOptima();

        assertTrue(Arrays.equals(result, optima));
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptima_NotComputed() {
        multiStartOptimizer.getOptima();
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 2.0;

        UnivariateRealPointValuePair expectedOptimum = new UnivariateRealPointValuePair(4.0, 0.5);
        UnivariateRealPointValuePair[] expectedOptima = new UnivariateRealPointValuePair[3];
        expectedOptima[0] = expectedOptimum;
        expectedOptima[1] = null;
        expectedOptima[2] = null;

        when(optimizer.optimize(f, goal, min, max)).thenReturn(expectedOptimum);
        when(generator.nextDouble()).thenReturn(0.5);
        when(optimizer.getEvaluations()).thenReturn(10);
        when(optimizer.getMaxEvaluations()).thenReturn(100);

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max, startValue);
        UnivariateRealPointValuePair[] optimaResult = multiStartOptimizer.getOptima();

        assertEquals(result, expectedOptimum);
        assertTrue(Arrays.equals(optimaResult, expectedOptima));
        verify(optimizer, times(3)).optimize(f, goal, min, max);
        verify(generator, times(2)).nextDouble();
        verify(optimizer, times(3)).getEvaluations();
        verify(optimizer).setMaxEvaluations(100 - 10);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimize_NoConvergence() throws FunctionEvaluationException {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 2.0;

        when(optimizer.optimize(f, goal, min, max)).thenThrow(new ConvergenceException());

        multiStartOptimizer.optimize(f, goal, min, max, startValue);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimize_NotComputed() throws FunctionEvaluationException {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        multiStartOptimizer.getOptima();

        multiStartOptimizer.optimize(f, goal, min, max);
    }

    @Test
    public void testSortPairs_Minimize() {
        GoalType goal = GoalType.MINIMIZE;
        UnivariateRealPointValuePair o1 = new UnivariateRealPointValuePair(1.0, 0.1);
        UnivariateRealPointValuePair o2 = new UnivariateRealPointValuePair(2.0, 0.2);

        UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[2];
        optima[0] = o2;
        optima[1] = o1;

        multiStartOptimizer.sortPairs(goal);

        assertTrue(Arrays.equals(optima, multiStartOptimizer.optima));
    }

    @Test
    public void testSortPairs_Maximize() {
        GoalType goal = GoalType.MAXIMIZE;
        UnivariateRealPointValuePair o1 = new UnivariateRealPointValuePair(1.0, 0.1);
        UnivariateRealPointValuePair o2 = new UnivariateRealPointValuePair(2.0, 0.2);

        UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[2];
        optima[0] = o1;
        optima[1] = o2;

        multiStartOptimizer.sortPairs(goal);

        assertTrue(Arrays.equals(optima, multiStartOptimizer.optima));
    }
}