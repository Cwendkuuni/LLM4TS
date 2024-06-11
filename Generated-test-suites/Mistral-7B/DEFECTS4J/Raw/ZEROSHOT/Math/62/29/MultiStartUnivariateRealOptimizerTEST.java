import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MultiStartUnivariateRealOptimizerTest {

    private static final double EPSILON = 1e-10;
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    private UnivariateRealFunction testFunction;
    private RandomGenerator randomGenerator;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        optimizer = mock(BaseUnivariateRealOptimizer.class);
        testFunction = mock(UnivariateRealFunction.class);
        randomGenerator = mock(RandomGenerator.class);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, randomGenerator);
    }

    @Test
    public void testSetters() {
        assertNull(multiStartOptimizer.getConvergenceChecker());
        assertNull(multiStartOptimizer.getOptima());

        final ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        verify(convergenceChecker, times(1)).setOptimizer(multiStartOptimizer);

        multiStartOptimizer.setMaxEvaluations(100);
        verify(optimizer, times(1)).setMaxEvaluations(100);
        assertEquals(100, multiStartOptimizer.getMaxEvaluations());
        assertEquals(0, multiStartOptimizer.getEvaluations());
    }

    @Test
    public void testGetOptima() throws Exception {
        final int starts = 5;
        final double min = 0.0;
        final double max = 10.0;
        final UnivariateRealPointValuePair expectedOptima = new UnivariateRealPointValuePair(1.0, 2.0);

        when(optimizer.optimize(isA(UnivariateRealFunction.class), isA(GoalType.class), min, max))
                .thenReturn(expectedOptima);
        when(randomGenerator.nextDouble()).thenReturn(0.5, 0.5, 0.3, 0.8, 0.1);

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, randomGenerator);

        final UnivariateRealPointValuePair[] actualOptima = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        assertEquals(starts, actualOptima.length);
        assertArrayEquals(new UnivariateRealPointValuePair[]{expectedOptima, null, null, null, null}, actualOptima);

        final UnivariateRealPointValuePair[] allOptima = multiStartOptimizer.getOptima();
        assertArrayEquals(new UnivariateRealPointValuePair[]{expectedOptima, null, null, null, null}, allOptima);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaBeforeOptimize() {
        multiStartOptimizer.getOptima();
    }

    @Test(expected = ConvergenceException.class)
    public void testNoOptimum() throws Exception {
        when(optimizer.optimize(isA(UnivariateRealFunction.class), isA(GoalType.class), min, max))
                .thenThrow(new ConvergenceException(LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, 0));

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, randomGenerator);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
    }

    @Test
    public void testOptimizeWithConvergenceChecker() throws Exception {
        final int starts = 5;
        final double min = 0.0;
        final double max = 10.0;
        final double expectedValue = 1.5;
        final double expectedX = 2.5;

        final ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(convergenceChecker.checkPoint(isA(UnivariateRealPointValuePair.class))).thenReturn(false);

        when(optimizer.optimize(isA(UnivariateRealFunction.class), isA(GoalType.class), min, max))
                .thenReturn(new UnivariateRealPointValuePair(expectedX, expectedValue));

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, randomGenerator);
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);

        final UnivariateRealPointValuePair actualOptimum = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        assertEquals(expectedX, actualOptimum.getPoint(), EPSILON);
        assertEquals(expectedValue, actualOptimum.getValue(), EPSILON);

        verify(convergenceChecker, times(starts)).checkPoint(isA(UnivariateRealPointValuePair.class));
    }

    @Test
    public void testOptimizeWithoutConvergenceChecker() throws Exception {
        final int starts = 5;
        final double min = 0.0;
        final double max = 10.0;
        final double expectedValue = 1.5;
        final double expectedX = 2.5;

        when(optimizer.optimize(isA(UnivariateRealFunction.class), isA(GoalType.class), min, max))
                .thenReturn(new UnivariateRealPointValuePair(expectedX, expectedValue));

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, randomGenerator);

        final UnivariateRealPointValuePair actualOptimum = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        assertEquals(expectedX, actualOptimum.getPoint(), EPSILON);
        assertEquals(expectedValue, actualOptimum.getValue(), EPSILON);

        verify(optimizer, times(starts)).optimize(isA(UnivariateRealFunction.class), isA(GoalType.class), min, max);
    }

    @Test
    public void testOptimizeWithMaxEvaluations() throws Exception {
        final int starts = 5;
        final int maxEvaluations = 10;
        final double min = 0.0;
        final double max = 10.0;
        final double expectedValue = 1.5;
        final double expectedX = 2.5;

        when(optimizer.optimize(isA(UnivariateRealFunction.class), isA(GoalType.class), min, max))
                .thenReturn(new UnivariateRealPointValuePair(expectedX, expectedValue));

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, randomGenerator);
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);

        final UnivariateRealPointValuePair actualOptimum = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        assertEquals(expectedX, actualOptimum.getPoint(), EPSILON);
        assertEquals(expectedValue, actualOptimum.getValue(), EPSILON);

        verify(optimizer, times(maxEvaluations)).optimize(isA(UnivariateRealFunction.class), isA(GoalType.class), min, max);
    }

}