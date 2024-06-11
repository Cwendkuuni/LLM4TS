import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PearsonsCorrelationTest {

    @Test
    public void testPearsonsCorrelationConstructorWithoutData() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertEquals(null, pearsonsCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testPearsonsCorrelationConstructorWithRectangularArray() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertEquals(1.0, pearsonsCorrelation.getCorrelationMatrix().getEntry(0, 0), 0.0001);
    }

    @Test
    public void testPearsonsCorrelationComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertEquals(1.0, pearsonsCorrelation.computeCorrelationMatrix(data).getEntry(0, 0), 0.0001);
    }

    @Test
    public void testPearsonsCorrelationCorrelation() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0};
        double[] yArray = {3.0, 4.0};
        assertEquals(1.0, pearsonsCorrelation.correlation(xArray, yArray), 0.0001);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testPearsonsCorrelationInvalidCorrelation() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[] xArray = {1.0};
        double[] yArray = {3.0, 4.0};
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testPearsonsCorrelationCovarianceToCorrelation() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[][] covarianceMatrixData = {{2.0, 1.0}, {1.0, 4.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixData);
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.0001);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testPearsonsCorrelationInsufficientData() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[][] data = {{1.0}};
        pearsonsCorrelation.checkSufficientData(new BlockRealMatrix(data));
    }
}