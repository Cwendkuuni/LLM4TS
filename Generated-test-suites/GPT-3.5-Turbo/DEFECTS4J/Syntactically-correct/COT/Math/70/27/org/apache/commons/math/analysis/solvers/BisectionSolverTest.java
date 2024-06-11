import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private static final double EPSILON = 1E-6;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(f, 0, 5);
        assertEquals(2, result, EPSILON);
    }

    @Test
    public void testSolveEdgeCases() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(f, 0, 4);  // Minimum value of x
        assertEquals(2, result, EPSILON);

        result = solver.solve(f, 4, 5);  // Maximum value of x
        assertEquals(2, result, EPSILON);

        result = solver.solve(f, -5, 5);  // Negative and positive values of x
        assertEquals(2, result, EPSILON);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(f, -100, 100);  // Large interval to exceed max iterations
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new IllegalArgumentException("Invalid value of x");
            }
        };
        double result = solver.solve(f, -5, 5);  // Function evaluation throws exception
    }

}