import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix result = pc.computeCorrelationMatrix(new BlockRealMatrix(data));
        
        double[][] expected = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        RealMatrix expectedMatrix = new BlockRealMatrix(expected);
        
        assertEquals(expectedMatrix, result);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {5, 4, 3, 2, 1};
        
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double result = pc.correlation(xArray, yArray);
        
        assertEquals(-1.0, result, 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidArrayLength() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {1, 2, 3, 4};
        
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.correlation(xArray, yArray);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {2};
        
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.correlation(xArray, yArray);
    }

}