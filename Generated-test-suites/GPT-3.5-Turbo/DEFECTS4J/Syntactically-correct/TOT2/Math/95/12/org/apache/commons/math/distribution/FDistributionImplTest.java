import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionImplTest {
    
    @Test
    public void testCumulativeProbability_WithNegativeX_ShouldReturnZero() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.cumulativeProbability(-1.0);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testCumulativeProbability_WithValidX_ShouldReturnCorrectValue() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.cumulativeProbability(3.0);
        assertEquals(0.98166, result, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability_WithPZero_ShouldReturnZero() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.inverseCumulativeProbability(0.0);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability_WithPOne_ShouldReturnInfinity() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability_WithValidP_ShouldReturnCorrectValue() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.inverseCumulativeProbability(0.8);
        assertEquals(2.82842, result, 0.0001);
    }
    
}