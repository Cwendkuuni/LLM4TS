package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.StatUtils;

import org.junit.Test;

/**
 * Tests for {@link PearsonsCorrelation} class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test construction with no data.
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.correlationMatrix);
        assertEquals(0, pc.nObs);
    }

    /**
     * Test construction with 2x2 data array.
     */
    @Test
    public void test2x2DataArray() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8},
                {0.8, 1.0}
        });
        RealMatrix actual = pc.correlationMatrix;
        assertEquals(expected, actual);
        assertEquals(4, pc.nObs);
    }

    /**
     * Test construction with a 3x3 RealMatrix.
     */
    @Test
    public void test3x3RealMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        PearsonsCorrelation pc = new PearsonsCorrelation(inputMatrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {0.9659258264794511, 0.9510565162983116, 0.935761625459723},
                {0.9510565162983116, 0.9659258264794511, 0.9510565162983116},
                {0.935761625459723, 0.9510565162983116, 0.9659258264794511}
        });
        RealMatrix actual = pc.correlationMatrix;
        assertEquals(expected, actual);
        assertEquals(9, pc.nObs);
    }

    /**
     * Test construction with Covariance.
     */
    @Test
    public void testCovariance() {
        double[][] data = new double[][]{{1.0, 1.1}, {2.0, 2.1}, {3.0, 3.1}, {4.0, 4.1}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {0.9821525334237932, 0.9760210302391532},
                {0.9760210302391532, 0.9821525334237932}
        });
        RealMatrix actual = pc.correlationMatrix;
        assertEquals(expected, actual);
        assertEquals(4, pc.nObs);
    }

    /**
     * Test construction with a CovarianceMatrix.
     */
    @Test
    public void testCovarianceMatrix() {
        double[][] data = new double[][]{{1.0, 1.1}, {2.0, 2.1}, {3.0, 3.1}, {4.0, 4.1}};
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.132476781260966, 1.110505258692129},
                {1.110505258692129, 1.132476781260966}
        });
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 4);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {0.9821525334237932, 0.9760210302391532},
                {0.9760210302391532, 0.9821525334237932}
        });
        RealMatrix actual = pc.correlationMatrix;
        assertEquals(expected, actual);
        assertEquals(4, pc.nObs);
    }

    /**
     * Test getCorrelationMatrix().
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8},
                {0.8, 1.0}
        });
        RealMatrix actual = pc.getCorrelationMatrix();
        assertEquals(expected, actual);
    }

    /**
     * Test getCorrelationStandardErrors().
     */
    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {0.4898949886656076, 0.4898949886656076},
                {0.4898949886656076, 0.4898949886656076}
        });
        RealMatrix actual = pc.getCorrelationStandardErrors();
        assertEquals(expected, actual);
    }

    /**
     * Test getCorrelationPValues().
     *
     * @throws MathException if an error occurs estimating probabilities
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {0.2057447151929498, 0.2057447151929498},
                {0.2057447151929498, 0.2057447151929498}
        });
        RealMatrix actual = pc.getCorrelationPValues();
        assertEquals(expected, actual);
    }

    /**
     * Test correlation method with 1D arrays.
     */
    @Test
    public void testCorrelation1D() {
        double[] xArray = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] yArray = new double[]{2.0, 3.0, 3.5, 4.5};
        assertEquals(0.847457992729626, PearsonsCorrelation.correlation(xArray, yArray), 0.001);
    }

    /**
     * Test correlation method with 2D arrays.
     */
    @Test
    public void testCorrelation2D() {
        double[][] xArray = new double[][]{{1.0, 2.0, 3.0, 4.0}, {5.0, 6.0, 7.0, 8.0}};
        double[][] yArray = new double[][]{{2.0, 3.0, 3.5, 4.5}, {6.0, 7.0, 8.0, 9.0}};
        assertEquals(0.847457992729626, PearsonsCorrelation.correlation(xArray, yArray)[0][0], 0.001);
    }

    /**
     * Test correlation method with non-square matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationNonSquareMatrix() {
        double[][] xArray = new double[][]{{1.0, 2.0}, {5.0, 6.0, 7.0, 8.0}};
        double[][] yArray = new double[][]{
                {2.0, 3.0, 3.5},
                {4.5}
        };
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlation method with non-numeric matrices.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationNonNumericMatrix() {
        Complex[][] xArray = new Complex[][]{{Complex.ONE, Complex.ONE}, {Complex.ONE, Complex.ONE}};
        double[][] yArray = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlation method with matrices with different sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationDifferentSizes() {
        double[][] xArray = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        double[][] yArray = new double[]{1.0, 2.0};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test covarianceToCorrelation method.
     */
    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = new double[][]{{1.0, 1.1}, {2.0, 2.1}, {3.0, 3.1}, {4.0, 4.1}};
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.132476781260966, 1.110505258692129},
                {1.110505258692129, 1.132476781260966}
        });
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {0.9821525334237932, 0.9760210302391532},
                {0.9760210302391532, 0.9821525334237932}
        });
        RealMatrix actual = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertEquals(expected, actual);
    }
}