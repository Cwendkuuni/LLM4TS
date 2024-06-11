import static org.junit.Assert.*;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructorNoData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNotNull(pc);
        assertNull(pc.getCorrelationMatrix());
    }

    @Test
    public void testConstructorFromArray() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc);
        assertNotNull(pc.getCorrelationMatrix());
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = pc.computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double correlation = pc.correlation(xArray, yArray);
        assertEquals(1.0, correlation, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidLength() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0};
        double[] yArray = {4.0, 5.0, 6.0};
        pc.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix covarianceMatrix = pc.computeCovarianceMatrix(matrix);
        RealMatrix correlationMatrix = pc.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }

}