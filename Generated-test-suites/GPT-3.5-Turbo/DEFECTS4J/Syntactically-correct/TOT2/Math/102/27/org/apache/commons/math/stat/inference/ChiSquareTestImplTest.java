import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareBasic() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {20, 30, 50};
        long[] observed = {18, 28, 52};
        assertEquals(0.06666666666666667, chiSquareTest.chiSquare(expected, observed), 0.0001);
    }

    @Test
    public void testChiSquareTestBasic() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {20, 30, 50};
        long[] observed = {18, 28, 52};
        assertEquals(0.8815909837936205, chiSquareTest.chiSquareTest(expected, observed), 0.0001);
    }

    @Test
    public void testChiSquareDataSetsComparisonBasic() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {10, 15, 25};
        long[] observed2 = {12, 18, 28};
        assertEquals(0.03333333333333333, chiSquareTest.chiSquareDataSetsComparison(observed1, observed2), 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonBasic() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {10, 15, 25};
        long[] observed2 = {12, 18, 28};
        assertEquals(0.868347313786721, chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2), 0.0001);
    }

    @Test
    public void testChiSquareTestException() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {20, 30, 50};
        long[] observed = {18, 28};
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testChiSquareTestDataSetsComparisonException() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {10, 15, 25};
        long[] observed2 = {12, 18};
        try {
            chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}