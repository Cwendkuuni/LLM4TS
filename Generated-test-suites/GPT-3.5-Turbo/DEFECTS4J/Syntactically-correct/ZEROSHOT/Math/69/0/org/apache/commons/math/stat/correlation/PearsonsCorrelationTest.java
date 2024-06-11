import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    private final PearsonsCorrelation correlation;

    public PearsonsCorrelationTest() {
        correlation = new PearsonsCorrelation();
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0};
        double[] yArray = {2.0, 4.0, 6.0, 8.0};
        double expected = 1.0;
        double result = correlation.correlation(xArray, yArray);
        assertEquals(expected, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidDimensions() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0};
        double[] yArray = {2.0, 4.0, 6.0};
        correlation.correlation(xArray, yArray);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        double[][] expected = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        RealMatrix matrix = correlation.computeCorrelationMatrix(data);
        double[][] result = matrix.getData();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceData = {{4.0, 2.0}, {2.0, 9.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
        double[][] expected = {{1.0, 0.23570226039551587}, {0.23570226039551587, 1.0}};
        RealMatrix matrix = correlation.covarianceToCorrelation(covarianceMatrix);
        double[][] result = matrix.getData();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        double[][] expected = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix matrix = correlation.getCorrelationMatrix();
        double[][] result = matrix.getData();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        double[][] expected = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix matrix = correlation.getCorrelationStandardErrors();
        double[][] result = matrix.getData();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        double[][] expected = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix matrix = correlation.getCorrelationPValues();
        double[][] result = matrix.getData();
        assertArrayEquals(expected, result);
    }

}