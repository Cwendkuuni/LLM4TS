import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {
    
    private PearsonsCorrelation correlation;

    @Before
    public void setUp() {
        correlation = new PearsonsCorrelation();
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = correlation.computeCorrelationMatrix(data);
        double[][] expected = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        assertArrayEquals(expected, matrix.getData());
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double result = correlation.correlation(xArray, yArray);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = correlation.covarianceToCorrelation(covarianceMatrix);
        double[][] expected = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        assertArrayEquals(expected, correlationMatrix.getData());
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        correlation = new PearsonsCorrelation(data);
        RealMatrix matrix = correlation.getCorrelationMatrix();
        double[][] expected = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        assertArrayEquals(expected, matrix.getData());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        correlation = new PearsonsCorrelation(data);
        RealMatrix standardErrors = correlation.getCorrelationStandardErrors();
        double[][] expected = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};
        assertArrayEquals(expected, standardErrors.getData());
    }

    @Test
    public void testGetCorrelationPValues() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        correlation = new PearsonsCorrelation(data);
        RealMatrix pValues = correlation.getCorrelationPValues();
        double[][] expected = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        assertArrayEquals(expected, pValues.getData());
    }
}