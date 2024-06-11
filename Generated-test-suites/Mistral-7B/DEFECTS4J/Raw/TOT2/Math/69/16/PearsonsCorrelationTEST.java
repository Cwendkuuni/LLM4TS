import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructorNullData() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonsCorrelation.nObs);
    }

    @Test
    public void testConstructorEmptyData() {
        double data[][] = {};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonsCorrelation.nObs);
    }

    @Test
    public void testConstructorSingleColumnData() {
        double data[][] = {{1}, {2}, {3}, {4}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(1, correlationMatrix.getRowDimension());
        assertEquals(1, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-9);
    }

    @Test
    public void testConstructorRectangularData() {
        double data[][] = {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-9);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-9);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-9);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-9);
    }

    @Test
    public void testConstructorCovariance() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix covarianceMatrix = new SimpleRegression().covariance(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, data.length);
        assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-9);
        assertEquals(0.8157651, correlationMatrix.getEntry(0, 1), 1e-9);
        assertEquals(0.7836357, correlationMatrix.getEntry(0, 2), 1e-9);
        assertEquals(0.8157651, correlationMatrix.getEntry(1, 0), 1e-9);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-9);
        assertEquals(0.7836357, correlationMatrix.getEntry(1, 2), 1e-9);
        assertEquals(0.8157651, correlationMatrix.getEntry(2, 0), 1e-9);
        assertEquals(1.0, correlationMatrix.getEntry(2, 2), 1e-9);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double data[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-9);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-9);
        assertEquals(0.7, correlationMatrix.getEntry(0, 2), 1e-9);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-9);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-9);
        assertEquals(0.7, correlationMatrix.getEntry(1, 2), 1e-9);
        assertEquals(0.7, correlationMatrix.getEntry(2, 0), 1e-9);
        assertEquals(1.0, correlationMatrix.getEntry(2, 1), 1e-9);
        assertEquals(1.0, correlationMatrix.getEntry(2, 2), 1e-9);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double data[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix stdErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertEquals(3, stdErrors.getRowDimension());
        assertEquals(3, stdErrors.getColumnDimension());
        assertEquals(0.4283266, stdErrors.getEntry(0, 0), 1e-9);
        assertEquals(0.3664554, stdErrors.getEntry(0, 1), 1e-9);
        assertEquals(0.4283266, stdErrors.getEntry(0, 2), 1e-9);
        assertEquals(0.3664554, stdErrors.getEntry(1, 0), 1e-9);
        assertEquals(0.0, stdErrors.getEntry(1, 1), 1e-9);
        assertEquals(0.4283266, stdErrors.getEntry(1, 2), 1e-9);
        assertEquals(0.0, stdErrors.getEntry(2, 0), 1e-9);
        assertEquals(0.0, stdErrors.getEntry(2, 1), 1e-9);
        assertEquals(0.0, stdErrors.getEntry(2, 2), 1e-9);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double data[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix pValues = pearsonsCorrelation.getCorrelationPValues();
        assertEquals(3, pValues.getRowDimension());
        assertEquals(3, pValues.getColumnDimension());
        assertEquals(0.5578735, pValues.getEntry(0, 0), 1e-9);
        assertEquals(0.1576343, pValues.getEntry(0, 1), 1e-9);
        assertEquals(0.1576343, pValues.getEntry(0, 2), 1e-9);
        assertEquals(0.1576343, pValues.getEntry(1, 0), 1e-9);
        assertEquals(1.0, pValues.getEntry(1, 1), 1e-9);
        assertEquals(0.1576343, pValues.getEntry(1, 2), 1e-9);
        assertEquals(1.0, pValues.getEntry(2, 0), 1e-9);
        assertEquals(1.0, pValues.getEntry(2, 1), 1e-9);
        assertEquals(1.0, pValues.getEntry(2, 2), 1e-9);
    }

    @Test
    public void testCorrelation() {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {2, 4, 6, 8, 10};
        double correlation = PearsonsCorrelation.correlation(x, y);
        assertEquals(0.8, correlation, 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidDataSize() {
        double[] x = {1};
        double[] y = {2, 3};
        PearsonsCorrelation.correlation(x, y);
    }

    @Test
    public void testCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix correlationMatrix = new PearsonsCorrelation(data).computeCorrelationMatrix(data);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new RealArray2D(3, 3,
                new double[][]{{1.0, 0.8, 0.7}, {0.8, 1.0, 0.7}, {0.7, 0.7, 1.0}}));
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), correlationMatrix.getData(), 1e-9);
    }
}