package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.TDistributionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test cases for {@link PearsonsCorrelation} class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test constructor without data.
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNull(correlation.getCorrelationMatrix());
        assertEquals(0, correlation.getNObs());
    }

    /**
     * Test constructor with double array data.
     */
    @Test
    public void testConstructorWithData() {
        double[][] data = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertEquals(3, correlation.getNObs());
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        for (int i = 0; i < correlationMatrix.getRowDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                double correlationCoefficient = 0;
                if (i == j) {
                    correlationCoefficient = 1;
                } else {
                    correlationCoefficient = getCorrelationCoefficient(data[i], data[j]);
                }
                assertEquals(correlationCoefficient, correlationMatrix.getEntry(i, j), 0.001);
                assertEquals(correlationCoefficient, correlationMatrix.getEntry(j, i), 0.001);
            }
        }
    }

    /**
     * Test constructor with RealMatrix data.
     */
    @Test
    public void testConstructorWithRealMatrixData() {
        RealMatrix matrix = new BlockRealMatrix(new double[][] { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} });
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        assertEquals(3, correlation.getNObs());
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        for (int i = 0; i < correlationMatrix.getRowDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                double correlationCoefficient = 0;
                if (i == j) {
                    correlationCoefficient = 1;
                } else {
                    correlationCoefficient = getCorrelationCoefficient(matrix.getColumnVector(i), matrix.getColumnVector(j));
                }
                assertEquals(correlationCoefficient, correlationMatrix.getEntry(i, j), 0.001);
                assertEquals(correlationCoefficient, correlationMatrix.getEntry(j, i), 0.001);
            }
        }
    }

    /**
     * Test constructor with Covariance data.
     */
    @Test
    public void testConstructorWithCovarianceData() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][] { {1, 0.5}, {0.5, 1} });
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, 3);
        assertEquals(3, correlation.getNObs());
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(1, correlationMatrix.getEntry(1, 1), 0.001);
    }

    /**
     * Test getCorrelationMatrix method.
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        for (int i = 0; i < correlationMatrix.getRowDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                double correlationCoefficient = 0;
                if (i == j) {
                    correlationCoefficient = 1;
                } else {
                    correlationCoefficient = getCorrelationCoefficient(data[i], data[j]);
                }
                assertEquals(correlationCoefficient, correlationMatrix.getEntry(i, j), 0.001);
                assertEquals(correlationCoefficient, correlationMatrix.getEntry(j, i), 0.001);
            }
        }
    }

    /**
     * Test getCorrelationStandardErrors method.
     */
    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = correlation.getCorrelationStandardErrors();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        for (int i = 0; i < correlationMatrix.getRowDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                double correlationCoefficient = correlationMatrix.getEntry(i, j);
                double standardError = Math.sqrt((1 - correlationCoefficient * correlationCoefficient) / (correlation.getNObs() - 2));
                assertEquals(standardError, correlationStandardErrors.getEntry(i, j), 0.001);
                assertEquals(standardError, correlationStandardErrors.getEntry(j, i), 0.001);
            }
        }
    }

    /**
     * Test getCorrelationPValues method.
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix correlationPValues = correlation.getCorrelationPValues();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        for (int i = 0; i < correlationMatrix.getRowDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                double correlationCoefficient = correlationMatrix.getEntry(i, j);
                double pValue = 2 * (1 - new TDistributionImpl(correlation.getNObs() - 2).cumulativeProbability(Math.abs(correlationCoefficient) * Math.sqrt((correlation.getNObs() - 2) / (1 - correlationCoefficient * correlationCoefficient))));
                assertEquals(pValue, correlationPValues.getEntry(i, j), 0.001);
                assertEquals(pValue, correlationPValues.getEntry(j, i), 0.001);
            }
        }
    }

    /**
     * Test correlation method with double arrays.
     */
    @Test
    public void testCorrelationWithDoubleArrays() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double correlationCoefficient = 0.8;
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        try {
            assertEquals(correlationCoefficient, correlation.correlation(xArray, yArray), 0.001);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test correlation method with RealVector data.
     */
    @Test
    public void testCorrelationWithRealVectorData() {
        RealMatrix matrix = new BlockRealMatrix(new double[][] { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} });
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        for (int i = 0; i < matrix.getColumnDimension(); i++) {
            RealVector xVector = matrix.getColumnVector(i);
            RealVector yVector = matrix.getColumnVector(i + 1);
            try {
                double correlationCoefficient = correlation.correlation(xVector, yVector.toArray());
                assertEquals(getCorrelationCoefficient(matrix.getColumnVector(i), matrix.getColumnVector(i + 1)), correlationCoefficient, 0.001);
            } catch (IllegalArgumentException e) {
                fail("Unexpected exception: " + e.getMessage());
            }
        }
    }

    /**
     * Test covarianceToCorrelation method.
     */
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][] { {1, 0.5}, {0.5, 1} });
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = correlation.covarianceToCorrelation(covarianceMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(1, correlationMatrix.getEntry(1, 1), 0.001);
    }

    /**
     * Test checkSufficientData method.
     */
    @Test(expected = MathRuntimeException.class)
    public void testCheckSufficientDataWithInsufficientData() {
        RealMatrix matrix = new BlockRealMatrix(new double[][] { {1} });
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.checkSufficientData(matrix);
        fail("Unexpected result");
    }

    /**
     * Test correlation method with insufficient data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {4};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
        fail("Unexpected result");
    }

    /**
     * Test correlation method with different length arrays.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithDifferentLengthArrays() {
        double[] xArray = {1, 2};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
        fail("Unexpected result");
    }

    /**
     * Calculates the correlation coefficient between two arrays.
     *
     * @param xArray the first array
     * @param yArray the second array
     * @return the correlation coefficient
     */
    private double getCorrelationCoefficient(double[] xArray, double[] yArray) {
        SimpleRegression regression = new SimpleRegression();
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;
        double sumY2 = 0;
        for (int i = 0; i < xArray.length; i++) {
            double x = xArray[i];
            double y = yArray[i];
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
            sumY2 += y * y;
        }
        double meanX = sumX / xArray.length;
        double meanY = sumY / xArray.length;
        double varianceX = sumX2 / xArray.length - meanX * meanX;
        double varianceY = sumY2 / xArray.length - meanY * meanY;
        return sumXY / Math.sqrt(varianceX * varianceY);
    }
}