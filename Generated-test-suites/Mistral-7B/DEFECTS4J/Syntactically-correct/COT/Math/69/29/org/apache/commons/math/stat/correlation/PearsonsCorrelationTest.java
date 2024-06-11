package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.StatUtils;

import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonsCorrelation.nObs);
    }

    @Test
    public void testDataConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}}, correlationMatrix.getData());
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}}, correlationMatrix.getData());
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    @Test
    public void testCovarianceConstructor() {
        double[][] covarianceMatrixData = {{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixData);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.8, 0.8}, {0.8, 1.0, 1.0}, {0.8, 1.0, 1.0}}, correlationMatrix.getData());
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}}, correlationMatrix.getData());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertArrayEquals(new double[][]{{0.57735, 0.60276, 0.60276}, {0.60276, 0.57735, 0.60276}, {0.60276, 0.60276, 0.57735}}, correlationStandardErrors.getData());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        assertArrayEquals(new double[][]{{0.0, 0.03774, 0.01268}, {0.03774, 0.0, 0.03774}, {0.01268, 0.03774, 0.0}}, correlationPValues.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataConstructor() {
        double[][] data = {{1.0, 2.0}, {4.0, 6.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0}});
        new PearsonsCorrelation(matrix);
    }

    @Test(expected = MathException.class)
    public void testInvalidCovarianceConstructor() {
        double[][] covarianceMatrixData = {{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, -1.0}};
        new PearsonsCorrelation(new BlockRealMatrix(covarianceMatrixData), 3);
    }
}