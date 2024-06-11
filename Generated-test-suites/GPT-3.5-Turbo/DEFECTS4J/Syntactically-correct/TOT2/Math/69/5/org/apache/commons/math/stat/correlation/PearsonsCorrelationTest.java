import static org.junit.Assert.*;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {2, 4, 6, 8, 10};
        double expectedCorrelation = 1.0;
        assertEquals(expectedCorrelation, pc.correlation(xArray, yArray), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidArrayLength() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1, 2, 3};
        double[] yArray = {2, 4, 6, 8, 10};
        pc.correlation(xArray, yArray);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        pc.computeCorrelationMatrix(data);
        // Add assertions to check the correlation matrix calculation
    }

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        pc.covarianceToCorrelation(covarianceMatrix);
        // Add assertions to check the covariance to correlation conversion
    }

    // Add more test cases for other methods in PearsonsCorrelation class

}