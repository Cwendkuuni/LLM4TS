import org.apache.commons.math.MathException;
import org.apache.commons.math.linear.*;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearsonsCorrelation;

    @Before
    public void setup() {
        pearsonsCorrelation = new PearsonsCorrelation();
    }

    // Test computeCorrelationMatrix method with valid input
    @Test
    public void testComputeCorrelationMatrix_ValidInput_ReturnsCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix expected = new BlockRealMatrix(new double[][]{
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0}
        });

        RealMatrix result = pearsonsCorrelation.computeCorrelationMatrix(data);

        Assert.assertEquals(expected, result);
    }

    // Test computeCorrelationMatrix method with insufficient data
    @Test(expected = IllegalArgumentException.class)
    public void testComputeCorrelationMatrix_InsufficientData_ThrowsIllegalArgumentException() {
        double[][] data = {{1, 2}, {3, 4}};
        pearsonsCorrelation.computeCorrelationMatrix(data);
    }

    // Test correlation method with valid input
    @Test
    public void testCorrelation_ValidInput_ReturnsPearsonsCorrelationCoefficient() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double expected = 1.0;

        double result = pearsonsCorrelation.correlation(xArray, yArray);

        Assert.assertEquals(expected, result, 0.0001);
    }

    // Test correlation method with invalid input (different array lengths)
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelation_ArraysOfDifferentLengths_ThrowsIllegalArgumentException() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};

        pearsonsCorrelation.correlation(xArray, yArray);
    }

    // Test correlation method with invalid input (insufficient data)
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelation_InsufficientData_ThrowsIllegalArgumentException() {
        double[] xArray = {1};
        double[] yArray = {2};

        pearsonsCorrelation.correlation(xArray, yArray);
    }

    // Test covarianceToCorrelation method with valid input
    @Test
    public void testCovarianceToCorrelation_ValidInput_ReturnsCorrelationMatrix() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{
                {4.0, 2.0},
                {2.0, 1.0}
        });
        RealMatrix expected = new BlockRealMatrix(new double[][]{
                {1.0, 0.8165},
                {0.8165, 1.0}
        });

        RealMatrix result = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);

        Assert.assertEquals(expected, result);
    }

    // Test getCorrelationStandardErrors method
    @Test
    public void testGetCorrelationStandardErrors_ReturnsMatrixWithStandardErrors() {
        RealMatrix correlationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.8165},
                {0.8165, 1.0}
        });
        RealMatrix expected = new BlockRealMatrix(new double[][]{
                {0.0, 0.4082},
                {0.4082, 0.0}
        });

        pearsonsCorrelation = new PearsonsCorrelation(correlationMatrix);
        RealMatrix result = pearsonsCorrelation.getCorrelationStandardErrors();

        Assert.assertEquals(expected, result);
    }

    // Test getCorrelationPValues method
    @Test
    public void testGetCorrelationPValues_ReturnsMatrixWithPValues() throws MathException {
        RealMatrix correlationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.8165},
                {0.8165, 1.0}
        });
        RealMatrix expected = new BlockRealMatrix(new double[][]{
                {0.0, 0.3454},
                {0.3454, 0.0}
        });

        pearsonsCorrelation = new PearsonsCorrelation(correlationMatrix);
        RealMatrix result = pearsonsCorrelation.getCorrelationPValues();

        Assert.assertEquals(expected, result);
    }

    // Test getCorrelationMatrix method
    @Test
    public void testGetCorrelationMatrix_ReturnsCorrelationMatrix() {
        RealMatrix correlationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.8165},
                {0.8165, 1.0}
        });

        pearsonsCorrelation = new PearsonsCorrelation(correlationMatrix);
        RealMatrix result = pearsonsCorrelation.getCorrelationMatrix();

        Assert.assertEquals(correlationMatrix, result);
    }
}