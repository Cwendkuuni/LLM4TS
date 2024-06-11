import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.MathException;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNotNull(correlation);
    }

    @Test
    public void testConstructorWithArray() {
        double[][] data = {{1.0, 2.0}, {2.0, 4.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.getCorrelationMatrix());
    }

    @Test
    public void testComputeCorrelationMatrixWithRealMatrix() {
        double[][] data = {{1.0, 2.0}, {2.0, 4.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.computeCorrelationMatrix(correlation.getCorrelationMatrix()));
    }

    @Test
    public void testComputeCorrelationMatrixWithArray() {
        double[][] data = {{1.0, 2.0}, {2.0, 4.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.computeCorrelationMatrix(data));
    }

    @Test
    public void testCorrelationWithValidArrays() throws MathException {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {3.0, 6.0, 9.0};
        double result = correlation.correlation(xArray, yArray);
        assertEquals(1.0, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidArrays() throws MathException {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0};
        double[] yArray = {3.0, 6.0, 9.0};
        correlation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0}, {2.0, 4.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.covarianceToCorrelation(correlation.getCorrelationMatrix()));
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0}, {2.0, 4.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.getCorrelationStandardErrors());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0}, {2.0, 4.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.getCorrelationPValues());
    }
}