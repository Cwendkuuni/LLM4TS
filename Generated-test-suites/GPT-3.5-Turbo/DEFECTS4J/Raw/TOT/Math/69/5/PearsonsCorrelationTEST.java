import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.exception.MathException;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.stat.correlation.Covariance;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearsonsCorrelation;

    @Before
    public void setUp() {
        pearsonsCorrelation = new PearsonsCorrelation();
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(data);
        assertArrayEquals(expectedCorrelationMatrix.getData(), correlationMatrix.getData());
    }

    @Test
    public void testComputeCorrelationMatrix_InvalidDimensions() {
        double[][] data = {{1, 2, 3}, {4, 5}, {7, 8, 9}};
        try {
            pearsonsCorrelation.computeCorrelationMatrix(data);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double expectedCorrelation = 1.0;
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(expectedCorrelation, correlation, 0.0001);
    }

    @Test
    public void testCorrelation_InvalidDimensions() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        try {
            pearsonsCorrelation.correlation(xArray, yArray);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertArrayEquals(expectedCorrelationMatrix.getData(), correlationMatrix.getData());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertNotNull(correlationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        assertNotNull(correlationPValues);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceData = {{1, 2, 3}, {2, 5, 7}, {3, 7, 10}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertArrayEquals(expectedCorrelationMatrix.getData(), correlationMatrix.getData());
    }

    @Test
    public void testCovarianceToCorrelation_ZeroVariance() {
        double[][] covarianceData = {{1, 2, 3}, {2, 0, 7}, {3, 7, 0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
        try {
            pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }
}