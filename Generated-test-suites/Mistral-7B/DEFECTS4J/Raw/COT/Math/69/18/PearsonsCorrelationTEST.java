package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.TDistributionImpl;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import org.junit.Test;

/**
 * Test class for PearsonsCorrelation
 */
public class PearsonsCorrelationTest {

    /**
     * Test constructor without data
     */
    @Test
    public void testConstructorWithoutData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.correlationMatrix);
        assertEquals(0, pc.nObs);
    }

    /**
     * Test double array constructor
     */
    @Test
    public void testConstructorWithDoubleArray() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][] { { 1.0, 0.819152, 0.651855 },
                { 0.819152, 1.0, 0.819152 }, { 0.651855, 0.819152, 1.0 } });
        assertEquals(expected, pc.correlationMatrix);
        assertEquals(3, pc.nObs);
    }

    /**
     * Test RealMatrix constructor
     */
    @Test
    public void testConstructorWithRealMatrix() {
        RealMatrix matrix = new BlockRealMatrix(new double[][] { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 },
                { 3.0, 4.0, 5.0 } });
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix expected = new BlockRealMatrix(new double[][] { { 1.0, 0.819152, 0.651855 },
                { 0.819152, 1.0, 0.819152 }, { 0.651855, 0.819152, 1.0 } });
        assertEquals(expected, pc.correlationMatrix);
        assertEquals(3, pc.nObs);
    }

    /**
     * Test Covariance constructor
     */
    @Test
    public void testConstructorWithCovariance() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][] { { 2.5, 1.5, 1.5 }, { 1.5, 2.5, 1.5 },
                { 1.5, 1.5, 2.5 } });
        Covariance covariance = new Covariance(covarianceMatrix);
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        RealMatrix expected = new BlockRealMatrix(new double[][] { { 1.0, 0.819152, 0.651855 },
                { 0.819152, 1.0, 0.819152 }, { 0.651855, 0.819152, 1.0 } });
        assertEquals(expected, pc.correlationMatrix);
        assertEquals(3, pc.nObs);
    }

    /**
     * Test RealMatrix constructor with standard deviations
     */
    @Test
    public void testConstructorWithRealMatrixAndStandardDeviations() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][] { { 2.5, 1.5, 1.5 }, { 1.5, 2.5, 1.5 },
                { 1.5, 1.5, 2.5 } });
        double[] standardDeviations = { 1.0, 1.0, 1.0 };
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3, standardDeviations);
        RealMatrix expected = new BlockRealMatrix(new double[][] { { 1.0, 0.819152, 0.651855 },
                { 0.819152, 1.0, 0.819152 }, { 0.651855, 0.819152, 1.0 } });
        assertEquals(expected, pc.correlationMatrix);
        assertEquals(3, pc.nObs);
    }

    /**
     * Test getCorrelationMatrix method
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][] { { 1.0, 0.819152, 0.651855 },
                { 0.819152, 1.0, 0.819152 }, { 0.651855, 0.819152, 1.0 } });
        assertEquals(expected, pc.correlationMatrix);
    }

    /**
     * Test getCorrelationStandardErrors method
     */
    @Test
    public void testGetCorrelationStandardErrors() throws Exception {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][] { { 0.5, 0.363135, 0.265027 },
                { 0.363135, 0.5, 0.363135 }, { 0.265027, 0.363135, 0.5 } });
        RealMatrix result = pc.getCorrelationStandardErrors();
        assertEquals(expected, result);
    }

    /**
     * Test getCorrelationPValues method
     *
     * @throws Exception
     */
    @Test
    public void testGetCorrelationPValues() throws Exception {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][] { { 0.975223, 0.974566, 0.974566 },
                { 0.974566, 0.975223, 0.975223 }, { 0.974566, 0.975223, 0.975223 } });
        RealMatrix result = pc.getCorrelationPValues();
        assertEquals(expected, result);
    }

    /**
     * Test correlation method with double arrays
     */
    @Test
    public void testCorrelationWithDoubleArrays() {
        double[] xArray = { 1.0, 2.0, 3.0 };
        double[] yArray = { 2.0, 3.0, 4.0 };
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        double expected = 0.819152;
        assertEquals(expected, correlation, 0.001);
    }

    /**
     * Test covarianceToCorrelation method
     */
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][] { { 2.5, 1.5, 1.5 }, { 1.5, 2.5, 1.5 },
                { 1.5, 1.5, 2.5 } });
        RealMatrix expected = new BlockRealMatrix(new double[][] { { 1.0, 0.819152, 0.651855 },
                { 0.819152, 1.0, 0.819152 }, { 0.651855, 0.819152, 1.0 } });
        RealMatrix result = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertEquals(expected, result);
    }
}