import static org.junit.Assert.*;
import org.junit.Test;

public class ChiSquareTestImplTest {
    
    @Test
    public void testChiSquare() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 4.0};
        long[] observed = {1, 2, 3};
        assertEquals(0.5, chiSquareTest.chiSquare(expected, observed), 0.0001);
    }
    
    @Test
    public void testChiSquareTest() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 4.0};
        long[] observed = {1, 2, 3};
        try {
            assertEquals(0.352, chiSquareTest.chiSquareTest(expected, observed), 0.001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testChiSquareTestWithAlpha() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 4.0};
        long[] observed = {1, 2, 3};
        try {
            assertFalse(chiSquareTest.chiSquareTest(expected, observed, 0.05));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testChiSquareTwoWayTable() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        assertEquals(0.163636, chiSquareTest.chiSquare(counts), 0.0001);
    }
    
    @Test
    public void testChiSquareTestTwoWayTable() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        try {
            assertEquals(0.884, chiSquareTest.chiSquareTest(counts), 0.001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testChiSquareTestTwoWayTableWithAlpha() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
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
        long[] observed2 = {3, 2, 1};
        assertEquals(1.2, chiSquareTest.chiSquareDataSetsComparison(observed1, observed2), 0.0001);
    }
    
    @Test
    public void testChiSquareTestDataSetsComparison() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        try {
            assertEquals(0.456, chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2), 0.001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        try {
            assertFalse(chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}