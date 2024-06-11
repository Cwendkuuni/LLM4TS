package org.apache.commons.math.stat.inference;

import junit.framework.TestCase;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;

/**
 * Tests for {@link ChiSquareTestImpl} class.
 */
public class ChiSquareTestImplTest extends TestCase {

    /** Test instance. */
    private ChiSquareTestImpl test;

    /** chi-square distribution instance. */
    private ChiSquaredDistribution distribution;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        test = new ChiSquareTestImpl();
        distribution = new ChiSquaredDistributionImpl(1.0);
    }

    /**
     * Tests the ChiSquareTestImpl constructor.
     */
    public void testConstructor() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        assertNotNull(instance);
    }

    /**
     * Tests the chiSquare method with valid inputs.
     */
    public void testChiSquareWithValidInputs() {
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double result = test.chiSquare(expected, observed);
        double expectedResult = 2.0; // degrees of freedom = 2
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Tests the chiSquare method with invalid inputs.
     */
    public void testChiSquareWithInvalidInputs() {
        try {
            double[] expected = {};
            long[] observed = {};
            double result = test.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            double[] expected = {1.0};
            long[] observed = {};
            double result = test.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            double[] expected = {1.0};
            long[] observed = {1, 1};
            double result = test.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            double[] expected = {1.0, 2.0};
            long[] observed = {1, 2, 3};
            double result = test.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            double[] expected = {1.0, 2.0};
            long[] observed = {1, 1, 1, 1, 1};
            double result = test.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Tests the chiSquareTest method with valid inputs.
     */
    public void testChiSquareTestWithValidInputs() {
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double alpha = 0.05;
        double result = test.chiSquareTest(expected, observed, alpha);
        double expectedResult = 0.95;
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Tests the chiSquareTest method with invalid inputs.
     */
    public void testChiSquareTestWithInvalidInputs() {
        try {
            double[] expected = {};
            long[] observed = {};
            double result = test.chiSquareTest(expected, observed, 0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            double[] expected = {1.0};
            long[] observed = {};
            double result = test.chiSquareTest(expected, observed, 0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            double[] expected = {1.0};
            long[] observed = {1, 1};
            double result = test.chiSquareTest(expected, observed, 0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            double[] expected = {1.0, 2.0};
            long[] observed = {1, 2, 3};
            double result = test.chiSquareTest(expected, observed, 0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            double[] expected = {1.0, 2.0};
            long[] observed = {1, 1, 1, 1, 1};
            double result = test.chiSquareTest(expected, observed, 0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            double[] expected = {1.0, 2.0};
            long[] observed = {2, 3};
            double result = test.chiSquareTest(expected, observed, -0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            double[] expected = {1.0, 2.0};
            long[] observed = {2, 3};
            double result = test.chiSquareTest(expected, observed, 1.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            double[] expected = {1.0, 2.0};
            long[] observed = {2, 3};
            double result = test.chiSquareTest(expected, observed, 0.6);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // Expected
        }
    }

    /**
     * Tests the chiSquareTest method with edge cases.
     */
    public void testChiSquareTestWithEdgeCases() {
        double[] expected = {1.0, 1.0};
        long[] observed = {1, 1};
        double alpha = 0.5;
        double result = test.chiSquareTest(expected, observed, alpha);
        double expectedResult = 0.5;
        assertEquals(expectedResult, result, 0.001);

        double[] expectedEdge = {0.5, 0.5};
        long[] observedEdge = {1, 1};
        double alphaEdge = 0.5;
        double resultEdge = test.chiSquareTest(expectedEdge, observedEdge, alphaEdge);
        double expectedResultEdge = 0.5;
        assertEquals(expectedResultEdge, resultEdge, 0.001);
    }

    /**
     * Tests the chiSquareTestDataSetsComparison method with valid inputs.
     */
    public void testChiSquareTestDataSetsComparisonWithValidInputs() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 1, 3};
        double alpha = 0.05;
        double result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        double expectedResult = 0.95;
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Tests the chiSquareTestDataSetsComparison method with invalid inputs.
     */
    public void testChiSquareTestDataSetsComparisonWithInvalidInputs() {
        try {
            long[] observed1 = {};
            long[] observed2 = {};
            double result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            long[] observed1 = {1};
            long[] observed2 = {};
            double result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            long[] observed1 = {1, 2};
            long[] observed2 = {1};
            double result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            long[] observed1 = {1, 2};
            long[] observed2 = {1, 1};
            double result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            long[] observed1 = {1, 2};
            long[] observed2 = {1, 1, 1};
            double result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            long[] observed1 = {1, 2};
            long[] observed2 = {2, 1, 1};
            double result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            long[] observed1 = {1, 2};
            long[] observed2 = {2, 1, 1};
            double result = test.chiSquareTestDataSetsComparison(observed1, observed2, -0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            long[] observed1 = {1, 2};
            long[] observed2 = {2, 1, 1};
            double result = test.chiSquareTestDataSetsComparison(observed1, observed2, 1.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            long[] observed1 = {1, 2};
            long[] observed2 = {2, 1, 1};
            double result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.6);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // Expected
        }
    }

    /**
     * Tests the chiSquareTestDataSetsComparison method with edge cases.
     */
    public void testChiSquareTestDataSetsComparisonWithEdgeCases() {
        long[] observed1 = {1, 1};
        long[] observed2 = {1, 1};
        double alpha = 0.5;
        double result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        double expectedResult = 0.5;
        assertEquals(expectedResult, result, 0.001);

        long[] observed1Edge = {0.5, 0.5};
        long[] observed2Edge = {1, 1};
        double alphaEdge = 0.5;
        double resultEdge = test.chiSquareTestDataSetsComparison(observed1Edge, observed2Edge, alphaEdge);
        double expectedResultEdge = 0.5;
        assertEquals(expectedResultEdge, resultEdge, 0.001);
    }
}