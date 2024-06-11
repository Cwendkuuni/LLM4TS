import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class MultiStartUnivariateRealOptimizerTest {

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizerMock;
    private RandomGenerator generatorMock;
    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction functionMock;
    private GoalType goal;

    @Before
    public void setUp() {
        optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        generatorMock = mock(RandomGenerator.class);
        optimizer = new MultiStartUnivariateRealOptimizer(optimizerMock, 5, generatorMock);
        functionMock = mock(UnivariateRealFunction.class);
        goal = GoalType.MINIMIZE;
    }

    @Test
    public void testConstructor() {
        assertEquals(optimizerMock, optimizer.optimizer);
        assertEquals(5, optimizer.starts);
        assertEquals(generatorMock, optimizer.generator);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checkerMock = mock(ConvergenceChecker.class);
        optimizer.setConvergenceChecker(checkerMock);
        verify(optimizerMock).setConvergenceChecker(checkerMock);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checkerMock = mock(ConvergenceChecker.class);
        when(optimizerMock.getConvergenceChecker()).thenReturn(checkerMock);
        assertEquals(checkerMock, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 100;
        when(optimizerMock.getMaxEvaluations()).thenReturn(maxEvaluations);
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = 50;
        when(optimizerMock.getEvaluations()).thenReturn(evaluations);
        assertEquals(evaluations, optimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);
        verify(optimizerMock).setMaxEvaluations(maxEvaluations);
    }

    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optimaMock = new UnivariateRealPointValuePair[5];
        UnivariateRealPointValuePair[] expectedOptima = new UnivariateRealPointValuePair[5];
        when(optimizerMock.getOptima()).thenReturn(optimaMock);
        assertEquals(expectedOptima, optimizer.getOptima());
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaWithoutOptimization() {
        optimizer.getOptima();
    }

    @Test
    public void testOptimize() throws Exception {
        double min = 1.0;
        double max = 2.0;
        double startValue = 1.5;
        UnivariateRealPointValuePair expectedOptimumMock = mock(UnivariateRealPointValuePair.class);
        UnivariateRealPointValuePair[] expectedOptimaMock = new UnivariateRealPointValuePair[]{expectedOptimumMock};
        when(optimizerMock.optimize(functionMock, goal, min, max, startValue)).thenReturn(expectedOptimumMock);
        when(optimizerMock.getEvaluations()).thenReturn(10);

        UnivariateRealPointValuePair optimum = optimizer.optimize(functionMock, goal, min, max, startValue);

        verify(generatorMock, never()).nextDouble();
        verify(optimizerMock).optimize(functionMock, goal, min, max, startValue);
        verify(optimizerMock).setMaxEvaluations(90);
        verify(optimizerMock).getEvaluations();

        assertEquals(expectedOptimumMock, optimum);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoOptimum() throws Exception {
        double min = 1.0;
        double max = 2.0;
        double startValue = 1.5;
        when(optimizerMock.optimize(functionMock, goal, min, max, startValue)).thenThrow(new ConvergenceException());

        optimizer.optimize(functionMock, goal, min, max, startValue);
    }

    @Test
    public void testOptimizeWithStarts() throws Exception {
        double min = 1.0;
        double max = 2.0;
        double expectedBound1 = 1.1;
        double expectedBound2 = 1.8;
        double startValue = 1.5;
        UnivariateRealPointValuePair expectedOptimumMock = mock(UnivariateRealPointValuePair.class);
        UnivariateRealPointValuePair[] expectedOptimaMock = new UnivariateRealPointValuePair[]{expectedOptimumMock};
        when(optimizerMock.optimize(functionMock, goal, expectedBound1, expectedBound2)).thenReturn(expectedOptimumMock);
        when(optimizerMock.getEvaluations()).thenReturn(10);
        when(generatorMock.nextDouble()).thenReturn(0.1);
        when(generatorMock.nextDouble()).thenReturn(0.8);

        UnivariateRealPointValuePair optimum = optimizer.optimize(functionMock, goal, min, max, startValue);

        verify(generatorMock, times(2)).nextDouble();
        verify(optimizerMock).optimize(functionMock, goal, expectedBound1, expectedBound2);
        verify(optimizerMock).setMaxEvaluations(90);
        verify(optimizerMock).getEvaluations();

        assertEquals(expectedOptimumMock, optimum);
    }
}