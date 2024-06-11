import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);

        // Test with valid arguments
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            double expectedCumulativeProbability = Beta.regularizedBeta((1.0 * x) / (2.0 + 1.0 * x), 0.5, 0.5);
            assertEquals("Error in cumulativeProbability for x = " + x, expectedCumulativeProbability, cumulativeProbability, 0.001);
        }

        // Test with negative argument
        try {
            distribution.cumulativeProbability(-1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test with non-positive degrees of freedom
        try {
            new FDistributionImpl(-1.0, 2.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new FDistributionImpl(1.0, -2.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);

        // Test with valid arguments
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
            double expectedInverseCumulativeProbability = FDistribution.FInverseCumulativeProbability(p, 1.0, 2.0);
            assertEquals("Error in inverseCumulativeProbability for p = " + p, expectedInverseCumulativeProbability, inverseCumulativeProbability, 0.001);
        }

        // Test with invalid arguments
        try {
            distribution.inverseCumulativeProbability(-0.1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        try {
            distribution.inverseCumulativeProbability(1.1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        try {
            new FDistributionImpl(1.0, 2.0).inverseCumulativeProbability(1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}