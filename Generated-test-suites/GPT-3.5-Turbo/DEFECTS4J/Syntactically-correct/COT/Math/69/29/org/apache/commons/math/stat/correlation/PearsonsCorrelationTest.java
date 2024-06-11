
import org.junit.Test;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.stat.regression.SimpleRegression;

import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[][] expected = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        double[][] actual = correlation.computeCorrelationMatrix(data).getData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double expected = 1.0;
        double actual = correlation.correlation(x, y);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidDimensions() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] x = {1, 2, 3};
        double[] y = {4, 5};
        correlation.correlation(x, y);
    }

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] covarianceMatrix = {{2, 1}, {1, 2}};
        double[][] expected = {{1, 0.5}, {0.5, 1}};
        double[][] actual = correlation.covarianceToCorrelation(new BlockRealMatrix(covarianceMatrix)).getData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCorrelationMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[][] expected = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        correlation = new PearsonsCorrelation(data);
        double[][] actual = correlation.getCorrelationMatrix().getData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[][] expected = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        correlation = new PearsonsCorrelation(data);
        double[][] actual = correlation.getCorrelationStandardErrors().getData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCorrelationPValues() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[][] expected = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        correlation = new PearsonsCorrelation(data);
        double[][] actual = correlation.getCorrelationPValues().getData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testConstructorWithRealMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        assertNotNull(correlation);
    }

    @Test
    public void testConstructorWithCovariance() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        Covariance covariance = new Covariance(matrix);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
        assertNotNull(correlation);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNullCovariance() {
        Covariance covariance = new Covariance();
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
    }

    @Test
    public void testConstructorWithCovarianceMatrix() {
        double[][] covarianceMatrix = {{2, 1}, {1, 2}};
        RealMatrix matrix = new BlockRealMatrix(covarianceMatrix);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix, 3);
        assertNotNull(correlation);
    }

    @Test
    public void testGetNobs() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        int expected = 3;
        int actual = correlation.getNobs();
        assertEquals(expected, actual);
    }
}

