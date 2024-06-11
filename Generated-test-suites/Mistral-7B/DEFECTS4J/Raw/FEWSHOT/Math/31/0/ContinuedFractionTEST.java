package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.Fraction;
import org.junit.Test;

/**
 * Test cases for the ContinuedFraction class.
 */
public class ContinuedFractionTest {

    /**
     * Test for evaluate method with default constructor and epsilon and maxIterations.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     */
    @Test
    public void testEvaluateDefaultConstructor() throws ConvergenceException, MaxCountExceededException {
        ContinuedFraction continuedFraction = new SquareRootContinuedFraction();
        double x = 2.0;
        double result = continuedFraction.evaluate(x);
        Fraction expectedResult = new Fraction(1, 2);
        assertEquals("Square root of 2 evaluation error.", expectedResult.getNumerator(), result, 1e-8);
    }

    /**
     * Test for evaluate method with epsilon only.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     */
    @Test
    public void testEvaluateEpsilon() throws ConvergenceException, MaxCountExceededException {
        ContinuedFraction continuedFraction = new SquareRootContinuedFraction();
        double x = 2.0;
        double epsilon = 1e-6;
        double result = continuedFraction.evaluate(x, epsilon);
        Fraction expectedResult = new Fraction(1, 2);
        assertEquals("Square root of 2 evaluation error with epsilon.", expectedResult.getNumerator(), result, epsilon);
    }

    /**
     * Test for evaluate method with maxIterations only.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     */
    @Test
    public void testEvaluateMaxIterations() throws ConvergenceException, MaxCountExceededException {
        ContinuedFraction continuedFraction = new SquareRootContinuedFraction();
        double x = 2.0;
        int maxIterations = 15;
        continueWithExpectedResult(continuedFraction, x, maxIterations, 1.4142135623730955);
    }

    /**
     * Test for evaluate method with epsilon and maxIterations.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     */
    @Test
    public void testEvaluateEpsilonAndMaxIterations() throws ConvergenceException, MaxCountExceededException {
        ContinuedFraction continuedFraction = new SquareRootContinuedFraction();
        double x = 2.0;
        double epsilon = 1e-8;
        int maxIterations = 20;
        continueWithExpectedResult(continuedFraction, x, epsilon, maxIterations, 1.4142135623730955);
    }

    /**
     * Test for evaluate method with epsilon, maxIterations and custom tolerance.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     */
    @Test
    public void testEvaluateEpsilonAndMaxIterationsWithCustomTolerance() throws ConvergenceException, MaxCountExceededException {
        ContinuedFraction continuedFraction = new SquareRootContinuedFraction();
        double x = 2.0;
        double epsilon = 1e-6;
        int maxIterations = 20;
        double customTolerance = 1e-7;
        double result = continuedFraction.evaluate(x, epsilon, maxIterations, customTolerance);
        Fraction expectedResult = new Fraction(1, 2);
        assertEquals("Square root of 2 evaluation error with custom tolerance.", expectedResult.getNumerator(), result, customTolerance);
    }

    /**
     * Test for evaluate method with infinite values.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfiniteValue() throws ConvergenceException, MaxCountExceededException {
        ContinuedFraction continuedFraction = new LogarithmContinuedFraction();
        double x = -1.0;
        double epsilon = 1e-8;
        int maxIterations = 20;
        continuedFraction.evaluate(x, epsilon, maxIterations);
    }

    /**
     * Test for evaluate method with NaN value.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     */
    @Test(expected = ArithmeticException.class)
    public void testEvaluateNaNValue() throws ConvergenceException, MaxCountExceededException {
        ContinuedFraction continuedFraction = new LogarithmContinuedFraction();
        double x = Double.NaN;
        double epsilon = 1e-8;
        int maxIterations = 20;
        continuedFraction.evaluate(x, epsilon, maxIterations);
    }

    /**
     * Test for evaluate method with value outside supported range.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateOutsideSupportedRange() throws ConvergenceException, MaxCountExceededException {
        ContinuedFraction continuedFraction = new LogarithmContinuedFraction();
        double x = 1.5;
        double epsilon = 1e-8;
        int maxIterations = 20;
        continuedFraction.evaluate(x, epsilon, maxIterations);
    }

    /**
     * Helper method for testing evaluation method with expected result.
     * @param continuedFraction the continued fraction to be tested.
     * @param x the evaluation point.
     * @param epsilon the maximum error allowed.
     * @param maxIterations the maximum number of convergents.
     * @param expectedResult the expected result.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     */
    private void continueWithExpectedResult(ContinuedFraction continuedFraction, double x, double epsilon, int maxIterations, double expectedResult)
            throws ConvergenceException, MaxCountExceededException {
        double result = continuedFraction.evaluate(x, epsilon, maxIterations);
        assertEquals("Continued fraction evaluation error.", expectedResult, result, epsilon);
    }

    /**
     * Helper method for testing evaluation method with expected result and maxIterations.
     * @param continuedFraction the continued fraction to be tested.
     * @param x the evaluation point.
     * @param maxIterations the maximum number of convergents.
     * @param expectedResult the expected result.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     */
    private void continueWithExpectedResult(ContinuedFraction continuedFraction, double x, int maxIterations, double expectedResult)
            throws ConvergenceException, MaxCountExceededException {
        double result = continuedFraction.evaluate(x, maxIterations);
        assertEquals("Continued fraction evaluation error.", expectedResult, result, 1e-8);
    }

}