import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearsonsCorrelation;
    private double[][] data;
    private RealMatrix matrix;
    
    @Before
    public void setUp() {
        data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        matrix = new BlockRealMatrix(data);
        pearsonsCorrelation = new PearsonsCorrelation(matrix);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] expected = new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(matrix);
        Assert.assertArrayEquals(expected, correlationMatrix.getData());
    }

    @Test
    public void testComputeCorrelationMatrix_InvalidData() {
        double[][] invalidData = new double[][]{{1, 2}, {3, 4}};
        RealMatrix invalidMatrix = new BlockRealMatrix(invalidData);
        
        try {
            pearsonsCorrelation.computeCorrelationMatrix(invalidMatrix);
            Assert.fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            Assert.assertEquals("insufficient data: only 2 rows and 2 columns.", e.getMessage());
        }
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double expected = 1.0;
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(expected, correlation, 0.0001);
    }

    @Test
    public void testCorrelation_DifferentLengths() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        
        try {
            pearsonsCorrelation.correlation(xArray, yArray);
            Assert.fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            Assert.assertEquals("invalid array dimensions. xArray has size 3; yArray has 2 elements", e.getMessage());
        }
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] expected = new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        Assert.assertArrayEquals(expected, correlationStandardErrors.getData());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] expected = new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        Assert.assertArrayEquals(expected, correlationPValues.getData());
    }

    @Test
    public void testGetCorrelationMatrix() {
        Assert.assertEquals(matrix, pearsonsCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testPearsonsCorrelation_Constructor1() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        Assert.assertNull(correlation.getCorrelationMatrix());
        Assert.assertEquals(0, correlation.getCorrelationStandardErrors().getColumnDimension());
        Assert.assertEquals(0, correlation.getCorrelationPValues().getColumnDimension());
    }

    @Test
    public void testPearsonsCorrelation_Constructor2() {
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expectedMatrix = new BlockRealMatrix(data);
        Assert.assertEquals(expectedMatrix, correlation.getCorrelationMatrix());
    }

    @Test
    public void testPearsonsCorrelation_Constructor3() {
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        Assert.assertEquals(matrix, correlation.getCorrelationMatrix());
    }

    @Test
    public void testPearsonsCorrelation_Constructor4() {
        // Covariance instance should be mocked for testing covarianceToCorrelation() method.
        covariance = mock(Covariance.class);
        when(covariance.getCovarianceMatrix()).thenReturn(matrix);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
        Assert.assertEquals(matrix, correlation.getCorrelationMatrix());
    }

    @Test
    public void testPearsonsCorrelation_Constructor5() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(3, 3);
        int numberOfObservations = 100;
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, numberOfObservations);
        Assert.assertEquals(covarianceMatrix, correlation.getCorrelationMatrix());
    }
}