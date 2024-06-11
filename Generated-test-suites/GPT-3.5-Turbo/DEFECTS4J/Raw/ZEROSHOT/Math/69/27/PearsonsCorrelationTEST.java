import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        double[][] correlationMatrix = correlation.getCorrelationMatrix().getData();
        assertEquals(1.0, correlationMatrix[0][0], 0.0001);
        assertEquals(-1.0, correlationMatrix[0][1], 0.0001);
        assertEquals(-1.0, correlationMatrix[0][2], 0.0001);
        assertEquals(-1.0, correlationMatrix[1][0], 0.0001);
        assertEquals(1.0, correlationMatrix[1][1], 0.0001);
        assertEquals(1.0, correlationMatrix[1][2], 0.0001);
        assertEquals(-1.0, correlationMatrix[2][0], 0.0001);
        assertEquals(1.0, correlationMatrix[2][1], 0.0001);
        assertEquals(1.0, correlationMatrix[2][2], 0.0001);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double result = correlation.correlation(xArray, yArray);
        assertEquals(1.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidLength() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {1};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrixData = {{2, -1}, {-1, 2}};
        double[][] expectedCorrelationMatrixData = {{1.0, -0.5}, {-0.5, 1.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixData);
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = correlation.covarianceToCorrelation(covarianceMatrix);
        double[][] correlationMatrixData = correlationMatrix.getData();
        assertEquals(expectedCorrelationMatrixData[0][0], correlationMatrixData[0][0], 0.0001);
        assertEquals(expectedCorrelationMatrixData[0][1], correlationMatrixData[0][1], 0.0001);
        assertEquals(expectedCorrelationMatrixData[1][0], correlationMatrixData[1][0], 0.0001);
        assertEquals(expectedCorrelationMatrixData[1][1], correlationMatrixData[1][1], 0.0001);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        double[][] expectedCorrelationMatrixData = {{1.0, -1.0, -1.0}, {-1.0, 1.0, 1.0}, {-1.0, 1.0, 1.0}};
        double[][] actualCorrelationMatrixData = correlationMatrix.getData();
        assertArrayEquals(expectedCorrelationMatrixData, actualCorrelationMatrixData);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = correlation.getCorrelationStandardErrors();
        double[][] expectedStandardErrorsData = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};
        double[][] actualStandardErrorsData = correlationStandardErrors.getData();
        assertArrayEquals(expectedStandardErrorsData, actualStandardErrorsData);
    }
    
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = correlation.getCorrelationPValues();
        double[][] expectedPValuesData = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};
        double[][] actualPValuesData = correlationPValues.getData();
        assertArrayEquals(expectedPValuesData, actualPValuesData);
    }

    @Test
    public void testGetCorrelationMatrixEmpty() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertNull(correlationMatrix);
    }

    @Test
    public void testGetCorrelationStandardErrorsEmpty() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationStandardErrors = correlation.getCorrelationStandardErrors();
        assertNull(correlationStandardErrors);
    }

    @Test(expected = MathRuntimeException.class)
    public void testInvalidData() {
        double[] xArray = {1};
        double[] yArray = {1};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    @Test
    public void testPearsonsCorrelationEmpty() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNull(correlation.getCorrelationMatrix());
    }
}