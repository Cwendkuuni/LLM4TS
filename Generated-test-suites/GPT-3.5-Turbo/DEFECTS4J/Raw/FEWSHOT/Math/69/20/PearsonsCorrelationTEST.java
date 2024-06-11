import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearson;

    @Before
    public void setUp() {
        pearson = new PearsonsCorrelation();
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix expected = new BlockRealMatrix(new double[][] {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        
        RealMatrix result = pearson.computeCorrelationMatrix(data);
        
        assertEquals(expected, result);
    }

    @Test
    public void testComputeCorrelationMatrixWithInvalidSize() {
        double[][] data = {{1, 2, 3}};
        
        try {
            pearson.computeCorrelationMatrix(data);
            fail("IllegalArgumentException should be thrown for invalid data size");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("insufficient data"));
        }
    }

    @Test
    public void testComputeCorrelationMatrixWithEmptyData() {
        double[][] data = {};
        RealMatrix expected = new BlockRealMatrix(new double[][] {});
        
        RealMatrix result = pearson.computeCorrelationMatrix(data);
        
        assertEquals(expected, result);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covariance = new BlockRealMatrix(new double[][] {{4, 2}, {2, 9}});
        RealMatrix expected = new BlockRealMatrix(new double[][] {{1, 0.3086066999}, {0.3086066999, 1}});
        
        RealMatrix result = pearson.covarianceToCorrelation(covariance);
        
        assertEquals(expected, result);
    }

    @Test
    public void testCorrelationWithValidData() {
        double[] xArray = {1, 2, 3, 4};
        double[] yArray = {2, 4, 6, 8};
        double expected = 1.0;
        
        double result = pearson.correlation(xArray, yArray);
        
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testCorrelationWithInvalidArrayLength() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {2, 4, 6, 8};
        
        try {
            pearson.correlation(xArray, yArray);
            fail("IllegalArgumentException should be thrown for invalid array length");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("invalid array dimensions"));
        }
    }

    @Test
    public void testCorrelationWithInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {2};
        
        try {
            pearson.correlation(xArray, yArray);
            fail("IllegalArgumentException should be thrown for insufficient data");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("invalid array dimensions"));
        }
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix expected = new BlockRealMatrix(new double[][] {{1, 1}, {1, 1}});
        
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix result = pearson.getCorrelationMatrix();
        
        assertEquals(expected, result);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix expected = new BlockRealMatrix(new double[][] {{0, 0}, {0, 0}});
        
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix result = pearson.getCorrelationStandardErrors();
        
        assertEquals(expected, result);
    }

    @Test
    public void testGetCorrelationPValues() {
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix expected = new BlockRealMatrix(new double[][] {{0, 0}, {0, 0}});
        
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix result = pearson.getCorrelationPValues();
        
        assertEquals(expected, result);
    }

}