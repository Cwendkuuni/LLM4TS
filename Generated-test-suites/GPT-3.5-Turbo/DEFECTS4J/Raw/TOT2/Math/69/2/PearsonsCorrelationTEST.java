import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructorWithoutData() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNotNull(pearsonsCorrelation);
        assertEquals(0, pearsonsCorrelation.getCorrelationMatrix().getRowDimension());
    }

    @Test
    public void testConstructorFromRectangularArray() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation);
        assertEquals(1.0, pearsonsCorrelation.getCorrelationMatrix().getEntry(0, 1), 0.0001);
    }

    @Test
    public void testCorrelationForEqualLengthArrays() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(1.0, correlation, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationForUnequalLengthArrays() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testComputeCorrelationMatrixFromRealMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix standardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertNotNull(standardErrors);
    }

    @Test
    public void testGetCorrelationPValues() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        try {
            RealMatrix pValues = pearsonsCorrelation.getCorrelationPValues();
            assertNotNull(pValues);
        } catch (MathException e) {
            fail("Exception thrown during getCorrelationPValues");
        }
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix covarianceMatrix = new Covariance(data).getCovarianceMatrix();
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }

}