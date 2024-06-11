import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTest;

    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl();
    }

    @Test
    public void testChiSquare() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        assertEquals(0.0, chiSquareTest.chiSquare(expected, observed), 0.0001);
    }

    @Test
    public void testChiSquareTest() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        try {
            double pValue = chiSquareTest.chiSquareTest(expected, observed);
            assertTrue(pValue > 0.0 && pValue < 1.0);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareTestWithAlpha() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        try {
            assertFalse(chiSquareTest.chiSquareTest(expected, observed, 0.01));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareWith2WayTable() {
        long[][] counts = {{1, 2}, {3, 4}};
        assertEquals(0.0, chiSquareTest.chiSquare(counts), 0.0001);
    }

    @Test
    public void testChiSquareTestWith2WayTable() {
        long[][] counts = {{1, 2}, {3, 4}};
        try {
            double pValue = chiSquareTest.chiSquareTest(counts);
            assertTrue(pValue > 0.0 && pValue < 1.0);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareTestWith2WayTableAndAlpha() {
        long[][] counts = {{1, 2}, {3, 4}};
        try {
            assertFalse(chiSquareTest.chiSquareTest(counts, 0.01));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        assertEquals(0.0, chiSquareTest.chiSquareDataSetsComparison(observed1, observed2), 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        try {
            double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
            assertTrue(pValue > 0.0 && pValue < 1.0);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        try {
            assertFalse(chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.01));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}