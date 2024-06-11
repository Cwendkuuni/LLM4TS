import static org.junit.Assert.*;

import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.stat.correlation.Covariance;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.junit.Before;
import org.junit.Test;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation correlation;
    private RealMatrix data;
    private RealMatrix covarianceMatrix;

    @Before
    public void setUp() {
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        data = MatrixUtils.createRealMatrix(testData);
        covarianceMatrix = MatrixUtils.createRealMatrix(new double[][]{{6, 6, 6}, {6, 6, 6}, {6, 6, 6}});
        correlation = new PearsonsCorrelation(data);
    }

    @Test
    public void testGetCorrelationMatrix() {
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertTrue(expectedMatrix.equals(correlationMatrix));
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        RealMatrix correlationStandardErrors = correlation.getCorrelationStandardErrors();
        assertTrue(expectedMatrix.equals(correlationStandardErrors));
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        RealMatrix correlationPValues = correlation.getCorrelationPValues();
        assertTrue(expectedMatrix.equals(correlationPValues));
    }

    @Test
    public void testComputeCorrelationMatrix() {
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        RealMatrix computedMatrix = correlation.computeCorrelationMatrix(data);
        assertTrue(expectedMatrix.equals(computedMatrix));
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        RealMatrix computedMatrix = correlation.covarianceToCorrelation(covarianceMatrix);
        assertTrue(expectedMatrix.equals(computedMatrix));
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};

        try {
            double correlationCoefficient = correlation.correlation(xArray, yArray);
            assertEquals(1.0, correlationCoefficient, 0.000001);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationUnequalArrays() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6, 7};

        correlation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {4};

        correlation.correlation(xArray, yArray);
    }
}
