import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {
    
    @Test
    public void testSolve() {
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 4.0;
        double expected = 2.0;
        double result = solver.solve(min, max);
        assertEquals(expected, result, 1E-6);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        double min = -1.0;
        double max = 1.0;
        solver.solve(min, max);
    }
    
    @Test
    public void testSolveWithFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunctionImpl f = new UnivariateRealFunctionImpl();
        double min = 0.0;
        double max = 4.0;
        double expected = 2.0;
        double result = solver.solve(f, min, max);
        assertEquals(expected, result, 1E-6);
    }
    
    private class UnivariateRealFunctionImpl implements UnivariateRealFunction {
        public double value(double x) {
            return x * x - 4;
        }
    }
}