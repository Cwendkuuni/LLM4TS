import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

public class BisectionSolverTest {
    
    private BisectionSolver solver;
    
    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }
    
    @Test
    public void testSolve() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double result = solver.solve(f, 0, 3);
        assertEquals(2.0, result, 1E-6);
        
        result = solver.solve(f, -3, 0);
        assertEquals(-2.0, result, 1E-6);
        
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        
        result = solver.solve(f, 1, 4);
        assertEquals(Math.PI, result, 1E-6);
        
        result = solver.solve(f, 4, 8);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, 4, 8));
    }
    
    @Test
    public void testSolveWithInvalidInterval() throws FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        
        assertThrows(IllegalArgumentException.class, () -> solver.solve(f, 3, 1));
    }
}