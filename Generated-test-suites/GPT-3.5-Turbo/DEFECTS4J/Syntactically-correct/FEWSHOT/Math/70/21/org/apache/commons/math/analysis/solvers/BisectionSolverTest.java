import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {
    private BisectionSolver solver;
    private TestFunction testFunction;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        testFunction = new TestFunction();
        double result = solver.solve(testFunction, 0, 1);
        assertEquals(0.5, result, 1e-6);
    }

    private class TestFunction implements UnivariateRealFunction {
        public double value(double x) throws FunctionEvaluationException {
            // Define your own test function here
            return x * x - 4;
        }
    }
}