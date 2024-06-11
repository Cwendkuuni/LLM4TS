import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImplConstructor() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        double cdf = distribution.cumulativeProbability(0.0);
        assertEquals(0.0, cdf, EPSILON);
    }

    @Test
    public void testCumulativeProbabilityPositive() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        double cdf = distribution.cumulativeProbability(1.0);
        double expectedCdf = Beta.regularizedBeta(4.0, 1.0, 3.0);
        assertEquals(expectedCdf, cdf, EPSILON);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegative() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        double cdf = distribution.cumulativeProbability(-1.0);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        double inverseCdf = distribution.inverseCumulativeProbability(0.0);
        assertEquals(0.0, inverseCdf, EPSILON);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        double inverseCdf = distribution.inverseCumulativeProbability(1.0);
        double expectedInverseCdf = Double.POSITIVE_INFINITY;
        assertEquals(expectedInverseCdf, inverseCdf, EPSILON);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.inverseCumulativeProbability(2.0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setNumeratorDegreesOfFreedom(4.0);
        assertEquals(4.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setDenominatorDegreesOfFreedom(4.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setDenominatorDegreesOfFrequency(-1.0);
    }
}