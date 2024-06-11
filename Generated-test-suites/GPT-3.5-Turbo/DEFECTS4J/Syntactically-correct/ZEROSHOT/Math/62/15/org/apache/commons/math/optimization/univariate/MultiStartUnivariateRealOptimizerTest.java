import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiStartUnivariateRealOptimizerTest {

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private RandomGenerator generator;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;

    @Before
    public void setUp() {
        optimizer = mock(BaseUnivariateRealOptimizer.class);
        generator = mock(RandomGenerator.class);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, generator);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = mock(ConvergenceChecker.class);
        multiStartOptimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = mock(ConvergenceChecker.class);
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;

        UnivariateRealPointValuePair pointValuePair1 = new UnivariateRealPointValuePair(0.5, 0.25);
        UnivariateRealPointValuePair pointValuePair2 = new UnivariateRealPointValuePair(0.3, 0.15);
        UnivariateRealPointValuePair pointValuePair3 = new UnivariateRealPointValuePair(0.2, 0.1);
        UnivariateRealPointValuePair[] optima = {pointValuePair1, pointValuePair2, pointValuePair3};

        when(generator.nextDouble()).thenReturn(0.5, 0.3, 0.2);
        when(optimizer.optimize(f, goal, min, max)).thenReturn(pointValuePair1, pointValuePair2, pointValuePair3);

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);

        assertNotNull(result);
        assertEquals(pointValuePair1, result);
        assertEquals(3, multiStartOptimizer.getOptima().length);
        assertArrayEquals(optima, multiStartOptimizer.getOptima());
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeThrowsException() throws FunctionEvaluationException {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;

        when(generator.nextDouble()).thenReturn(0.5, 0.3, 0.2);
        when(optimizer.optimize(f, goal, min, max)).thenThrow(new ConvergenceException());

        multiStartOptimizer.optimize(f, goal, min, max);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaThrowsException() {
        multiStartOptimizer.getOptima();
    }

    private void assertArrayEquals(UnivariateRealPointValuePair[] expected, UnivariateRealPointValuePair[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}