import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver solver;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 3;
            }
        };
        double result = solver.solve(f, 1, 2);
        assertEquals(Math.sqrt(3), result, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        solver.solve(f, 1, 2);
    }
}