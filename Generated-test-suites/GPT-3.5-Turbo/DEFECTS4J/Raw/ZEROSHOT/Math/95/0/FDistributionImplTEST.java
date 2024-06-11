import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl f = new FDistributionImpl(3, 6);
        double result = f.cumulativeProbability(2);
        assertEquals(0.8098590832834094, result, 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl f = new FDistributionImpl(3, 6);
        double result = f.inverseCumulativeProbability(0.8);
        assertEquals(1.8018875665980922, result, 0.000001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl f = new FDistributionImpl(3, 6);
        double result = f.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl f = new FDistributionImpl(3, 6);
        double result = f.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl f = new FDistributionImpl(3, 6);
        double result = f.getInitialDomain(0.5);
        assertEquals(1.2, result, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistributionImpl f = new FDistributionImpl(3, 6);
        f.setNumeratorDegreesOfFreedom(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistributionImpl f = new FDistributionImpl(3, 6);
        f.setDenominatorDegreesOfFreedom(-1);
    }
}