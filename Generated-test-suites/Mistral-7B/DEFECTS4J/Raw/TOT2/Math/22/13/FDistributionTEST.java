import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.test.AbstractRealDistributionTest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FDistributionTest extends AbstractRealDistributionTest {

    /**
     * Test constructor with valid degrees of freedom.
     *
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test
    public void testConstruction() throws NotStrictlyPositiveException {
        final double numeratorDF = 3.0;
        final double denominatorDF = 4.0;
        FDistribution distribution = new FDistribution(numeratorDF, denominatorDF);
        assertFalse(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertEquals(numeratorDF, distribution.getNumeratorDegreesOfFreedom(), 1e-14);
        assertEquals(denominatorDF, distribution.getDenominatorDegreesOfFreedom(), 1e-14);
    }

    /**
     * Test constructor with invalid degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstruction() {
        new FDistribution(-1.0, 2.0);
    }

    /**
     * Test constructor with random number generator.
     *
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test
    public void testConstructionWithRandomGenerator() throws NotStrictlyPositiveException {
        final double numeratorDF = 3.0;
        final double denominatorDF = 4.0;
        final Well19937c rng = new Well19937c();
        FDistribution distribution = new FDistribution(rng, numeratorDF, denominatorDF);
        assertFalse(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertEquals(numeratorDF, distribution.getNumeratorDegreesOfFreedom(), 1e-14);
        assertEquals(denominatorDF, distribution.getDenominatorDegreesOfFreedom(), 1e-14);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertEquals(distribution.density(1.5), 0.13678953586661198, 1e-10);
        assertEquals(distribution.density(2.5), 0.08317878441638238, 1e-10);
        assertEquals(distribution.density(3.5), 0.03374762241360929, 1e-10);
        assertEquals(distribution.density(4.5), 0.013427321439861585, 1e-10);
        assertEquals(distribution.density(5.5), 0.005136140727242384, 1e-10);
    }

    /**
     * Test cumulative probability method.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertEquals(distribution.cumulativeProbability(0.0), 0.0, 1e-10);
        assertEquals(distribution.cumulativeProbability(0.1), 0.0448583563907646, 1e-10);
        assertEquals(distribution.cumulativeProbability(0.5), 0.5, 1e-10);
        assertEquals(distribution.cumulativeProbability(1.0), 0.8811894422358254, 1e-10);
        assertEquals(distribution.cumulativeProbability(2.0), 0.975733315363216, 1e-10);
        assertEquals(distribution.cumulativeProbability(3.0), 0.998839345263532, 1e-10);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testNumericalMean() {
        FDistribution distribution =
                new FDistribution(new Well19937c(), 3.0, 4.0);
        assertEquals(distribution.getNumericalMean(), 2.0 / 3.0, 1e-10);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testNumericalVariance() {
        FDistribution distribution =
                new FDistribution(new Well19937c(), 3.0, 4.0);
        double variance = distribution.getNumericalVariance();
        assertEquals(variance, 2.0 / 9.0, 1e-10);
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testSupportLowerBound() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertEquals(distribution.getSupportLowerBound(), 0.0, 1e-10);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testSupportUpperBound() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertEquals(distribution.getSupportUpperBound(), Double.POSITIVE_INFINITY, 1e-10);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertTrue(distribution.isSupportConnected());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        String string = distribution.toString();
        assertTrue(string.contains("FDistribution["));
        assertTrue(string.contains("numeratorDegreesOfFreedom=" + 3.0));
        assertTrue(string.contains("denominatorDegreesOfFreedom=" + 4.0));
    }
}