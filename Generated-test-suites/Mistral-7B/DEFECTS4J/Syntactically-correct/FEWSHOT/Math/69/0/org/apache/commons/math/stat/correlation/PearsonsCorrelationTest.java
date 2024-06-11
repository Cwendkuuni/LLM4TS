package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealArray2D;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.stat.regression.SimpleRegression;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertNull(pearsonCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataArray() {
        double[][] data = new double[1][];
        new PearsonsCorrelation(data);
    }

    @Test
    public void testValidDataArray() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8944271904510251, 0.8660254037844386},
                {0.8944271904510251, 1.0, 0.8944271904510251},
                {0.8660254037844386, 0.8944271904510251, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testValidRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8888888888888888},
                {0.8888888888888888, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(2, pearsonCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3}});
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testValidCovariance() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 2}, {2, 4, 2}, {2, 2, 3}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(new Covariance(covarianceMatrix, 3));
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8888888888888888},
                {0.8888888888888888, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testValidCovarianceMatrix() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 2}, {2, 4, 2}, {2, 2, 3}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8888888888888888},
                {0.8888888888888888, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCovarianceMatrix() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        new PearsonsCorrelation(covarianceMatrix, 2);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][]{{0.3420201424606355, 0.3420201424606355},
                {0.3420201424606355, 0.3420201424606355},
                {0.3420201424606355, 0.3420201424606355}});
        assertEquals(expectedCorrelationStandardErrors, correlationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
        RealMatrix expectedCorrelationPValues = new Array2DRowRealMatrix(new double[][]{{0.0337106393491712, 0.0337106393491712},
                {0.0337106393491712, 0.0337106393491712},
                {0.0337106393491712, 0.0337106393491712}});
        assertEquals(expectedCorrelationPValues, correlationPValues);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.8888888888888888, correlation, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationArrays() {
        double[] xArray = {1};
        double[] yArray = {};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationArraysLength() {
        double[] xArray = {1};
        double[] yArray = {1, 2};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test
    public void testCorrelationMatrix() {
        RealMatrix xMatrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        RealMatrix yMatrix = new Array2DRowRealMatrix(new double[][]{{10, 11}, {12, 13}, {14, 15}});
        RealMatrix correlationMatrix = new PearsonsCorrelation(xMatrix, yMatrix).getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{0.988890162185328, 0.9853629698448815},
                {0.9853629698448815, 0.988890162185328}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }
}