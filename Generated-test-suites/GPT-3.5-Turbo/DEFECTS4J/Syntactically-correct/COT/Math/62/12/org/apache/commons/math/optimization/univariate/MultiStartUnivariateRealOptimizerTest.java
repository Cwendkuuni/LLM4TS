import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;

public class MultiStartUnivariateRealOptimizerTest {
    private BaseUnivariateRealOptimizer<Object> optimizer;
    private MultiStartUnivariateRealOptimizer<Object> multiStart;

    @Before
    public void setUp() {
        optimizer = mock(BaseUnivariateRealOptimizer.class);
        multiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 1, null);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<Object> checker = mock(ConvergenceChecker.class);
        multiStart.setConvergenceChecker(checker);
        verify(optimizer).setConvergenceChecker(checker);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<Object> checker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(checker);
        assertEquals(checker, multiStart.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 100;
        when(optimizer.getMaxEvaluations()).thenReturn(maxEvaluations);
        assertEquals(maxEvaluations, multiStart.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = 50;
        when(optimizer.getEvaluations()).thenReturn(evaluations);
        assertEquals(evaluations, multiStart.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        multiStart.setMaxEvaluations(maxEvaluations);
        verify(optimizer).setMaxEvaluations(maxEvaluations);
    }

    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[2];
        multiStart.optima = optima;
        assertArrayEquals(optima, multiStart.getOptima());
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        FUNC f = mock(FUNC.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        UnivariateRealPointValuePair expected = mock(UnivariateRealPointValuePair.class);
        when(optimizer.optimize(f, goal, min, max)).thenReturn(expected);
        UnivariateRealPointValuePair result = multiStart.optimize(f, goal, min, max);
        verify(optimizer).setConvergenceChecker(any(ConvergenceChecker.class));
        verify(optimizer).getEvaluations();
        verify(optimizer).setMaxEvaluations(anyInt());
        assertEquals(expected, result);
        assertEquals(optimizer.getEvaluations(), multiStart.getEvaluations());
    }

    @Test
    public void testOptimizeWithStartValue() {
        FUNC f = mock(FUNC.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        UnivariateRealPointValuePair expected = mock(UnivariateRealPointValuePair.class);
        when(optimizer.optimize(f, goal, min, max, startValue)).thenReturn(expected);
        UnivariateRealPointValuePair result = multiStart.optimize(f, goal, min, max, startValue);
        verify(optimizer).setConvergenceChecker(any(ConvergenceChecker.class));
        verify(optimizer).getEvaluations();
        verify(optimizer).setMaxEvaluations(anyInt());
        assertEquals(expected, result);
        assertEquals(optimizer.getEvaluations(), multiStart.getEvaluations());
    }
}