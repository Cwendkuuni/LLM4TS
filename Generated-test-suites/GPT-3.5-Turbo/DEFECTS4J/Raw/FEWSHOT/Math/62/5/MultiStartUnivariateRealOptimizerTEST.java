package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private BaseUnivariateRealOptimizer<FUNC> optimizer;
    private RandomGenerator generator;
    private MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer;
    
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        optimizer = (BaseUnivariateRealOptimizer<FUNC>) mock(BaseUnivariateRealOptimizer.class);
        generator = mock(RandomGenerator.class);
        
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 3, generator);
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
        int maxEvaluations = 5;
        
        when(optimizer.getMaxEvaluations()).thenReturn(maxEvaluations);
        
        int result = multiStartOptimizer.getMaxEvaluations();
        
        assertEquals(maxEvaluations, result);
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = 10;
        
        when(optimizer.getEvaluations()).thenReturn(evaluations);
        
        int result = multiStartOptimizer.getEvaluations();
        
        assertEquals(evaluations, result);
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 15;
        
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        
        verify(optimizer).setMaxEvaluations(maxEvaluations);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetOptima() {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        
        ConvergenceChecker<UnivariateRealPointValuePair> checker =
                mock(ConvergenceChecker.class);
        
        UnivariateRealPointValuePair[] optimaMock = new UnivariateRealPointValuePair[3];
        optimaMock[0] = mock(UnivariateRealPointValuePair.class);
        optimaMock[1] = mock(UnivariateRealPointValuePair.class);
        optimaMock[2] = mock(UnivariateRealPointValuePair.class);
        
        when(optimizer.optimize(f,  GoalType.MINIMIZE, 1, 2)).thenReturn(optimaMock[0]);
        when(optimizer.optimize(f,  GoalType.MINIMIZE, 3, 4)).thenReturn(optimaMock[1]);
        when(optimizer.optimize(f,  GoalType.MINIMIZE, 5, 6)).thenReturn(optimaMock[2]);
        
        multiStartOptimizer.setConvergenceChecker(checker);
        
        UnivariateRealPointValuePair[] result = multiStartOptimizer.getOptima();
        
        assertEquals(optimaMock, result);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaMathIllegalStateException() {
        multiStartOptimizer.getOptima();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOptimize() throws Exception {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;
        double startValue = 2;
        
        UnivariateRealPointValuePair optima = mock(UnivariateRealPointValuePair.class);
        
        when(optimizer.optimize(f, goal, min, max, startValue)).thenReturn(optima);
        
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max, startValue);
        
        assertEquals(optima, result);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = ConvergenceException.class)
    public void testOptimizeConvergenceException() throws Exception {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;
        double startValue = 2;
        
        UnivariateRealPointValuePair optimaMock = mock(UnivariateRealPointValuePair.class);
        
        when(optimizer.optimize(f, goal, min, max, startValue)).thenReturn(optimaMock);
        
        UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[3];
        optima[0] = optimaMock;
        optima[1] = null;
        optima[2] = null;
        
        when(optimizer.getEvaluations()).thenReturn(5);
        
        when(generator.nextDouble()).thenReturn(0.7);
        when(generator.nextDouble()).thenReturn(0.5);
        
        when(optimizer.getMaxEvaluations()).thenReturn(15);
        
        multiStartOptimizer.optimize(f, goal, min, max, startValue);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeMathIllegalStateException() throws Exception {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;
        double startValue = 2;
        
        multiStartOptimizer.optimize(f, goal, min, max, startValue);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;
        double startValue = 2;
        
        when(optimizer.optimize(f, goal, min, max, startValue))
                .thenThrow(FunctionEvaluationException.class);
        
        multiStartOptimizer.optimize(f, goal, min, max, startValue);
    }

}