import static org.junit.Assert.*;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(matrix);

        // Add assertions here to test the functionality of computeCorrelationMatrix method
        // For example:
        // assertEquals(expectedValue, correlationMatrix.getEntry(row, col), 0.0001);
    }

    // Add more test methods for other public methods in PearsonsCorrelation class

}