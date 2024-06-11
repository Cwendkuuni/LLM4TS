import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BisectionSolverTest {
    
    @Test
    public void testSolve() throws FunctionEvaluationException, MaxIterationsExceededException {
        // Create a BisectionSolver object
        BisectionSolver solver = new BisectionSolver();
        
        // Test case 1: testing solve method
        UnivariateRealFunction function1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 2;
            }
        };
        double result1 = solver.solve(function1, 1, 3);
        assertEquals(2.0, result1, 0.0001);
        
        // Test case 2: testing solve method with initial guess
        UnivariateRealFunction function2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - 4;
            }
        };
        double result2 = solver.solve(function2, 0, 3, 2);
        assertEquals(2.0, result2, 0.0001);
        
        // Test case 3: testing solve method with max iterations exceeded
        UnivariateRealFunction function3 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        try {
            solver.solve(function3, 0, Math.PI);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Exception expected
        }
    }
}