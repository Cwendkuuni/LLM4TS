import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix result = correlation.getCorrelationMatrix();
        assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double result = correlation.correlation(xArray, yArray);
        assertEquals(1.0, result, 0.0001);
        // Add more assertions as needed
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix covarianceMatrix = correlation.getCorrelationMatrix();
        RealMatrix result = correlation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(result);
        // Add more assertions as needed
    }

    // Additional test cases for other methods can be added here

}