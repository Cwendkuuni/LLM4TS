import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();

        assertEquals(correlationMatrix.getRowDimension(), 3);
        assertEquals(correlationMatrix.getColumnDimension(), 3);
        // Add more specific assertions if needed
    }

    @Test
    public void testComputeCorrelationMatrix_withInsufficientData() {
        double[][] data = {{1}, {4}};
        boolean exceptionThrown = false;
        try {
            PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        
        // Add assertions for the expected correlation value
    }

    @Test
    public void testCorrelation_withInvalidData() {
        double[] xArray = {1};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        boolean exceptionThrown = false;
        try {
            double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    // Add more test cases for other methods including edge cases and exception handling scenarios

}