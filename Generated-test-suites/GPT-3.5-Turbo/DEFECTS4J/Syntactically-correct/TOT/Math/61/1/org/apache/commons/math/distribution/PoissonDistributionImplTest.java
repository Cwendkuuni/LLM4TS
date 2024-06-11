import org.junit.Test;
import org.apache.commons.math.MathException;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {
    
    @Test
    public void testGetMean() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5);
        assertEquals(5, pd.getMean(), 0);
    }
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5);
        assertEquals(0.03368973499542734, pd.probability(3), 0.000001);
        assertEquals(0.17546736976785063, pd.probability(10), 0.000001);
        assertEquals(0, pd.probability(-1), 0);
        assertEquals(0, pd.probability(Integer.MAX_VALUE), 0);
    }
    
    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5);
        try {
            assertEquals(0.2650259152973616, pd.cumulativeProbability(3), 0.000001);
            assertEquals(0.9925897863220421, pd.cumulativeProbability(10), 0.000001);
            assertEquals(0, pd.cumulativeProbability(-1), 0);
            assertEquals(1, pd.cumulativeProbability(Integer.MAX_VALUE), 0);
        } catch (MathException e) {
            fail("MathException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5);
        try {
            assertEquals(0.6988107008427733, pd.normalApproximateProbability(3), 0.000001);
            assertEquals(0.9786526228860753, pd.normalApproximateProbability(10), 0.000001);
        } catch (MathException e) {
            fail("MathException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSample() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5);
        try {
            int sample = pd.sample();
            assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        } catch (MathException e) {
            fail("MathException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5);
        assertEquals(0, pd.getDomainLowerBound(0), 0);
        assertEquals(Integer.MIN_VALUE, pd.getDomainLowerBound(0.5), 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5);
        assertEquals(Integer.MAX_VALUE, pd.getDomainUpperBound(0), 0);
        assertEquals(Integer.MAX_VALUE, pd.getDomainUpperBound(0.5), 0);
    }
}