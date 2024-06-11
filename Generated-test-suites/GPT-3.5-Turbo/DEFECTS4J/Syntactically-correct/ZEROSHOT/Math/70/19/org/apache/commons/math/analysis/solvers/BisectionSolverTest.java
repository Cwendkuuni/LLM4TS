import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {
    
    @Test
    public void testSolve() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(1, 2);
        assertEquals(1.5, result, 0.0001);
    }
    
    @Test
    public void testSolveWithFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(function, 1, 3);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.exp(x) - 1;
            }
        };
        solver.solve(function, 0, 1);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                if (x == 0) {
                    throw new IllegalArgumentException();
                }
                return 1 / x;
            }
        };
        solver.solve(function, 1, 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInterval() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(2, 1);
    }
}