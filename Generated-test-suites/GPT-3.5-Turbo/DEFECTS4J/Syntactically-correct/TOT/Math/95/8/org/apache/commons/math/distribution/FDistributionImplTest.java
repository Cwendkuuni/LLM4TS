import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionImplTest {

    private double epsilon = 1e-6;

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl f = new FDistributionImpl(5, 10);
        assertEquals(0.91245, f.cumulativeProbability(0.5), epsilon);
        assertEquals(0.97608, f.cumulativeProbability(1.0), epsilon);
        assertEquals(1.0, f.cumulativeProbability(100), epsilon);

        // edge case: x <= 0
        assertEquals(0.0, f.cumulativeProbability(-5.0), epsilon);
        assertEquals(0.0, f.cumulativeProbability(0.0), epsilon);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl f = new FDistributionImpl(5, 10);
        assertEquals(0.34588, f.inverseCumulativeProbability(0.2), epsilon);
        assertEquals(0.348429, f.inverseCumulativeProbability(0.21), epsilon);
        assertEquals(0.5, f.inverseCumulativeProbability(0.5), epsilon);
        assertEquals(1.61281, f.inverseCumulativeProbability(0.9), epsilon);

        // edge cases: p = 0 or 1
        assertEquals(0.0, f.inverseCumulativeProbability(0.0), epsilon);
        assertEquals(Double.POSITIVE_INFINITY, f.inverseCumulativeProbability(1.0), epsilon);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl f = new FDistributionImpl(5, 10);
        assertEquals(0.0, f.getDomainLowerBound(0.2), epsilon);
        assertEquals(0.0, f.getDomainLowerBound(0.5), epsilon);
        // edge case: p = 0 -> returns 0
        assertEquals(0.0, f.getDomainLowerBound(0.0), epsilon);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl f = new FDistributionImpl(5, 10);
        assertEquals(Double.MAX_VALUE, f.getDomainUpperBound(0.2), epsilon);
        assertEquals(Double.MAX_VALUE, f.getDomainUpperBound(0.5), epsilon);
        // edge case: p = 1 -> returns Double.MAX_VALUE
        assertEquals(Double.MAX_VALUE, f.getDomainUpperBound(1.0), epsilon);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl f = new FDistributionImpl(5, 10);
        assertEquals(2.25, f.getInitialDomain(0.2), epsilon);
        assertEquals(2.25, f.getInitialDomain(0.5), epsilon);
        // edge case: p = 0 -> returns mean
        assertEquals(2.0, f.getInitialDomain(0.0), epsilon);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomException() {
        FDistributionImpl f = new FDistributionImpl(5, 10);
        f.setNumeratorDegreesOfFreedom(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomException() {
        FDistributionImpl f = new FDistributionImpl(5, 10);
        f.setDenominatorDegreesOfFreedom(0);
    }

}