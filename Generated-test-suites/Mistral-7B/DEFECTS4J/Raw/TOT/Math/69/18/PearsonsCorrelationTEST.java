package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.distribution.TDistribution;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for PearsonsCorrelation class
 */
public class PearsonsCorrelationTest {

    /**
     * Test correlation matrix computation from a double[][] input
     */
    @Test
    public void testCorrelationMatrixFromArray() {
        double[][] input = new double[][] {
            {1.0, 2.0, 3.0, 4.0},
            {2.0, 3.0, 4.0, 5.0},
            {3.0, 4.0, 5.0, 6.0},
            {4.0, 5.0, 6.0, 7.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(input);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.8, 0.6, 0.5},
            {0.8, 1.0, 0.8, 0.7},
            {0.6, 0.8, 1.0, 0.8},
            {0.5, 0.7, 0.8, 1.0}
        });
        assertEquals(expected, correlation.getCorrelationMatrix());
    }

    /**
     * Test correlation matrix computation from a RealMatrix input
     */
    @Test
    public void testCorrelationMatrixFromMatrix() {
        double[][] input = new double[][] {
            {1.0, 2.0, 3.0, 4.0},
            {2.0, 3.0, 4.0, 5.0},
            {3.0, 4.0, 5.0, 6.0},
            {4.0, 5.0, 6.0, 7.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(input);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.8, 0.6, 0.5},
            {0.8, 1.0, 0.8, 0.7},
            {0.6, 0.8, 1.0, 0.8},
            {0.5, 0.7, 0.8, 1.0}
        });
        assertEquals(expected, correlation.getCorrelationMatrix());
    }

    /**
     * Test correlation matrix computation from a Covariance instance
     */
    @Test
    public void testCorrelationMatrixFromCovariance() {
        double[][] input = new double[][] {
            {1.0, 2.0, 3.0, 4.0},
            {2.0, 3.0, 4.0, 5.0},
            {3.0, 4.0, 5.0, 6.0},
            {4.0, 5.0, 6.0, 7.0}
        };
        Covariance covariance = new Covariance(input);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.8, 0.6, 0.5},
            {0.8, 1.0, 0.8, 0.7},
            {0.6, 0.8, 1.0, 0.8},
            {0.5, 0.7, 0.8, 1.0}
        });
        assertEquals(expected, correlation.getCorrelationMatrix());
    }

    /**
     * Test correlation matrix computation from a RealMatrix with insufficient data
     */
    @Test(expected = MathRuntimeException.class)
    public void testCorrelationMatrixFromInsufficientDataMatrix() {
        double[][] input = new double[][] {
            {1.0, 2.0},
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(new BlockRealMatrix(input));
    }

    /**
     * Test correlation standard errors computation
     */
    @Test
    public void testCorrelationStandardErrors() {
        double[][] input = new double[][] {
            {1.0, 2.0, 3.0, 4.0},
            {2.0, 3.0, 4.0, 5.0},
            {3.0, 4.0, 5.0, 6.0},
            {4.0, 5.0, 6.0, 7.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(input);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            {0.68255576, 0.6423655, 0.61552307, 0.5906816},
            {0.6423655, 0.68255576, 0.65752946, 0.6312367},
            {0.61552307, 0.65752946, 0.68255576, 0.66518189},
            {0.5906816, 0.6312367, 0.66518189, 0.6911759}
        });
        RealMatrix standardErrors = correlation.getCorrelationStandardErrors();
        assertArrayEquals(expected.getData(), standardErrors.getData(), 0.001);
    }

    /**
     * Test correlation p-values computation
     */
    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] input = new double[][] {
            {1.0, 2.0, 3.0, 4.0},
            {2.0, 3.0, 4.0, 5.0},
            {3.0, 4.0, 5.0, 6.0},
            {4.0, 5.0, 6.0, 7.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(input);
        RealMatrix pValues = correlation.getCorrelationPValues();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            {0.0, 0.01428283, 0.03241576, 0.05608652},
            {0.01428283, 0.0, 0.01428283, 0.02181632},
            {0.03241576, 0.01428283, 0.0, 0.01428283},
            {0.05608652, 0.02181632, 0.01428283, 0.0}
        });
        assertArrayEquals(expected.getData(), pValues.getData(), 0.001);
    }

    /**
     * Test correlation computation from double[] inputs
     */
    @Test
    public void testCorrelationFromArrays() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {2.0, 3.0, 4.0, 5.0};
        double correlationValue = PearsonsCorrelation.correlation(x, y);
        double expected = 0.8;
        assertEquals(expected, correlationValue, 0.001);

        double[] x2 = {1.0, 2.0, 3.0, 4.0};
        double[] y2 = {1.0, 1.0, 1.0, 1.0};
        double correlationValue2 = PearsonsCorrelation.correlation(x2, y2);
        assertEquals(1.0, correlationValue2, 0.001);
    }

    /**
     * Test correlation computation from double[] inputs with insufficient data
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationFromInsufficientDataArrays() {
        double[] x = {1.0};
        double[] y = {2.0};
        PearsonsCorrelation.correlation(x, y);
    }
}