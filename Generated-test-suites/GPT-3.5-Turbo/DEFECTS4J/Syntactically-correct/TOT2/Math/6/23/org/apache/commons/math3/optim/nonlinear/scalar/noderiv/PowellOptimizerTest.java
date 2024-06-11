import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PowellOptimizerTest {

    private PowellOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(0.001, 0.001);
    }

    @Test
    public void testDoOptimize() {
        // Typical use case
        double[] startPoint = {1.0, 2.0};
        optimizer.setStartPoint(startPoint);
        PointValuePair result = optimizer.optimize();
        assertEquals(0.0, result.getValue(), 1e-10);

        // Edge case: Empty start point
        double[] emptyStartPoint = {};
        optimizer.setStartPoint(emptyStartPoint);
        PointValuePair resultEmpty = optimizer.optimize();
        assertNull(resultEmpty);

        // Error scenario: Null convergence checker
        optimizer = new PowellOptimizer(0.001, 0.001, null);
        optimizer.setStartPoint(startPoint);
        PointValuePair resultNullChecker = optimizer.optimize();
        assertNotNull(resultNullChecker);
    }

    @Test
    public void testNewPointAndDirection() {
        // Typical use case
        double[] p = {1.0, 2.0};
        double[] d = {0.5, 0.5};
        double optimum = 0.1;
        double[][] result = optimizer.newPointAndDirection(p, d, optimum);
        double[] newPoint = result[0];
        double[] newDirection = result[1];
        
        assertEquals(1.05, newPoint[0], 1e-10);
        assertEquals(2.05, newPoint[1], 1e-10);
        assertEquals(0.05, newDirection[0], 1e-10);
        assertEquals(0.05, newDirection[1], 1e-10);
    }

    @Test
    public void testLineSearch() {
        // Typical use case
        double[] startPoint = {1.0, 2.0};
        double[] direction = {0.5, 0.5};
        LineSearch lineSearch = optimizer.new LineSearch(0.001, 0.001);
        UnivariatePointValuePair result = lineSearch.search(startPoint, direction);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 1e-10);
        assertEquals(0.0, result.getValue(), 1e-10);

        // Edge case: Zero direction
        double[] zeroDirection = {0.0, 0.0};
        UnivariatePointValuePair resultZeroDir = lineSearch.search(startPoint, zeroDirection);
        assertNotNull(resultZeroDir);
        assertEquals(0.0, resultZeroDir.getPoint(), 1e-10);
        assertEquals(0.0, resultZeroDir.getValue(), 1e-10);
    }
}