package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test class for {@link PearsonsCorrelation} class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test constructor without data.
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNotNull(pc);
        assertEquals(0, pc.getNObs());
        assertNull(pc.getCorrelationMatrix());
    }

    /**
     * Test constructor from a 2x2 double array.
     */
    @Test
    public void testTwoByTwoDataConstructor() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc);
        assertEquals(4, pc.getNObs());
        RealMatrix mat = pc.getCorrelationMatrix();
        assertNotNull(mat);
        assertEquals(2, mat.getRowDimension());
        assertEquals(2, mat.getColumnDimension());
        assertEquals(1.0, mat.getEntry(0, 0), 1e-14);
        assertEquals(-0.816496580927726, mat.getEntry(0, 1), 1e-14);
        assertEquals(-0.816496580927726, mat.getEntry(1, 0), 1e-14);
        assertEquals(1.0, mat.getEntry(1, 1), 1e-14);
    }

    /**
     * Test constructor from a RealMatrix.
     */
    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new RealArray2D(2, 3, new double[][]{{1.0, 2.0, 3.0}, {3.0, 4.0, 5.0}}));
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        assertNotNull(pc);
        assertEquals(6, pc.getNObs());
        RealMatrix corrMatrix = pc.getCorrelationMatrix();
        assertNotNull(corrMatrix);
        assertEquals(3, corrMatrix.getColumnDimension());
        assertEquals(3, corrMatrix.getRowDimension());
        assertEquals(1.0, corrMatrix.getEntry(0, 0), 1e-14);
        assertEquals(-0.788675442285333, corrMatrix.getEntry(0, 1), 1e-14);
        assertEquals(-0.832496582535557, corrMatrix.getEntry(0, 2), 1e-14);
        assertEquals(-0.788675442285333, corrMatrix.getEntry(1, 0), 1e-14);
        assertEquals(-0.832496582535557, corrMatrix.getEntry(1, 1), 1e-14);
        assertEquals(1.0, corrMatrix.getEntry(2, 2), 1e-14);
    }

    /**
     * Test constructor from a Covariance.
     */
    @Test
    public void testCovarianceConstructor() throws Exception {
        double[][] covarianceData = {{1.0, 1.0, 0.5}, {1.0, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        Covariance covariance = new Covariance(covarianceData);
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        assertNotNull(pc);
        assertEquals(3, pc.getNObs());
        RealMatrix corrMatrix = pc.getCorrelationMatrix();
        assertNotNull(corrMatrix);
        assertEquals(3, corrMatrix.getColumnDimension());
        assertEquals(3, corrMatrix.getRowDimension());
        assertEquals(1.0, corrMatrix.getEntry(0, 0), 1e-14);
        assertEquals(0.714285714285714, corrMatrix.getEntry(0, 1), 1e-14);
        assertEquals(0.714285714285714, corrMatrix.getEntry(1, 0), 1e-14);
        assertEquals(0.75, corrMatrix.getEntry(2, 2), 1e-14);
    }

    /**
     * Test method getCorrelationMatrix().
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix mat = pc.getCorrelationMatrix();
        assertNotNull(mat);
        assertEquals(2, mat.getRowDimension());
        assertEquals(2, mat.getColumnDimension());
    }

    /**
     * Test method getCorrelationStandardErrors().
     */
    @Test
    public void testGetCorrelationStandardErrors() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix stdErrs = pc.getCorrelationStandardErrors();
        assertNotNull(stdErrs);
        assertEquals(2, stdErrs.getRowDimension());
        assertEquals(2, stdErrs.getColumnDimension());
        assertEquals(0.5, stdErrs.getEntry(0, 0), 1e-14);
        assertEquals(0.5, stdErrs.getEntry(0, 1), 1e-14);
        assertEquals(0.5, stdErrs.getEntry(1, 0), 1e-14);
        assertEquals(0.5, stdErrs.getEntry(1, 1), 1e-14);
    }

    /**
     * Test method getCorrelationPValues().
     */
    @Test
    public void testGetCorrelationPValues() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix pValues = pc.getCorrelationPValues();
        assertNotNull(pValues);
        assertEquals(2, pValues.getRowDimension());
        assertEquals(2, pValues.getColumnDimension());
        assertEquals(1.0, pValues.getEntry(0, 0), 1e-14);
        assertEquals(1.0, pValues.getEntry(0, 1), 1e-14);
        assertEquals(1.0, pValues.getEntry(1, 0), 1e-14);
        assertEquals(1.0, pValues.getEntry(1, 1), 1e-14);
    }

    /**
     * Test method computeCorrelationMatrix(RealMatrix).
     */
    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix corrMatrix = pc.computeCorrelationMatrix(matrix);
        assertEquals(2, corrMatrix.getRowDimension());
        assertEquals(2, corrMatrix.getColumnDimension());
        assertEquals(1.0, corrMatrix.getEntry(0, 0), 1e-14);
        assertEquals(-0.816496580927726, corrMatrix.getEntry(0, 1), 1e-14);
        assertEquals(-0.816496580927726, corrMatrix.getEntry(1, 0), 1e-14);
        assertEquals(1.0, corrMatrix.getEntry(1, 1), 1e-14);
    }

    /**
     * Test method correlation(double[], double[]).
     */
    @Test
    public void testCorrelation() throws Exception {
        double[] xData = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yData = {2.0, 4.0, 6.0, 8.0, 10.0};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double correlation = pc.correlation(xData, yData);
        assertEquals(0.780974561253276, correlation, 1e-14);
    }

    /**
     * Test method covarianceToCorrelation(RealMatrix).
     */
    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceData = {{1.0, 1.0, 0.5}, {1.0, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        RealMatrix covarianceMatrix = new Covariance(covarianceData).getCovarianceMatrix();
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix corrMatrix = pc.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(corrMatrix);
        assertEquals(3, corrMatrix.getColumnDimension());
        assertEquals(3, corrMatrix.getRowDimension());
        assertEquals(1.0, corrMatrix.getEntry(0, 0), 1e-14);
        assertEquals(0.714285714285714, corrMatrix.getEntry(0, 1), 1e-14);
        assertEquals(0.714285714285714, corrMatrix.getEntry(1, 0), 1e-14);
        assertEquals(0.75, corrMatrix.getEntry(2, 2), 1e-14);
    }

    /**
     * Test method checkSufficientData(RealMatrix).
     */
    @Test(expected = MathRuntimeException.class)
    public void testInsufficientData() {
        double[][] data = {{1.0, 2.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.checkSufficientData(new BlockRealMatrix(data));
    }
}