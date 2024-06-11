import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolveWithInitial() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 0.0, 1.0, 0.5);
        assertEquals(0.5, result, 1E-6);
    }

    @Test
    public void testSolveWithoutInitial() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 0.0, 1.0);
        assertEquals(0.5, result, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction mockFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        BisectionSolver solver = new BisectionSolver(mockFunction);
        solver.solve(-1.0, 1.0);
    }

    @Test
    public void testSolveIntervalTooSmall() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 1.0, 1.0);
        assertEquals(1.0, result, 1E-6);
    }
}