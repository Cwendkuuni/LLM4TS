import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        assertEquals(0.0, chiSquareTest.chiSquare(expected, observed), 0.0001);
    }

    @Test
    public void testChiSquareWithIncorrectLengthArrays() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> {
            chiSquareTest.chiSquare(expected, observed);
        });
    }

    @Test
    public void testChiSquareTest() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        assertTrue(chiSquareTest.chiSquareTest(expected, observed) >= 0.0);
    }

    @Test
    public void testChiSquareTestWithInvalidAlpha() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> {
            chiSquareTest.chiSquareTest(expected, observed, -0.5);
        });
    }

    // Additional test cases for other methods can be added here

}