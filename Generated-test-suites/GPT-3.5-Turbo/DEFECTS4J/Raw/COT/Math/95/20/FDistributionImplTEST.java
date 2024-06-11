
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.FDistributionImpl;

import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 5.0);

        try {
            double result = fDistribution.cumulativeProbability(1.0);

            // Assert
            assertEquals(0.604260017, result, 1e-9);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 5.0);

        try {
            double result = fDistribution.inverseCumulativeProbability(0.6);

            // Assert
            assertEquals(1.954185728, result, 1e-9);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 5.0);

        double result = fDistribution.getDomainLowerBound(0.6);

        // Assert
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 5.0);

        double result = fDistribution.getDomainUpperBound(0.6);

        // Assert
        assertEquals(Double.MAX_VALUE, result, 1e-9);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 5.0);

        double result = fDistribution.getInitialDomain(0.6);

        // Assert
        assertEquals(1.6666666667, result, 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithInvalidValue() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 5.0);

        // Act
        fDistribution.setNumeratorDegreesOfFreedom(-5.0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 5.0);

        double result = fDistribution.getNumeratorDegreesOfFreedom();

        // Assert
        assertEquals(2.0, result, 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithInvalidValue() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 5.0);

        // Act
        fDistribution.setDenominatorDegreesOfFreedom(-5.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 5.0);

        double result = fDistribution.getDenominatorDegreesOfFreedom();

        // Assert
        assertEquals(5.0, result, 1e-9);
    }
}
