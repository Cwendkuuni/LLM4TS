import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChiSquareTestImplTest {
    
    private ChiSquareTestImpl chiSquareTest;

    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl();
    }

    @Test
    public void testChiSquare() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {4, 5, 6};
        
        double result = chiSquareTest.chiSquare(expected, observed);
        
        assertEquals(0.44444, result, 0.00001);
    }

    @Test
    public void testChiSquareTest() throws MathException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {4, 5, 6};
        double alpha = 0.05;
        
        boolean result = chiSquareTest.chiSquareTest(expected, observed, alpha);
        
        assertTrue(result);
    }

    @Test
    public void testChiSquare() {
        long[][] counts = {
            {1, 2},
            {3, 4}
        };
        
        double result = chiSquareTest.chiSquare(counts);
        
        assertEquals(2.91666, result, 0.00001);
    }

    @Test
    public void testChiSquareTest() throws MathException {
        long[][] counts = {
            {1, 2},
            {3, 4}
        };
        double alpha = 0.05;
        
        boolean result = chiSquareTest.chiSquareTest(counts, alpha);
        
        assertTrue(result);
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        
        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        
        assertEquals(0.44444, result, 0.00001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        double alpha = 0.05;
        
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        
        assertTrue(result);
    }
}