import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.test.AbstractRealDistributionTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest extends AbstractRealDistributionTest {

    @Test
    public void testFDistributionConstruction() {
        try {
            FDistribution fDist = new FDistribution(2.0, 4.0);
            assertEquals(2.0, fDist.getNumeratorDegreesOfFreedom(), 0.001);
            assertEquals(4.0, fDist.getDenominatorDegreesOfFreedom(), 0.001);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFDistributionDensity() {
        try {
            FDistribution fDist = new FDistribution(2.0, 4.0);
            double[] testValues = new double[]{-1.0, 0.1, 1.0, 2.0, 3.0, 5.0};
            for (double x : testValues) {
                double density = fDist.density(x);
                if (Double.isNaN(density)) {
                    fail("Unexpected NaN value for x = " + x);
                }
                System.out.println("For x = " + x + ", density is = " + density);
            }
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        try {
            FDistribution fDist = new FDistribution(2.0, 4.0);
            double[] testValues = new double[]{-1.0, 0.0, 0.1, 1.0, 2.0, 3.0, 5.0, 10.0};
            for (double x : testValues) {
                double cp = fDist.cumulativeProbability(x);
                if (cp < 0 || cp > 1) {
                    fail("Unexpected cumulative probability value for x = " + x + ", cp = " + cp);
                }
                System.out.println("For x = " + x + ", cumulative probability is = " + cp);
            }
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFDistributionNegativeParameters() {
        try {
            FDistribution fDist = new FDistribution(-2.0, 4.0);
            fail("Expected NotStrictlyPositiveException for negative degrees of freedom");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        try {
            FDistribution fDist = new FDistribution(2.0, -4.0);
            fail("Expected NotStrictlyPositiveException for negative degrees of freedom");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testFDistributionInvalidParameters() {
        try {
            FDistribution fDist = new FDistribution(0.0, 4.0);
            fail("Expected NotStrictlyPositiveException for zero degrees of freedom");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        try {
            FDistribution fDist = new FDistribution(2.0, 0.0);
            fail("Expected NotStrictlyPositiveException for zero degrees of freedom");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        try {
            FDistribution fDist = new FDistribution(2.0, -1.0);
            fail("Expected NotStrictlyPositiveException for negative degrees of freedom");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testFDistributionMean() {
        try {
            FDistribution fDist = new FDistribution(2.0, 4.0);
            double mean = fDist.getNumericalMean();
            assertEquals(1.0, mean, 0.001);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFDistributionVariance() {
        try {
            FDistribution fDist = new FDistribution(2.0, 4.0);
            double variance = fDist.getNumericalVariance();
            assertEquals(0.09523809523809523, variance, 0.0001);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            FDistribution fDist = new FDistribution(3.0, 4.0);
            double variance = fDist.getNumericalVariance();
            assertEquals(0.03731563873156387, variance, 0.0001);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            FDistribution fDist = new FDistribution(1.0, 1.0);
            double variance = fDist.getNumericalVariance();
            assertNotNull(variance);
            assertTrue(Double.isNaN(variance));
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFDistributionRandomValues() {
        try {
            FDistribution fDist = new FDistribution(2.0, 4.0, new Well19937c());
            RealDistribution uniformDist = new UniformRealDistribution(0.0, 1.0);
            for (int i = 0; i < 1000; ++i) {
                double x = fDist.inverseCumulativeProbability(uniformDist.randomValue());
                double p = fDist.cumulativeProbability(x);
                assertEquals(x, fDist.inverseCumulativeProbability(p), 0.001);
            }
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFDistributionRandomValuesWithNegativeDF() {
        try {
            FDistribution fDist = new FDistribution(-2.0, 4.0, new Well19937c());
            RealDistribution uniformDist = new UniformRealDistribution(0.0, 1.0);
            for (int i = 0; i < 1000; ++i) {
                double x = fDist.inverseCumulativeProbability(uniformDist.randomValue());
                fail("Expected NotStrictlyPositiveException for negative x");
            }
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }
}