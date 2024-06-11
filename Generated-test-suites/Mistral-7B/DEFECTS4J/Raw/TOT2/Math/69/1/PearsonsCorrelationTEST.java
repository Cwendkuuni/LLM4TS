import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Pearson's Correlation.
 */
public class PearsonsCorrelationTest {

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new RealArray2D.Dense2DRowRegistration(new double[0][0]));
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(matrix);
        RealMatrix result = pearsonCorr.getCorrelationMatrix();
        Assert.assertNull(result);
    }

    @Test
    public void testIdentityMatrix() {
        double[][] data = {{1.0, 1.0}, {1.0, 1.0}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(inputMatrix);
        RealMatrix expectedResult = new BlockRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RealMatrix result = pearsonCorr.getCorrelationMatrix();
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testPositiveCorrelation() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(inputMatrix);
        RealMatrix expectedResult = new BlockRealMatrix(new double[][]{{0.866025403784, 0.866025403784}, {0.866025403784, 0.866025403784}});
        RealMatrix result = pearsonCorr.getCorrelationMatrix();
        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCorrelation() {
        double[][] data = {{1.0, -2.0, 3.0}, {4.0, -5.0, 6.0}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(inputMatrix);
    }

    @Test
    public void testStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(inputMatrix);
        RealMatrix expectedResult = new BlockRealMatrix(new double[][]{{0.498353397119, 0.498353397119}, {0.498353397119, 0.498353397119}});
        RealMatrix result = pearsonCorr.getCorrelationStandardErrors();
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(inputMatrix);
        RealMatrix expectedResult = new BlockRealMatrix(new double[][]{{0.153369280221, 0.153369280221}, {0.153369280221, 0.153369280221}});
        RealMatrix result = pearsonCorr.getCorrelationPValues();
        Assert.assertEquals(expectedResult, result);
    }
}