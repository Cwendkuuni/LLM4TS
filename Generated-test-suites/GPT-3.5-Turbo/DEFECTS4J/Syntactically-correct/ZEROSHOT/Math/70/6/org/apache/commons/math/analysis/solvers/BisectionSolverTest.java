import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunction f;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        double expected = 2.0;
        double result = solver.solve(f, 0, 3);
        assertEquals(expected, result, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        solver.solve(f, -10, 10);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction invalidFunc = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        solver.solve(invalidFunc, -10, 10);
    }

    @Test
    public void testSolveInfiniteLoop() throws MaxIterationsExceededException, FunctionEvaluationException {
        double expected = 15.0;
        double result = solver.solve(f, 5, 100);
        assertEquals(expected, result, 1E-6);
    }

    @Test
    public void testSolveNegativeRoot() throws MaxIterationsExceededException, FunctionEvaluationException {
        double expected = -2.0;
        double result = solver.solve(f, -3, 0);
        assertEquals(expected, result, 1E-6);
    }

    @Test
    public void testSolveSmallInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        double expected = 0.0;
        double result = solver.solve(f, -0.0001, 0.0001);
        assertEquals(expected, result, 1E-6);
    }

    @Test
    public void testSolveLargeInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        double expected = 100.0;
        double result = solver.solve(f, 10, 1000);
        assertEquals(expected, result, 1E-6);
    }

}