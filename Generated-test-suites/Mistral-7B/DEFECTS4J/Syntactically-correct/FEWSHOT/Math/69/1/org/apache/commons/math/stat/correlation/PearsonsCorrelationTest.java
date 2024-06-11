package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.stat.regression.SimpleRegression;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNull(correlation.getCorrelationMatrix());
        assertEquals(0, correlation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyData2DArray() {
        double[][] emptyData = {};
        new PearsonsCorrelation(emptyData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyRealMatrix() {
        RealMatrix emptyMatrix = new BlockRealMatrix(0, 0);
        new PearsonsCorrelation(emptyMatrix);
    }

    @Test
    public void testData2DArray() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());

        checkCorrelationMatrix(correlationMatrix);
    }

    @Test
    public void testRealMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();

        assertEquals(matrix, correlation.getInputMatrix());
        assertEquals(correlationMatrix, correlation.getCorrelationMatrix());
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());

        checkCorrelationMatrix(correlationMatrix);
    }

    @Test
    public void testCovariance() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();

        assertEquals(data.length, correlation.nObs);
        assertEquals(correlationMatrix, correlation.getCorrelationMatrix());
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());

        checkCorrelationMatrix(correlationMatrix);
    }

    @Test
    public void testCovarianceMatrix() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix covarianceMatrix = covariance(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, data.length);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();

        assertEquals(data.length, correlation.nObs);
        assertEquals(covarianceMatrix, correlation.getCovarianceMatrix());
        assertEquals(correlationMatrix, correlation.getCorrelationMatrix());
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());

        checkCorrelationMatrix(correlationMatrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = correlation.getCorrelationStandardErrors();

        assertEquals(correlationMatrix.getRowDimension(), correlationStandardErrors.getRowDimension());
        assertEquals(correlationMatrix.getColumnDimension(), correlationStandardErrors.getColumnDimension());

        for (int i = 0; i < correlationStandardErrors.getRowDimension(); i++) {
            for (int j = 0; j < correlationStandardErrors.getColumnDimension(); j++) {
                double entry = correlationStandardErrors.getEntry(i, j);
                double r = correlationMatrix.getEntry(i, j);
                double stdErr = Math.sqrt((1 - r * r) / (correlation.nObs - 2));
                assertEquals(stdErr, entry, 1e-7);
            }
        }
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();

        RealMatrix correlationPValues = correlation.getCorrelationPValues();
        TDistributionImpl tDistribution = new TDistributionImpl(correlation.nObs - 2);

        for (int i = 0; i < correlation.nObs; i++) {
            for (int j = 0; j < correlation.nObs; j++) {
                if (i == j) {
                    assertEquals(0d, correlationPValues.getEntry(i, j), 1e-9);
                } else {
                    double r = correlationMatrix.getEntry(i, j);
                    double t = Math.abs(r) * Math.sqrt((correlation.nObs - 2) / (1 - r * r));
                    double pValue = 2 * (1 - tDistribution.cumulativeProbability(t));
                    assertEquals(pValue, correlationPValues.getEntry(i, j), 1e-9);
                }
            }
        }
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.8, correlation, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataArrays() {
        double[] xArray = {1, 2};
        double[] yArray = {3};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    private void checkCorrelationMatrix(RealMatrix correlationMatrix) {
        assertEquals(1d, correlationMatrix.getEntry(0, 0), 1e-7);
        assertEquals(0.857461542611861, correlationMatrix.getEntry(0, 1), 1e-6);
        assertEquals(0.857461542611861, correlationMatrix.getEntry(1, 0), 1e-6);
        assertEquals(1d, correlationMatrix.getEntry(1, 1), 1e-7);
        assertEquals(-0.142137127315883, correlationMatrix.getEntry(0, 2), 1e-6);
        assertEquals(-0.142137127315883, correlationMatrix.getEntry(2, 0), 1e-6);
        assertEquals(0.433012701892219, correlationMatrix.getEntry(1, 2), 1e-6);
        assertEquals(0.433012701892219, correlationMatrix.getEntry(2, 1), 1e-6);
        assertEquals(1d, correlationMatrix.getEntry(2, 2), 1e-7);
    }

    private RealMatrix covariance(double[][] data) {
        int nRows = data.length;
        int nCols = data[0].length;
        double[][] covariance = new double[nCols][nCols];
        for (int i = 0; i < nCols; i++) {
            double meanI = mean(data, i);
            for (int j = 0; j < nCols; j++) {
                double meanJ = mean(data, j);
                double sum = 0;
                for (int k = 0; k < nRows; k++) {
                    sum += (data[k][i] - meanI) * (data[k][j] - meanJ);
                }
                covariance[i][j] = sum / (nRows - 1);
                covariance[j][i] = covariance[i][j];
            }
        }
        return new BlockRealMatrix(covariance);
    }

    private double mean(double[][] data, int column) {
        double sum = 0;
        for (int row = 0; row < data.length; row++) {
            sum += data[row][column];
        }
        return sum / data.length;
    }
}