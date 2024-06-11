import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        double[][] expected = {{1.0, 1.0}, {1.0, 1.0}};
        Assert.assertArrayEquals(expected, correlation.getCorrelationMatrix().getData(), 0.0001);
    }

    @Test
    public void testComputeCorrelationMatrix_EmptyMatrix() {
        double[][] data = {{}, {}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        double[][] expected = {{1.0, 1.0}, {1.0, 1.0}};
        Assert.assertArrayEquals(expected, correlation.getCorrelationMatrix().getData(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeCorrelationMatrix_InsufficientData() {
        double[][] data = {{1.0}, {2.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
    }

    @Test
    public void testComputeCorrelationMatrix_SingleValueMatrix() {
        double[][] data = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        double[][] expected = {{1.0, 1.0}, {1.0, 1.0}};
        Assert.assertArrayEquals(expected, correlation.getCorrelationMatrix().getData(), 0.0001);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        double[][] expected = {{0.0, 0.0}, {0.0, 0.0}};
        Assert.assertArrayEquals(expected, correlation.getCorrelationStandardErrors().getData(), 0.0001);
    }

    @Test
    public void testGetCorrelationStandardErrors_EmptyMatrix() {
        double[][] data = {{}, {}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        double[][] expected = {{0.0, 0.0}, {0.0, 0.0}};
        Assert.assertArrayEquals(expected, correlation.getCorrelationStandardErrors().getData(), 0.0001);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        double[][] expected = {{0.0, 0.0}, {0.0, 0.0}};
        Assert.assertArrayEquals(expected, correlation.getCorrelationPValues().getData(), 0.0001);
    }

    @Test
    public void testGetCorrelationPValues_EmptyMatrix() throws MathException {
        double[][] data = {{}, {}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        double[][] expected = {{0.0, 0.0}, {0.0, 0.0}};
        Assert.assertArrayEquals(expected, correlation.getCorrelationPValues().getData(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelation_DifferentArrayLengths() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelation_InsufficientData() {
        double[] xArray = {1.0};
        double[] yArray = {2.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    @Test
    public void testCorrelation_CorrectResult() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double expected = 1.0;
        Assert.assertEquals(expected, correlation.correlation(xArray, yArray), 0.0001);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceData = {{1.0, 2.0}, {2.0, 4.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] expected = {{1.0, 1.0}, {1.0, 1.0}};
        Assert.assertArrayEquals(expected, correlation.covarianceToCorrelation(new BlockRealMatrix(covarianceData)).getData(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientData_InsufficientRows() {
        double[][] data = {{1.0}, {2.0}, {3.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.checkSufficientData(new BlockRealMatrix(data));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientData_InsufficientColumns() {
        double[][] data = {{1.0, 2.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.checkSufficientData(new BlockRealMatrix(data));
    }
}