import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.util.LocalizedFormats;

public class NormalDistributionImplTest {

    //Test density method
    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(0), 0);
    }

    //Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0), 0);
    }

    //Test inverseCumulativeProbability method
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.inverseCumulativeProbability(0.5), 0);
    }

    //Test sample method
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.sample();
        assertTrue(result >= Double.NEGATIVE_INFINITY && result <= Double.POSITIVE_INFINITY);
    }

    //Test getMean method
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0);
    }

    //Test getStandardDeviation method
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(1, distribution.getStandardDeviation(), 0);
    }

    //Test getSolverAbsoluteAccuracy method
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    //Test getDomainLowerBound method
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.2), 0);
    }

    //Test getDomainUpperBound method
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.8), 0);
    }

    //Test getInitialDomain method
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1, distribution.getInitialDomain(0.2), 0);
    }
}