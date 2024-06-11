import org.apache.commons.math.MathException;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {
    
    private PearsonsCorrelation pearsonsCorrelation;
    
    @Before
    public void setUp() {
        pearsonsCorrelation = new PearsonsCorrelation();
    }
    
    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        
        RealMatrix result = pearsonsCorrelation.computeCorrelationMatrix(data);
        
        assertTrue(expected.equals(result));
    }
    
    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {1, 2, 3, 4, 5};
        double expected = 1.0;
        
        double result = pearsonsCorrelation.correlation(xArray, yArray);
        
        assertEquals(expected, result, 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationUnequalLength() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {1, 2, 3, 4};
        
        pearsonsCorrelation.correlation(xArray, yArray);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {2};
        
        pearsonsCorrelation.correlation(xArray, yArray);
    }
    
    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrixArray = {{2, 0}, {0, 3}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixArray);
        
        double[][] expectedArray = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix expected = new BlockRealMatrix(expectedArray);
        
        RealMatrix result = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        
        assertTrue(expected.equals(result));
    }
    
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        pearsonsCorrelation = new PearsonsCorrelation(data);
        
        double[][] expectedArray = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        RealMatrix expected = new BlockRealMatrix(expectedArray);
        
        RealMatrix result = pearsonsCorrelation.getCorrelationMatrix();
        
        assertTrue(expected.equals(result));
    }
    
    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        pearsonsCorrelation = new PearsonsCorrelation(data);
        
        double[][] expectedArray = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};
        RealMatrix expected = new BlockRealMatrix(expectedArray);
        
        RealMatrix result = pearsonsCorrelation.getCorrelationStandardErrors();
        
        assertTrue(expected.equals(result));
    }
    
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        pearsonsCorrelation = new PearsonsCorrelation(data);
        
        double[][] expectedArray = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};
        RealMatrix expected = new BlockRealMatrix(expectedArray);
        
        RealMatrix result = pearsonsCorrelation.getCorrelationPValues();
        
        assertTrue(expected.equals(result));
    }
}