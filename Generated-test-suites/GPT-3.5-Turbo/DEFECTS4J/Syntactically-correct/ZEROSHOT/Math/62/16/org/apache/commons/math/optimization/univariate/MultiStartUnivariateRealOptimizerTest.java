import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.mockito.Mockito.*;

public class MultiStartUnivariateRealOptimizerTest {
    @Test
    public void testSetConvergenceChecker() {
        // Arrange
        BaseUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> optimizer = mock(BaseUnivariateRealOptimizer.class);
        ConvergenceChecker<UnivariateRealPointValuePair> checker = mock(ConvergenceChecker.class);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, mock(RandomGenerator.class));

        // Act
        multiStartOptimizer.setConvergenceChecker(checker);

        // Assert
        verify(optimizer).setConvergenceChecker(checker);
    }

    @Test
    public void testGetConvergenceChecker() {
        // Arrange
        BaseUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> optimizer = mock(BaseUnivariateRealOptimizer.class);
        ConvergenceChecker<UnivariateRealPointValuePair> checker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(checker);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, mock(RandomGenerator.class));

        // Act
        ConvergenceChecker<UnivariateRealPointValuePair> result = multiStartOptimizer.getConvergenceChecker();

        // Assert
        Assert.assertEquals(checker, result);
    }

    @Test
    public void testGetMaxEvaluations() {
        // Arrange
        BaseUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> optimizer = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, mock(RandomGenerator.class));

        // Act
        multiStartOptimizer.setMaxEvaluations(100);

        // Assert
        verify(optimizer).setMaxEvaluations(100);
        Assert.assertEquals(100, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // Arrange
        BaseUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> optimizer = mock(BaseUnivariateRealOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(50);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, mock(RandomGenerator.class));

        // Act
        int result = multiStartOptimizer.getEvaluations();

        // Assert
        Assert.assertEquals(50, result);
    }

    @Test
    public void testSetMaxEvaluations() {
        // Arrange
        BaseUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> optimizer = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, mock(RandomGenerator.class));


        // Act & Assert
        multiStartOptimizer.setMaxEvaluations(100);
        verify(optimizer).setMaxEvaluations(100);
    }

    @Test
    public void testGetOptima() {
        // Arrange
        BaseUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> optimizer =
                mock(BaseUnivariateRealOptimizer.class);
        UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[3];
        optima[0] = new UnivariateRealPointValuePair(1.0, 1.0);
        optima[2] = new UnivariateRealPointValuePair(3.0, 3.0);
        when(optimizer.getOptima()).thenReturn(optima);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, mock(RandomGenerator.class));

        // Act
        UnivariateRealPointValuePair[] result = multiStartOptimizer.getOptima();

        // Assert
        verify(optimizer).getOptima();
        Assert.assertArrayEquals(optima, result);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptima_NoOptimumComputed() {
        // Arrange
        BaseUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> optimizer =
                mock(BaseUnivariateRealOptimizer.class);
        UnivariateRealPointValuePair[] optima = null;
        when(optimizer.getOptima()).thenReturn(optima);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, mock(RandomGenerator.class));

        // Act & Assert
        UnivariateRealPointValuePair[] result = multiStartOptimizer.getOptima();
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        // Arrange
        BaseUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> optimizer =
                mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, mock(RandomGenerator.class));
        UnivariateRealPointValuePair expectedResult = new UnivariateRealPointValuePair(1.0, 1.0);
        when(optimizer.optimize(any(), any(), anyDouble(), anyDouble())).thenReturn(expectedResult);

        // Act
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(mock(TestFunction.class),
                GoalType.MINIMIZE, 0.0, 10.0);

        // Assert
        Assert.assertSame(expectedResult, result);
    }

    @Test
    public void testOptimize_StartsLessThanOrEqualTo1() throws FunctionEvaluationException {
        // Arrange
        BaseUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> optimizer =
                mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 1, mock(RandomGenerator.class));
        UnivariateRealPointValuePair expectedResult = new UnivariateRealPointValuePair(1.0, 1.0);
        when(optimizer.optimize(any(), any(), anyDouble(), anyDouble())).thenReturn(expectedResult);

        // Act
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(mock(TestFunction.class),
                GoalType.MINIMIZE, 0.0, 10.0);

        // Assert
        Assert.assertSame(expectedResult, result);
        verifyNoMoreInteractions(optimizer);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimize_NoConvergence() throws FunctionEvaluationException {
        // Arrange
        BaseUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> optimizer =
                mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, mock(RandomGenerator.class));
        when(optimizer.optimize(any(), any(), anyDouble(), anyDouble())).thenThrow(new ConvergenceException());

        // Act & Assert
        multiStartOptimizer.optimize(mock(TestFunction.class), GoalType.MINIMIZE, 0.0, 10.0);
    }

    @Test
    public void testSortPairs() {
        // Arrange
        BaseUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> optimizer =
                mock(BaseUnivariateRealOptimizer.class);
        UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[3];
        optima[0] = new UnivariateRealPointValuePair(1.0, 1.0);
        optima[2] = new UnivariateRealPointValuePair(3.0, 3.0);
        MultiStartUnivariateRealOptimizer<BaseUnivariateRealOptimizer<TestFunction>> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, mock(RandomGenerator.class));
        multiStartOptimizer.optima = optima;

        // Act
        multiStartOptimizer.sortPairs(GoalType.MINIMIZE);

        // Assert
        Assert.assertEquals(3.0, multiStartOptimizer.optima[0].getValue(), 0.0);
        Assert.assertEquals(1.0, multiStartOptimizer.optima[2].getValue(), 0.0);
    }

    private class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return 0;
        }
    }
}