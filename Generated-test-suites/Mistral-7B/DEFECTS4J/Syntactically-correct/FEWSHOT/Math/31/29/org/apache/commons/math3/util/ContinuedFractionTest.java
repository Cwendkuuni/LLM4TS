package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.Fresnel;
import org.apache.commons.math3.util.Precision;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Test cases for ContinuedFraction class.
 */
public class ContinuedFractionTest {

    /**
     * Test for {@code evaluate(x)} method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEvaluate() throws Exception {
        final double epsilon = 1e-15;
        final int maxIterations = 100;
        ContinuedFraction cf = new SquareRootContinuedFraction();

        for (double x = 0; x <= 10; x += 0.5) {
            double result = cf.evaluate(x, epsilon, maxIterations);
            double exact = Math.sqrt(x);
            assertEquals(exact, result, epsilon);
        }

        cf = new LogarithmBase2ContinuedFraction();
        for (double x = 1; x <= 10; x *= 2) {
            double result = cf.evaluate(x, epsilon, maxIterations);
            double exact = Math.log(x) / Math.log(2);
            assertEquals(exact, result, epsilon);
        }

        cf = new CotangentContinuedFraction();
        for (double x = 0.1; x <= Math.PI / 2; x += 0.1) {
            double result = cf.evaluate(x, epsilon, maxIterations);
            double exact = 1.0 / Math.tan(x);
            assertEquals(exact, result, epsilon);
        }

        cf = new TangentContinuedFraction();
        for (double x = 0.1; x <= Math.PI / 2; x += 0.1) {
            double result = cf.evaluate(x, epsilon, maxIterations);
            double exact = Math.tan(x);
            assertEquals(exact, result, epsilon);
        }

        cf = new FibonacciContinuedFraction();
        for (int n = 0; n <= 10; n++) {
            double result = cf.evaluate(n, epsilon, maxIterations);
            double exact = Fibonacci.fibonacci(n);
            assertEquals(exact, result, epsilon);
        }

        cf = new EulerContinuedFraction();
        for (double x = 0; x <= 10; x += 1) {
            double result = cf.evaluate(x, epsilon, maxIterations);
            double exact = EulerNumber.e(x);
            assertEquals(exact, result, epsilon);
        }

        cf = new SineContinuedFraction();
        for (double x = 0.1; x <= Math.PI; x += 0.1) {
            double result = cf.evaluate(x, epsilon, maxIterations);
            double exact = Math.sin(x);
            assertEquals(exact, result, epsilon);
        }

        // test for infinite divergence
        cf = new CotangentContinuedFraction();
        double x = 0;
        try {
            cf.evaluate(x, epsilon, maxIterations);
            fail("Expected ConvergenceException for x = 0");
        } catch (ConvergenceException e) {
            assertEquals(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, e.getMessage());
        }

        // test for NaN divergence
        cf = new TangentContinuedFraction();
        x = Double.NaN;
        try {
            cf.evaluate(x, epsilon, maxIterations);
            fail("Expected ConvergenceException for x = NaN");
        } catch (ConvergenceException e) {
            assertEquals(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, e.getMessage());
        }

        // test for maximum iterations
        cf = new SquareRootContinuedFraction();
        x = 100000;
        try {
            cf.evaluate(x, epsilon, 1);
            fail("Expected MaxCountExceededException for x = 100000");
        } catch (MaxCountExceededException e) {
            assertEquals(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, e.getMessage());
        }
    }

    /**
     * Test for {@code getA(n, x)} and {@code getB(n, x)} methods.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCoefficients() throws Exception {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        for (int n = 0; n < 10; n++) {
            double x = n + 1.0;
            double a = cf.getA(n, x);
            double b = cf.getB(n, x);
            assertEquals(a * x * x + b, Math.pow(x, 2), 1e-15);
        }
    }

    /**
     * Test for {@code evaluate(x, epsilon, maxIterations)} with custom epsilon
     * and maxIterations.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCustomEvaluate() throws Exception {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        double epsilon = 1e-20;
        double maxIterations = 50;
        for (double x = 0; x <= 10; x += 0.5) {
            double result = cf.evaluate(x, epsilon, (int) maxIterations);
            double exact = Math.sqrt(x);
            assertEquals(exact, result, epsilon);
        }
    }

    /**
     * Test for {@code evaluate(x, epsilon)} with custom epsilon.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCustomEvaluateEpsilon() throws Exception {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        double epsilon = 1e-20;
        for (double x = 0; x <= 10; x += 0.5) {
            double result = cf.evaluate(x, epsilon);
            double exact = Math.sqrt(x);
            assertEquals(exact, result, epsilon);
        }
    }

    /**
     * Test for {@code evaluate(x, maxIterations)} with custom maxIterations.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCustomEvaluateMaxIterations() throws Exception {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        int maxIterations = 5;
        for (double x = 0; x <= 10; x += 0.5) {
            double result = cf.evaluate(x, maxIterations);
            double exact = Math.sqrt(x);
            assertTrue(Precision.abs(result - exact) < 1e-9);
        }
    }

    /**
     * Test for {@code evaluate(x)} with BigDecimal.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEvaluateBigDecimal() throws Exception {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        MathContext context = new MathContext(40, BigDecimal.ROUND_HALF_EVEN);
        double epsilon = 1e-20;
        for (double x = 0; x <= 10; x += 0.5) {
            BigDecimal xDec = new BigDecimal(x, context);
            BigDecimal result = new BigDecimal(cf.evaluate(x, epsilon, Integer.MAX_VALUE));
            BigDecimal exact = Math.sqrt(new BigDecimal(x)).toBigDecimal(context);
            assertEquals(exact, result, new BigDecimal(epsilon));
        }
    }

    /**
     * Test for {@code evaluate(x, epsilon, maxIterations)} with BigDecimal.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCustomEvaluateBigDecimal() throws Exception {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        MathContext context = new MathContext(40, BigDecimal.ROUND_HALF_EVEN);
        double epsilon = 1e-20;
        for (double x = 0; x <= 10; x += 0.5) {
            BigDecimal xDec = new BigDecimal(x, context);
            BigDecimal result = new BigDecimal(cf.evaluate(x, epsilon, Integer.MAX_VALUE));
            BigDecimal exact = Math.sqrt(new BigDecimal(x)).toBigDecimal(context);
            assertEquals(exact, result, new BigDecimal(epsilon));
        }
    }

    /**
     * Test for {@code evaluate(x, epsilon)} with BigDecimal.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCustomEvaluateBigDecimalEpsilon() throws Exception {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        MathContext context = new MathContext(40, BigDecimal.ROUND_HALF_EVEN);
        double epsilon = 1e-20;
        for (double x = 0; x <= 10; x += 0.5) {
            BigDecimal xDec = new BigDecimal(x, context);
            BigDecimal result = new BigDecimal(cf.evaluate(x, epsilon));
            BigDecimal exact = Math.sqrt(new BigDecimal(x)).toBigDecimal(context);
            assertEquals(exact, result, new BigDecimal(epsilon));
        }
    }

    /**
     * Test for {@code evaluate(x, maxIterations)} with BigDecimal.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCustomEvaluateBigDecimalMaxIterations() throws Exception {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        MathContext context = new MathContext(40, BigDecimal.ROUND_HALF_EVEN);
        int maxIterations = 5;
        for (double x = 0; x <= 10; x += 0.5) {
            BigDecimal xDec = new BigDecimal(x, context);
            BigDecimal result = new BigDecimal(cf.evaluate(x, maxIterations));
            BigDecimal exact = Math.sqrt(new BigDecimal(x)).toBigDecimal(context);
            assertTrue(Precision.abs(result.subtract(exact)).compareTo(new BigDecimal(epsilon)) < 0);
        }
    }

}