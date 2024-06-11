import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    private PowellOptimizer optimizer;
    private double[] startPoint;

    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(0.01, 0.01);
        startPoint = new double[]{1, 1, 1};
    }

    @Test
    public void testDoOptimize() {
        PointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertNotNull(result.getValue());
    }

    @Test
    public void testNewPointAndDirection() {
        double[] p = {1, 2, 3};
        double[] d = {0.1, 0.2, 0.3};
        double optimum = 0.5;

        double[][] result = optimizer.newPointAndDirection(p, d, optimum);

        assertNotNull(result);
        assertNotNull(result[0]);
        assertNotNull(result[1]);
        assertEquals(p.length, result[0].length);
        assertEquals(p.length, result[1].length);
    }
}