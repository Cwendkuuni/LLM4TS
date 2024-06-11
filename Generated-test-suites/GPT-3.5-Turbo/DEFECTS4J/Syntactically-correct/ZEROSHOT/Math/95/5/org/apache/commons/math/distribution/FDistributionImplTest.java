import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {
    
    private FDistributionImpl fDistribution;
    
    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(4.0, 6.0);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(4.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(6.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        double result = fDistribution.cumulativeProbability(2.0);
        assertEquals(0.470580, result, 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double result = fDistribution.inverseCumulativeProbability(0.3);
        assertEquals(0.612965, result, 0.000001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        double result = fDistribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        double result = fDistribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0.0);
    }
    
    @Test
    public void testGetInitialDomain() {
        double result = fDistribution.getInitialDomain(0.5);
        assertEquals(1.5, result, 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        fDistribution.setNumeratorDegreesOfFreedom(-4.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        fDistribution.setDenominatorDegreesOfFreedom(-6.0);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        fDistribution.setNumeratorDegreesOfFreedom(8.0);
        assertEquals(8.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        fDistribution.setDenominatorDegreesOfFreedom(12.0);
        assertEquals(12.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
    
}