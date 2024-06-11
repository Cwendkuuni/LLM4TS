import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {
    private PowellOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(1e-6, 1e-6);
    }

    @After
    public void tearDown() {
        optimizer = null;
    }

    @Test
    public void testDoOptimize() {
        // Test case 1
        double[] startPoint = {0, 0};
        optimizer.setStartPoint(startPoint);
        PointValuePair result = optimizer.doOptimize();
        double[] expectedPoint = {0, 0};
        assertEquals(expectedPoint[0], result.getPoint()[0], 1e-6);
        assertEquals(expectedPoint[1], result.getPoint()[1], 1e-6);

        // Test case 2
        startPoint = new double[] {-2, -2};
        optimizer.setStartPoint(startPoint);
        result = optimizer.doOptimize();
        expectedPoint = new double[] {-1, 1};
        assertEquals(expectedPoint[0], result.getPoint()[0], 1e-6);
        assertEquals(expectedPoint[1], result.getPoint()[1], 1e-6);
    }
}