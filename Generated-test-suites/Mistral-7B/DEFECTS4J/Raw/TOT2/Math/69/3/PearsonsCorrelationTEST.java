import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PearsonsCorrelation
 */
public class PearsonsCorrelationTest {

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation()}
     */
    @Test
    public void testEmptyConstructor() {
        // Create an instance using empty constructor
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        // Check that the correlation matrix and number of observations are null
        Assert.assertNull(correlation.getCorrelationMatrix());
        Assert.assertEquals(0, correlation.nObs);
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(double[][])}
     */
    @Test
    public void testConstructorWithArrayData() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8, 0.6},
                {0.8, 1.0, 0.8},
                {0.6, 0.8, 1.0}
        });
        RealMatrix actualCorrelationMatrix = correlation.getCorrelationMatrix();

        // Check that the correlation matrix and number of observations match the expected values
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), actualCorrelationMatrix.getData(), 0.0);
        Assert.assertEquals(3, correlation.nObs);
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(RealMatrix)}
     */
    @Test
    public void testConstructorWithMatrixData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 3.0, 4.0},
                {3.0, 4.0, 5.0},
                {4.0, 5.0, 6.0}
        });
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8, 0.6, 0.4},
                {0.8, 1.0, 0.8, 0.8},
                {0.6, 0.8, 1.0, 0.6},
                {0.4, 0.8, 0.6, 1.0}
        });
        RealMatrix actualCorrelationMatrix = correlation.getCorrelationMatrix();

        // Check that the correlation matrix and number of observations match the expected values
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), actualCorrelationMatrix.getData(), 0.0);
        Assert.assertEquals(4, correlation.nObs);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationMatrix()}
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8, 0.6},
                {0.8, 1.0, 0.8},
                {0.6, 0.8, 1.0}
        });
        RealMatrix actualCorrelationMatrix = correlation.getCorrelationMatrix();

        // Check that the correlation matrices are equal
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), actualCorrelationMatrix.getData(), 0.0);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationStandardErrors()}
     */
    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][]{
                {0.4, 0.4, 0.4},
                {0.4, 0.4, 0.4},
                {0.4, 0.4, 0.4}
        });
        RealMatrix actualCorrelationStandardErrors = correlation.getCorrelationStandardErrors();

        // Check that the correlation standard errors matrices are equal
        Assert.assertArrayEquals(expectedCorrelationStandardErrors.getData(), actualCorrelationStandardErrors.getData(), 0.0);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationPValues()}
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationPValues = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.9599, 0.9133},
                {0.9599, 1.0, 0.9599},
                {0.9133, 0.9599, 1.0}
        });
        RealMatrix actualCorrelationPValues = correlation.getCorrelationPValues();

        // Check that the correlation p-values matrices are equal
        Assert.assertArrayEquals(expectedCorrelationPValues.getData(), actualCorrelationPValues.getData(), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#correlation(double[], double[])}
     */
    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {1.5, 2.5, 3.5};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double expectedCorrelation = 0.8;
        double actualCorrelation = correlation.correlation(xArray, yArray);

        // Check that the correlation coefficients are equal
        Assert.assertEquals(expectedCorrelation, actualCorrelation, 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#covarianceToCorrelation(RealMatrix)}
     */
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.5},
                {1.5, 2.25}
        });
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8728},
                {0.8728, 1.0}
        });
        RealMatrix actualCorrelationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);

        // Check that the correlation matrices are equal
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), actualCorrelationMatrix.getData(), 0.0);
    }

    /**
     * Test method for {@link PearsonsCorrelation#checkSufficientData(RealMatrix)}
     */
    @Test(expected = MathRuntimeException.class)
    public void testCheckSufficientData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0}
        });
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.checkSufficientData(matrix);
    }
}