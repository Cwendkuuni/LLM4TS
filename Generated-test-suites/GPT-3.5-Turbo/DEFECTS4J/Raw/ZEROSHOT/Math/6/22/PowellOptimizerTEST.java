import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PowellOptimizerTest {

    private PowellOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(1e-8, 1e-8);
    }

    @Test
    public void testDoOptimize() {
        double[] startPoint = {1, 2, 3};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);

        PointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    @Test
    public void testNewPointAndDirection() {
        double[] p = {1, 2, 3};
        double[] d = {-1, -1, -1};
        double optimum = 2;

        double[][] result = optimizer.newPointAndDirection(p, d, optimum);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertEquals(3, result[1].length);
    }

    @Test
    public void testLineSearch() {
        double[] p = {1, 2, 3};
        double[] d = {-1, -1, -1};

        double alpha = optimizer.line.search(p, d);

        assertNotNull(alpha);
    }
}