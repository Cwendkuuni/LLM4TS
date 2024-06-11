import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test cases for {@link NormalDistributionImpl} class
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor NormalDistributionImpl with valid arguments
     */
    @Test
    public void testNormalDistributionImplValidArguments() {
        NormalDistribution normalDist = new NormalDistributionImpl(2.5, 1.2);
        assertEquals(2.5, normalDist.getMean(), 0.0);
        assertEquals(1.2, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor NormalDistributionImpl with invalid arguments
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNormalDistributionImplInvalidArguments() {
        new NormalDistributionImpl(-1.0, 2.5);
    }

    /**
     * Test method getMean()
     */
    @Test
    public void testGetMean() {
        NormalDistribution normalDist = new NormalDistributionImpl();
        assertEquals(0.0, normalDist.getMean(), 0.0);

        NormalDistribution normalDistMean2 = new NormalDistributionImpl(2.5);
        assertEquals(2.5, normalDistMean2.getMean(), 0.0);
    }

    /**
     * Test method setMean()
     */
    @Test
    public void testSetMean() {
        NormalDistribution normalDist = new NormalDistributionImpl();
        normalDist.setMean(3.5);
        assertEquals(3.5, normalDist.getMean(), 0.0);
    }

    /**
     * Test method getStandardDeviation()
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution normalDist = new NormalDistributionImpl();
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.0);

        NormalDistribution normalDistStandardDeviation2 = new NormalDistributionImpl();
        normalDistStandardDeviation2.setStandardDeviation(2.5);
        assertEquals(2.5, normalDistStandardDeviation2.getStandardDeviation(), 0.0);
    }

    /**
     * Test method setStandardDeviation()
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistribution normalDist = new NormalDistributionImpl();
        normalDist.setStandardDeviation(3.5);
        assertEquals(3.5, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test method cumulativeProbability() with valid arguments
     */
    @Test
    public void testCumulativeProbabilityValidArguments() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double resultCumulativeProbability = 0.5;
        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double cumulativeProbability = normalDist.cumulativeProbability(x);
            assertEquals("x = " + x, resultCumulativeProbability, cumulativeProbability, 0.001);
            resultCumulativeProbability += 0.01;
        }
    }

    /**
     * Test method cumulativeProbability() with out of range arguments
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRangeArguments() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        normalDist.cumulativeProbability(-10.0);
    }

    /**
     * Test method inverseCumulativeProbability() with valid arguments
     */
    @Test
    public void testInverseCumulativeProbabilityValidArguments() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, normalDist.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(1.0, normalDist.inverseCumulativeProbability(1.0), 0.001);
    }

    /**
     * Test method inverseCumulativeProbability() with invalid arguments
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidArguments() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        normalDist.inverseCumulativeProbability(-1.0);
        normalDist.inverseCumulativeProbability(2.0);
    }

    /**
     * Test method inverseCumulativeProbability() with p = 0 or 1
     */
    @Test
    public void testInverseCumulativeProbabilityPZeroOrOne() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.NEGATIVE_INFINITY, normalDist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, normalDist.inverseCumulativeProbability(1.0), 0.0);
    }

    /**
     * Test method inverseCumulativeProbability() with large p
     */
    @Test
    public void testInverseCumulativeProbabilityLargeP() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.99999999999999999;
        double result = normalDist.inverseCumulativeProbability(p);
        assertEquals(result, Double.POSITIVE_INFINITY, 0.0);
    }

    /**
     * Test method inverseCumulativeProbability() with small p
     */
    @Test
    public void testInverseCumulativeProbabilitySmallP() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.000000000000001;
        double result = normalDist.inverseCumulativeProbability(p);
        assertEquals(result, Double.NEGATIVE_INFINITY, 0.0);
    }

    /**
     * Test method
     * {@link #inverseCumulativeProbability(double)} with p = 0.5
     */
    @Test
    public void testInverseCumulativeProbabilityPZeroPointFive() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.5;
        double result = normalDist.inverseCumulativeProbability(p);
        assertEquals(result, 0.0, 0.001);
    }

    /**
     * Test method inverseCumulativeProbability() with convergence exception
     */
    @Test
    public void testInverseCumulativeProbabilityConvergenceException() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        assertThrows(MaxIterationsExceededException.class, () -> normalDist.inverseCumulativeProbability(1.3));
    }

    /**
     * Test method inverseCumulativeProbability() with custom lower and upper bounds
     */
    @Test
    public void testInverseCumulativeProbabilityCustomBounds() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double result = normalDist.inverseCumulativeProbability(0.6);
        assertEquals(result, 0.3542, 0.001);
    }
}