import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.stat.correlation.Covariance;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.junit.Before;
import org.junit.Test;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation correlation;

    @Before
    public void setUp() {
        correlation = new PearsonsCorrelation();
    }

    @Test
    public void testConstructor() {
        assertNotNull(correlation);
    }

    @Test
    public void testConstructorWithMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation);
    }

    @Test
    public void testConstructorWithRealMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        correlation = new PearsonsCorrelation(new BlockRealMatrix(data));
        assertNotNull(correlation);
    }

    @Test
    public void testConstructorWithCovariance() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Covariance covariance = new Covariance(new BlockRealMatrix(data));
        correlation = new PearsonsCorrelation(covariance);
        assertNotNull(correlation);
    }

    @Test
    public void testConstructorWithCovarianceMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        correlation = new PearsonsCorrelation(covarianceMatrix, 3);
        assertNotNull(correlation);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.getCorrelationMatrix());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.getCorrelationStandardErrors());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.getCorrelationPValues());
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.computeCorrelationMatrix(new BlockRealMatrix(data)));
    }

    @Test
    public void testComputeCorrelationMatrixWithDoubleMatrixData() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.computeCorrelationMatrix(data));
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        assertEquals(new SimpleRegression().getR(), correlation.correlation(xArray, yArray), 0);
    }

}