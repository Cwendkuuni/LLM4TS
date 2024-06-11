import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);
        assertEquals(0.22404180765538775, pd.probability(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);
        try {
            assertEquals(0.42319008112684364, pd.cumulativeProbability(4), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);
        try {
            assertEquals(0.8120103896586096, pd.normalApproximateProbability(5), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);
        try {
            int sample = pd.sample();
            assertTrue(sample >= 0);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);
        assertEquals(3.0, pd.getMean(), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);
        assertEquals(0, pd.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);
        assertEquals(Integer.MAX_VALUE, pd.getDomainUpperBound(0.5));
    }
}