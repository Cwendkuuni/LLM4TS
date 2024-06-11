import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {
    private PearsonsCorrelation pearsonsCorrelation;

    @Test
    public void testConstructor() {
        pearsonsCorrelation = new PearsonsCorrelation();
        assertNotNull(pearsonsCorrelation);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        pearsonsCorrelation = new PearsonsCorrelation(data);
        double[][] expected = {{1.0, 1.0}, {1.0, 1.0}};
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        double[][] actual = correlationMatrix.getData();
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeCorrelationMatrixWithInsufficientData() {
        double[][] data = {{1, 2, 3}};
        pearsonsCorrelation = new PearsonsCorrelation(data);
    }

    @Test
    public void testCorrelation() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        pearsonsCorrelation = new PearsonsCorrelation();
        double expected = 1.0;
        double actual = pearsonsCorrelation.correlation(x, y);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidArrayLength() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5};
        pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.correlation(x, y);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix covarianceMatrix = ((PearsonsCorrelation) pearsonsCorrelation).getCorrelationMatrix();
        double[][] expected = {{1.0, 1.0}, {1.0, 1.0}};
        double[][] actual = covarianceMatrix.getData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        pearsonsCorrelation = new PearsonsCorrelation(data);
        double[][] expected = {{1.0, 1.0}, {1.0, 1.0}};
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        double[][] actual = correlationMatrix.getData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        pearsonsCorrelation = new PearsonsCorrelation(data);
        double[][] expected = {{0.0, 0.0}, {0.0, 0.0}};
        RealMatrix correlationStandardErrors = ((PearsonsCorrelation) pearsonsCorrelation).getCorrelationStandardErrors();
        double[][] actual = correlationStandardErrors.getData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
          double[][] data = {{1, 2, 3}, {4, 5, 6}};
          pearsonsCorrelation = new PearsonsCorrelation(data);
          double[][] expected = {{0.0, 0.0}, {0.0, 0.0}};
          RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
          double[][] actual = correlationPValues.getData();
          assertArrayEquals(expected, actual);
    }

    @Test(expected = MathException.class)
    public void testGetCorrelationPValuesWithError() throws MathException {
          double[][] data = {{1, 2, 3}, {4, 5, 6}};
          pearsonsCorrelation = new PearsonsCorrelation(data);
          pearsonsCorrelation.getCorrelationPValues();
    }
}