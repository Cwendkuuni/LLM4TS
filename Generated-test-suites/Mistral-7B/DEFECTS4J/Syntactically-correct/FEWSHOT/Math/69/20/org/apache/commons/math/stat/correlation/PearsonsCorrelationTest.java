package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.stat.regression.SimpleRegression;

public class PearsonsCorrelationTest {

    @Test
    public void testCreateInstanceWithoutData() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonsCorrelation.nObs);
    }

    @Test
    public void testCreateInstanceFromDataArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    @Test
    public void testCreateInstanceFromRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInstanceFromInvalidRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}});
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testCreateInstanceFromCovariance() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(new Covariance(covarianceMatrix));
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        RealMatrix correlationMatrix = new PearsonsCorrelation(matrix).computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, correlationMatrix.getEntry(0, 0));
        assertEquals(0.963357521303206, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.935572092235716, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.963357521303206, correlationMatrix.getEntry(1, 0));
        assertEquals(0.935572092235716, correlationMatrix.getEntry(1, 1));
        assertEquals(0.949272835319027, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.949272835319027, correlationMatrix.getEntry(2, 1));
        assertEquals(1.0, correlationMatrix.getEntry(2, 2));
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidData1() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {4.0, 5.0, 6.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidData2() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0));
        assertEquals(0.963357521303206, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.935572092235716, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.963357521303206, correlationMatrix.getEntry(1, 0));
        assertEquals(0.935572092235716, correlationMatrix.getEntry(1, 1));
        assertEquals(0.949272835319027, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.949272835319027, correlationMatrix.getEntry(2, 1));
        assertEquals(1.0, correlationMatrix.getEntry(2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}});
        new PearsonsCorrelation().checkSufficientData(matrix);
    }
}