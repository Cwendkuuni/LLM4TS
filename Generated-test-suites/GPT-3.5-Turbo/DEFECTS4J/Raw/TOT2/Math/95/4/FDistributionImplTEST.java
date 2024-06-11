import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {
    
    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        double result = fDistribution.cumulativeProbability(1.5);
        assertEquals(0.465686, result, 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        double result = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(1.000000, result, 0.000001);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        fDistribution.setNumeratorDegreesOfFreedom(8);
        assertEquals(8, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        fDistribution.setDenominatorDegreesOfFreedom(10);
        assertEquals(10, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomException() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        fDistribution.setNumeratorDegreesOfFreedom(-4);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomException() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        fDistribution.setDenominatorDegreesOfFreedom(-6);
    }
    
}