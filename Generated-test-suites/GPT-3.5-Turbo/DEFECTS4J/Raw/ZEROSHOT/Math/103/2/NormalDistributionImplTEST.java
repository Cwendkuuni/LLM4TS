import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl distribution;

    @Before
    public void setup() {
        distribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        double mean = distribution.getMean();
        assertEquals(0.0, mean, 0.0);
    }

    @Test
    public void testSetMean() {
        distribution.setMean(5.0);
        double mean = distribution.getMean();
        assertEquals(5.0, mean, 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        double sd = distribution.getStandardDeviation();
        assertEquals(1.0, sd, 0.0);
    }

    @Test
    public void testSetStandardDeviation() {
        distribution.setStandardDeviation(2.0);
        double sd = distribution.getStandardDeviation();
        assertEquals(2.0, sd, 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double x = 1.0;
        double cumulativeProbability = distribution.cumulativeProbability(x);
        assertEquals(0.841344746, cumulativeProbability, 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double p = 0.5;
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
        assertEquals(0.0, inverseCumulativeProbability, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
        double p = 1.5;
        distribution.inverseCumulativeProbability(p);
    }

    @Test
    public void testGetDomainLowerBound() {
        double p = 0.3;
        double lowerBound = distribution.getDomainLowerBound(p);
        assertEquals(-Double.MAX_VALUE, lowerBound, 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        double p = 0.7;
        double upperBound = distribution.getDomainUpperBound(p);
        assertEquals(Double.MAX_VALUE, upperBound, 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        double p = 0.5;
        double initialDomain = distribution.getInitialDomain(p);
        assertEquals(0.0, initialDomain, 0.0);
    }
}