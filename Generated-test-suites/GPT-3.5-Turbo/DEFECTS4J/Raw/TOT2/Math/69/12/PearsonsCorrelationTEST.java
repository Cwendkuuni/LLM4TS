import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix result = pc.computeCorrelationMatrix(matrix);
        assertEquals(result.getRowDimension(), 2);
        assertEquals(result.getColumnDimension(), 2);
        assertEquals(result.getEntry(0, 0), 1.0, 0.0001);
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double result = pc.correlation(xArray, yArray);
        assertEquals(result, 1.0, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidLength() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0};
        pc.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix covMatrix = pc.computeCovarianceMatrix(matrix);
        RealMatrix result = pc.covarianceToCorrelation(covMatrix);
        assertEquals(result.getRowDimension(), 2);
        assertEquals(result.getColumnDimension(), 2);
        assertEquals(result.getEntry(0, 1), 1.0, 0.0001);
    }

    @Test
    public void testGetCorrelationMatrix() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        pc = new PearsonsCorrelation(matrix);
        RealMatrix result = pc.getCorrelationMatrix();
        assertEquals(result.getRowDimension(), 3);
        assertEquals(result.getColumnDimension(), 3);
        assertEquals(result.getEntry(0, 1), 1.0, 0.0001);
    }

}