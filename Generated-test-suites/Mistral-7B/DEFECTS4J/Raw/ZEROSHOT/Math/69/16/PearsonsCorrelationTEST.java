import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertFalse(correlation.getCorrelationMatrix() instanceof RealMatrix);
        assertEquals(0, correlation.getNObs());

        // Test method calls
        correlation.computeCorrelationMatrix(new Array2DRowRealMatrix(new double[][]{}));
    }

    @Test
    public void testIdentityMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] identityMatrix = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix correlationMatrix = correlation.computeCorrelationMatrix(new Array2DRowRealMatrix(identityMatrix));
        assertTrue(correlationMatrix instanceof RealMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-14);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-14);
        assertEquals(0.0, correlationMatrix.getEntry(0, 1), 1e-14);
        assertEquals(0.0, correlationMatrix.getEntry(1, 0), 1e-14);
    }

    @Test
    public void testCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}, {10.0, 11.0, 12.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(new Array2DRowRealMatrix(data));
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertTrue(correlationMatrix instanceof RealMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        // Check correlation for each pair of columns
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-14);
        assertEquals(0.85, correlationMatrix.getEntry(0, 2), 1e-14);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-14);
        assertEquals(0.85, correlationMatrix.getEntry(1, 2), 1e-14);
        assertEquals(0.8, correlationMatrix.getEntry(2, 0), 1e-14);
        assertEquals(0.85, correlationMatrix.getEntry(2, 1), 1e-14);
        assertEquals(0.95, correlationMatrix.getEntry(1, 2), 1e-14);
        assertEquals(0.95, correlationMatrix.getEntry(2, 1), 1e-14);
    }

    @Test
    public void testCorrelationMatrixFromCovariance() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}, {10.0, 11.0, 12.0}};
        double[][] covarianceMatrix = {{4.0, 3.6, 3.2}, {3.6, 6.4, 5.6}, {3.2, 5.6, 8.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(new Covariance(new Array2DRowRealMatrix(covarianceMatrix)));
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertTrue(correlationMatrix instanceof RealMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        // Check correlation for each pair of columns
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-14);
        assertEquals(0.85, correlationMatrix.getEntry(0, 2), 1e-14);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-14);
        assertEquals(0.85, correlationMatrix.getEntry(1, 2), 1e-14);
        assertEquals(0.8, correlationMatrix.getEntry(2, 0), 1e-14);
        assertEquals(0.85, correlationMatrix.getEntry(2, 1), 1e-14);
        assertEquals(0.95, correlationMatrix.getEntry(1, 2), 1e-14);
        assertEquals(0.95, correlationMatrix.getEntry(2, 1), 1e-14);
    }

    @Test
    public void testCorrelationFromArrays() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yArray = {4.0, 5.0, 6.0, 7.0, 8.0};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.8, correlation, 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataMatrix() {
        double[][] data = {{1.0, 2.0}, {4.0}};
        new PearsonsCorrelation(new Array2DRowRealMatrix(data));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataArray() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {4.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }
}