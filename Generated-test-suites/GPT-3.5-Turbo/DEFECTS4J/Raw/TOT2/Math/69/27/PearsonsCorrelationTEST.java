import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix matrix = new BlockRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        RealMatrix result = correlation.computeCorrelationMatrix(matrix);
        assertEquals(1.0, result.getEntry(0, 0), 0.0001);
        assertEquals(1.0, result.getEntry(1, 1), 0.0001);
        assertEquals(1.0, result.getEntry(2, 2), 0.0001);
    }

    @Test
    public void testComputeCorrelationMatrixWithEmptyMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix matrix = new BlockRealMatrix(new double[][]{});
        try {
            correlation.computeCorrelationMatrix(matrix);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] xArray = {1, 2, 3};
        double[] yArray = {2, 4, 6};
        double result = correlation.correlation(xArray, yArray);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testCorrelationWithInvalidArrayDimensions() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] xArray = {1, 2, 3};
        double[] yArray = {2, 4};
        try {
            correlation.correlation(xArray, yArray);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{2, 1}, {1, 2}});
        RealMatrix result = correlation.covarianceToCorrelation(covarianceMatrix);
        assertEquals(1.0, result.getEntry(0, 0), 0.0001);
        assertEquals(1.0, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testCovarianceToCorrelationWithNullMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        try {
            correlation.covarianceToCorrelation(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetCorrelationMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        RealMatrix result = correlation.getCorrelationMatrix();
        assertEquals(1.0, result.getEntry(0, 0), 0.0001);
        assertEquals(1.0, result.getEntry(1, 1), 0.0001);
        assertEquals(1.0, result.getEntry(2, 2), 0.0001);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        PearsonsCorrelation correlation = new PearsonsCorrelation(new double[][]{{1, 2}, {3, 4}});
        RealMatrix result = correlation.getCorrelationStandardErrors();
        assertEquals(0.0, result.getEntry(0, 0), 0.0001);
        assertEquals(0.0, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testGetCorrelationPValues() {
        PearsonsCorrelation correlation = new PearsonsCorrelation(new double[][]{{1, 2}, {3, 4}});
        try {
            RealMatrix result = correlation.getCorrelationPValues();
            assertEquals(0.0, result.getEntry(0, 0), 0.0001);
        } catch (MathException e) {
            fail("MathException thrown unexpectedly");
        }
    }

    @Test
    public void testGetCorrelationPValuesWithMathException() {
        PearsonsCorrelation correlation = new PearsonsCorrelation(new double[][]{{1, 2}, {3, 4}});
        correlation = new PearsonsCorrelation(new double[][]{{1, 2}, {3, 4, 5}});
        try {
            correlation.getCorrelationPValues();
            fail("Expected MathException");
        } catch (MathException e) {
            assertTrue(true);
        }
    }
}