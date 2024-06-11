package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.FresnelS;
import org.junit.Test;

/**
 * Test cases for {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluation of squareroot continued fraction.
     */
    @Test
    public void testSquarerootContinuedFraction() {
        ContinuedFraction sqrt = new SqrtContinuedFraction();
        assertEquals(1.5, sqrt.evaluate(0.5), 1e-9);
        assertEquals(2.5, sqrt.evaluate(1.0), 1e-9);
        assertEquals(2.44948974278317, sqrt.evaluate(2.0), 1e-9);
        assertEquals(1.4142135623730955, sqrt.evaluate(3.0), 1e-9);
        assertEquals(1.732050807573086, sqrt.evaluate(4.0), 1e-9);
        assertEquals(2.0, sqrt.evaluate(16.0), 1e-9);
    }

    /**
     * Test evaluation of cotangent continued fraction.
     */
    @Test
    public void testCotangentContinuedFraction() {
        ContinuedFraction cot = new CotangentContinuedFraction();
        assertEquals(1.2599210451025201, cot.evaluate(1.0), 1e-9);
        assertEquals(-0.5773502691896257, cot.evaluate(2.0), 1e-9);
        assertEquals(-1.5773502691896257, cot.evaluate(3.0), 1e-9);
        assertEquals(1.0/FresnelS.s(1), cot.evaluate(1e-2), 1e-9);
        assertEquals(-3.62686025413171, cot.evaluate(1e-5), 1e-9);
    }

    /**
     * Test evaluation of logarithm base e continued fraction.
     */
    @Test
    public void testLogarithmBaseEContinuedFraction() {
        ContinuedFraction logE = new LogarithmBaseEContinuedFraction();
        assertEquals(0.0, logE.evaluate(1.0), 1e-9);
        assertEquals(0.4342944819032518, logE.evaluate(2.0), 1e-9);
        assertEquals(0.6931471805599453, logE.evaluate(e), 1e-9);
        assertEquals(0.9182968657562617, logE.evaluate(3.0), 1e-9);
        assertEquals(1.16330385298144, logE.evaluate(4.0), 1e-9);
        assertEquals(1.46158822713493, logE.evaluate(5.0), 1e-9);
    }

    /**
     * Test evaluation of hyperbolic tangent continued fraction.
     */
    @Test
    public void testHyperbolicTangentContinuedFraction() {
        ContinuedFraction tanh = new HyperbolicTangentContinuedFraction();
        assertEquals(0.0, tanh.evaluate(0.0), 1e-9);
        assertEquals(0.5465896974857757, tanh.evaluate(1.0), 1e-9);
        assertEquals(1.231390288567954, tanh.evaluate(2.0), 1e-9);
        assertEquals(2.23606797749979, tanh.evaluate(3.0), 1e-9);
        assertEquals(3.17271157285315, tanh.evaluate(4.0), 1e-9);
    }

    /**
     * Test evaluation of inverse tangent continued fraction.
     */
    @Test
    public void testInverseTangentContinuedFraction() {
        ContinuedFraction arctan = new InverseTangentContinuedFraction();
        assertEquals(0.0, arctan.evaluate(0.0), 1e-9);
        assertEquals(0.546302489664466, arctan.evaluate(1.0), 1e-9);
        assertEquals(1.047197551196598, arctan.evaluate(2.0), 1e-9);
        assertEquals(1.6487212707001282, arctan.evaluate(3.0), 1e-9);
        assertEquals(2.094595151582455, arctan.evaluate(4.0), 1e-9);
    }

    /**
     * Test evaluation of inverse cotangent continued fraction.
     */
    @Test
    public void testInverseCotangentContinuedFraction() {
        ContinuedFraction arccot = new InverseCotangentContinuedFraction();
        assertEquals(1.5707963267948966, arccot.evaluate(1.0), 1e-9);
        assertEquals(1.178113518751391, arccot.evaluate(2.0), 1e-9);
        assertEquals(0.935692620465887, arccot.evaluate(3.0), 1e-9);
        assertEquals(0.7853981633974483, arccot.evaluate(3.141592653589793), 1e-9);
        assertEquals(1.0471975511965977, arccot.evaluate(4.0), 1e-9);
    }

    /**
     * Test evaluation of inverse tangent half continued fraction.
     */
    @Test
    public void testInverseTangentHalfContinuedFraction() {
        ContinuedFraction arctanHalf = new InverseTangentHalfContinuedFraction();
        assertEquals(0.0, arctanHalf.evaluate(0.0), 1e-9);
        assertEquals(0.25, arctanHalf.evaluate(0.25), 1e-9);
        assertEquals(0.5, arctanHalf.evaluate(0.5), 1e-9);
        assertEquals(0.75, arctanHalf.evaluate(0.75), 1e-9);
        assertEquals(1.0, arctanHalf.evaluate(1.0), 1e-9);
    }

    /**
     * Test evaluation of logarithmic base 2 continued fraction.
     */
    @Test
    public void testLogBase2ContinuedFraction() {
        ContinuedFraction log2 = new LogarithmBase2ContinuedFraction();
        assertEquals(0.0, log2.evaluate(1.0), 1e-9);
        assertEquals(0.25, log2.evaluate(2.0), 1e-9);
        assertEquals(0.5, log2.evaluate(4.0), 1e-9);
        assertEquals(1.0, log2.evaluate(16.0), 1e-9);
    }

    /**
     * Test evaluation with epsilon and maxIterations.
     */
    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        ContinuedFraction sqrt = new SqrtContinuedFraction();
        assertEquals(1.732050807573086, sqrt.evaluate(4.0), 5e-9);
        assertEquals(1.732050807573086, sqrt.evaluate(4.0, 1e-6, 50), 5e-9);

        ContinuedFraction cot = new CotangentContinuedFraction();
        assertEquals(-3.62686025413171, cot.evaluate(1e-5), 1e-8);
        assertEquals(-3.62686025413171, cot.evaluate(1e-5, 1e-6, 50), 1e-8);
    }

    /**
     * Test convergence exception when maxIterations is reached.
     */
    @Test
    public void testConvergenceException() {
        ContinuedFraction sqrt = new SqrtContinuedFraction();
        assertThrows(MaxCountExceededException.class, () -> sqrt.evaluate(1.0, 1e-10, 10));
    }

    /**
     * Test infinite divergence exception.
     */
    @Test
    public void testInfiniteDivergenceException() {
        ContinuedFraction cot = new CotangentContinuedFraction();
        assertThrows(ConvergenceException.class, () -> cot.evaluate(0.0));
        assertThrows(ConvergenceException.class, () -> cot.evaluate(1.0/0.0));
    }
}