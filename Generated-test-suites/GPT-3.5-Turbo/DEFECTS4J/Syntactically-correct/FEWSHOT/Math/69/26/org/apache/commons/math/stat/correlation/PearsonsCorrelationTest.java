import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PearsonsCorrelationTest {
    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0}
        };
        double[][] expected = {
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        double[][] result = correlationMatrix.getData();
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[0].length; j++) {
                assertEquals(expected[i][j], result[i][j], 0.0001);
            }
        }
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 4.0, 6.0};
        double expected = 1.0;
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double result = correlation.correlation(xArray, yArray);
        assertEquals(expected, result, 0.0001);

        double[] invalidXArray = {1.0};
        double[] invalidYArray = {2.0};
        try {
            correlation.correlation(invalidXArray, invalidYArray);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}