import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunctionMock function;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        function = new UnivariateRealFunctionMock();
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(function, 0, 10);
        assertEquals(5.0, result, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_MaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        solver.solve(function, 0, 10, 0);
    }

    @Test
    public void testSolve_WithInitialPoint() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(function, 0, 10, 5);
        assertEquals(5.0, result, 0.0001);
    }

    private class UnivariateRealFunctionMock implements UnivariateRealFunction {

        public double value(double x) throws FunctionEvaluationException {
            // Provide your own implementation here
        }
    }
}