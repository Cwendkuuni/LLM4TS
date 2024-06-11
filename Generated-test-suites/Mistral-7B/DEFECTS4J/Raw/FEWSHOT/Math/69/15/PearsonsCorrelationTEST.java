package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    @Test
    public void testDataConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.81915254, 0.81915254}, {0.81915254, 1.0, 0.81915254}, {0.81915254, 0.81915254, 1.0}});
        assertEquals(expected, pc.getCorrelationMatrix());
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.81915254, 0.81915254}, {0.81915254, 1.0, 0.81915254}, {0.81915254, 0.81915254, 1.0}});
        assertEquals(expected, pc.getCorrelationMatrix());
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testCovarianceConstructor() {
        Covariance covariance = new Covariance(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}}));
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.81649658}, {0.81649658, 1.0}});
        assertEquals(expected, pc.getCorrelationMatrix());
        assertEquals(2, pc.nObs);
    }

    @Test
    public void testCovarianceMatrixConstructor() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 2);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.81649658}, {0.81649658, 1.0}});
        assertEquals(expected, pc.getCorrelationMatrix());
        assertEquals(2, pc.nObs);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.81915254, 0.81915254}, {0.81915254, 1.0, 0.81915254}, {0.81915254, 0.81915254, 1.0}});
        assertEquals(expected, pc.getCorrelationMatrix());
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.4472136, 0.4472136}, {0.4472136, 0.4472136}});
        assertEquals(expected, pc.getCorrelationStandardErrors());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.34213158, 0.34213158}, {0.34213158, 0.34213158}});
        assertEquals(expected, pc.getCorrelationPValues());
    }

    @Test
    public void testCorrelationConstructorInvalidData() {
        double[] xArray = {1.0};
        assertThrows(IllegalArgumentException.class, () -> pc.correlation(xArray, null));
        assertThrows(IllegalArgumentException.class, () -> pc.correlation(null, xArray));
        assertThrows(IllegalArgumentException.class, () -> pc.correlation(xArray, new double[0]));
    }

    @Test
    public void testCorrelationMatrixInvalidData() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}};
        assertThrows(IllegalArgumentException.class, () -> new PearsonsCorrelation(data));
    }

    @Test
    public void testCovarianceMatrixInvalidData() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        assertThrows(IllegalArgumentException.class, () -> new PearsonsCorrelation(covarianceMatrix, 2));
    }

    @Test
    public void testCovarianceInvalidData() {
        Covariance covariance = new Covariance(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
        assertThrows(IllegalArgumentException.class, () -> new PearsonsCorrelation(covariance));
    }
}