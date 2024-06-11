import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructorWithoutData() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix matrix = correlation.getCorrelationMatrix();
        assertNull(matrix);
        int nObs = correlation.getNObs();
        assertEquals(0, nObs);
    }

    @Test
    public void testConstructorWithArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix matrix = correlation.getCorrelationMatrix();
        assertNotNull(matrix);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        int nObs = correlation.getNObs();
        assertEquals(3, nObs);
    }

    @Test
    public void testConstructorWithMatrix() {
        RealMatrix matrix = new BlockRealMatrix(3, 3);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix resultMatrix = correlation.getCorrelationMatrix();
        assertNotNull(resultMatrix);
        assertEquals(3, resultMatrix.getRowDimension());
        assertEquals(3, resultMatrix.getColumnDimension());
        int nObs = correlation.getNObs();
        assertEquals(3, nObs);
    }

    @Test
    public void testConstructorWithCovariance() {
        Covariance covariance = new Covariance();
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
        RealMatrix matrix = correlation.getCorrelationMatrix();
        assertNotNull(matrix);
        assertEquals(0, matrix.getRowDimension());
        assertEquals(0, matrix.getColumnDimension());
        int nObs = correlation.getNObs();
        assertEquals(0, nObs);
    }

    @Test
    public void testConstructorWithCovarianceMatrix() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(3, 3);
        int numberOfObservations = 10;
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, numberOfObservations);
        RealMatrix matrix = correlation.getCorrelationMatrix();
        assertNotNull(matrix);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        int nObs = correlation.getNObs();
        assertEquals(10, nObs);
    }

    @Test
    public void testComputeCorrelationMatrixWithMatrix() {
        RealMatrix matrix = new BlockRealMatrix(3, 3);
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix resultMatrix = correlation.computeCorrelationMatrix(matrix);
        assertNotNull(resultMatrix);
        assertEquals(3, resultMatrix.getRowDimension());
        assertEquals(3, resultMatrix.getColumnDimension());
    }

    @Test
    public void testComputeCorrelationMatrixWithArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix resultMatrix = correlation.computeCorrelationMatrix(data);
        assertNotNull(resultMatrix);
        assertEquals(3, resultMatrix.getRowDimension());
        assertEquals(3, resultMatrix.getColumnDimension());
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double result = correlation.correlation(xArray, yArray);
        assertEquals(1.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] xArray = {1.0};
        double[] yArray = {2.0};
        correlation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix covarianceMatrix = new BlockRealMatrix(3, 3);
        RealMatrix resultMatrix = correlation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(resultMatrix);
        assertEquals(3, resultMatrix.getRowDimension());
        assertEquals(3, resultMatrix.getColumnDimension());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsufficientData() {
        double[][] data = {{1.0}};
        new PearsonsCorrelation(data);
    }

}