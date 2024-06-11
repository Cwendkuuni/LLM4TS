import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testDefaultConstructor() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNotNull(pearsonsCorrelation);
    }

    @Test
    public void testArrayConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation);
    }

    @Test
    public void testMatrixConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        assertNotNull(pearsonsCorrelation);
    }

    @Test
    public void testCovarianceConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        Covariance covariance = new Covariance(matrix);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);
        assertNotNull(pearsonsCorrelation);
    }

    @Test
    public void testCovarianceMatrixConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        int numberOfObservations = 2;
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, numberOfObservations);
        assertNotNull(pearsonsCorrelation);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertNotNull(correlationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        assertNotNull(correlationPValues);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testComputeCorrelationMatrixWithArray() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(data);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(1.0, correlation, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidArray() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testCheckSufficientDataWithValidMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.checkSufficientData(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataWithInsufficientRows() {
        double[][] data = {{1, 2, 3}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.checkSufficientData(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataWithInsufficientColumns() {
        double[][] data = {{1}, {4}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.checkSufficientData(matrix);
    }

}

//