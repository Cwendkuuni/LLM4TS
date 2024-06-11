import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearsonsCorrelation;
    private double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

    @Before
    public void setUp() {
        pearsonsCorrelation = new PearsonsCorrelation(data);
    }

    @Test
    public void testGetCorrelationMatrix() {
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        Assert.assertNotNull(correlationStandardErrors);
        Assert.assertEquals(3, correlationStandardErrors.getRowDimension());
        Assert.assertEquals(3, correlationStandardErrors.getColumnDimension());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        Assert.assertNotNull(correlationPValues);
        Assert.assertEquals(3, correlationPValues.getRowDimension());
        Assert.assertEquals(3, correlationPValues.getColumnDimension());
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix testMatrix = new BlockRealMatrix(testData);
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(testMatrix);
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
    }

    @Test
    public void testComputeCorrelationMatrixWithArray() {
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(testData);
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
    }
    
    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        try {
            double correlation = pearsonsCorrelation.correlation(xArray, yArray);
            Assert.assertNotNull(correlation);
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidData() {
        double[] xArray = {1};
        double[] yArray = {2};
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCorrelationWithInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {2};
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix testMatrix = new BlockRealMatrix(testData);
        PearsonsCorrelation covariance = new PearsonsCorrelation(testMatrix);
        RealMatrix covarianceMatrix = covariance.getCorrelationMatrix();
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
    }

    @Test(expected = MathRuntimeException.class)
    public void testCovarianceToCorrelationWithNullMatrix() {
        RealMatrix nullMatrix = null;
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(nullMatrix);
    }

    @Test
    public void testCheckSufficientData() {
        try {
            pearsonsCorrelation.checkSufficientData(new BlockRealMatrix(data));
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = MathRuntimeException.class)
    public void testCheckSufficientDataWithInsufficientData() {
        double[][] insufficientData = {{1, 2, 3}};
        pearsonsCorrelation.checkSufficientData(new BlockRealMatrix(insufficientData));
    }
}