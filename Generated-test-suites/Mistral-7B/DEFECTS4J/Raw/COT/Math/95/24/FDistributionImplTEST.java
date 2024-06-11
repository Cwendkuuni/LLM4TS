import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        Assert.assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method {@link FDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);

        // Test with invalid input
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-1.0), 0.0);

        // Test with valid input
        Assert.assertTrue(Double.isFinite(distribution.cumulativeProbability(0.5)));
        Assert.assertTrue(Double.isFinite(distribution.cumulativeProbability(2.0)));

        // Test with edge cases
        double pValue = 0.0;
        Assert.assertEquals(0.0, distribution.cumulativeProbability(pValue), 0.0);

        pValue = 1.0;
        Assert.assertEquals(1.0, distribution.cumulativeProbability(pValue), 0.0);

        // Test with specific values
        double xValue = 0.5;
        double expectedValue = Beta.regularizedBeta(2.0 * xValue / (4.0 + 2.0 * xValue), 1.0, 1.0);
        Assert.assertEquals(expectedValue, distribution.cumulativeProbability(xValue), 1e-10);

        // Test with large input
        xValue = Double.POSITIVE_INFINITY;
        Assert.assertEquals(1.0, distribution.cumulativeProbability(xValue), 0.0);
    }

    /**
     * Test method {@link FDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        distribution.inverseCumulativeProbability(-1.0);
    }

    /**
     * Test method {@link FDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbabilityValidProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);

        // Test with invalid input
        Assert.assertEquals(0.0, distribution.inverseCumulativeProbability(1.0), 0.0);

        // Test with valid input
        double pValue = 0.5;
        double expectedValue = 0.5 * (4.0 / (4.0 - 2.0));
        Assert.assertEquals(expectedValue, distribution.inverseCumulativeProbability(pValue), 1e-10);

        // Test with edge cases
        pValue = 0.0;
        Assert.assertEquals(0.0, distribution.inverseCumulativeProbability(pValue), 0.0);

        pValue = 1.0;
        Assert.assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(pValue), 0.0);
    }

    /**
     * Test method {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomInvalidDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        distribution.setNumeratorDegreesOfFreedom(-1.0);
    }

    /**
     * Test method {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)}.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedomValidDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        double newDegreesOfFreedom = 5.0;
        distribution.setNumeratorDegreesOfFreedom(newDegreesOfFreedom);
        Assert.assertEquals(newDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomInvalidDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        distribution.setDenominatorDegreesOfFreedom(-1.0);
    }

    /**
     * Test method {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)}.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedomValidDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        double newDegreesOfFreedom = 5.0;
        distribution.setDenominatorDegreesOfFreedom(newDegreesOfFreedom);
        Assert.assertEquals(newDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
}