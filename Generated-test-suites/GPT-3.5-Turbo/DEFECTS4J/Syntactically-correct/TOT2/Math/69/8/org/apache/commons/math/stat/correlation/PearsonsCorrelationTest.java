import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructorWithoutData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
    }

    @Test
    public void testConstructorWithRectangularArray() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc.getCorrelationMatrix());
    }

    @Test
    public void testConstructorWithRealMatrix() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        assertNotNull(pc.getCorrelationMatrix());
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pc.computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double correlation = pc.correlation(xArray, yArray);
        assertEquals(1.0, correlation, 0.0001);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCorrelationWithInvalidInput() {
        double[] xArray = {1, 2};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix covarianceMatrix = pc.computeCovarianceMatrix(matrix);
        RealMatrix correlationMatrix = pc.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }

    // Additional tests for getCorrelationStandardErrors and getCorrelationPValues could be added as required

}