package org.apache.commons.math.stat.inference;

import org.apache.commons.math.MathException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTestImpl;
    private ChiSquaredDistribution distribution;

    public ChiSquareTestImplTest(ChiSquaredDistribution distribution) {
        this.distribution = distribution;
        chiSquareTestImpl = new ChiSquareTestImpl(distribution);
    }

    @Parameterized.Parameters(name = "{index}: ChiSquareTestImpl with distribution={0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new ChiSquaredDistributionImpl(1.0),
                new ChiSquaredDistributionImpl(2.0)
        });
    }

    @Test
    public void testChiSquareWithExpectedAndObserved() throws MathException, IllegalArgumentException {
        long[] expected = {1, 2};
        long[] observed = {2, 1};
        double chiSquareStatistic = chiSquareTestImpl.chiSquare(expected, observed);
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        assertEquals(3.0, chiSquareStatistic, 0.001);
        assertEquals(0.1112543224159341, pValue, 0.001);
    }

    @Test
    public void testChiSquareWithInvalidExpectedOrObservedLengths() throws MathException, IllegalArgumentException {
        long[] expected = new long[1];
        long[] observed = new long[1];
        try {
            chiSquareTestImpl.chiSquare(expected, observed);
        } catch (IllegalArgumentException e) {
            assertEquals("observed, expected array lengths incorrect", e.getMessage());
        }

        expected = new long[2];
        observed = new long[3];
        try {
            chiSquareTestImpl.chiSquare(expected, observed);
        } catch (IllegalArgumentException e) {
            assertEquals("observed, expected array lengths incorrect", e.getMessage());
        }
    }

    @Test
    public void testChiSquareWithInvalidExpectedOrObservedValues() throws MathException, IllegalArgumentException {
        long[] expected = {-1, 2};
        long[] observed = {2, 1};
        try {
            chiSquareTestImpl.chiSquare(expected, observed);
        } catch (IllegalArgumentException e) {
            assertEquals("observed counts must be non-negative and expected counts must be postive", e.getMessage());
        }

        long[] expectedInvalid = {1, -2};
        long[] observed = {2, 1};
        try {
            chiSquareTestImpl.chiSquare(expectedInvalid, observed);
        } catch (IllegalArgumentException e) {
            assertEquals("observed counts must be non-negative and expected counts must be postive", e.getMessage());
        }

        long[] expected = new long[2];
        long[] observed = new long[2];
        try {
            chiSquareTestImpl.chiSquare(expected, observed);
            assertFalse(Arrays.equals(expected, observed));
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquareWithTwoWayTable() throws MathException, IllegalArgumentException {
        long[][] counts = {{1, 1}, {1, 1}};
        double chiSquareStatistic = chiSquareTestImpl.chiSquare(counts);
        double pValue = chiSquareTestImpl.chiSquareTest(counts);
        assertEquals(0.0, chiSquareStatistic, 0.001);
        assertEquals(1.0, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestWithInvalidTwoWayTable() throws MathException, IllegalArgumentException {
        long[][] counts = {{1}, {1, 1}};
        try {
            chiSquareTestImpl.chiSquareTest(counts);
        } catch (IllegalArgumentException e) {
            assertEquals("Input table must be rectangular", e.getMessage());
        }
    }

    @Test
    public void testChiSquareTestWithInvalidTwoWayTableValues() throws MathException, IllegalArgumentException {
        long[][] counts = {{1, -1}, {1, 1}};
        try {
            chiSquareTestImpl.chiSquareTest(counts);
        } catch (IllegalArgumentException e) {
            assertEquals("All entries in input 2-way table must be non-negative", e.getMessage());
        }
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithValid2DArrays() throws MathException, IllegalArgumentException {
        long[] observed1 = {1, 1, 2, 3};
        long[] observed2 = {2, 1, 3, 1};
        double chiSquareStatistic = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(2.776752764656349, chiSquareStatistic, 0.001);
        assertEquals(0.2487831512926849, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithInvalidObservedArrays() throws MathException, IllegalArgumentException {
        long[] observed1 = {1, 1};
        long[] observed2 = {null, 1};
        try {
            chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        } catch (IllegalArgumentException e) {
            assertEquals("Both observed arrays must not be null", e.getMessage());
        }

        long[] observed1 = {1, 1};
        long[] observed2 = new long[1];
        try {
            chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        } catch (IllegalArgumentException e) {
            assertEquals("Both observed arrays must have the same length", e.getMessage());
        }

        long[] observed1 = {1, 1};
        long[] observed2 = new long[2];
        try {
            chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        } catch (IllegalArgumentException e) {
            assertEquals("Both observed arrays must have the same length", e.getMessage());
        }
    }

    @Test
    public void testSetDistribution() {
        ChiSquaredDistributionImpl distributionMock = Mockito.mock(ChiSquaredDistributionImpl.class);
        chiSquareTestImpl.setDistribution(distributionMock);
        assertEquals(distributionMock, chiSquareTestImpl.getDistribution());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetDistributionWithNullDistribution() {
        chiSquareTestImpl.setDistribution(null);
    }
}