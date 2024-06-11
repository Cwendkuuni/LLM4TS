package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test class for {@link PearsonsCorrelation}
 */
public class PearsonsCorrelationTest {

    /**
     * Test constructor without data
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNotNull(pc);
        assertEquals(0, pc.getNObs());
        assertNull(pc.getCorrelationMatrix());
    }

    /**
     * Test constructor with double array data
     */
    @Test
    public void testDoubleArrayConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {3.0, 2.0, 1.0}, {2.0, 1.0, 3.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc);
        assertEquals(3, pc.getNObs());
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.86602540378444},
                {0.86602540378444, 1.0}});
        RealMatrix actual = pc.getCorrelationMatrix();
        assertNotNull(actual);
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.000001);
    }

    /**
     * Test constructor with RealMatrix data
     */
    @Test
    public void testRealMatrixConstructor() {
        RealMatrix data = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                   {3.0, 2.0, 1.0},
                                                                   {2.0, 1.0, 3.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc);
        assertEquals(3, pc.getNObs());
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.86602540378444},
                {0.86602540378444, 1.0}});
        RealMatrix actual = pc.getCorrelationMatrix();
        assertNotNull(actual);
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.000001);
    }

    /**
     * Test correlation method with double arrays
     */
    @Test
    public void testCorrelationDoubleArray() {
        double[] xData = {1.0, 2.0, 3.0};
        double[] yData = {3.0, 2.0, 1.0};
        double correlation = PearsonsCorrelation.correlation(xData, yData);
        assertEquals(0.86602540378444, correlation, 0.0001);
    }

    /**
     * Test getCorrelationStandardErrors method
     */
    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {3.0, 2.0, 1.0}, {2.0, 1.0, 3.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        assertEquals(correlationMatrix.getRowDimension(), correlationStandardErrors.getRowDimension());
        assertEquals(correlationMatrix.getColumnDimension(), correlationStandardErrors.getColumnDimension());
        for (int i = 0; i < correlationMatrix.getRowDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                double corr = correlationMatrix.getEntry(i, j);
                double stdErr = correlationStandardErrors.getEntry(i, j);
                assertTrue(stdErr > 0);
                double expected = Math.sqrt((1 - corr * corr) / (pc.getNObs() - 2));
                assertEquals(expected, stdErr, 0.0001);
            }
        }
    }

    /**
     * Test getCorrelationPValues method
     * @throws MathException
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {3.0, 2.0, 1.0}, {2.0, 1.0, 3.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        assertEquals(correlationMatrix.getRowDimension(), correlationPValues.getRowDimension());
        assertEquals(correlationMatrix.getColumnDimension(), correlationPValues.getColumnDimension());
        for (int i = 0; i < correlationMatrix.getRowDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                double corr = correlationMatrix.getEntry(i, j);
                double pValue = correlationPValues.getEntry(i, j);
                assert(pValue > 0);
                double tValue = Math.abs(corr) * Math.sqrt((pc.getNObs() - 2) / (1 - corr * corr));
                double expected = 2 * (1 - new TDistributionImpl(pc.getNObs() - 2).cumulativeProbability(tValue));
                assertEquals(expected, pValue, 0.0001);
            }
        }
    }

    /**
     * Test covarianceToCorrelation method
     */
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.5, 0.5, 0.5},
                {0.5, 2.0, 0.5}, {0.5, 0.5, 1.5}});
        RealMatrix correlationMatrix = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.577350269189626},
                {0.577350269189626, 1.0}});
        assertEquals(expected.getRowDimension(), correlationMatrix.getRowDimension());
        assertEquals(expected.getColumnDimension(), correlationMatrix.getColumnDimension());
        assertArrayEquals(expected.getData(), correlationMatrix.getData(), 0.000001);
    }
}