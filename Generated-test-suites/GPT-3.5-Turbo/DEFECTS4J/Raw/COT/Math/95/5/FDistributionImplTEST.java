
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(2.0, 3.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 1e-15);
        assertEquals(0.211, fDistribution.cumulativeProbability(1.0), 1e-3);
        assertEquals(0.437, fDistribution.cumulativeProbability(2.0), 1e-3);
        assertEquals(0.618, fDistribution.cumulativeProbability(3.0), 1e-3);
        assertEquals(0.742, fDistribution.cumulativeProbability(4.0), 1e-3);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 1e-15);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 1e-15);
        assertEquals(3.851, fDistribution.inverseCumulativeProbability(0.6), 1e-3);
        assertEquals(9.000, fDistribution.inverseCumulativeProbability(0.9), 1e-3);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        fDistribution.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, fDistribution.getNumeratorDegreesOfFreedom(), 1e-15);
        fDistribution.setNumeratorDegreesOfFreedom(7.0);
        assertEquals(7.0, fDistribution.getNumeratorDegreesOfFreedom(), 1e-15);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        fDistribution.setDenominatorDegreesOfFreedom(6.0);
        assertEquals(6.0, fDistribution.getDenominatorDegreesOfFreedom(), 1e-15);
        fDistribution.setDenominatorDegreesOfFreedom(8.0);
        assertEquals(8.0, fDistribution.getDenominatorDegreesOfFreedom(), 1e-15);
    }

}