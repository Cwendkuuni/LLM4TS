import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2, 4);
        assertEquals(0.2290703, fd.density(0.5), 0.000001);
        assertEquals(0, fd.density(0), 0.000001);
        assertEquals(0.0625, fd.density(1), 0.000001);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2, 5);
        assertEquals(0.15624999907644067, fd.cumulativeProbability(0.5), 0.000001);
        assertEquals(0, fd.cumulativeProbability(0), 0.000001);
        assertEquals(0.25937496185302734, fd.cumulativeProbability(1), 0.000001);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(4, 6);
        assertEquals(4, fd.getNumeratorDegreesOfFreedom(), 0.000001);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(4, 6);
        assertEquals(6, fd.getDenominatorDegreesOfFreedom(), 0.000001);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(3, 6);
        assertEquals(1.5, fd.getNumericalMean(), 0.000001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(4, 7);
        assertEquals(7.695652173913044, fd.getNumericalVariance(), 0.000001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(4, 5);
        assertEquals(0, fd.getSupportLowerBound(), 0.000001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(4, 5);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.000001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(4, 5);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(4, 5);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(4, 5);
        assertTrue(fd.isSupportConnected());
    }
}