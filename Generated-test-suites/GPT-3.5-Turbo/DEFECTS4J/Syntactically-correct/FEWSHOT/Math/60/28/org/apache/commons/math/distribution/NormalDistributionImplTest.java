import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class NormalDistributionImplTest {
    NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);

    @Test
    public void testGetMean() {
        assertEquals(0, normal.getMean(), 0);
        assertNotEquals(1, normal.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normal.getStandardDeviation(), 0);
        assertNotEquals(2, normal.getStandardDeviation(), 0);
    }
    
    @Test
    public void testDensity() {
        assertEquals(0.24197072451914337, normal.density(0), 1e-9);
        assertNotEquals(1, normal.getMean(), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normal.cumulativeProbability(0), 1e-9);
        assertNotEquals(1, normal.cumulativeProbability(0), 0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0, normal.inverseCumulativeProbability(0.5), 1e-9);
        assertNotEquals(1, normal.inverseCumulativeProbability(0.5), 0);
    }

    @Test
    public void testSample() throws MathException {
        assertNotNull(normal.sample());
    }
    
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(1e-9, normal.getSolverAbsoluteAccuracy(), 0);
        assertNotEquals(0, normal.getSolverAbsoluteAccuracy(), 0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normal.getDomainLowerBound(0.1), 0);
        assertNotEquals(-1, normal.getDomainLowerBound(0.1), 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, normal.getDomainUpperBound(0.9), 0);
        assertNotEquals(1, normal.getDomainUpperBound(0.9), 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normal.getInitialDomain(0.1), 0);
        assertNotEquals(0, normal.getInitialDomain(0.1), 0);
    }
}