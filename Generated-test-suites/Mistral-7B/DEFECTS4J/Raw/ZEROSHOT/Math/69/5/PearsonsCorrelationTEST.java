import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yArray = {2.0, 4.0, 6.0, 8.0, 10.0};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(0.8, correlation, 0.01);
    }

    @Test
    public void testComputationCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.82283361, 0.83209855}, {0.82283361, 1.0, 0.82283361}, {0.83209855, 0.82283361, 1.0}});
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    @Test
    public void testComputationCorrelationMatrixFromCovariance() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN());
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.82283361, 0.83209855}, {0.82283361, 1.0, 0.82283361}, {0.83209855, 0.82283361, 1.0}});
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    @Test
    public void testComputationCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        RealMatrix expectedCorrelationPValues = new BlockRealMatrix(new double[][]{{1.0, 0.0556, 0.0556}, {0.0556, 1.0, 0.0556}, {0.0556, 0.0556, 1.0}});
        Assert.assertEquals(expectedCorrelationPValues, correlationPValues);
    }

    @Test
    public void testComputationCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        RealMatrix expectedCorrelationStandardErrors = new BlockRealMatrix(new double[][]{{0.44721361, 0.44721361, 0.44721361}, {0.44721361, 0.44721361, 0.44721361}, {0.44721361, 0.44721361, 0.44721361}});
        Assert.assertEquals(expectedCorrelationStandardErrors, correlationStandardErrors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsufficientData() {
        double[] xArray = {1.0};
        double[] yArray = {2.0};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSquareMatrix() {
        double[][] data = {{1.0, 2.0}, {4.0, 5.0}, {7.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.computeCorrelationMatrix(data);
    }
}