import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(2.0, 3.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0001);
        assertEquals(0.1439, fDistribution.cumulativeProbability(0.5), 0.0001);
        assertEquals(0.3159, fDistribution.cumulativeProbability(1.0), 0.0001);
        assertEquals(0.4478, fDistribution.cumulativeProbability(2.0), 0.0001);
        assertEquals(0.6844, fDistribution.cumulativeProbability(5.0), 0.0001);
        assertEquals(0.8348, fDistribution.cumulativeProbability(10.0), 0.0001);
        assertEquals(1.0, fDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(0.068, fDistribution.inverseCumulativeProbability(0.1), 0.0001);
        assertEquals(0.194, fDistribution.inverseCumulativeProbability(0.2), 0.0001);
        assertEquals(0.33, fDistribution.inverseCumulativeProbability(0.3), 0.0001);
        assertEquals(1.383, fDistribution.inverseCumulativeProbability(0.7), 0.0001);
        assertEquals(4.340, fDistribution.inverseCumulativeProbability(0.9), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.0), 0.0001);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0.0001);
        assertEquals(0.0, fDistribution.getDomainLowerBound(1.0), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.0), 0.0001);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.5), 0.0001);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(1.0), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(1.5, fDistribution.getInitialDomain(0.0), 0.0001);
        assertEquals(1.5, fDistribution.getInitialDomain(0.5), 0.0001);
        assertEquals(1.5, fDistribution.getInitialDomain(1.0), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithNegativeValue() {
        fDistribution.setNumeratorDegreesOfFreedom(-2.0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedomWithPositiveValue() {
        fDistribution.setNumeratorDegreesOfFreedom(2.0);
        assertEquals(2.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithNegativeValue() {
        fDistribution.setDenominatorDegreesOfFreedom(-2.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedomWithPositiveValue() {
        fDistribution.setDenominatorDegreesOfFreedom(3.0);
        assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }
}