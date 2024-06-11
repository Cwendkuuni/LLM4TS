package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    /**
     * Test constructor with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInputConstructor() {
        new PearsonsCorrelation(null);
    }

    /**
     * Test constructor with empty data array
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDataArrayConstructor() {
        double[][] emptyData = {};
        new PearsonsCorrelation(emptyData);
    }

    /**
     * Test constructor with invalid data array
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataArrayConstructor() {
        double[][] invalidData = {{1}, {3, 2}};
        new PearsonsCorrelation(invalidData);
    }

    /**
     * Test constructor with valid data array
     */
    @Test
    public void testValidDataArrayConstructor() {
        double[][] validData = {{1, 2, 3, 4}, {5, 6, 7, 8}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(validData);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.848528137}, {0.848528137, 1.0}});
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    /**
     * Test constructor with valid RealMatrix input
     */
    @Test
    public void testValidRealMatrixConstructor() {
        double[][] validData = {{1, 2, 3, 4}, {5, 6, 7, 8}};
        RealMatrix validMatrix = new BlockRealMatrix(validData);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(validMatrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.848528137}, {0.848528137, 1.0}});
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    /**
     * Test Covariance constructor
     */
    @Test
    public void testCovarianceConstructor() throws MathException {
        double[][] validData = {{1, 2, 3, 4}, {5, 6, 7, 8}};
        Covariance covariance = new Covariance(validData);
        RealMatrix correlationMatrix = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN()).getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.848528137}, {0.848528137, 1.0}});
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    /**
     * Test correlation method with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInputCorrelationMethod() {
        double[] xArray = null;
        double[] yArray = null;
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlation method with non-matching lengths input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonMatchingLengthsInputCorrelationMethod() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {1, 2};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlation method with valid input
     */
    @Test
    public void testValidInputCorrelationMethod() {
        double[] xArray = {1, 2, 3, 4};
        double[] yArray = {1, 2, 3, 4};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        double expectedCorrelation = 1.0;
        Assert.assertEquals(expectedCorrelation, correlation, 0.001);
    }

    /**
     * Test getCorrelationMatrix method with null input
     */
    @Test(expected = MathRuntimeException.class)
    public void testNullInputGetCorrelationMatrixMethod() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.getCorrelationMatrix();
    }

    /**
     * Test getCorrelationStandardErrors method with null input
     */
    @Test(expected = MathRuntimeException.class)
    public void testNullInputGetCorrelationStandardErrorsMethod() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.getCorrelationStandardErrors();
    }

    /**
     * Test getCorrelationStandardErrors method with valid input
     */
    @Test
    public void testValidInputGetCorrelationStandardErrorsMethod() throws MathException {
        double[][] validData = {{1, 2, 3, 4}, {5, 6, 7, 8}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(validData);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][]{{0.495981019, 0.495981019}, {0.495981019, 0.495981019}});
        Assert.assertEquals(expectedCorrelationStandardErrors, correlationStandardErrors);
    }

    /**
     * Test getCorrelationPValues method with null input
     */
    @Test(expected = MathRuntimeException.class)
    public void testNullInputGetCorrelationPValuesMethod() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.getCorrelationPValues();
    }

    /**
     * Test getCorrelationPValues method with valid input
     */
    @Test
    public void testValidInputGetCorrelationPValuesMethod() throws MathException {
        double[][] validData = {{1, 2, 3, 4}, {5, 6, 7, 8}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(validData);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        RealMatrix expectedCorrelationPValues = new Array2DRowRealMatrix(new double[][]{{0.022770848, 0.022770848}, {0.022770848, 0.022770848}});
        Assert.assertEquals(expectedCorrelationPValues, correlationPValues);
    }
}