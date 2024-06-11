import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testSolve() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(0, 10);
        assertEquals(5.0, result, 1E-6);
    }

    @Test
    public void testSolve_WithInitial() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(0, 10, 5);
        assertEquals(5.0, result, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_MaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(0, 10, 11);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_FunctionEvaluationException() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(new MockUnivariateRealFunction(), 0, 10);
    }

    private class MockUnivariateRealFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException(x);
        }
    }
}