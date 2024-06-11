
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {
    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        // Initialize a test instance of FDistributionImpl
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 10.0;
        fDistribution = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 0.5;
        double result = fDistribution.cumulativeProbability(x);
        assertEquals(0.3432731418536789, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        double p = 0.8;
        double result = fDistribution.inverseCumulativeProbability(p);
        assertEquals(0.7467086201967259, result, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        double p = 0.2;
        double result = fDistribution.getDomainLowerBound(p);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        double p = 0.8;
        double result = fDistribution.getDomainUpperBound(p);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        double p = 0.5;
        double result = fDistribution.getInitialDomain(p);
        assertEquals(1.25, result, 0.0001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        double newNumeratorDegreesOfFreedom = 7.0;
        fDistribution.setNumeratorDegreesOfFreedom(newNumeratorDegreesOfFreedom);
        assertEquals(newNumeratorDegreesOfFreedom, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double result = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        double newDenominatorDegreesOfFreedom = 15.0;
        fDistribution.setDenominatorDegreesOfFreedom(newDenominatorDegreesOfFreedom);
        assertEquals(newDenominatorDegreesOfFreedom, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double result = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(10.0, result, 0.0001);
    }
    
    // Additional test cases for edge cases and exceptions:
    @Test
    public void testCumulativeProbabilityWithZeroX() {
        double x = 0.0;
        double result = fDistribution.cumulativeProbability(x);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testCumulativeProbabilityWithNegativeX() {
        double x = -1.0;
        double result = fDistribution.cumulativeProbability(x);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityWithZeroP() {
        double p = 0.0;
        double result = fDistribution.inverseCumulativeProbability(p);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityWithOneP() {
        double p = 1.0;
        double result = fDistribution.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0001);
    }
}