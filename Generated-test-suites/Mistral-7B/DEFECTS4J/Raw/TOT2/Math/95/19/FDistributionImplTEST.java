import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        double result = fDist.cumulativeProbability(1.5);
        double expected = Beta.regularizedBeta((1.0 * 1.5) / (2.0 + 1.0 * 1.5), 0.5, 0.5);
        assertEquals(expected, result, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithInvalidInput() throws MathException {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        fDist.cumulativeProbability(-1.0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        fDist.setNumeratorDegreesOfFreedom(3.0);
        assertEquals(3.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithNegativeValue() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        fDist.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        fDist.setDenominatorDegreesOfFreedom(3.0);
        assertEquals(3.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithNegativeValue() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        fDist.setDenominatorDegreesOfFreedom(-1.0);
    }
}