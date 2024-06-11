import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {

        double rel = 1e-10;
        double abs = 1e-10;

        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);

        double[] startPoint = {0.0, 0.0};
        optimizer.setStartPoint(startPoint);

        double[] objectiveFunctionCoefficients = {3, 4};
        optimizer.setObjectiveFunctionCoefficients(objectiveFunctionCoefficients);

        PointValuePair result = optimizer.doOptimize();

        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 0.0;

        assertArrayEquals(expectedPoint, result.getPoint(), 1e-5);
        assertEquals(expectedValue, result.getValue(), 1e-5);
    }

}