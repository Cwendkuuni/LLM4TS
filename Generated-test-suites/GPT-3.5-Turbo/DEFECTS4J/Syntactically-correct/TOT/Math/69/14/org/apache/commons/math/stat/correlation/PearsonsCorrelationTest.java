import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PearsonsCorrelationTest {

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        double[][] expectedCorrelationMatrix = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        RealMatrix actualCorrelationMatrix = pc.getCorrelationMatrix();
        assertTrue(actualCorrelationMatrix instanceof RealMatrix);
        assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        double[][] expectedCorrelationStandardErrors = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        RealMatrix actualCorrelationStandardErrors = pc.getCorrelationStandardErrors();
        assertTrue(actualCorrelationStandardErrors instanceof RealMatrix);
        assertEquals(expectedCorrelationStandardErrors, actualCorrelationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        double[][] expectedCorrelationPValues = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        RealMatrix actualCorrelationPValues = pc.getCorrelationPValues();
        assertTrue(actualCorrelationPValues instanceof RealMatrix);
        assertEquals(expectedCorrelationPValues, actualCorrelationPValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithDifferentLengthArrays() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {1, 2};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {2};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.correlation(xArray, yArray);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double actualCorrelation = pc.correlation(xArray, yArray);
        double expectedCorrelation = 1.0;
        assertEquals(expectedCorrelation, actualCorrelation, 0.001);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix actualCorrelationMatrix = pc.computeCorrelationMatrix(data);
        double[][] expectedCorrelationMatrix = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        assertTrue(actualCorrelationMatrix instanceof RealMatrix);
        assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeCorrelationMatrixWithInsufficientData() {
        double[][] data = {{1}, {2}};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.computeCorrelationMatrix(data);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrix = {{4, 2}, {2, 1}};
        int numberOfObservations = 3;
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix actualCorrelationMatrix = pc.covarianceToCorrelation(covarianceMatrix, numberOfObservations);
        double[][] expectedCorrelationMatrix = {{1, 1}, {1, 1}};
        assertTrue(actualCorrelationMatrix instanceof RealMatrix);
        assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCovarianceToCorrelationWithNullCovarianceMatrix() {
        RealMatrix covarianceMatrix = null;
        int numberOfObservations = 3;
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.covarianceToCorrelation(covarianceMatrix, numberOfObservations);
    }

    @Test
    public void testCheckSufficientData() {
        double[][] matrix = {{1, 2}, {3, 4}};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.checkSufficientData(new BlockRealMatrix(matrix));
    }

    @Test(expected = MathRuntimeException.class)
    public void testCheckSufficientDataWithInsufficientRows() {
        double[][] matrix = {{1, 2}};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.checkSufficientData(new BlockRealMatrix(matrix));
    }

    @Test(expected = MathRuntimeException.class)
    public void testCheckSufficientDataWithInsufficientColumns() {
        double[][] matrix = {{1}, {2}};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.checkSufficientData(new BlockRealMatrix(matrix));
    }

}