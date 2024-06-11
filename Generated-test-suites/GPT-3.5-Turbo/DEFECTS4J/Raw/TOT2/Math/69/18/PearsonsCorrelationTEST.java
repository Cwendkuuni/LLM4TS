import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix result = pc.computeCorrelationMatrix(matrix);

        assertNotNull(result);
        assertEquals(1.0, result.getEntry(0, 0), 0.0001);
        assertEquals(1.0, result.getEntry(1, 1), 0.0001);
        assertEquals(1.0, result.getEntry(0, 1), 0.0001);
        assertEquals(1.0, result.getEntry(1, 0), 0.0001);
    }

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix covarianceMatrix = pc.computeCorrelationMatrix(matrix);
        RealMatrix result = pc.covarianceToCorrelation(covarianceMatrix);

        assertNotNull(result);
        assertEquals(1.0, result.getEntry(0, 0), 0.0001);
        assertEquals(1.0, result.getEntry(1, 1), 0.0001);
        assertEquals(1.0, result.getEntry(0, 1), 0.0001);
        assertEquals(1.0, result.getEntry(1, 0), 0.0001);
    }

    @Test
    public void testCorrelationEdgeCases() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {3.0, 5.0, 7.0};
        
        double result = pc.correlation(xArray, yArray);

        assertNotNull(result);
        assertEquals(1.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidInput() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0};
        double[] yArray = {3.0, 4.0};

        pc.correlation(xArray, yArray);
    }
}