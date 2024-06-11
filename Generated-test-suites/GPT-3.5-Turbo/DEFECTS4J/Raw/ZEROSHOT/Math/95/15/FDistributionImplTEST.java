import org.junit.Test;

public class FDistributionImplTest {
    
    // Test cumulativeProbability method
    
    @Test
    public void testCumulativeProbabilityWhenXIsZero() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(1, 1);
        double result = fDist.cumulativeProbability(0);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testCumulativeProbabilityWhenXIsGreaterThanZero() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(1, 1);
        double result = fDist.cumulativeProbability(2.5);
        assertEquals(0.9863, result, 0.0001);
    }
    
    @Test
    public void testCumulativeProbabilityWhenXIsLessThanZero() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(1, 1);
        double result = fDist.cumulativeProbability(-2);
        assertEquals(0.0, result, 0.0001);
    }
    
    // Test inverseCumulativeProbability method
    
    @Test
    public void testInverseCumulativeProbabilityWhenPIsZero() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(1, 1);
        double result = fDist.inverseCumulativeProbability(0);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityWhenPIsOne() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(1, 1);
        double result = fDist.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0001);
    }
    
    // Test set and get methods
    
    @Test
    public void testSetAndgetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(1, 1);
        fDist.setNumeratorDegreesOfFreedom(2);
        double result = fDist.getNumeratorDegreesOfFreedom();
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testSetAndgetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(1, 1);
        fDist.setDenominatorDegreesOfFreedom(2);
        double result = fDist.getDenominatorDegreesOfFreedom();
        assertEquals(2.0, result, 0.0001);
    }
    
}