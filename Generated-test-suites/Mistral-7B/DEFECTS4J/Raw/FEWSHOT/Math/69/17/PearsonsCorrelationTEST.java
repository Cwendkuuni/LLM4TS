package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class PearsonsCorrelationTest {

    @Test
    public void testCorrelationMatrixWithEmptyData() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonsCorrelation.getnObs());
    }

    @Test
    public void testCorrelationMatrixWithOneVariable() {
        double[] data = new double[] { 1.0 };
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(1, pearsonsCorrelation.getCorrelationMatrix().getRowDimension());
        assertEquals(1, pearsonsCorrelation.getCorrelationMatrix().getColumnDimension());
        assertEquals(1.0, pearsonsCorrelation.getCorrelationMatrix().getEntry(0, 0), 0.001);
        assertEquals(0, pearsonsCorrelation.getCorrelationMatrix().getEntry(0, 0), 0.001);
        assertEquals(1, pearsonsCorrelation.getnObs());
    }

    @Test
    public void testCorrelationMatrixWithTwoVariables() {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } });
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix actual = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
        assertEquals(2, pearsonsCorrelation.getnObs());
    }

    @Test
    public void testCorrelationMatrixWithTwoVariablesFromRealMatrix() {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        RealMatrix input = new BlockRealMatrix(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } });
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(input);
        RealMatrix actual = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
        assertEquals(2, pearsonsCorrelation.getnObs());
    }

    @Test
    public void testCorrelationMatrixWithTwoVariablesFromCovariance() throws MathException {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        Covariance covariance = new Covariance(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } });
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);
        RealMatrix actual = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
        assertEquals(2, pearsonsCorrelation.getnObs());
    }

    @Test
    public void testCorrelationMatrixWithTwoVariablesFromCovarianceMatrix() throws MathException {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } });
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, 2);
        RealMatrix actual = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
        assertEquals(2, pearsonsCorrelation.getnObs());
    }

    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } });
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(correlationMatrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { Math.sqrt(0.5), Math.sqrt(0.5) }, { Math.sqrt(0.5), Math.sqrt(0.5) } });
        RealMatrix actual = pearsonsCorrelation.getCorrelationStandardErrors();
        assertEquals(correlationMatrix.getRowDimension(), actual.getRowDimension());
        assertEquals(correlationMatrix.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } });
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(correlationMatrix);
        TDistribution tDistribution = new TDistribution(2);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 0.9772, 0.9772 }, { 0.9772, 0.9772 } });
        RealMatrix actual = pearsonsCorrelation.getCorrelationPValues();
        assertEquals(correlationMatrix.getRowDimension(), actual.getRowDimension());
        assertEquals(correlationMatrix.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeCorrelationMatrixWithInvalidMatrix() {
        RealMatrix invalidMatrix = new BlockRealMatrix(new double[][] { { 1.0 } });
        new PearsonsCorrelation(invalidMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidArrays() {
        double[] xArray = new double[] { 1.0 };
        double[] yArray = null;
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithDifferentLengthArrays() {
        double[] xArray = new double[] { 1.0, 2.0 };
        double[] yArray = new double[] { 1.0 };
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInsufficientData() {
        double[] xArray = new double[] { 1.0, 2.0 };
        double[] yArray = new double[] { 1.0, 2.0, 3.0 };
        new PearsonsCorrelation().correlation(xArray, yArray);
    }
}