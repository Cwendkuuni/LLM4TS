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
    public void testSolve_validFunction_returnsRoot() throws Exception {
        double expected = 3.0;

        // Define the test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
        };

        // Set the interval
        double min = 0.0;
        double max = 5.0;

        double result = solver.solve(f, min, max);

        assertEquals(expected, result, 1E-6);
    }

    @Test
    public void testSolve_validFunction_minimumAccuracy() throws Exception {
        double expected = 0.0;

        // Define the test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };

        // Set the interval
        double min = -1.0;
        double max = 1.0;

        double result = solver.solve(f, min, max);

        assertEquals(expected, result, 1E-100);
    }

    @Test(expected = RuntimeException.class)
    public void testSolve_invalidFunction_throwException() throws Exception {
        // Define the test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException("Function evaluation error");
            }
        };

        // Set the interval
        double min = -1.0;
        double max = 1.0;

        solver.solve(f, min, max); // Exception expected
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_maxIterationsExceeded_throwException() throws Exception {
        // Define the test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };

        // Set the interval
        double min = 0.0;
        double max = 5.0;

        // Set the maximum iteration count to a very small value
        solver.setMaximalIterationCount(1);

        solver.solve(f, min, max); // Exception expected
    }

}