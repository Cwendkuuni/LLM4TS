
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {4L, 5L, 6L};
        
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(2.3333, result, 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidLength() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        
        double[] expected = {1.0, 2.0};
        long[] observed = {4L, 5L, 6L};
        
        chiSquareTest.chiSquare(expected, observed);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidCounts() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {-4L, 5L, 6L};
        
        chiSquareTest.chiSquare(expected, observed);
    }
    
    // Additional test cases for all public methods in the class ...
}

