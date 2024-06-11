package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.math.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class PearsonsCorrelationTest {
    
    private PearsonsCorrelation correlation;
    private RealMatrix matrix;
    
    @Before
    public void setUp() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        matrix = new BlockRealMatrix(data);
        correlation = new PearsonsCorrelation(matrix);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(correlation);
    }
    
    @Test
    public void testGetCorrelationMatrix() {
        RealMatrix expected = matrix.getSubMatrix(1, 2, 1, 2);
        RealMatrix actual = correlation.getCorrelationMatrix();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetCorrelationStandardErrors() {
        RealMatrix expected = new BlockRealMatrix(2, 2);
        expected.setEntry(0, 0, 0.0);
        expected.setEntry(0, 1, 0.0);
        expected.setEntry(1, 0, 0.0);
        expected.setEntry(1, 1, 0.0);
        RealMatrix actual = correlation.getCorrelationStandardErrors();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetCorrelationPValues() {
        RealMatrix expected = new BlockRealMatrix(2, 2);
        expected.setEntry(0, 0, 0.0);
        expected.setEntry(0, 1, 0.0);
        expected.setEntry(1, 0, 0.0);
        expected.setEntry(1, 1, 0.0);
        RealMatrix actual = correlation.getCorrelationPValues();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeCorrelationMatrix() {
        RealMatrix expected = matrix.getSubMatrix(1, 2, 1, 2);
        RealMatrix actual = correlation.computeCorrelationMatrix(matrix);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double expected = 1.0;
        double actual = correlation.correlation(xArray, yArray);
        assertEquals(expected, actual, 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidArrayDimensions() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0};
        correlation.correlation(xArray, yArray);
    }
    
    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceData = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
        
        double[][] correlationData = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        RealMatrix expected = new BlockRealMatrix(correlationData);
        
        RealMatrix actual = correlation.covarianceToCorrelation(covarianceMatrix);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCheckSufficientData() {
        RealMatrix validMatrix = new BlockRealMatrix(2, 2);
        correlation.checkSufficientData(validMatrix);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataInsufficientRows() {
        RealMatrix invalidMatrix = new BlockRealMatrix(1, 2);
        correlation.checkSufficientData(invalidMatrix);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataInsufficientColumns() {
        RealMatrix invalidMatrix = new BlockRealMatrix(2, 1);
        correlation.checkSufficientData(invalidMatrix);
    }
}