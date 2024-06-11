import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PearsonsCorrelationTest {

    @Test
    public void testPearsonsCorrelationConstructor() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertEquals(null, correlation.getCorrelationMatrix());
        assertEquals(0, correlation.getCorrelationStandardErrors().getColumnDimension());
        assertEquals(0, correlation.getCorrelationPValues().getColumnDimension());
    }

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix matrix = new BlockRealMatrix(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
        RealMatrix correlationMatrix = correlation.computeCorrelationMatrix(matrix);
        assertEquals(correlationMatrix.getEntry(0, 0), 1.0, 0.0001);
        assertEquals(correlationMatrix.getEntry(0, 1), 1.0, 0.0001);
        assertEquals(correlationMatrix.getEntry(1, 0), 1.0, 0.0001);
        assertEquals(correlationMatrix.getEntry(1, 1), 1.0, 0.0001);
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double expected = 1.0;
        double result = correlation.correlation(xArray, yArray);
        assertEquals(expected, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidInput() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] xArray = {1.0};
        double[] yArray = {2.0, 3.0};
        correlation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][] {{1.0, 0.5}, {0.5, 1.0}});
        RealMatrix correlationMatrix = correlation.covarianceToCorrelation(covarianceMatrix);
        assertEquals(correlationMatrix.getEntry(0, 0), 1.0, 0.0001);
        assertEquals(correlationMatrix.getEntry(0, 1), 0.5, 0.0001);
        assertEquals(correlationMatrix.getEntry(1, 0), 0.5, 0.0001);
        assertEquals(correlationMatrix.getEntry(1, 1), 1.0, 0.0001);
    }
}