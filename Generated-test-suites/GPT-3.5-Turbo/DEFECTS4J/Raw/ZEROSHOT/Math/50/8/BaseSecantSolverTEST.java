import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseSecantSolverTest {
    
    private BaseSecantSolver solver;
    private UnivariateRealFunction function;
    
    @Before
    public void setUp() {
        solver = new BaseSecantSolverTestImpl(0.000001, BaseSecantSolver.Method.REGULA_FALSI);
        function = mock(UnivariateRealFunction.class);
    }
    
    @Test
    public void testSolve_ExactSolution() {
        when(function.value(0.0)).thenReturn(0.0);
        
        double result = solver.solve(100, function, -1.0, 1.0, 0.5);
        
        assertEquals(0.0, result, 0.000001);
    }
    
    @Test
    public void testSolve_Approximation() {
        when(function.value(0.1)).thenReturn(0.05);
        when(function.value(-0.1)).thenReturn(-0.05);
        
        double result = solver.solve(100, function, -1.0, 1.0, 0.5);
        
        assertEquals(0.1, result, 0.000001);
    }
}

class BaseSecantSolverTestImpl extends BaseSecantSolver {
    
    public BaseSecantSolverTestImpl(double absoluteAccuracy, Method method) {
        super(absoluteAccuracy, method);
    }
    
    @Override
    public double computeObjectiveValue(double x) {
        return 0.0;
    }
    
    @Override
    protected void verifyBracketing(double x0, double x1) {
        // Do nothing
    }
}