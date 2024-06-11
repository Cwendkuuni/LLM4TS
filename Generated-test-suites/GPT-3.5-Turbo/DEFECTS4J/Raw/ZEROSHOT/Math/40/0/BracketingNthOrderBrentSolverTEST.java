import org.apache.commons.math.analysis.UnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testSolve() {

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        double result = solver.solve(100, f, -10, 10);
        assertEquals(2, result, EPSILON);

        result = solver.solve(100, f, 0, 10);
        assertEquals(2, result, EPSILON);

        result = solver.solve(100, f, -10, 0);
        assertEquals(-2, result, EPSILON);

        result = solver.solve(100, f, -2, 2);
        assertEquals(2, result, EPSILON);

        result = solver.solve(100, f, -10, -2);
        assertEquals(-2, result, EPSILON);

        result = solver.solve(100, f, 2, 10);
        assertEquals(2, result, EPSILON);
    }

    @Test
    public void testSolveWithStartValue() {

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        double result = solver.solve(100, f, -10, 10, 1);
        assertEquals(2, result, EPSILON);

        result = solver.solve(100, f, 0, 10, 1);
        assertEquals(2, result, EPSILON);

        result = solver.solve(100, f, -10, 0, -1);
        assertEquals(-2, result, EPSILON);

        result = solver.solve(100, f, -2, 2, 1);
        assertEquals(2, result, EPSILON);

        result = solver.solve(100, f, -10, -2, -1);
        assertEquals(-2, result, EPSILON);

        result = solver.solve(100, f, 2, 10, 3);
        assertEquals(2, result, EPSILON);
    }

}