import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {
    
    @Test
    public void testSolveWithFunction() {
        // Test solving with a function
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new TestFunction();
        double result = solver.solve(f, 0.0, 2.0);
        assertEquals(1.0, result, 1e-6);
    }
    
    @Test
    public void testSolveWithMinAndMax() {
        // Test solving with min and max values
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(0.0, 2.0);
        assertEquals(1.0, result, 1e-6);
    }
    
    @Test
    public void testSolveWithMinAndMaxAndInitial() {
        // Test solving with min, max, and initial values
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(0.0, 2.0, 1.5);
        assertEquals(1.0, result, 1e-6);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        // Test max iterations exceeded exception
        BisectionSolver solver = new BisectionSolver();
        solver.solve(0.0, 2.0);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        // Test function evaluation exception
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new TestFunctionWithException();
        solver.solve(f, 0.0, 2.0);
    }
    
    @Test
    public void testEdgeCases() {
        // Test edge cases
        BisectionSolver solver = new BisectionSolver();
        double result1 = solver.solve(0.0, 0.5);
        double result2 = solver.solve(0.0, 1.0);
        assertEquals(0.25, result1, 1e-6);
        assertEquals(0.5, result2, 1e-6);
    }
    
    private class TestFunction implements UnivariateRealFunction {
        
        @Override
        public double value(double x) {
            return x - 1.0;
        }
    }
    
    private class TestFunctionWithException implements UnivariateRealFunction {
        
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException(x);
        }
    }

}