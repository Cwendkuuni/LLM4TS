import static org.junit.Assert.*;

import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        // Test case for computing correlation matrix
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pearsons = new PearsonsCorrelation(data);
        assertNotNull(pearsons.getCorrelationMatrix());
    }

    @Test
    public void testCorrelation() {
        // Test case for calculating correlation coefficient
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation pearsons = new PearsonsCorrelation();
        double correlation = pearsons.correlation(xArray, yArray);
        assertEquals(1.0, correlation, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidArrays() {
        // Test case for invalid array lengths in correlation calculation
        double[] xArray = {1.0, 2.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation pearsons = new PearsonsCorrelation();
        pearsons.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        // Test case for deriving correlation matrix from covariance matrix
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pearsons = new PearsonsCorrelation(data);
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = pearsons.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCorrelationInsufficientData() {
        // Test case for insufficient data in correlation calculation
        double[] xArray = {1.0};
        double[] yArray = {4.0};
        PearsonsCorrelation pearsons = new PearsonsCorrelation();
        pearsons.correlation(xArray, yArray);
    }
}