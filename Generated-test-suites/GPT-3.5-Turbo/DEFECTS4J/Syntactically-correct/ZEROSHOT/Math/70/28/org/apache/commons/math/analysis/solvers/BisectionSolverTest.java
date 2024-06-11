import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver solver;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }

    @Test
    public void testSolve() throws Exception {
        double min = -10.0;
        double max = 10.0;
        double sol = solver.solve(min, max);
        assertEquals(0.0, sol, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceededException() throws Exception {
        double min = -10.0;
        double max = 10.0;
        solver.setMaximalIterationCount(0);
        solver.solve(min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws Exception {
        double min = -10.0;
        double max = 10.0;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        solver.solve(f, min, max);
    }

    @Test
    public void testSolveUnivariateRealFunction() throws Exception {
        double min = -10.0;
        double max = 10.0;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 1.0;
            }
        };
        double sol = solver.solve(f, min, max);
        assertEquals(1.0, sol, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveUnivariateRealFunctionMaxIterationsExceededException() throws Exception {
        double min = -10.0;
        double max = 10.0;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 1.0;
            }
        };
        solver.setMaximalIterationCount(0);
        solver.solve(f, min, max);
    }

    private void verifyInterval(double min, double max) {
        assertTrue(min < max);
    }

    private void clearResult() {
        solver.clearResult();
        assertNull(solver.getResult());
        assertEquals(0, solver.getIterationCount());
    }

    private void setResult(double result, int iterationCount) {
        solver.setResult(result, iterationCount);
        assertNotNull(solver.getResult());
        assertEquals(result, solver.getResult(), 0.0);
        assertEquals(iterationCount, solver.getIterationCount());
    }

    private double midpoint(double min, double max) {
        return (min + max) / 2;
    }

}