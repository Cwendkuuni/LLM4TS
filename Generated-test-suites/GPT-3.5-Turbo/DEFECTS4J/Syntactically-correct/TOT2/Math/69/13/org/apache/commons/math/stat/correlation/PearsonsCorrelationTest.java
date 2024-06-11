import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructorWithoutData() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNotNull(pearsonsCorrelation);
    }

    @Test
    public void testConstructorWithDoubleArrayData() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
    }

    // Add test cases for other constructors

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(1.0, correlation, 0.0001);
    }

    // Add more test cases for other methods

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix covarianceMatrix = new Covariance(matrix).getCovarianceMatrix();
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }
}