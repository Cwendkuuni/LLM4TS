import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearsonsCorrelation;
    private double[][] validData;
    private double[][] invalidData;

    @Before
    public void setup() {
        validData = new double[][]{{1, 2, 3, 4}, {2, 4, 6, 8}, {3, 6, 9, 12}};
        invalidData = new double[][]{{1, 2}, {2, 4, 6, 8}};
        pearsonsCorrelation = new PearsonsCorrelation(validData);
    }

    // Test computeCorrelationMatrix with valid data
    @Test
    public void testComputeCorrelationMatrixValidData() {
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(validData);
        double[][] expectedMatrix = new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        assertArrayEquals(expectedMatrix, correlationMatrix.getData());
    }

    // Test computeCorrelationMatrix with invalid data
    @Test(expected = MathRuntimeException.class)
    public void testComputeCorrelationMatrixInvalidData() {
        pearsonsCorrelation.computeCorrelationMatrix(invalidData);
    }

    // Test correlation with valid data
    @Test
    public void testCorrelationValidData() {
        double[] xArray = {1, 2, 3, 4};
        double[] yArray = {2, 4, 6, 8};
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        double expectedCorrelation = 1.0;
        assertEquals(expectedCorrelation, correlation, 0.0);
    }

    // Test correlation with invalid data
    @Test(expected = MathRuntimeException.class)
    public void testCorrelationInvalidData() {
        double[] xArray = {1, 2};
        double[] yArray = {2, 4, 6, 8};
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    // Test getCorrelationMatrix
    @Test
    public void testGetCorrelationMatrix() {
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        double[][] expectedMatrix = new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        assertArrayEquals(expectedMatrix, correlationMatrix.getData());
    }

    // Test getCorrelationStandardErrors
    @Test
    public void testGetCorrelationStandardErrors() {
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        double[][] expectedStandardErrors = new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};
        assertArrayEquals(expectedStandardErrors, correlationStandardErrors.getData());
    }

    // Test getCorrelationPValues
    @Test
    public void testGetCorrelationPValues() {
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        double[][] expectedPValues = new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};
        assertArrayEquals(expectedPValues, correlationPValues.getData());
    }

    // Test IllegalArgumentException in constructor with invalid data
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidData() {
        new PearsonsCorrelation(invalidData);
    }

    // Test IllegalArgumentException in computeCorrelationMatrix with insufficient data
    @Test(expected = MathRuntimeException.class)
    public void testComputeCorrelationMatrixInsufficientData() {
        double[][] insufficientData = new double[][]{{1, 2}, {2, 4}};
        pearsonsCorrelation.computeCorrelationMatrix(insufficientData);
    }

    // Test IllegalArgumentException in correlation with arrays of different lengths
    @Test(expected = MathRuntimeException.class)
    public void testCorrelationDifferentLengths() {
        double[] xArray = {1, 2, 3, 4};
        double[] yArray = {2, 4, 6};
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    // Test IllegalArgumentException in correlation with insufficient data
    @Test(expected = MathRuntimeException.class)
    public void testCorrelationInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {2};
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    // Test covarianceToCorrelation
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        double[][] expectedMatrix = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        assertArrayEquals(expectedMatrix, correlationMatrix.getData());
    }
}