import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.MathException;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        assertEquals(expectedMatrix, pearsonsCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testComputeCorrelationMatrixWithSingleValue() {
        double[][] data = {{1}, {4}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0}});
        assertEquals(expectedMatrix, pearsonsCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testComputeCorrelationMatrixWithEmptyData() {
        double[][] data = {};
        try {
            PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double expectedCorrelation = 1.0;
        assertEquals(expectedCorrelation, pearsonsCorrelation.correlation(xArray, yArray), 0.0001);
    }

    @Test
    public void testCorrelationWithDifferentLengthArrays() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        try {
            pearsonsCorrelation.correlation(xArray, yArray);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCorrelationWithInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {4};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        try {
            pearsonsCorrelation.correlation(xArray, yArray);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCorrelationMatrixToCorrelation() {
        double[][] data = {{1, 2}, {3, 4}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix covarianceMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        assertEquals(expectedMatrix, correlationMatrix);
    }

    @Test
    public void testCorrelationMatrixToCorrelationWithEmptyMatrix() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix emptyMatrix = new Array2DRowRealMatrix(new double[][]{});
        try {
            pearsonsCorrelation.covarianceToCorrelation(emptyMatrix);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCorrelationMatrixToCorrelationWithInsufficientData() {
        double[][] data = {{1, 2}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix covarianceMatrix = pearsonsCorrelation.getCorrelationMatrix();
        try {
            pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

}