package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private RandomGenerator generator;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    
    @Before
    public void setUp() {
        optimizer = mock(BaseUnivariateRealOptimizer.class);
        generator = mock(RandomGenerator.class);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
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
        ConvergenceChecker<UnivariateRealPointValuePair> result = multiStartOptimizer.getConvergenceChecker();
        assertEquals(checker, result);
    }
    
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 100;
        when(optimizer.getMaxEvaluations()).thenReturn(maxEvaluations);
        int result = multiStartOptimizer.getMaxEvaluations();
        assertEquals(maxEvaluations, result);
    }
    
    @Test
    public void testGetEvaluations() {
        int evaluations = 50;
        when(optimizer.getEvaluations()).thenReturn(evaluations);
        int result = multiStartOptimizer.getEvaluations();
        assertEquals(evaluations, result);
    }
    
    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        verify(optimizer).setMaxEvaluations(maxEvaluations);
    }
    
    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optima = {new UnivariateRealPointValuePair(1.0, 5.0), new UnivariateRealPointValuePair(2.0, 10.0), null};
        when(optimizer.getOptima()).thenReturn(optima);
        UnivariateRealPointValuePair[] result = multiStartOptimizer.getOptima();
        assertEquals(optima, result);
    }
    
    @Test(expected=MathIllegalStateException.class)
    public void testGetOptimaNoOptimumComputed() {
        multiStartOptimizer.getOptima();
    }
    
    @Test
    public void testOptimize() throws Exception {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 10.0;
        UnivariateRealPointValuePair expected = new UnivariateRealPointValuePair(1.0, 5.0);
        when(optimizer.optimize(f, goal, min, max)).thenReturn(expected);
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        assertEquals(expected, result);
    }
    
    @Test
    public void testOptimizeWithStartValue() throws Exception {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MAXIMIZE;
        double min = 1.0;
        double max = 10.0;
        double startValue = 5.0;
        UnivariateRealPointValuePair expected = new UnivariateRealPointValuePair(2.0, 10.0);
        when(optimizer.optimize(f, goal, min, max, startValue)).thenReturn(expected);
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max, startValue);
        assertEquals(expected, result);
    }
    
    @Test
    public void testOptimizeWithRandomGenerator() throws Exception {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 10.0;
        double bound1 = 2.0;
        double bound2 = 7.0;
        UnivariateRealPointValuePair expected = new UnivariateRealPointValuePair(1.0, 5.0);
        when(generator.nextDouble()).thenReturn(0.3, 0.7);
        when(optimizer.optimize(f, goal, bound1, bound2)).thenReturn(expected);
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        assertEquals(expected, result);
    }
    
    @Test
    public void testOptimizeWithNullResult() throws Exception {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 10.0;
        when(optimizer.optimize(f, goal, min, max)).thenThrow(new FunctionEvaluationException(null));
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        assertNull(result);
    }
    
    @Test(expected=ConvergenceException.class)
    public void testOptimizeNoConvergence() throws Exception {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 10.0;
        when(optimizer.optimize(f, goal, min, max)).thenThrow(new ConvergenceException(null));
        multiStartOptimizer.optimize(f, goal, min, max);
    }
    
    @Test
    public void testSortPairs() {
        UnivariateRealPointValuePair[] pairs = {new UnivariateRealPointValuePair(1.0, 5.0), new UnivariateRealPointValuePair(2.0, 10.0), null};
        GoalType goal = GoalType.MAXIMIZE;
        multiStartOptimizer.sortPairs(goal);
        UnivariateRealPointValuePair[] expected = {new UnivariateRealPointValuePair(2.0, 10.0), new UnivariateRealPointValuePair(1.0, 5.0), null};
        assertEquals(expected, pairs);
    }
}