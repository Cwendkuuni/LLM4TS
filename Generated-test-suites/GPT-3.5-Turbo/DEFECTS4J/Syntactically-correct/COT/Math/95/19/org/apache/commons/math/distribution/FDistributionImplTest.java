import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(3.0, 4.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double result = fDistribution.cumulativeProbability(1.0);
        assertEquals(0.24213075056, result, 1e-10);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_negativeInput() throws MathException {
        fDistribution.cumulativeProbability(-1.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double result = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0.67003252483, result, 1e-10);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_invalidInput() throws MathException {
        fDistribution.inverseCumulativeProbability(2.0);
    }

    @Test
    public void testGetDomainLowerBound() {
        double result = fDistribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 1e-10);
    }

    @Test
    public void testGetDomainUpperBound() {
        double result = fDistribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 1e-10);
    }

    @Test
    public void testGetInitialDomain() {
        double result = fDistribution.getInitialDomain(0.5);
        assertEquals(2.0, result, 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_negativeDegrees() {
        fDistribution.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_negativeDegrees() {
        fDistribution.setDenominatorDegreesOfFreedom(-1.0);
    }
}