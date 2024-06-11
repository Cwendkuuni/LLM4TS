import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        assertEquals(0.06680720126885731, fDistribution.cumulativeProbability(2.0), 0.0000001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        assertEquals(0.28846189344624247, fDistribution.inverseCumulativeProbability(0.1), 0.0000001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(0.0, 3.0);
        try {
            fDistribution.setNumeratorDegreesOfFreedom(-5.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 0.0);
        try {
            fDistribution.setDenominatorDegreesOfFreedom(-3.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        assertEquals(5.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

}