import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 20);
        try {
            double result = fDistribution.cumulativeProbability(2.0);
            assertEquals(0.2951758625221709, result, 0.00001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 20);
        try {
            double result = fDistribution.inverseCumulativeProbability(0.5d);
            assertEquals(1.033042827821728, result, 0.00001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 20);
        fDistribution.setNumeratorDegreesOfFreedom(15);
        assertEquals(15, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 20);
        fDistribution.setDenominatorDegreesOfFreedom(25);
        assertEquals(25, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomException() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 20);
        fDistribution.setNumeratorDegreesOfFreedom(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomException() {
        FDistributionImpl fDistribution = new FDistributionImpl(10, 20);
        fDistribution.setDenominatorDegreesOfFreedom(-20);
    }
}