import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        try {
            assertEquals(0.0, fDistribution.cumulativeProbability(-1), 0.0001);
            assertEquals(0.028382584644189996, fDistribution.cumulativeProbability(0.5), 0.0001);
            assertEquals(0.16798555206209193, fDistribution.cumulativeProbability(1), 0.0001);
            assertEquals(0.2104694530680394, fDistribution.cumulativeProbability(1.5), 0.0001);
            assertEquals(0.24448567472220943, fDistribution.cumulativeProbability(2), 0.0001);
            assertEquals(0.5281042467003846, fDistribution.cumulativeProbability(5), 0.0001);
            assertEquals(0.8188728101098948, fDistribution.cumulativeProbability(10), 0.0001);
            assertEquals(1.0, fDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        try {
            assertEquals(0.0, fDistribution.inverseCumulativeProbability(0), 0.0001);
            assertEquals(1.2916417340506024, fDistribution.inverseCumulativeProbability(0.2), 0.0001);
            assertEquals(2.2527803352782533, fDistribution.inverseCumulativeProbability(0.5), 0.0001);
            assertEquals(6.944800605051038, fDistribution.inverseCumulativeProbability(0.9), 0.0001);
            assertEquals(2.088992027665923, fDistribution.inverseCumulativeProbability(0.3), 0.0001);
            assertEquals(2.837120567828898, fDistribution.inverseCumulativeProbability(0.4), 0.0001);
            assertEquals(3.6862641355260925, fDistribution.inverseCumulativeProbability(0.6), 0.0001);
            assertEquals(5.881643599780639, fDistribution.inverseCumulativeProbability(0.8), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.5), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(2.0, fDistribution.getInitialDomain(0.5), 0.0001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        fDistribution.setNumeratorDegreesOfFreedom(5);
        assertEquals(5.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomInvalid() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        fDistribution.setNumeratorDegreesOfFreedom(-1);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(2.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        fDistribution.setDenominatorDegreesOfFreedom(5);
        assertEquals(5.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomInvalid() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        fDistribution.setDenominatorDegreesOfFreedom(-1);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }
}