import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class MultiStartUnivariateRealOptimizerTest {
    
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    private ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker;
    private RandomGenerator generator;
    
    @Before
    public void setup() {
        optimizer = mock(BaseUnivariateRealOptimizer.class);
        convergenceChecker = mock(ConvergenceChecker.class);
        generator = mock(RandomGenerator.class);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, generator);
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
    }
    
    @Test
    public void testSetConvergenceChecker() {
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        verify(optimizer).setConvergenceChecker(convergenceChecker);
    }
    
    @Test
    public void testGetConvergenceChecker() {
        assertEquals(convergenceChecker, multiStartOptimizer.getConvergenceChecker());
        verify(optimizer).getConvergenceChecker();
    }
    
    @Test
    public void testGetMaxEvaluations() {
        multiStartOptimizer.getMaxEvaluations();
        verify(optimizer).getMaxEvaluations();
    }
    
    @Test
    public void testGetEvaluations() {
        multiStartOptimizer.getEvaluations();
        verify(optimizer).getEvaluations();
    }
    
    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        verify(optimizer).setMaxEvaluations(maxEvaluations);
    }
    
    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[5];
        when(optimizer.getOptima()).thenReturn(optima);
        
        assertArrayEquals(optima, multiStartOptimizer.getOptima());
        verify(optimizer).getOptima();
    }
    
    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaMathIllegalStateException() {
        multiStartOptimizer.getOptima();
    }
    
    @Test
    public void testOptimize() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        
        UnivariateRealPointValuePair expectedOptimum = new UnivariateRealPointValuePair(0.5, 0.0);
        when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), eq(min), eq(max), eq(startValue)))
                .thenReturn(expectedOptimum);
        
        UnivariateRealPointValuePair optimum = multiStartOptimizer.optimize(null, GoalType.MINIMIZE, min, max, startValue);
        
        assertEquals(expectedOptimum, optimum);
        verify(optimizer, times(5)).optimize(any(UnivariateRealFunction.class), any(GoalType.class), eq(min), eq(max), eq(startValue));
        verify(generator, times(4)).nextDouble();
    }
    
    @Test(expected = ConvergenceException.class)
    public void testOptimizeConvergenceException() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        
        when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), eq(min), eq(max), eq(startValue)))
                .thenThrow(ConvergenceException.class);
        
        multiStartOptimizer.optimize(null, GoalType.MINIMIZE, min, max, startValue);
        
        verify(optimizer, times(5)).optimize(any(UnivariateRealFunction.class), any(GoalType.class), eq(min), eq(max), eq(startValue));
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        
        when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), eq(min), eq(max), eq(startValue)))
                .thenThrow(FunctionEvaluationException.class);
        
        multiStartOptimizer.optimize(null, GoalType.MINIMIZE, min, max, startValue);
        
        verify(optimizer, times(5)).optimize(any(UnivariateRealFunction.class), any(GoalType.class), eq(min), eq(max), eq(startValue));
    }
    
    @Test
    public void testSortPairsMinimize() {
        UnivariateRealPointValuePair o1 = new UnivariateRealPointValuePair(1.0, 0.0);
        UnivariateRealPointValuePair o2 = new UnivariateRealPointValuePair(2.0, 0.0);
        UnivariateRealPointValuePair o3 = new UnivariateRealPointValuePair(3.0, 0.0);
        
        UnivariateRealPointValuePair[] optima = {o3, null, o1, o2, null};
        multiStartOptimizer.sortPairs(GoalType.MINIMIZE);
        
        UnivariateRealPointValuePair[] expectedOptima = {o1, o2, o3, null, null};
        assertArrayEquals(expectedOptima, optima);
    }
    
    @Test
    public void testSortPairsMaximize() {
        UnivariateRealPointValuePair o1 = new UnivariateRealPointValuePair(1.0, 0.0);
        UnivariateRealPointValuePair o2 = new UnivariateRealPointValuePair(2.0, 0.0);
        UnivariateRealPointValuePair o3 = new UnivariateRealPointValuePair(3.0, 0.0);
        
        UnivariateRealPointValuePair[] optima = {o3, null, o1, o2, null};
        multiStartOptimizer.sortPairs(GoalType.MAXIMIZE);
        
        UnivariateRealPointValuePair[] expectedOptima = {o3, o2, o1, null, null};
        assertArrayEquals(expectedOptima, optima);
    }
    
    @Test
    public void testOptimizeCallsConvergenceChecker() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        
        UnivariateRealPointValuePair expectedOptimum = new UnivariateRealPointValuePair(0.5, 0.0);
        when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), eq(min), eq(max), eq(startValue)))
                .thenReturn(expectedOptimum);
        
        multiStartOptimizer.optimize(null, GoalType.MINIMIZE, min, max, startValue);
        
        ArgumentCaptor<UnivariateRealPointValuePair> argument = ArgumentCaptor.forClass(UnivariateRealPointValuePair.class);
        verify(convergenceChecker, times(5)).converged(eq(0), argument.capture(), any(UnivariateRealPointValuePair[].class));
        
        assertEquals(expectedOptimum, argument.getValue());
    }
}