import org.apache.commons.math.analysis.UnivariateRealFunction;
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
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        double result = solver.solve(f, 0, 1);

        assertEquals(0.8767262153932635, result, 1E-6);
    }

    @Test
    public void testSolveWithInvalidInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        try {
            solver.solve(f, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid interval: [1, 0]", e.getMessage());
        }
    }

    @Test
    public void testSolveWithMaxIterationsExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        try {
            solver.solve(f, -2, 2);
            fail("Expected MaxIterationsExceededException to be thrown");
        } catch (MaxIterationsExceededException e) {
            assertEquals("Maximum iterations (100) exceeded", e.getMessage());
        }
    }
}