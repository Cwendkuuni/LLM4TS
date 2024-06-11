import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(2, 5);
        double density = distribution.density(3.5);
        // Assert density calculation is correct
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(3, 6);
        double cumulativeProbability = distribution.cumulativeProbability(2.5);
        // Assert cumulative probability calculation is correct
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(4, 7);
        double numeratorDegreesOfFreedom = distribution.getNumeratorDegreesOfFreedom();
        // Assert numerator degrees of freedom is correct
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(5, 8);
        double denominatorDegreesOfFreedom = distribution.getDenominatorDegreesOfFreedom();
        // Assert denominator degrees of freedom is correct
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(6, 9);
        double numericalMean = distribution.getNumericalMean();
        // Assert numerical mean is correct
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(7, 10);
        double numericalVariance = distribution.getNumericalVariance();
        // Assert numerical variance is correct
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(8, 11);
        double supportLowerBound = distribution.getSupportLowerBound();
        // Assert support lower bound is correct
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(9, 12);
        double supportUpperBound = distribution.getSupportUpperBound();
        // Assert support upper bound is correct
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(10, 13);
        boolean isSupportLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
        // Assert isSupportLowerBoundInclusive is correct
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(11, 14);
        boolean isSupportUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
        // Assert isSupportUpperBoundInclusive is correct
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(12, 15);
        boolean isSupportConnected = distribution.isSupportConnected();
        // Assert isSupportConnected is correct
    }

}