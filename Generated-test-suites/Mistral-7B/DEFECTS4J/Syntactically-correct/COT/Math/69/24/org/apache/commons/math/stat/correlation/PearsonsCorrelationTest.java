package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.TDistributionImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 * Test class for {@link PearsonsCorrelation}
 */
public class PearsonsCorrelationTest {

    /**
     * Test constructor without data
     */
    @org.junit.Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        assertNull(pearson.getCorrelationMatrix());
        assertEquals(0, pearson.getNObs());
    }

    /**
     * Test constructor from double array
     */
    @org.junit.Test
    public void testConstructorFromData() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        assertNotNull(pearson.getCorrelationMatrix());
        assertEquals(3, pearson.getNObs());

        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-6);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(0, 2), 1e-6);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-6);
        // Symmetric matrix
        assertEquals(0.8, correlationMatrix.getEntry(1, 1), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(2, 2), 1e-6);

        assertEquals(3, pearson.getCorrelationStandardErrors().getRowDimension());
        assertEquals(3, pearson.getCorrelationStandardErrors().getColumnDimension());
        RealMatrix stdErr = pearson.getCorrelationStandardErrors();
        assertEquals(0.434, stdErr.getEntry(0, 0), 1e-6);
        assertEquals(0.442, stdErr.getEntry(0, 1), 1e-6);
        assertEquals(0.444, stdErr.getEntry(0, 2), 1e-6);
        assertEquals(0.442, stdErr.getEntry(1, 0), 1e-6);
        assertEquals(0.436, stdErr.getEntry(1, 1), 1e-6);
        assertEquals(0.438, stdErr.getEntry(1, 2), 1e-6);
        assertEquals(0.438, stdErr.getEntry(2, 0), 1e-6);
        assertEquals(0.436, stdErr.getEntry(2, 1), 1e-6);
        assertEquals(0.440, stdErr.getEntry(2, 2), 1e-6);
    }

    /**
     * Test constructor from RealMatrix
     */
    @org.junit.Test
    public void testConstructorFromRealMatrix() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                            {4.0, 5.0, 6.0},
                                                            {7.0, 8.0, 9.0}});
        PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
        assertNotNull(pearson.getCorrelationMatrix());
        assertEquals(3, pearson.getNObs());

        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-6);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(0, 2), 1e-6);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-6);
        // Symmetric matrix
        assertEquals(0.8, correlationMatrix.getEntry(1, 1), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(2, 2), 1e-6);
    }

    /**
     * Test constructor from Covariance
     */
    @org.junit.Test
    public void testConstructorFromCovariance() {
        double[][] covarianceData = new double[][]{{1.0, 0.5, 0.5},
                                                   {0.5, 2.0, 1.5},
                                                   {0.5, 1.5, 3.0}};
        Covariance covariance = new Covariance(covarianceData);
        PearsonsCorrelation pearson = new PearsonsCorrelation(covariance);
        assertNotNull(pearson.getCorrelationMatrix());
        assertEquals(3, pearson.getNObs());

        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-6);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-6);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 1e-6);
        assertEquals(0.5, correlationMatrix.getEntry(2, 2), 1e-6);
    }

    /**
     * Test constructor from CovarianceMatrix
     */
    @org.junit.Test
    public void testConstructorFromCovarianceMatrix() {
        double[][] covarianceData = new double[][]{{1.0, 0.5, 0.5},
                                                   {0.5, 2.0, 1.5},
                                                   {0.5, 1.5, 3.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
        PearsonsCorrelation pearson = new PearsonsCorrelation(covarianceMatrix, covarianceData.length);
        assertNotNull(pearson.getCorrelationMatrix());
        assertEquals(3, pearson.getNObs());

        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-6);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-6);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 1e-6);
        assertEquals(0.5, correlationMatrix.getEntry(2, 2), 1e-6);
    }

    /**
     * Test getCorrelationMatrix()
     */
    @org.junit.Test
    public void testGetCorrelationMatrix() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-6);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(0, 2), 1e-6);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-6);
        // Symmetric matrix
        assertEquals(0.8, correlationMatrix.getEntry(1, 1), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(2, 2), 1e-6);
    }

    /**
     * Test getCorrelationStandardErrors()
     */
    @org.junit.Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearson.getCorrelationStandardErrors();
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        RealMatrix stdErr = correlationStandardErrors;
        assertEquals(0.434, stdErr.getEntry(0, 0), 1e-6);
        assertEquals(0.442, stdErr.getEntry(0, 1), 1e-6);
        assertEquals(0.444, stdErr.getEntry(0, 2), 1e-6);
        assertEquals(0.442, stdErr.getEntry(1, 0), 1e-6);
        assertEquals(0.436, stdErr.getEntry(1, 1), 1e-6);
        assertEquals(0.438, stdErr.getEntry(1, 2), 1e-6);
        assertEquals(0.438, stdErr.getEntry(2, 0), 1e-6);
        assertEquals(0.436, stdErr.getEntry(2, 1), 1e-6);
        assertEquals(0.440, stdErr.getEntry(2, 2), 1e-6);
    }

    /**
     * Test getCorrelationPValues()
     * @throws MathException
     */
    @org.junit.Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pearson.getCorrelationPValues();
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        RealMatrix pValues = correlationPValues;
        assertEquals(0.256, pValues.getEntry(0, 0), 1e-6);
        assertEquals(0.128, pValues.getEntry(0, 1), 1e-6);
        assertEquals(0.081, pValues.getEntry(0, 2), 1e-6);
        assertEquals(0.128, pValues.getEntry(1, 0), 1e-6);
        assertEquals(0.256, pValues.getEntry(1, 1), 1e-6);
        assertEquals(0.081, pValues.getEntry(1, 2), 1e-6);
        assertEquals(0.081, pValues.getEntry(2, 0), 1e-6);
        assertEquals(0.256, pValues.getEntry(2, 1), 1e-6);
        assertEquals(0.128, pValues.getEntry(2, 2), 1e-6);
    }

    /**
     * Test correlation()
     */
    @org.junit.Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.8, correlation, 1e-6);
    }

    /**
     * Test covarianceToCorrelation()
     */
    @org.junit.Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceData = new double[][]{{1.0, 0.5, 0.5},
                                                   {0.5, 2.0, 1.5},
                                                   {0.5, 1.5, 3.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
        RealMatrix correlationMatrix = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-6);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-6);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 1e-6);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 1e-6);
        assertEquals(0.5, correlationMatrix.getEntry(2, 2), 1e-6);
    }

    /**
     * Test checkSufficientData()
     */
    @org.junit.Test(expected = MathRuntimeException.class)
    public void testCheckSufficientData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}});
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        pearson.checkSufficientData(matrix);
    }
}