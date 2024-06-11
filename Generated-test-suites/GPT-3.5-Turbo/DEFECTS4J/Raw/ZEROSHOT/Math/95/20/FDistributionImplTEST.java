import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        double result = fd.cumulativeProbability(0.5);
        assertEquals(0.3520608049535604, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        double result = fd.inverseCumulativeProbability(0.3);
        assertEquals(0.4403031420401509, result, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        double result = fd.getDomainLowerBound(0.4);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        double result = fd.getDomainUpperBound(0.4);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        double result = fd.getInitialDomain(0.4);
        assertEquals(5.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        fd.setNumeratorDegreesOfFreedom(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        fd.setDenominatorDegreesOfFreedom(-1);
    }
}