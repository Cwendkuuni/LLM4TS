import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver solver;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }

    @After
    public void tearDown() {
        solver = null;
    }

    @Test
    public void testSolve_withValidInputs_shouldReturnCorrectResult() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4; // x^2 - 4
            }
        };
        double result = solver.solve(f, -10, 10);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_withUnsolvableFunction_shouldThrowMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                if (x == 5) {
                    throw new FunctionEvaluationException("Function cannot be evaluated at x=5");
                }
                return Math.pow(x, 2) - 4; // x^2 - 4
            }
        };
        double result = solver.solve(f, -10, 10);
        // Assert that the method throws MaxIterationsExceededException
    }

    @Test
    public void testSolve_withNonContinuousFunction_shouldReturnCorrectResult() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                if (x >= 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };
        double result = solver.solve(f, -10, 10);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testSolve_withEmptyInterval_shouldReturnCorrectResult() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4; // x^2 - 4
            }
        };
        double result = solver.solve(f, 0, 0);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testSolve_withEqualEndpoints_shouldReturnCorrectResult() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4; // x^2 - 4
            }
        };
        double result = solver.solve(f, 2.5, 2.5);
        assertEquals(2.5, result, 0.0001);
    }

}