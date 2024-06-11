import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.test.AbstractRealDistributionTest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionTest extends AbstractRealDistributionTest {

    @Test
    public void testFDistribution() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        RealDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                           denominatorDegreesOfFreedom);
        // check basic properties
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
        // check support
        double lowerBound = distribution.getSupportLowerBound();
        double upperBound = distribution.getSupportUpperBound();
        assertTrue(lowerBound >= 0.0);
        assertTrue(upperBound > 0.0);
        // check density
        assertEquals(distribution.density(2.0), 0.01741926490724357, 1e-6);
        // check cumulative probability
        assertEquals(distribution.cumulativeProbability(1.5), 0.06873983905492518, 1e-6);
        assertEquals(distribution.cumulativeProbability(4.0), 0.999694297166046, 1e-6);
        // check mean
        double mean = distribution.getNumericalMean();
        assertEquals(mean, denominatorDegreesOfFreedom / (denominatorDegreesOfFreedom - 2), 1e-6);
        // check variance
        double variance = distribution.getNumericalVariance();
        if (denominatorDegreesOfFreedom > 4) {
            assertEquals(variance, (2 * (denominatorDegreesOfFreedom * denominatorDegreesOfFreedom) * (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) /
                             ((numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) * (denominatorDegreesOfFreedom - 4))), 1e-6);
        } else {
            assertTrue(Double.isNaN(variance));
        }
    }
}