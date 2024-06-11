import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumeratorDegreesOfFreedom() {
        new FDistributionImpl(-1.0, 3.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDenominatorDegreesOfFreedom() {
        new FDistributionImpl(2.0, -1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        double p = 0.5;
        double expected = 0.33589626351861; // value from MathWorld

        double actual = distribution.cumulativeProbability(p);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithInvalidArgument() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.cumulativeProbability(-1.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        double p = 0.5;
        double expected = 1.23573678021233; // value from MathWorld

        double actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidArgument() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.inverseCumulativeProbability(1.5);
    }
}