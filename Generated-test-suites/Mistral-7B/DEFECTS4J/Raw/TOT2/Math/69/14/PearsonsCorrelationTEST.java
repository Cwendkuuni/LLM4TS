import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link PearsonsCorrelation} class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test for empty constructor.
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNotNull(correlation);
        Assert.assertNull(correlation.correlationMatrix);
    }

    /**
     * Test for constructor with empty data array.
     */
    @Test
    public void testConstructorWithEmptyData() {
        double[][] emptyData = {};
        PearsonsCorrelation correlation = new PearsonsCorrelation(emptyData);
        assertNotNull(correlation);
        Assert.assertNull(correlation.correlationMatrix);
    }

    /**
     * Test for constructor with valid rectangular array data.
     */
    @Test
    public void testConstructorWithValidData() {
        double[][] validData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(validData);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8333333333333333, 0.8761904589634146},
                {0.8333333333333333, 1.0, 0.8333333333333333},
                {0.8761904589634146, 0.8333333333333333, 1.0}
        });
        RealMatrix actualCorrelationMatrix = correlation.correlationMatrix;
        assertEquals(expectedCorrelationMatrix.toString(), actualCorrelationMatrix.toString());
    }

    /**
     * Test for constructor with invalid rectangular array data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidData() {
        double[][] invalidData = {{1, 2}, {1, 2, 3}, {3}};
        new PearsonsCorrelation(invalidData);
    }

    /**
     * Test for constructor with valid RealMatrix data.
     */
    @Test
    public void testConstructorWithValidRealMatrixData() {
        RealMatrix validMatrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2},
                {3, 4}
        });
        PearsonsCorrelation correlation = new PearsonsCorrelation(validMatrix);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8465368797730932},
                {0.8465368797730932, 1.0}
        });

        RealMatrix actualCorrelationMatrix = correlation.correlationMatrix;
        assertEquals(expectedCorrelationMatrix.toString(), actualCorrelationMatrix.toString());
    }

    /**
     * Test for constructor with null RealMatrix data.
     */
    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNullRealMatrixData() {
        new PearsonsCorrelation(null);
    }

    /**
     * Test for method getCorrelationMatrix.
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] validData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(validData);

        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8333333333333333, 0.8761904589634146},
                {0.8333333333333333, 1.0, 0.8333333333333333},
                {0.8761904589634146, 0.8333333333333333, 1.0}
        });
        RealMatrix actualCorrelationMatrix = correlation.getCorrelationMatrix();

        assertEquals(expectedCorrelationMatrix.toString(), actualCorrelationMatrix.toString());
    }

    /**
     * Test for method getCorrelationStandardErrors.
     */
    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] validData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(validData);
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][]{
                {0.4898945344069626, 0.453472751662351},
                {0.453472751662351, 0.4898945344069626}
        });
        RealMatrix actualCorrelationStandardErrors = correlation.getCorrelationStandardErrors();

        assertEquals(expectedCorrelationStandardErrors.toString(), actualCorrelationStandardErrors.toString());
    }

    /**
     * Test for method getCorrelationPValues.
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] validData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(validData);
        RealMatrix expectedCorrelationPValues = new Array2DRowRealMatrix(new double[][]{
                {0.9998534404554389, 0.9934096446491872},
                {0.9934096446491872, 0.9998534404554389}
        });
        RealMatrix actualCorrelationPValues = correlation.getCorrelationPValues();

        assertEquals(expectedCorrelationPValues.toString(), actualCorrelationPValues.toString());
    }

    /**
     * Test for correlation method with valid data arrays.
     */
    @Test
    public void testCorrelationWithValidDataArrays() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {2, 4, 6, 8, 10};
        double correlationCoefficient = 0.8;

        SimpleRegression regression = new SimpleRegression();
        for(int i=0; i<xArray.length; i++) {
            regression.addData(xArray[i], yArray[i]);
        }
        double regressionR = regression.getR();

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        double actualCorrelationCoefficient = pearsonCorrelation.correlation(xArray, yArray);

        Assert.assertEquals(correlationCoefficient, actualCorrelationCoefficient, 0.001);
    }

    /**
     * Test for correlation method with invalid data arrays.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidDataArrays() {
        double[] xArray = {1, 2};
        double[] yArray = {2, 4, 6};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    /**
     * Test for covarianceToCorrelation method with valid covariance matrix data.
     */
    @Test
    public void testCovarianceToCorrelationWithValidCovarianceMatrixData() {
        RealMatrix validCovarianceMatrix = new Array2DRowRealMatrix(new double[][]{
                {1, 0.5},
                {0.5, 1}
        });
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.866025403784439},
                {0.866025403784439, 1.0}
        });

        RealMatrix actualCorrelationMatrix = new PearsonsCorrelation().covarianceToCorrelation(validCovarianceMatrix);

        assertEquals(expectedCorrelationMatrix.toString(), actualCorrelationMatrix.toString());
    }

    /**
     * Test for covarianceToCorrelation method with null covariance matrix data.
     */
    @Test(expected = MathRuntimeException.class)
    public void testCovarianceToCorrelationWithNullCovarianceMatrixData() {
        new PearsonsCorrelation().covarianceToCorrelation(null);
    }
}