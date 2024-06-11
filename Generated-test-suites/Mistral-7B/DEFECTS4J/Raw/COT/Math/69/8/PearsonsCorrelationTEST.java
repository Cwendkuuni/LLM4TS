package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        Assert.assertNull(pearsonsCorrelation.getCorrelationMatrix());
        Assert.assertEquals(0, pearsonsCorrelation.nObs);
    }

    @Test
    public void testDouble2DArrayConstructor() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
        Assert.assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(1, 0), 0.001);
        Assert.assertEquals(0.4, correlationMatrix.getEntry(1, 1), 0.001);
        Assert.assertEquals(0.6, correlationMatrix.getEntry(2, 1), 0.001);
        Assert.assertEquals(0.6, correlationMatrix.getEntry(1, 2), 0.001);
        Assert.assertEquals(0.6, correlationMatrix.getEntry(2, 1), 0.001);
        Assert.assertEquals(0.4, correlationMatrix.getEntry(2, 2), 0.001);
    }

    @Test
    public void testRealMatrixConstructor() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        // Test the values
        testValues(correlationMatrix);
        // Test the dimensions
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
    }

    @Test
    public void testCovarianceConstructor() {
        double[][] dataX = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        double[][] dataY = new double[][]{{2.0, 3.0, 4.0}, {5.0, 6.0, 7.0}};
        Covariance covariance = new Covariance(dataX, dataY);
        RealMatrix correlationMatrix = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN()).getCorrelationMatrix();
        // Test the values
        testValues(correlationMatrix);
        // Test the dimensions
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
    }

    @Test
    public void testRealMatrixWithInsufficientData() {
        double[][] data = new double[][]{{1.0, 2.0}, {4.0, 5.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        try {
            new PearsonsCorrelation(matrix);
            Assert.fail("Expected an IllegalArgumentException");
        } catch (MathRuntimeException e) {
            Assert.assertEquals("insufficient data: only 2 rows and 2 columns.", e.getMessage());
        }
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        // Test the values
        testValues(correlationMatrix);
        // Test the dimensions
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        Assert.assertEquals(3, correlationStandardErrors.getRowDimension());
        Assert.assertEquals(3, correlationStandardErrors.getColumnDimension());
        Assert.assertEquals(0.34202010360282256, correlationStandardErrors.getEntry(0, 0), 0.001);
        Assert.assertEquals(0.4996433122136215, correlationStandardErrors.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.4996433122136215, correlationStandardErrors.getEntry(1, 0), 0.001);
        Assert.assertEquals(0.3155776933472651, correlationStandardErrors.getEntry(1, 1), 0.001);
        Assert.assertEquals(0.499354426672361, correlationStandardErrors.getEntry(1, 2), 0.001);
        Assert.assertEquals(0.499354426672361, correlationStandardErrors.getEntry(2, 1), 0.001);
        Assert.assertEquals(0.3150765527783462, correlationStandardErrors.getEntry(2, 2), 0.001);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        Assert.assertEquals(3, correlationPValues.getRowDimension());
        Assert.assertEquals(3, correlationPValues.getColumnDimension());
        Assert.assertEquals(0.1, correlationPValues.getEntry(0, 0), 0.01);
        Assert.assertEquals(0.1, correlationPValues.getEntry(1, 1), 0.01);
        Assert.assertEquals(0.1, correlationPValues.getEntry(2, 2), 0.01);
        Assert.assertEquals(0.0522269343961175, correlationPValues.getEntry(0, 1), 0.01);
        Assert.assertEquals(0.0522269343961175, correlationPValues.getEntry(1, 0), 0.01);
        Assert.assertEquals(0.0273859822454035, correlationPValues.getEntry(0, 2), 0.01);
        Assert.assertEquals(0.0273859822454035, correlationPValues.getEntry(2, 0), 0.01);
        Assert.assertEquals(0.0136929911227017, correlationPValues.getEntry(1, 2), 0.01);
        Assert.assertEquals(0.0136929911227017, correlationPValues.getEntry(2, 1), 0.01);
    }

    @Test
    public void testCorrelationMethodWithArray() throws IllegalArgumentException {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        Assert.assertEquals(0.8, correlation, 0.001);
    }

    @Test
    public void testCorrelationMethodWithInsufficientData() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {4.0, 5.0};
        try {
            new PearsonsCorrelation().correlation(xArray, yArray);
            Assert.fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid array dimensions. xArray has size {2}; yArray has {2} elements", e.getMessage());
        }
    }

    @Test
    public void testCorrelationMethodWithInsufficientData2() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {4.0};
        try {
            new PearsonsCorrelation().correlation(xArray, yArray);
            Assert.fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid array dimensions. xArray has size {2}; yArray has {1} elements", e.getMessage());
        }
    }

    @Test
    public void testCorrelationMethodWithInsufficientData3() {
        double[] xArray = {1.0};
        double[] yArray = {4.0, 5.0};
        try {
            new PearsonsCorrelation().correlation(xArray, yArray);
            Assert.fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid array dimensions. xArray has size {1}; yArray has {2} elements", e.getMessage());
        }
    }

    @Test
    public void testCorrelationMethodWithInsufficientData4() {
        double[] xArray = {1.0};
        double[] yArray = {};
        try {
            new PearsonsCorrelation().correlation(xArray, yArray);
            Assert.fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid array dimensions for correlation: xArray has size {1}; yArray has {0} elements", e.getMessage());
        }
    }

    @Test
    public void testCorrelationMethodWithNullArrays() {
        double[] xArray = null;
        double[] yArray = {4.0, 5.0};
        try {
            new PearsonsCorrelation().correlation(xArray, yArray);
            Assert.fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Null input data for correlation calculation", e.getMessage());
        }

        double[] xArray2 = {1.0, 2.0};
        double[] yArray2 = null;
        try {
            new PearsonsCorrelation().correlation(xArray2, yArray2);
            Assert.fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Null input data for correlation calculation", e.getMessage());
        }
    }

    @Test
    public void testCovarianceToCorrelationMethod() {
        double[][] covarianceMatrixData = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixData);
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        // Test the values
        testValues(correlationMatrix);
        // Test the dimensions
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
    }

    @Test
    public void testCovarianceToCorrelationMethodWithInsufficientData() {
        double[][] covarianceMatrixData = {{1.0, 2.0}, {2.0, 3.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixData);
        try {
            new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
            Assert.fail("Expected an IllegalArgumentException");
        } catch (MathRuntimeException e) {
            Assert.assertEquals("insufficient data: only 2 rows and 2 columns.", e.getMessage());
        }
    }

    @Test
    public void testComputeCorrelationMatrixMethodWithEmptyMatrix() {
        double[][] data = {};
        RealMatrix matrix = new BlockRealMatrix(data);
        try {
            new PearsonsCorrelation().computeCorrelationMatrix(matrix);
            Assert.fail("Expected an IllegalArgumentException");
        } catch (MathRuntimeException e) {
            Assert.assertEquals("insufficient data: only 0 rows and 0 columns.", e.getMessage());
        }
    }

    @Test
    public void testComputeCorrelationMatrixMethodWithInsufficientData() {
        double[][] data = {{1.0}, {2.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        try {
            new PearsonsCorrelation().computeCorrelationMatrix(matrix);
            Assert.fail("Expected an IllegalArgumentException");
        } catch (MathRuntimeException e) {
            Assert.assertEquals("insufficient data: only 1 rows and 1 columns.", e.getMessage());
        }
    }

    @Test
    public void testComputeCorrelationMatrixMethod() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = new PearsonsCorrelation().computeCorrelationMatrix(matrix);
        // Test the values
        testValues(correlationMatrix);
        // Test the dimensions
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
    }

    private void testValues(RealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                double entry = matrix.getEntry(i, j);
                if (i == j) {
                    Assert.assertEquals(1.0, entry, 0.001);
                } else {
                    double correlation = new PearsonsCorrelation().correlation(new double[]{matrix.getColumnVector(i).toArray()[0]}, new double[]{matrix.getColumnVector(j).toArray()[0]});
                    Assert.assertEquals(entry, correlation, 0.001);
                }
            }
        }
    }
}