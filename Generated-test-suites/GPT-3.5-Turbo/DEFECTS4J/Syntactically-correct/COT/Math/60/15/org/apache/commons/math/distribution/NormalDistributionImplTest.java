import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    private static final double DELTA = 1e-9;
    
    private NormalDistributionImpl normalDistribution;
    
    @Before
    public void setup() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), DELTA);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), DELTA);
    }
    
    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, normalDistribution.density(0), DELTA);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), DELTA);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0, normalDistribution.inverseCumulativeProbability(0.5), DELTA);
    }
    
    @Test
    public void testSample() throws MathException {
        double sample = normalDistribution.sample();
        assertTrue(sample <= 3 && sample >= -3);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidStandardDeviation() {
        NormalDistributionImpl invalidDistribution = new NormalDistributionImpl(0, -1);
    }
    
    @Test
    public void testExtremeInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), DELTA);
    }
    
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(1e-9, normalDistribution.getSolverAbsoluteAccuracy(), DELTA);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0), DELTA);
        assertEquals(0, normalDistribution.getDomainLowerBound(0.5), DELTA);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0, normalDistribution.getDomainUpperBound(0), DELTA);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.5), DELTA);
    }
    
    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normalDistribution.getInitialDomain(0.25), DELTA);
        assertEquals(1, normalDistribution.getInitialDomain(0.75), DELTA);
        assertEquals(0, normalDistribution.getInitialDomain(0.5), DELTA);
    }
}