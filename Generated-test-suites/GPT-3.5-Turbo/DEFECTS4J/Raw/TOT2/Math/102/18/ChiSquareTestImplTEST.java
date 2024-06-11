import static org.junit.Assert.*;
import org.junit.Test;

public class ChiSquareTestImplTest {
    
    // Test case for constructor 1
    @Test
    public void testConstructor1() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        assertNotNull(chiSquareTest);
    }
    
    // Test case for constructor 2
    @Test
    public void testConstructor2() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(distribution);
        assertNotNull(chiSquareTest);
    }
    
    // Test case for chiSquare method
    @Test
    public void testChiSquare() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(0.0, result, 0);
    }
    
    // Test case for chiSquareTest method
    @Test
    public void testChiSquareTest() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        try {
            double result = chiSquareTest.chiSquareTest(expected, observed);
            assertEquals(1.0, result, 0);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    // Test case for chiSquareTest method with invalid alpha
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidAlpha() throws Exception {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        chiSquareTest.chiSquareTest(expected, observed, 0.6);
    }
    
    // Test case for chiSquare method with empty arrays
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareEmptyArrays() throws Exception {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {};
        long[] observed = {};
        chiSquareTest.chiSquare(expected, observed);
    }
    
    // Test case for chiSquare method with different array lengths
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDifferentArrayLengths() throws Exception {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2, 3};
        chiSquareTest.chiSquare(expected, observed);
    }
    
    // Add more test cases for other methods
    
}