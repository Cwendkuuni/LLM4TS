import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplCreation() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        NormalDistribution normalDistributionWithMeanAndStdDev = new NormalDistributionImpl(5, 2);
        NormalDistribution normalDistributionWithMeanStdDevAndAccuracy = new NormalDistributionImpl(3, 4, 0.01);
        NormalDistributionImpl normalDistributionWithNegativeStdDev = null;

        try {
            normalDistributionWithNegativeStdDev = new NormalDistributionImpl(-2, 3);
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        assertEquals("Mean and standard deviation should be set correctly", 0, normalDistribution.getMean(), 0.001);
        assertEquals("Mean and standard deviation should be set correctly", 1, normalDistribution.getStandardDeviation(), 0.001);
        assertEquals("Mean and standard deviation should be set correctly", 5, normalDistributionWithMeanAndStdDev.getMean(), 0.001);
        assertEquals("Mean and standard deviation should be set correctly", 2, normalDistributionWithMeanAndStdDev.getStandardDeviation(), 0.001);
        assertEquals("Mean, standard deviation and accuracy should be set correctly", 3, normalDistributionWithMeanStdDevAndAccuracy.getMean(), 0.001);
        assertEquals("Mean, standard deviation and accuracy should be set correctly", 4, normalDistributionWithMeanStdDevAndAccuracy.getStandardDeviation(), 0.001);
        assertNull("Negative standard deviation should not be allowed", normalDistributionWithNegativeStdDev);
    }

    @Test
    public void testDensity() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        double densityAtZero = normalDistribution.density(0);
        double densityAtMean = normalDistribution.density(0);
        double densityAtTwoStandardDeviations = normalDistribution.density(2);
        double densityAtFourStandardDeviations = normalDistribution.density(4);
        double densityAtNegativeValue = normalDistribution.density(-1);

        assertEquals("Density at zero should be correct", 0.398942280401433, densityAtZero, 0.001);
        assertEquals("Density at mean should be correct", 0.398942280401433, densityAtMean, 0.001);
        assertEquals("Density at two standard deviations should be correct", 0.120405053931668, densityAtTwoStandardDeviations, 0.001);
        assertEquals("Density at four standard deviations should be close to zero", 0.0001228, densityAtFourStandardDeviations, 0.001);
        assertNaN("Density at negative value is not defined", densityAtNegativeValue);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        double probabilityAtZero = normalDistribution.cumulativeProbability(0);
        double probabilityAtMean = normalDistribution.cumulativeProbability(0);
        double probabilityAtTwoStandardDeviations = normalDistribution.cumulativeProbability(2);
        double probabilityAtFourStandardDeviations = normalDistribution.cumulativeProbability(4);
        double probabilityAtNegativeValue = normalDistribution.cumulativeProbability(-1);

        assertEquals("Probability at zero should be close to zero", 0.0001228, probabilityAtZero, 0.001);
        assertEquals("Probability at mean should be 0.5", 0.5, probabilityAtMean, 0.001);
        assertEquals("Probability at two standard deviations should be close to 0.95", 0.954506570253432, probabilityAtTwoStandardDeviations, 0.001);
        assertEquals("Probability at four standard deviations should be close to 1", 0.997311063935224, probabilityAtFourStandardDeviations, 0.001);
        assertEquals("Probability at negative value is not defined", Double.NEGATIVE_INFINITY, probabilityAtNegativeValue);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityBelowZero() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        normalDistribution.inverseCumulativeProbability(-0.1);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        double xForProbability01 = normalDistribution.inverseCumulativeProbability(0.1);
        double xForProbability05 = normalDistribution.inverseCumulativeProbability(0.5);
        double xForProbability095 = normalDistribution.inverseCumulativeProbability(0.95);

        assertEquals("Inverse cumulative probability for 0.1 should be close to 0.1235", 0.123528324513218, xForProbability01, 0.001);
        assertEquals("Inverse cumulative probability for 0.5 should be close to 0", 0, xForProbability05, 0.001);
        assertEquals("Inverse cumulative probability for 0.95 should be close to 1.6449", 1.64485355628421, xForProbability095, 0.001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        int numberOfSamples = 1000;
        double sumOfSamples = 0;

        for (int i = 0; i < numberOfSamples; i++) {
            sumOfSamples += normalDistribution.sample();
        }

        double meanOfSamples = sumOfSamples / numberOfSamples;

        assertEquals("Mean of samples should be close to zero", 0, meanOfSamples, 0.01);
    }

    @Test
    public void testSampleNegativeValues() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        int numberOfSamples = 10000;
        int numberOfNegativeSamples = 0;

        for (int i = 0; i < numberOfSamples; i++) {
            double sample = normalDistribution.sample();
            if (sample < 0) {
                numberOfNegativeSamples++;
            }
        }

        double percentageOfNegativeSamples = ((double) numberOfNegativeSamples / numberOfSamples) * 100;

        assertFalse("Percentage of negative samples should be less than 0.5%", percentageOfNegativeSamples > 0.5);
    }
}