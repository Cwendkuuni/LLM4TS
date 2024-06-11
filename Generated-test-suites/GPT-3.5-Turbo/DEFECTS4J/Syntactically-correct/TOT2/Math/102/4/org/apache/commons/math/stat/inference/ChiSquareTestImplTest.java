import static org.junit.Assert.*;
import org.junit.Test;

public class ChiSquareTestImplTest {
    
    @Test
    public void testConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test);
    }

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        double result = test.chiSquare(expected, observed);
        assertEquals(0.6666666666666666, result, 0.0001);
    }

    @Test
    public void testChiSquareTest() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        double result = test.chiSquareTest(expected, observed);
        assertEquals(0.4363443452418952, result, 0.0001);
    }

    @Test
    public void testChiSquareTestWithAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        double alpha = 0.05;
        boolean result = test.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
    }

    @Test
    public void testChiSquareLongArray() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{2, 3}, {4, 5}};
        double result = test.chiSquare(counts);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testChiSquareTestWithLongArray() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{2, 3}, {4, 5}};
        double result = test.chiSquareTest(counts);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testChiSquareTestWithLongArrayAndAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{2, 3}, {4, 5}};
        double alpha = 0.05;
        boolean result = test.chiSquareTest(counts, alpha);
        assertFalse(result);
    }
    
    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {3, 4, 5};
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.007142857142857143, result, 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {3, 4, 5};
        double result = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.10454769080227376, result, 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {3, 4, 5};
        double alpha = 0.05;
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertFalse(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] invalidInput = {{2, 3}, {4}};
        test.checkArray(invalidInput);
    }
}