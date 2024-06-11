import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new RealArray2D.Dense2D(0, 0));
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        Assert.assertNull(correlation.getCorrelationMatrix());
    }

    @Test
    public void testIdentityMatrix() {
        double[][] data = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        Assert.assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.001);
        Assert.assertEquals(1.0, correlationMatrix.getEntry(2, 2), 0.001);
        Assert.assertEquals(1.0, correlationMatrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(1.0, correlationMatrix.getEntry(1, 0), 0.001);
        Assert.assertEquals(1.0, correlationMatrix.getEntry(1, 2), 0.001);
        Assert.assertEquals(1.0, correlationMatrix.getEntry(2, 0), 0.001);
        Assert.assertEquals(1.0, correlationMatrix.getEntry(2, 1), 0.001);
    }

    @Test
    public void testNullData() {
        double[][] data = null;
        try {
            new PearsonsCorrelation(data);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyData() {
        double[][] data = {};
        try {
            new PearsonsCorrelation(data);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        RealMatrix correlationMatrix = new PearsonsCorrelation(new BlockRealMatrix(data)).getCorrelationMatrix();
        Assert.assertEquals(-0.7416573867739413d, correlationMatrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.8164965809277261d, correlationMatrix.getEntry(1, 2), 0.001);
        Assert.assertEquals(0.7416573867739413d, correlationMatrix.getEntry(1, 1), 0.001);
        Assert.assertEquals(-0.8164965809277261d, correlationMatrix.getEntry(2, 1), 0.001);
        Assert.assertEquals(0.4472136020599055d, correlationMatrix.getEntry(2, 2), 0.001);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        RealMatrix correlationMatrix = new PearsonsCorrelation(new BlockRealMatrix(data)).getCorrelationMatrix();
        RealMatrix correlationStandardErrors = new PearsonsCorrelation(new BlockRealMatrix(data)).getCorrelationStandardErrors();
        Assert.assertEquals(0.5574033739884067d, correlationStandardErrors.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.562157712731135d, correlationStandardErrors.getEntry(1, 0), 0.001);
        Assert.assertEquals(0.522978874799529d, correlationStandardErrors.getEntry(1, 1), 0.001);
        Assert.assertEquals(0.522978874799529d, correlationStandardErrors.getEntry(2, 1), 0.001);
        Assert.assertEquals(0.522978874799529d, correlationStandardErrors.getEntry(2, 2), 0.001);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1, 1.5, 2}, {1.5, 2, 2.5}, {2, 2.5, 3}};
        RealMatrix correlationMatrix = new PearsonsCorrelation(new BlockRealMatrix(data)).getCorrelationMatrix();
        RealMatrix correlationPValues = new PearsonsCorrelation(new BlockRealMatrix(data)).getCorrelationPValues();
        Assert.assertEquals(0.08422022063492548d, correlationPValues.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.015035776172291388d, correlationPValues.getEntry(1, 0), 0.001);
        Assert.assertEquals(0.24121031459508385d, correlationPValues.getEntry(1, 2), 0.001);
        Assert.assertEquals(0.24121031459508385d, correlationPValues.getEntry(2, 1), 0.001);
        Assert.assertEquals(0.9999999999999998d, correlationPValues.getEntry(2, 2), 0.001);
    }

    @Test
    public void testCorrelationWithCovariance() {
        double[][] data = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        Covariance covariance = new Covariance(data);
        RealMatrix correlationMatrix = new PearsonsCorrelation(covariance).getCorrelationMatrix();
        Assert.assertEquals(-0.7416573867739413d, correlationMatrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.8164965809277261d, correlationMatrix.getEntry(1, 2), 0.001);
        Assert.assertEquals(0.7416573867739413d, correlationMatrix.getEntry(1, 1), 0.001);
        Assert.assertEquals(-0.8164965809277261d, correlationMatrix.getEntry(2, 1), 0.001);
        Assert.assertEquals(0.4472136020599055d, correlationMatrix.getEntry(2, 2), 0.001);
    }

    @Test
    public void testCorrelationWithCovarianceAndNumberOfObservations() {
        double[][] data = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance.getCovarianceMatrix(), data.length);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        Assert.assertEquals(-0.7416573867739413d, correlationMatrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.8164965809277261d, correlationMatrix.getEntry(1, 2), 0.001);
        Assert.assertEquals(0.7416573867739413d, correlationMatrix.getEntry(1, 1), 0.001);
        Assert.assertEquals(-0.8164965809277261d, correlationMatrix.getEntry(2, 1), 0.001);
        Assert.assertEquals(0.4472136020599055d, correlationMatrix.getEntry(2, 2), 0.001);
    }
}