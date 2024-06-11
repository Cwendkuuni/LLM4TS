import org.apache.commons.math.linear.BlockRealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();

        assertNotNull(correlationMatrix);
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.0001);
        // Add more assertions as needed
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};

        double correlationCoefficient = pearsonsCorrelation.correlation(xArray, yArray);

        assertEquals(1.0, correlationCoefficient, 0.0001);
        // Add more assertions as needed
    }

    // Add more test cases for other methods in PearsonsCorrelation class

}