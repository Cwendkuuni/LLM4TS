import org.apache.commons.math.MathException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 15);
        double result = fDistribution.cumulativeProbability(5);
        assertEquals(0.898033682835, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 15);
        double result = fDistribution.inverseCumulativeProbability(0.898033682835);
        assertEquals(5, result, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 15);
        double result = fDistribution.getDomainLowerBound(0.898033682835);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 15);
        double result = fDistribution.getDomainUpperBound(0.898033682835);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 15);
        double result = fDistribution.getInitialDomain(0.898033682835);
        assertEquals(1.1538461538461537, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 15);
        fDistribution.setNumeratorDegreesOfFreedom(-2);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 15);
        double result = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(10, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 15);
        fDistribution.setDenominatorDegreesOfFreedom(-2);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 15);
        double result = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(15, result, 0.0001);
    }
}