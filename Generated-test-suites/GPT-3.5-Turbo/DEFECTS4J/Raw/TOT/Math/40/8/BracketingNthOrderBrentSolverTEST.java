import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() {
        // Create solver object
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        // Test min value
        double minValue = solver.solve(100, x -> x - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, minValue, 0);

        // Test max value
        double maxValue = solver.solve(100, x -> x - 1, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, maxValue, 0);

        // Test function with positive root
        double positiveRoot = solver.solve(100, x -> x * x - 4, 0, Double.POSITIVE_INFINITY);
        assertEquals(2.0, positiveRoot, 0);

        // Test function with negative root
        double negativeRoot = solver.solve(100, x -> x * x - 4, Double.NEGATIVE_INFINITY, 0);
        assertEquals(-2.0, negativeRoot, 0);

        // Test function with multiple roots
        double multipleRoots = solver.solve(100, x -> x * x - 4, -10, 10);
        assertEquals(2.0, multipleRoots, 0);
    }

    @Test
    public void testSolveWithStartValue() {
        // Create solver object
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        // Test min value
        double minValue = solver.solve(100, x -> x - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN);
        assertEquals(Double.NEGATIVE_INFINITY, minValue, 0);

        // Test max value
        double maxValue = solver.solve(100, x -> x - 1, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN);
        assertEquals(Double.POSITIVE_INFINITY, maxValue, 0);

        // Test function with positive root
        double positiveRoot = solver.solve(100, x -> x * x - 4, 0, Double.POSITIVE_INFINITY, Double.NaN);
        assertEquals(2.0, positiveRoot, 0);

        // Test function with negative root
        double negativeRoot = solver.solve(100, x -> x * x - 4, Double.NEGATIVE_INFINITY, 0, Double.NaN);
        assertEquals(-2.0, negativeRoot, 0);

        // Test function with multiple roots
        double multipleRoots = solver.solve(100, x -> x * x - 4, -10, 10, Double.NaN);
        assertEquals(2.0, multipleRoots, 0);
    }

}