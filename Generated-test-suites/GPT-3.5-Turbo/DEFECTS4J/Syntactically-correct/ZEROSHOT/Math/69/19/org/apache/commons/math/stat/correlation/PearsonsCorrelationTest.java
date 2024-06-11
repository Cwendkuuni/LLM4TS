import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        RealMatrix actual = pc.getCorrelationMatrix();
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double expected = 1.0;
        double actual = pc.correlation(xArray, yArray);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidArrayDimensions() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covMatrixData = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covMatrixData);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        RealMatrix actual = pc.covarianceToCorrelation(covarianceMatrix);
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
        RealMatrix actual = pc.getCorrelationStandardErrors();
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
        RealMatrix actual = pc.getCorrelationPValues();
        assertArrayEquals(expected.getData(), actual.getData());
    }
}