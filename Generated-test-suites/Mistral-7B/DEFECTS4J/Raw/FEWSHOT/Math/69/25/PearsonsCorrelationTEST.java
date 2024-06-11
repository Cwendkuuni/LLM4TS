package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

public class PearsonsCorrelationTest {

    @Test
    public void testDefaultConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.correlationMatrix);
        assertEquals(0, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDataArrayConstructor() {
        new PearsonsCorrelation(new double[0][]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyRealMatrixConstructor() {
        new PearsonsCorrelation(new RealMatrix());
    }

    @Test
    public void testRectangularArrayConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc.correlationMatrix);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        assertNotNull(pc.correlationMatrix);
        assertEquals(2, pc.nObs);
    }

    @Test
    public void testCovarianceConstructor() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(new Covariance(covarianceMatrix, 3));
        assertNotNull(pc.correlationMatrix);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testCovarianceMatrixConstructor() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        assertNotNull(pc.correlationMatrix);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        assertNotNull(correlationStandardErrors);
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
    }

    @Test(expected = MathException.class)
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        assertNotNull(correlationPValues);
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        // MathException is expected as this test doesn't actually check the values
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RealMatrix correlationMatrix = new PearsonsCorrelation(matrix).getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8284275362318844, 0.8660254037844385},
                {0.8284275362318844, 1.0, 0.8660254037844385},
                {0.8660254037844385, 0.8660254037844385, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 4.0, 6.0};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationArrays() {
        double[] xArray = {1.0};
        double[] yArray = {2.0, 4.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }
}