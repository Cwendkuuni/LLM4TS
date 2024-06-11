import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix result = pearsonsCorrelation.computeCorrelationMatrix(data);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double expected = 1.0;
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double result = pearsonsCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(expected, result, 0.0);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix result = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix result = pearsonsCorrelation.getCorrelationMatrix();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix result = pearsonsCorrelation.getCorrelationStandardErrors();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix result = pearsonsCorrelation.getCorrelationPValues();
        Assert.assertEquals(expected, result);
    }

}