import org.junit.Test;

import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(0.385164807134504, f.density(0.5), 1e-9);
        assertEquals(0.2706712539305469, f.density(1), 1e-9);
        assertEquals(0.09975062399632685, f.density(2), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(0, f.cumulativeProbability(-1), 1e-9);
        assertEquals(0, f.cumulativeProbability(0), 1e-9);
        assertEquals(0.8575513412183925, f.cumulativeProbability(1), 1e-9);
        assertEquals(0.947281454279538, f.cumulativeProbability(2), 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(2, f.getNumeratorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(3, f.getDenominatorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(3, f.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(9, f.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(0, f.getSupportLowerBound(), 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(Double.POSITIVE_INFINITY, f.getSupportUpperBound(), 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(true, f.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(false, f.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(true, f.isSupportConnected());
    }
}