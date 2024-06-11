package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.Fresnel;

/**
 * Test cases for ContinuedFraction class.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluate method with default parameters.
     */
    @Test
    public void testEvaluateDefault() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(2.0);
        assertEquals(Math.sqrt(2.0), result, 1e-6);
    }

    /**
     * Test evaluate method with custom epsilon.
     */
    @Test
    public void testEvaluateCustomEpsilon() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(2.0, 1e-12);
        assertEquals(Math.sqrt(2.0), result, 1e-12);
    }

    /**
     * Test evaluate method with custom maxIterations.
     */
    @Test
    public void testEvaluateCustomMaxIterations() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(2.0, 10);
        assertEquals(Math.sqrt(2.0), result, 1e-6);
    }

    /**
     * Test evaluate method with infinite values.
     */
    @Test
    public void testEvaluateInfiniteValues() {
        ContinuedFraction continuedFraction = new CotangentContinuedFraction();
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(0.0));
    }

    /**
     * Test evaluate method with NaN values.
     */
    @Test
    public void testEvaluateNaNValues() {
        ContinuedFraction continuedFraction = new CotangentContinuedFraction();
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(Double.NaN));
    }

    /**
     * Test evaluate method with MaxCountExceededException.
     */
    @Test
    public void testEvaluateMaxCountExceeded() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        assertThrows(MaxCountExceededException.class, () -> continuedFraction.evaluate(2.0, 0));
    }

    /**
     * Test getA method.
     */
    @Test
    public void testGetA() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        assertEquals(1.0, continuedFraction.getA(0, 2.0), 1e-6);
        assertEquals(-1.0 / 2.0, continuedFraction.getA(1, 2.0), 1e-6);
        assertEquals(1.0 / (2.0 * 2.0 + 1.0), continuedFraction.getA(2, 2.0), 1e-6);
    }

    /**
     * Test getB method.
     */
    @Test
    public void testGetB() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        assertEquals(2.0, continuedFraction.getB(0, 2.0), 1e-6);
        assertEquals(2.0, continuedFraction.getB(1, 2.0), 1e-6);
        assertEquals(2.0 * 2.0, continuedFraction.getB(2, 2.0), 1e-6);
    }

    /**
     * Test SqrtContinuedFraction class.
     */
    @Test
    public void testSqrtContinuedFraction() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double[] expectedSqrt2 = {1.0, 1.0, 2.0, 2.0, 1.0, 3.0, 2.0, 1.0, 15.0, 10.0, 7.0, 16.0, 11.0, 2.0, 1.0, 1, 1, 1, 1};
        double[] expectedSqrt3 = {1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 3.0, 1.0, 1.0, 1.0, 4.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        double[] expectedSqrt5 = {1.0, 1.0, 2.0, 2.0, 1.0, 2.0, 2.0, 1.0, 3.0, 3.0, 1.0, 1.0, 1.0, 6.0, 1.0, 1.0, 2.0, 2.0, 1.0, 1.0, 1.0};

        double[] resultSqrt2 = new double[20];
        for (int i = 0; i < 20; i++) {
            resultSqrt2[i] = continuedFraction.evaluate(2.0);
        }
        assertArrayEquals(expectedSqrt2, resultSqrt2, 1e-6);

        double[] resultSqrt3 = new double[20];
        for (int i = 0; i < 20; i++) {
            resultSqrt3[i] = continuedFraction.evaluate(3.0);
        }
        assertArrayEquals(expectedSqrt3, resultSqrt3, 1e-6);

        double[] resultSqrt5 = new double[20];
        for (int i = 0; i < 20; i++) {
            resultSqrt5[i] = continuedFraction.evaluate(5.0);
        }
        assertArrayEquals(expectedSqrt5, resultSqrt5, 1e-6);
    }

    /**
     * Test CotangentContinuedFraction class.
     */
    @Test
    public void testCotangentContinuedFraction() {
        ContinuedFraction continuedFraction = new CotangentContinuedFraction();
        double[] expectedCotangentPi = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0};

        double[] resultCotangentPi = new double[20];
        for (int i = 0; i < 20; i++) {
            resultCotangentPi[i] = continuedFraction.evaluate(Math.PI);
        }
        assertArrayEquals(expectedCotangentPi, resultCotangentPi, 1e-6);
    }

    /**
     * Test FresnelContinuedFraction class.
     */
    @Test
    public void testFresnelContinuedFraction() {
        ContinuedFraction continuedFraction = new FresnelContinuedFraction();
        double resultSin = continuedFraction.evaluate(Math.PI / 4);
        assertEquals(Math.sin(Math.PI / 4), resultSin, 1e-6);

        double resultCos = continuedFraction.evaluate(-Math.PI / 4);
        assertEquals(Math.cos(-Math.PI / 4), resultCos, 1e-6);

        double resultCosh = continuedFraction.evaluate(1.0);
        assertEquals(Math.cosh(1.0), resultCosh, 1e-6);

        double resultSinh = continuedFraction.evaluate(-1.0);
        assertEquals(Math.sinh(-1.0), resultSinh, 1e-6);
    }
}