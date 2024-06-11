import static org.junit.Assert.*;
import org.junit.Test;

public class ChiSquareTestImplTest {
    
    @Test
    public void testChiSquare() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        
        double result = chiSquareTest.chiSquare(expected, observed);
        
        assertEquals(0.25, result, 0.001);
    }
    
    @Test
    public void testChiSquareTest() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        
        double result = chiSquareTest.chiSquareTest(expected, observed);
        
        assertEquals(0.75, result, 0.001);
    }
    
    @Test
    public void testChiSquareTestDataSetsComparison() {
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {1, 2, 3};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        
        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        
        assertEquals(0.078, result, 0.001);
    }
    
    @Test
    public void testChiSquareTestDataSetsComparison() {
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {1, 2, 3};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        
        double result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        
        assertEquals(0.922, result, 0.001);
    }
    
    @Test
    public void testChiSquareTestErrorHandling() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}