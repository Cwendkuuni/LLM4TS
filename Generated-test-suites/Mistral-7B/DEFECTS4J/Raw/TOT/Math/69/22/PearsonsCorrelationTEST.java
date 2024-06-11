package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link PearsonsCorrelation} class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test correlation matrix computation for given input data.
     */
    @Test
    public void testCorrelationMatrix() {
        double[][] inputData = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{{1.0, 0.82892333, 0.86206897},
                {0.82892333, 1.0, 0.81649658},
                {0.86206897, 0.81649658, 1.0}});

        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(inputData);
        RealMatrix result = pearsonCorr.getCorrelationMatrix();

        assertEquals(expectedResult, result);
    }

    /**
     * Test correlation matrix computation for given covariance matrix.
     */
    @Test
    public void testCorrelationMatrixFromCovariance() {
        double[][] inputData = {{1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}};

        double[][] covarianceMatrix = {
                {5.14285714, 2.19803923, 1.68483327},
                {2.19803923, 6.04718282, 2.35619449},
                {1.68483327, 2.35619449, 4.73153353}};

        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.82892333, 0.86206897},
                {0.82892333, 1.0, 0.81649658},
                {0.86206897, 0.81649658, 1.0}});

        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix result = pearsonCorr.getCorrelationMatrix();

        assertEquals(expectedCorrelationMatrix, result);
    }

    /**
     * Test correlation between two arrays.
     */
    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 3.0, 4.0};

        double expectedResult = 0.82892333;

        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation();
        double result = pearsonCorr.correlation(xArray, yArray);

        assertEquals(expectedResult, result, 0.01);
    }

    /**
     * Test correlation between two arrays with different lengths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithDifferentLengths() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0};

        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation();
        pearsonCorr.correlation(xArray, yArray);
    }

    /**
     * Test correlation between two arrays with insufficient data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInsufficientData() {
        double[] xArray = {1.0};
        double[] yArray = {2.0};

        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation();
        pearsonCorr.correlation(xArray, yArray);
    }

    /**
     * Test correlation p-values computation.
     */
    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] inputData = {{1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}};

        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(inputData);
        RealMatrix pValues = pearsonCorr.getCorrelationPValues();

        double[][] expectedResult = {{0.25641143, 0.10333141, 0.10333141},
                {0.10333141, 0.25641143, 0.10333141},
                {0.10333141, 0.10333141, 0.25641143}};

        assertEquals(new Array2DRowRealMatrix(expectedResult), pValues);
    }
}