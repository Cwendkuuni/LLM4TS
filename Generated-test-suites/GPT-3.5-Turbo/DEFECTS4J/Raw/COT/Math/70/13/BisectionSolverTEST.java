import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunction f;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        double min = 1.0;
        double max = 3.0;
        double result = solver.solve(f, min, max);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveEdgeCases() throws MaxIterationsExceededException, FunctionEvaluationException {
        double min = 0.0;
        double max = 4.0;
        double result = solver.solve(f, min, max);
        assertEquals(2.0, result, 0.0001);

        min = -4.0;
        max = 0.0;
        result = solver.solve(f, min, max);
        assertEquals(-2.0, result, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        double min = -1.0;
        double max = 1.0;
        solver.solve(f, min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction invalidF = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        double min = -1.0;
        double max = 1.0;
        solver.solve(invalidF, min, max);
    }
}