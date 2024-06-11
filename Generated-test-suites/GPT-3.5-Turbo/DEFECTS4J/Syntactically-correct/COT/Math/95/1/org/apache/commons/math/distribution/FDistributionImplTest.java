import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testConstructor() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertNotNull(fDistribution);
        assertEquals(2.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
        assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(0.0, fDistribution.cumulativeProbability(-1.0), 0.0001);
        assertEquals(1.0, fDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0001);
        assertEquals(0.5424, fDistribution.cumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(0.0, fDistribution.inverseCumulativeProbability(0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1), 0.0001);
        assertEquals(0.3467, fDistribution.inverseCumulativeProbability(0.5), 0.0001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        fDistribution.setNumeratorDegreesOfFreedom(4.0);
        assertEquals(4.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        fDistribution.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }
}