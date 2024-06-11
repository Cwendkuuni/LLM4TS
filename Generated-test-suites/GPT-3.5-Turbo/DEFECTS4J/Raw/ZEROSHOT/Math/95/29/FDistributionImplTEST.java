import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDist = new FDistributionImpl(3, 5);
        double result = fDist.cumulativeProbability(2.0);
        assertEquals(0.890533431, result, 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDist = new FDistributionImpl(3, 5);
        double result = fDist.inverseCumulativeProbability(0.3);
        assertEquals(1.650000000, result, 0.000001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDist = new FDistributionImpl(3, 5);
        double result = fDist.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0.000001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDist = new FDistributionImpl(3, 5);
        double result = fDist.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0.000001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDist = new FDistributionImpl(3, 5);
        double result = fDist.getInitialDomain(0.5);
        assertEquals(2.5, result, 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(3, 5);
        fDist.setNumeratorDegreesOfFreedom(0.0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(3, 5);
        double result = fDist.getNumeratorDegreesOfFreedom();
        assertEquals(3.0, result, 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(3, 5);
        fDist.setDenominatorDegreesOfFreedom(0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(3, 5);
        double result = fDist.getDenominatorDegreesOfFreedom();
        assertEquals(5.0, result, 0.000001);
    }

}