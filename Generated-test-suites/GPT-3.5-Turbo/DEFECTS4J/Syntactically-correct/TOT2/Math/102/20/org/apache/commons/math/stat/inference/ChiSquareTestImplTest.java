import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class ChiSquareTestImplTest {
    
    private ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

    @Test
    public void testChiSquareWithValidInputs() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {4, 5, 6};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(0.25, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputs() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {4, 5};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTestWithValidInputs() throws MathException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {4, 5, 6};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(0.5, pValue, 0.0001);
    }

    @Test
    public void testChiSquareTestWithInvalidInputs() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {4, 5};
        assertThrows(IllegalArgumentException.class, () -> {
            chiSquareTest.chiSquareTest(expected, observed);
        });
    }

    @Test
    public void testChiSquareTestWithAlpha() throws MathException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {4, 5, 6};
        double alpha = 0.1;
        boolean result = chiSquareTest.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
    }

    // Additional test cases for other methods can be added here

}