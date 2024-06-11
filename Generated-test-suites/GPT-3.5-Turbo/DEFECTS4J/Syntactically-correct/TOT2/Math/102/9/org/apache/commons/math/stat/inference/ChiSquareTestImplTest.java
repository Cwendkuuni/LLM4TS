import static org.junit.Assert.*;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        assertEquals(0.0, chiSquareTest.chiSquare(expected, observed), 0.0001);
    }

    @Test
    public void testChiSquareTest() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        try {
            assertEquals(1.0, chiSquareTest.chiSquareTest(expected, observed), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareTestWithAlpha() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        try {
            assertFalse(chiSquareTest.chiSquareTest(expected, observed, 0.05));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareWithTwoWayTable() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {4, 5, 6}};
        assertEquals(0.0, chiSquareTest.chiSquare(counts), 0.0001);
    }

    @Test
    public void testChiSquareTestWithTwoWayTable() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {4, 5, 6}};
        try {
            assertEquals(1.0, chiSquareTest.chiSquareTest(counts), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareTestWithAlphaAndTwoWayTable() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {4, 5, 6}};
        try {
            assertFalse(chiSquareTest.chiSquareTest(counts, 0.05));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        assertEquals(0.0, chiSquareTest.chiSquareDataSetsComparison(observed1, observed2), 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        try {
            assertEquals(1.0, chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        try {
            assertFalse(chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

}