package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link PearsonsCorrelation} class.
 */
public class PearsonsCorrelationTest {

    @Test
    public void testEmptyMatrix() {
        // Given
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();

        // When & Then
        Assert.assertNull(pearsonCorrelation.getCorrelationMatrix());
        Assert.assertEquals(0, pearsonCorrelation.getNObs());
    }

    @Test
    public void testRectangularDataMatrix() {
        // Given
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);

        // When
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();

        // Then
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());

        // Typical use case
        double[] x = {1.0, 4.0, 7.0};
        double[] y = {2.0, 5.0, 8.0};
        double correlation = pearsonCorrelation.correlation(x, y);
        Assert.assertEquals(0.8, correlation, 0.01);

        // Edge case: correlation of the same array
        double[] z = {1.0, 1.0, 1.0};
        correlation = pearsonCorrelation.correlation(z, z);
        Assert.assertEquals(1.0, correlation, 0.01);

        // Error case: arrays with different lengths
        double[] w = {1.0, 2.0, 3.0};
        double[] v = {1.0, 2.0};
        try {
            correlation = pearsonCorrelation.correlation(w, v);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRealMatrixData() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);

        // When
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();

        // Then
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(2, correlationMatrix.getRowDimension());
        Assert.assertEquals(2, correlationMatrix.getColumnDimension());

        // Typical use case
        RealMatrix x = new Array2DRowRealMatrix(new double[][]{{1.0, 4.0}, {2.0, 5.0}});
        RealMatrix y = new Array2DRowRealMatrix(new double[][]{{3.0, 6.0}, {4.0, 7.0}});
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        Assert.assertEquals(expectedCorrelationMatrix, pearsonCorrelation.computeCorrelationMatrix(x, y));

        // Edge case: correlation of the same matrix
        RealMatrix z = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RealMatrix expectedIdentity = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        Assert.assertEquals(expectedIdentity, pearsonCorrelation.computeCorrelationMatrix(z));

        // Error case: matrices with different dimensions
        RealMatrix w = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RealMatrix v = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}});
        try {
            pearsonCorrelation.computeCorrelationMatrix(w, v);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCovarianceData() {
        // Given
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0}, {0.5, 1.0, 0}, {0, 0, 1.0}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);

        // When
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();

        // Then
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());

        // Typical use case
        RealMatrix x = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        RealMatrix y = new Array2DRowRealMatrix(new double[][]{{2.0, 3.0, 4.0}, {5.0, 6.0, 7.0}, {8.0, 9.0, 10.0}});
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{0.8, 0.6, 0.5}, {0.6, 0.8, 0.5}, {0.5, 0.5, 1.0}});
        Assert.assertEquals(expectedCorrelationMatrix, pearsonCorrelation.computeCorrelationMatrix(x, y));

        // Edge case: correlation of the same vector
        RealMatrix z = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        RealMatrix expectedIdentity = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        Assert.assertEquals(expectedIdentity, pearsonCorrelation.computeCorrelationMatrix(z));

        // Error case: covariance matrix with incorrect number of observations
        RealMatrix w = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}, {10.0, 11.0, 12.0}});
        try {
            pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, w.getRowDimension());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCorrelationStandardErrors() {
        // Given
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);

        // When
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();

        // Then
        Assert.assertNotNull(correlationMatrix);
        Assert.assertNotNull(correlationStandardErrors);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
        Assert.assertEquals(3, correlationStandardErrors.getRowDimension());
        Assert.assertEquals(3, correlationStandardErrors.getColumnDimension());

        // Typical use case
        double[] x = {1.0, 4.0, 7.0};
        double[] y = {2.0, 5.0, 8.0};
        double correlation = pearsonCorrelation.correlation(x, y);
        double stdErrorX = Math.sqrt((1 - correlation * correlation) / ( pearsonCorrelation.getNObs() - 2));
        double stdErrorY = Math.sqrt((1 - correlation * correlation) / ( pearsonCorrelation.getNObs() - 2));
        double[] expectedStandardErrors = {stdErrorX, stdErrorY, stdErrorX, stdErrorY, stdErrorX, stdErrorY};
        for (int i = 0; i < 6; i++) {
            Assert.assertEquals(expectedStandardErrors[i], correlationStandardErrors.getEntry(i, i), 0.01);
            Assert.assertEquals(expectedStandardErrors[i], correlationStandardErrors.getEntry(i, i), 0.01);
        }

        // Edge case: correlation of the same vector
        double[] z = {1.0, 1.0, 1.0};
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(0.0, correlationStandardErrors.getEntry(i, i), 0.01);
        }

        // Error case: insufficient data
        double[][] w = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            pearsonCorrelation = new PearsonsCorrelation(w);
            correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        // Given
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);

        // When
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();

        // Then
        Assert.assertNotNull(correlationMatrix);
        Assert.assertNotNull(correlationPValues);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
        Assert.assertEquals(3, correlationPValues.getRowDimension());
        Assert.assertEquals(3, correlationPValues.getColumnDimension());

        // Typical use case
        double[] x = {1.0, 4.0, 7.0};
        double[] y = {2.0, 5.0, 8.0};
        double correlation = pearsonCorrelation.correlation(x, y);
        double pValueX = 2 * (1 - new TDistributionImpl(pearsonCorrelation.getNObs() - 2).cumulativeProbability(Math.abs(correlation) * Math.sqrt((pearsonCorrelation.getNObs() - 2) / (1 - correlation * correlation))));
        double pValueY = 2 * (1 - new TDistributionImpl(pearsonCorrelation.getNObs() - 2).cumulativeProbability(Math.abs(correlation) * Math.sqrt((pearsonCorrelation.getNObs() - 2) / (1 - correlation * correlation))));
        double[] expectedPValues = {pValueX, pValueY, pValueX, pValueY, pValueX, pValueY};
        for (int i = 0; i < 6; i++) {
            Assert.assertEquals(expectedPValues[i], correlationPValues.getEntry(i, i), 0.01);
            Assert.assertEquals(expectedPValues[i], correlationPValues.getEntry(i, i), 0.01);
        }

        // Edge case: correlation of the same vector
        double[] z = {1.0, 1.0, 1.0};
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(1.0, correlationPValues.getEntry(i, i), 0.01);
        }

        // Error case: insufficient data
        double[][] w = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            pearsonCorrelation = new PearsonsCorrelation(w);
            correlationPValues = pearsonCorrelation.getCorrelationPValues();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}