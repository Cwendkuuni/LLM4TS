import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normal;

    @Before
    public void setup() {
        normal = new NormalDistributionImpl(0.0, 1.0);
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, normal.getMean(), 0.001);
    }

    @Test
    public void testSetMean() {
        normal.setMean(2.5);
        assertEquals(2.5, normal.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normal.getStandardDeviation(), 0.001);
    }

    @Test
    public void testSetStandardDeviation() {
        normal.setStandardDeviation(2.0);
        assertEquals(2.0, normal.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normal.cumulativeProbability(0.0), 0.001);
        assertEquals(0.8413, normal.cumulativeProbability(1.0), 0.001);
        assertEquals(0.1587, normal.cumulativeProbability(-1.0), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0.0, normal.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(1.0, normal.inverseCumulativeProbability(0.8413), 0.001);
        assertEquals(-1.0, normal.inverseCumulativeProbability(0.1587), 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInput() throws MathException {
        normal.inverseCumulativeProbability(1.5);
    }

    @Test
    public void testInverseCumulativeProbabilityUpperBound() throws MathException {
        assertEquals(Double.POSITIVE_INFINITY, normal.inverseCumulativeProbability(1.0), 0.001);
    }

    @Test
    public void testInverseCumulativeProbabilityLowerBound() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normal.inverseCumulativeProbability(0.0), 0.001);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normal.getDomainLowerBound(0.4), 0.001);
        assertEquals(0.0, normal.getDomainLowerBound(0.6), 0.001);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0.0, normal.getDomainUpperBound(0.4), 0.001);
        assertEquals(Double.MAX_VALUE, normal.getDomainUpperBound(0.6), 0.001);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(0.0, normal.getInitialDomain(0.5), 0.001);
        assertEquals(-1.0, normal.getInitialDomain(0.2), 0.001);
        assertEquals(1.0, normal.getInitialDomain(0.8), 0.001);
    }
}