import static org.junit.Assert.*;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructorWithoutData() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNull(pearsonsCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testConstructorWithRectangularArray() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        assertTrue(correlation >= -1 && correlation <= 1);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceData = {{1.0, 0.5}, {0.5, 1.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testEdgeCasesAndExceptions() {
        // Test constructor with null input
        try {
            PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(null);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Test correlation with invalid array dimensions
        double[] xArray = {1.0};
        double[] yArray = {2.0, 3.0};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        try {
            pearsonsCorrelation.correlation(xArray, yArray);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
}