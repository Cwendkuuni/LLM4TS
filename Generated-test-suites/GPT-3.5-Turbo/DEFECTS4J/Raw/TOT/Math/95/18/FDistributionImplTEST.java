import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {
    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(5, 10);
    }

    // Test cumulativeProbability method

    @Test
    public void testCumulativeProbability() throws MathException {
        double result = fDistribution.cumulativeProbability(3.0);
        assertEquals(0.9315302839028763, result, 0.0001);
    }

    // Test inverseCumulativeProbability method

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double result = fDistribution.inverseCumulativeProbability(0.8);
        assertEquals(2.310927481750314, result, 0.0001);
    }

    // Test getDomainLowerBound method

    @Test
    public void testGetDomainLowerBound() {
        double result = fDistribution.getDomainLowerBound(0.4);
        assertEquals(0.0, result, 0.0001);
    }

    // Test getDomainUpperBound method

    @Test
    public void testGetDomainUpperBound() {
        double result = fDistribution.getDomainUpperBound(0.6);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    // Test getInitialDomain method

    @Test
    public void testGetInitialDomain() {
        double result = fDistribution.getInitialDomain(0.3);
        assertEquals(1.25, result, 0.0001);
    }

    // Test setNumeratorDegreesOfFreedom method

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        fDistribution.setNumeratorDegreesOfFreedom(0);
    }

    // Test setDenominatorDegreesOfFreedom method

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        fDistribution.setDenominatorDegreesOfFreedom(0);
    }

}