import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {
    
    @Test
    public void testConstructorWithNoData() {
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
    public void testConstructorWithRealMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        assertNotNull(pc);
    }
    
    @Test
    public void testConstructorWithCovariance() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        assertNotNull(pc);
    }
    
    @Test
    public void testConstructorWithCovarianceMatrix() {
        double[][] covarianceMatrix = {{1, 2}, {2, 4}};
        int n = 2;
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, n);
        assertNotNull(pc);
    }
    
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
    }
    
    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        assertNotNull(correlationStandardErrors);
    }
    
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        assertNotNull(correlationPValues);
    }
    
    @Test
    public void testComputeCorrelationMatrixWithRealMatrix() {
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
        assertNotNull(correlation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidInput() {
        double[] xArray = {1};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double correlation = pc.correlation(xArray, yArray);
    }
    
    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrix = {{1, 2}, {2, 4}};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pc.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }
    
    @Test
    public void testCheckSufficientDataWithValidMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.checkSufficientData(matrix);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataWithInvalidMatrix() {
        double[][] data = {{1}, {4}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.checkSufficientData(matrix);
    }
}