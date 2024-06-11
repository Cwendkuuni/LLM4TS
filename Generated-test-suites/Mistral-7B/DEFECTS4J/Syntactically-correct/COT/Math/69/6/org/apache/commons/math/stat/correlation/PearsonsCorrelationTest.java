package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.Random;

/**
 * Test class for the PearsonsCorrelation class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test constructor with empty input.
     */
    @org.junit.Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        assertNull(pearson.correlationMatrix);
        assertEquals(0, pearson.nObs);
    }

    /**
     * Test constructor with a double array.
     */
    @org.junit.Test
    public void testDoubleArrayConstructor() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        assertNotNull(pearson.correlationMatrix);
        assertEquals(3, pearson.nObs);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.83269652},
                {0.83269652, 1.0},
                {0.83269652, 0.83269652}
        });
        assertEquals(expected, pearson.correlationMatrix);
    }

    /**
     * Test constructor with a RealMatrix.
     */
    @org.junit.Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
        assertNotNull(pearson.correlationMatrix);
        assertEquals(3, pearson.nObs);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.83269652},
                {0.83269652, 1.0},
                {0.83269652, 0.83269652}
        });
        assertEquals(expected, pearson.correlationMatrix);
    }

    /**
     * Test constructor with a Covariance.
     */
    @org.junit.Test
    public void testCovarianceConstructor() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearson = new PearsonsCorrelation(covariance);
        assertNotNull(pearson.correlationMatrix);
        assertEquals(2, pearson.nObs);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.84955813},
                {0.84955813, 1.0}
        });
        assertEquals(expected, pearson.correlationMatrix);
    }

    /**
     * Test method getCorrelationMatrix().
     */
    @org.junit.Test
    public void testGetCorrelationMatrix() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.83269652},
                {0.83269652, 1.0},
                {0.83269652, 0.83269652}
        });
        assertEquals(expected, correlationMatrix);
    }

    /**
     * Test method getCorrelationStandardErrors().
     */
    @org.junit.Test
    public void testGetCorrelationStandardErrors() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearson.getCorrelationStandardErrors();

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {0.46246959, 0.46633846, 0.46633846},
                {0.46633846, 0.46246959, 0.46246959},
                {0.46633846, 0.46246959, 0.46246959}
        });
        assertEquals(expected, correlationStandardErrors);
    }

    /**
     * Test method getCorrelationPValues().
     */
    @org.junit.Test
    public void testGetCorrelationPValues() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pearson.getCorrelationPValues();

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {0.0, 0.82971351, 0.82971351},
                {0.82971351, 0.0, 0.0},
                {0.82971351, 0.0, 0.0}
        });
        assertEquals(expected, correlationPValues);
    }

    /**
     * Test method correlation().
     */
    @org.junit.Test(expected = MathRuntimeException.class)
    public void testCorrelationInvalidData() {
        new PearsonsCorrelation().correlation(new double[0], new double[0]);
    }

    @org.junit.Test
    public void testCorrelationRandomData() {
        int n = 100;
        double[] x = new double[n];
        double[] y = new double[n];
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            x[i] = random.nextGaussian();
            y[i] = random.nextGaussian();
        }

        PearsonsCorrelation pearson = new PearsonsCorrelation();
        double correlation = pearson.correlation(x, y);
        assertNotNull(correlation);
    }

    @org.junit.Test
    public void testCorrelationDataWithSameValues() {
        double[] x = new double[]{1.0, 2.0, 2.0, 2.0, 2.0};
        double[] y = new double[]{2.0, 2.0, 2.0, 2.0, 2.0};

        PearsonsCorrelation pearson = new PearsonsCorrelation();
        double correlation = pearson.correlation(x, y);
        assertEquals(1.0, correlation);
    }

    /**
     * Test method covarianceToCorrelation().
     */
    @org.junit.Test
    public void testCovarianceToCorrelation() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix covarianceMatrix = new Covariance(data).getCovarianceMatrix();
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.84955813},
                {0.84955813, 1.0}
        });
        assertEquals(expected, correlationMatrix);
    }

    /**
     * Test method checkSufficientData().
     */
    @org.junit.Test(expected = MathRuntimeException.class)
    public void testCheckSufficientDataInvalidData() {
        new PearsonsCorrelation().checkSufficientData(new Array2DRowRealMatrix(new double[0][0]));
    }

    @org.junit.Test
    public void testCheckSufficientDataValidData() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        new PearsonsCorrelation().checkSufficientData(new Array2DRowRealMatrix(data));
    }
}