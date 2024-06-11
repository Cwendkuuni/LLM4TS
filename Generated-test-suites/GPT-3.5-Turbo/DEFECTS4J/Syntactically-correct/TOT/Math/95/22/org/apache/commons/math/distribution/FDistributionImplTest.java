import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.special.Beta;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {
    
    private FDistributionImpl fDistribution;
    
    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(12.0, 15.0);
    }
    
    @Test
    public void testCumulativeProbability() {
        try {
            double result = fDistribution.cumulativeProbability(0.5);
            assertEquals(0.1849, result, 0.0001);
        } catch (MathException e) {
            fail("MathException occurred");
        }
    }
    
    @Test
    public void testCumulativeProbabilityWithZero() {
        try {
            double result = fDistribution.cumulativeProbability(0.0);
            assertEquals(0.0, result, 0.0);
        } catch (MathException e) {
            fail("MathException occurred");
        }
    }
    
    @Test
    public void testCumulativeProbabilityWithNegativeValue() {
        try {
            double result = fDistribution.cumulativeProbability(-1.0);
            assertEquals(0.0, result, 0.0);
        } catch (MathException e) {
            fail("MathException occurred");
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        try {
            double result = fDistribution.inverseCumulativeProbability(0.5);
            assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
        } catch (MathException e) {
            fail("MathException occurred");
        }
    }
    
    @Test
    public void testInverseCumulativeProbabilityWithZero() {
        try {
            double result = fDistribution.inverseCumulativeProbability(0.0);
            assertEquals(0.0, result, 0.0);
        } catch (MathException e) {
            fail("MathException occurred");
        }
    }
    
    @Test
    public void testInverseCumulativeProbabilityWithOne() {
        try {
            double result = fDistribution.inverseCumulativeProbability(1.0);
            assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
        } catch (MathException e) {
            fail("MathException occurred");
        }
    }
    
    @Test
    public void testInverseCumulativeProbabilityWithInvalidProbability() {
        try {
            double result = fDistribution.inverseCumulativeProbability(1.5);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("p is not a valid probability.", e.getMessage());
        } catch (MathException e) {
            fail("MathException occurred");
        }
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
        assertEquals(1.2, result, 0.0);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        try {
            fDistribution.setNumeratorDegreesOfFreedom(10.0);
            double result = fDistribution.getNumeratorDegreesOfFreedom();
            assertEquals(10.0, result, 0.0);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException occurred");
        }
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedomWithZero() {
        try {
            fDistribution.setNumeratorDegreesOfFreedom(0.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("degrees of freedom must be positive.", e.getMessage());
        }
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        try {
            fDistribution.setDenominatorDegreesOfFreedom(20.0);
            double result = fDistribution.getDenominatorDegreesOfFreedom();
            assertEquals(20.0, result, 0.0);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException occurred");
        }
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedomWithZero() {
        try {
            fDistribution.setDenominatorDegreesOfFreedom(0.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("degrees of freedom must be positive.", e.getMessage());
        }
    }
}