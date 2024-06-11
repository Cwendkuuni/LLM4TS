import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix result = correlation.computeCorrelationMatrix(matrix);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testComputeCorrelationMatrix_DataArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix result = correlation.computeCorrelationMatrix(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 4.0, 6.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double result = correlation.correlation(xArray, yArray);
        double expected = 1.0;
        assertEquals(expected, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelation_InvalidArrayLength() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 4.0};
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
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix result = correlation.covarianceToCorrelation(covarianceMatrix);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix result = correlation.getCorrelationMatrix();
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix result = correlation.getCorrelationStandardErrors();
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix result = correlation.getCorrelationPValues();
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
        assertArrayEquals(expected.getData(), result.getData());
    }
}