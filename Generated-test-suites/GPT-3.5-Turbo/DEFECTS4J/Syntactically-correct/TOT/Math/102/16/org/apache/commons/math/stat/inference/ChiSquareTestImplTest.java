import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTest;

    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl();
    }

    @Test
    public void testChiSquare_WithValidInputs_ReturnsChiSquareValue() {
        double[] expected = {1, 2, 3, 4, 5};
        long[] observed = {1, 2, 3, 4, 5};
        double result = chiSquareTest.chiSquare(expected, observed);
        Assert.assertEquals(0.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_WithInvalidInputs_ThrowsIllegalArgumentException() {
        double[] expected = {1, 2, 3, 4, 5};
        long[] observed = {1, 2, 3, 4};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_WithInvalidInputs_ThrowsIllegalArgumentException2() {
        double[] expected = {1, 2, 3};
        long[] observed = {1, 2, 3, 4};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_WithNegativeExpectedValues_ThrowsIllegalArgumentException() {
        double[] expected = {1, -2, 3, 4, 5};
        long[] observed = {1, 2, 3, 4, 5};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_WithNegativeObservedValues_ThrowsIllegalArgumentException() {
        double[] expected = {1, 2, 3, 4, 5};
        long[] observed = {1, 2, 3, -4, 5};
        chiSquareTest.chiSquare(expected, observed);
    }

    // More test cases for other methods...

}