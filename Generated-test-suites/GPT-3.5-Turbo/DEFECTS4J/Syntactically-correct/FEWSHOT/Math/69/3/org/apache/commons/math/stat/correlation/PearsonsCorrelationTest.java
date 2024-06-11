package org.apache.commons.math.stat.correlation;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.junit.Test;

import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testPearsonsCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNotNull(correlation);
    }
    
    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = correlation.computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
    }
    
    @Test
    public void testComputeCorrelationMatrix_Array() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix correlationMatrix = correlation.computeCorrelationMatrix(data);
        assertNotNull(correlationMatrix);
    }
    
    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = correlation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }
    
    @Test
    public void testGetCorrelationMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation instance = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = instance.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
    }
    
    @Test
    public void testGetCorrelationStandardErrors() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation instance = new PearsonsCorrelation(matrix);
        RealMatrix standardErrors = instance.getCorrelationStandardErrors();
        assertNotNull(standardErrors);
    }
    
    @Test
    public void testGetCorrelationPValues() throws MathException {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation instance = new PearsonsCorrelation(matrix);
        RealMatrix pValues = instance.getCorrelationPValues();
        assertNotNull(pValues);
    }
    
    @Test
    public void testCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double result = correlation.correlation(xArray, yArray);
        assertTrue(result == 1.0);
    }
}