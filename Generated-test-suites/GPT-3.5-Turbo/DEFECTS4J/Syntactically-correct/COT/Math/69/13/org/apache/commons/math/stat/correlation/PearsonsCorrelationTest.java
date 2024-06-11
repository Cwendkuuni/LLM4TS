import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNotNull(pc);
    }

    @Test
    public void testConstructorWithArray() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc);
    }

    @Test
    public void testConstructorWithMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        assertNotNull(pc);
    }

    @Test
    public void testConstructorWithCovariance() {
        Covariance covariance = new Covariance();
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        assertNotNull(pc);
    }

    @Test
    public void testConstructorWithCovarianceMatrix() {
        double[][] covarianceMatrixData = {{1, 2, 3}, {2, 4, 6}, {3, 6, 9}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixData);
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        assertNotNull(pc);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        assertNotNull(correlationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        assertNotNull(correlationPValues);
    }

    @Test
    public void testComputeCorrelationMatrixWithMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pc.computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testComputeCorrelationMatrixWithArray() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pc.computeCorrelationMatrix(data);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double correlation = pc.correlation(xArray, yArray);
        assertEquals(1.0, correlation, 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidSize() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrixData = {{1, 2, 3}, {2, 4, 6}, {3, 6, 9}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixData);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pc.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataWithInsufficientRows() {
        double[][] data = {{1, 2, 3}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.checkSufficientData(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataWithInsufficientCols() {
        double[][] data = {{1}, {2}, {3}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.checkSufficientData(matrix);
    }

}