import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, pd.getMean(), 0.001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);
        assertEquals(0.049787, pd.probability(1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        try {
            assertEquals(0.982, pd.cumulativeProbability(2), 0.001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(4.0);
        try {
            assertEquals(0.691462, pd.normalApproximateProbability(3), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.5);
        try {
            int sample = pd.sample();
            assertTrue(sample >= 0 && sample < Integer.MAX_VALUE);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.0);
        assertEquals(0, pd.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.0);
        assertEquals(Integer.MAX_VALUE, pd.getDomainUpperBound(0.5));
    }
}