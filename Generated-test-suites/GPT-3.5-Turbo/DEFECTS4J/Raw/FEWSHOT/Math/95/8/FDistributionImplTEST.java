/*
 * JUnit test cases for FDistributionImpl class
 */

import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;

import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        
        assertEquals(0.0, fDistribution.cumulativeProbability(-1), 0);
        
        // Test values from 0 to 1 (inclusive)
        for (double x = 0; x <= 1; x += 0.1) {
            double expectedCumulativeProbability = Beta.regularizedBeta((3 * x) / (5 + 3 * x),
                    0.5 * 3,
                    0.5 * 5);
            assertEquals(expectedCumulativeProbability, fDistribution.cumulativeProbability(x), 0.0001);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        
        assertEquals(0d, fDistribution.inverseCumulativeProbability(0), 0);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1), 0);
        
        // Test values from 0 to 1 (exclusive)
        for (double p = 0.001; p < 1; p += 0.1) {
            double expectedInverseCumulativeProbability = fDistribution.inverseCumulativeProbability(p);
            double cumulativeProbability = fDistribution.cumulativeProbability(expectedInverseCumulativeProbability);
            assertEquals(p, cumulativeProbability, 0.0001);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        fDistribution.setNumeratorDegreesOfFreedom(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        fDistribution.setDenominatorDegreesOfFreedom(0);
    }
}