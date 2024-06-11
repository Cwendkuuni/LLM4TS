import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        try {
            assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0001);
            assertEquals(0.676533, fDistribution.cumulativeProbability(1.0), 0.0001);
            // Add additional test cases for edge cases and error scenarios here
        } catch (MathException e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        try {
            assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.0001);
            // Add additional test cases for edge cases and error scenarios here
        } catch (MathException e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0.0001);
        // Add additional test cases for edge cases here
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.5), 0.0001);
        // Add additional test cases for edge cases here
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(3.0, fDistribution.getInitialDomain(0.5), 0.0001);
        // Add additional test cases for edge cases here
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        fDistribution.setNumeratorDegreesOfFreedom(4.0);
        assertEquals(4.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
        // Add additional test cases for edge cases here
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        fDistribution.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
        // Add additional test cases for edge cases here
    }
}