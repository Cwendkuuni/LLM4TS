package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        assertNull(pearson.getCorrelationMatrix());
        assertEquals(0, pearson.nObs);
    }

    @Test
    public void testConstructorFromDataArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-10);
        assertEquals(0.8164965809277261, correlationMatrix.getEntry(0, 1), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(0, 2), 1e-10);
        assertEquals(0.8164965809277261, correlationMatrix.getEntry(1, 0), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(1, 2), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(2, 0), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(2, 1), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(2, 2), 1e-10);
        assertEquals(3, pearson.nObs);
    }

    @Test
    public void testConstructorFromRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        // same check as in testConstructorFromDataArray()
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-10);
        assertEquals(0.8164965809277261, correlationMatrix.getEntry(0, 1), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(0, 2), 1e-10);
        assertEquals(0.8164965809277261, correlationMatrix.getEntry(1, 0), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(1, 2), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(2, 0), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(2, 1), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(2, 2), 1e-10);
        assertEquals(3, pearson.nObs);
    }

    @Test
    public void testConstructorFromCovariance() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Covariance covariance = new Covariance(data);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        PearsonsCorrelation pearson = new PearsonsCorrelation(covarianceMatrix, covariance.getN());
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        // same check as in testConstructorFromDataArray()
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-10);
        assertEquals(0.8164965809277261, correlationMatrix.getEntry(0, 1), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(0, 2), 1e-10);
        assertEquals(0.8164965809277261, correlationMatrix.getEntry(1, 0), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(1, 2), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(2, 0), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(2, 1), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(2, 2), 1e-10);
        assertEquals(3, pearson.nObs);
    }

    @Test
    public void testConstructorFromCovarianceMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Covariance covariance = new Covariance(data);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        PearsonsCorrelation pearson = new PearsonsCorrelation(covarianceMatrix, covariance.getN());
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        // same check as in testConstructorFromDataArray()
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-10);
        assertEquals(0.8164965809277261, correlationMatrix.getEntry(0, 1), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(0, 2), 1e-10);
        assertEquals(0.8164965809277261, correlationMatrix.getEntry(1, 0), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(1, 2), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(2, 0), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(2, 1), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(2, 2), 1e-10);
        assertEquals(3, pearson.nObs);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        // same check as in testConstructorFromDataArray()
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-10);
        assertEquals(0.8164965809277261, correlationMatrix.getEntry(0, 1), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(0, 2), 1e-10);
        assertEquals(0.8164965809277261, correlationMatrix.getEntry(1, 0), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(1, 2), 1e-10);
        assertEquals(0.658505617293218, correlationMatrix.getEntry(2, 0), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(2, 1), 1e-10);
        assertEquals(1.0, correlationMatrix.getEntry(2, 2), 1e-10);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pearson.getCorrelationStandardErrors();
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == j) {
                    assertEquals(0.0, correlationStandardErrors.getEntry(i, j), 1e-10);
                } else {
                    double r = correlationMatrix.getEntry(i, j);
                    double nObs = pearson.nObs;
                    double stdevi = Math.sqrt((1.0 - r * r) / (nObs - 2));
                    double stdError = correlationStandardErrors.getEntry(i, j);
                    assertEquals(stdevi, stdError, 1e-10);
                }
            }
        }
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        RealMatrix correlationPValues = pearson.getCorrelationPValues();
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == j) {
                    assertEquals(0.0, correlationPValues.getEntry(i, j), 1e-10);
                } else {
                    double r = correlationMatrix.getEntry(i, j);
                    double nObs = pearson.nObs;
                    double t = r * Math.sqrt((nObs - 2) / (1.0 - r * r));
                    double pValue = correlationPValues.getEntry(i, j);
                    TDistribution tDist = new TDistributionImpl(nObs - 2);
                    double pValueExpected = 2 * (1.0 - tDist.cumulativeProbability(Math.abs(t)));
                    assertEquals(pValueExpected, pValue, 1e-10);
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataForCorrelationConstructor() {
        double[][] data = {{1.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataForCorrelationMethod() {
        double[] xArray = {1.0};
        double[] yArray = {};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }
}