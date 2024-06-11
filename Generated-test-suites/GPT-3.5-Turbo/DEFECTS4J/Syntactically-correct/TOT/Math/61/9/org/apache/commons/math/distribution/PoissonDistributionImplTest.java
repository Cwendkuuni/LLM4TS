import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {
    
    private PoissonDistributionImpl poisson;

    @Before
    public void setUp() {
        poisson = new PoissonDistributionImpl(5.0);
    }

    @Test
    public void testGetMean() {
        assertEquals(5.0, poisson.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.006737946999085467, poisson.probability(1), 0.000000001);
    }

    @Test
    public void testProbability_negativeValue() {
        assertEquals(0.0, poisson.probability(-2), 0.000000001);
    }

    @Test
    public void testProbability_maximumValue() {
        assertEquals(0.0, poisson.probability(Integer.MAX_VALUE), 0.000000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.03368973499542734, poisson.cumulativeProbability(2), 0.000000001);
    }

    @Test
    public void testCumulativeProbability_negativeValue() throws MathException {
        assertEquals(0.0, poisson.cumulativeProbability(-2), 0.000000001);
    }

    @Test
    public void testCumulativeProbability_maximumValue() throws MathException {
        assertEquals(1.0, poisson.cumulativeProbability(Integer.MAX_VALUE), 0.000000001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.9313837709805785, poisson.normalApproximateProbability(2), 0.000000001);
    }

    @Test
    public void testSample() throws MathException {
        int sample = poisson.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, poisson.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.5));
    }

}