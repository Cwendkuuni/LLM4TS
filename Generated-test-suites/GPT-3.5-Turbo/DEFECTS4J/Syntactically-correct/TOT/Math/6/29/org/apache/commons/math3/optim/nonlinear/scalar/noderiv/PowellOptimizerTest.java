import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizer() {
        // Typical use case
        double rel = 1e-10;
        double abs = 1e-10;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);

        // Test initialization
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0);

        // Test optimization
        double[] startPoint = {1, 2, 3};
        double[] expectedSolution = {1, 2, 3};
        double expectedValue = 0;
        double[] solution = optimizer.optimize(startPoint);
        double value = optimizer.computeObjectiveValue(solution);

        assertArrayEquals(expectedSolution, solution, 0);
        assertEquals(expectedValue, value, 0);
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testPowellOptimizerWithNegativeThreshold() {
        // Test error scenario with negative threshold
        double rel = 1e-10;
        double abs = -1e-10;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
    }

    @Test(expected=NumberIsTooSmallException.class)
    public void testPowellOptimizerWithSmallRelativeThreshold() {
        // Test error scenario with small relative threshold
        double rel = 1e-20;
        double abs = 1e-10;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
    }

    @Test
    public void testPowellOptimizerWithCustomConvergenceChecker() {
        // Typical use case with custom convergence checker
        double rel = 1e-10;
        double abs = 1e-10;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, new CustomConvergenceChecker());

        // Test initialization
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0);
        assertNotNull(optimizer.getConvergenceChecker());
        assertTrue(optimizer.getConvergenceChecker() instanceof CustomConvergenceChecker);
    }

    @Test
    public void testLineSearch() {
        // Typical use case
        double lineRel = 1e-10;
        double lineAbs = 1e-10;
        PowellOptimizer.LineSearch lineSearch = new PowellOptimizer.LineSearch(lineRel, lineAbs);

        // Test initialization
        assertNotNull(lineSearch);
        assertEquals(lineRel, lineSearch.getRelativeThreshold(), 0);
        assertEquals(lineAbs, lineSearch.getAbsoluteThreshold(), 0);

        // Test search
        double[] startPoint = {1, 2, 3};
        double[] direction = {1, 0, 0};
        double expectedOptimum = 1;
        double optimum = lineSearch.search(startPoint, direction).getPoint();

        assertEquals(expectedOptimum, optimum, 0);
    }

    @Test
    public void testNewPointAndDirection() {
        // Typical use case
        double[] point = {1, 2, 3};
        double[] direction = {1, 0, 0};
        double alpha = 1;
        double[] expectedPoint = {2, 2, 3};
        double[] expectedDirection = {1, 0, 0};

        double[][] result = PowellOptimizer.newPointAndDirection(point, direction, alpha);
        double[] newPoint = result[0];
        double[] newDirection = result[1];

        assertArrayEquals(expectedPoint, newPoint, 0);
        assertArrayEquals(expectedDirection, newDirection, 0);
    }

    // Custom convergence checker for testing purposes
    private class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            return true; // Always return converged for testing purposes
        }
    }
}