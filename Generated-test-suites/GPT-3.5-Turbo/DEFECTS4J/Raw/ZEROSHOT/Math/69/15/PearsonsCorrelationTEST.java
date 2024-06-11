import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

public class PearsonsCorrelationTest {
    private PearsonsCorrelation correlation;

    @Before
    public void setUp() {
        correlation = new PearsonsCorrelation();
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[][] expectedCorrelationMatrix = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        assertEquals(createMatrix(expectedCorrelationMatrix), correlation.computeCorrelationMatrix(data));
    }

    @Test
    public void testComputeCorrelationMatrix_SingleColumn() {
        double[][] data = {{1}, {2}, {3}};
        double[][] expectedCorrelationMatrix = {{1}};
        assertEquals(createMatrix(expectedCorrelationMatrix), correlation.computeCorrelationMatrix(data));
    }

    @Test
    public void testComputeCorrelationMatrix_NotEnoughData() {
        double[][] data = {{1, 2, 3}};
        try {
            correlation.computeCorrelationMatrix(data);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            String expected = "insufficient data: only 1 rows and 3 columns.";
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double expectedCorrelation = 1.0;
        assertEquals(expectedCorrelation, correlation.correlation(xArray, yArray), 0.001);
    }

    @Test
    public void testCorrelation_DifferentLengths() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        try {
            correlation.correlation(xArray, yArray);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            String expected = "invalid array dimensions. xArray has size 3; yArray has 2 elements";
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public void testCorrelation_InsufficientData() {
        double[] xArray = {1};
        double[] yArray = {2};
        try {
            correlation.correlation(xArray, yArray);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            String expected = "invalid array dimensions. xArray has size 1; yArray has 1 elements";
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrix = {{1, 2}, {3, 4}};
        double[][] expectedCorrelationMatrix = {{1, 1}, {1, 1}};
        assertEquals(createMatrix(expectedCorrelationMatrix), correlation.covarianceToCorrelation(createMatrix(covarianceMatrix)));
    }

    private RealMatrix createMatrix(double[][] data) {
        return new BlockRealMatrix(data);
    }
}