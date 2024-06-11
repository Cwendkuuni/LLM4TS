import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.MathException;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        
        assertNotNull(correlationMatrix);
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.0001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.0001);
        assertEquals(1.0, correlationMatrix.getEntry(0, 1), 0.0001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 0), 0.0001);
    }
    
    @Test
    public void testComputeCorrelationMatrixException() {
        double[][] data = {{1.0}, {2.0}};
        
        try {
            PearsonsCorrelation pc = new PearsonsCorrelation(data);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    
    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        
        double correlation = pc.correlation(xArray, yArray);
        
        assertEquals(1.0, correlation, 0.0001);
    }
    
    @Test
    public void testCorrelationException() {
        double[] xArray = {1.0};
        double[] yArray = {2.0, 3.0};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        
        try {
            pc.correlation(xArray, yArray);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    
    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceData = {{2.0, 1.0}, {1.0, 2.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        
        RealMatrix correlationMatrix = pc.covarianceToCorrelation(covarianceMatrix);
        
        assertNotNull(correlationMatrix);
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.0001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.0001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.0001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.0001);
    }
    
}