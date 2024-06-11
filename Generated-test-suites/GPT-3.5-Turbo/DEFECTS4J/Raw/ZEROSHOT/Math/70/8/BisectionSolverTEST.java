import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class BisectionSolverTest {
    
    private BisectionSolver solver;
    private UnivariateRealFunction function;
    
    @Before
    public void setUp() {
        solver = new BisectionSolver();
        function = Mockito.mock(UnivariateRealFunction.class);
    }
    
    @Test
    public void testSolve() throws FunctionEvaluationException, MaxIterationsExceededException {
        double min = 0.0;
        double max = 1.0;
        
        Mockito.when(function.value(min)).thenReturn(-1.0);
        Mockito.when(function.value(max)).thenReturn(1.0);
        
        double result = solver.solve(function, min, max);
        
        assertEquals(0.5, result, 1e-6);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_MaxIterationsExceededException() throws FunctionEvaluationException, MaxIterationsExceededException {
        double min = 0.0;
        double max = 1.0;
        
        Mockito.when(function.value(min)).thenReturn(-1.0);
        Mockito.when(function.value(max)).thenReturn(1.0);
        
        solver.setAbsoluteAccuracy(1e-10);
        solver.setMaximalIterationCount(10);
        
        solver.solve(function, min, max);
    }
    
    @Test
    public void testSolve_WithInitialGuess() throws FunctionEvaluationException, MaxIterationsExceededException {
        double min = 0.0;
        double max = 1.0;
        double initial = 0.1;
        
        Mockito.when(function.value(min)).thenReturn(-1.0);
        Mockito.when(function.value(initial)).thenReturn(-0.5);
        
        double result = solver.solve(function, min, max, initial);
        
        assertEquals(0.5, result, 1e-6);
    }
    
    @Test
    public void testSolve_WithSmallerAbsoluteAccuracy() throws FunctionEvaluationException, MaxIterationsExceededException {
        double min = 0.0;
        double max = 1.0;
        
        Mockito.when(function.value(min)).thenReturn(-1.0);
        Mockito.when(function.value(max)).thenReturn(1.0);
        
        solver.setAbsoluteAccuracy(1e-10);
        
        double result = solver.solve(function, min, max);
        
        assertEquals(0.5, result, 1e-10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolve_InvalidInterval() throws FunctionEvaluationException, MaxIterationsExceededException {
        double min = 1.0;
        double max = 0.0;
        
        solver.solve(function, min, max);
    }
}