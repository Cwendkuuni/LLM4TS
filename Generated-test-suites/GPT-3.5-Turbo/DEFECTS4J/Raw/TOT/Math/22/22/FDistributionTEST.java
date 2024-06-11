import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    private FDistribution fDistribution;

    @Before
    public void setUp() throws Exception {
        fDistribution = new FDistribution(5, 10);
    }

    @Test
    public void testConstructor_InvalidNumeratorDegreesOfFreedom() {
        try {
            fDistribution = new FDistribution(0, 10);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals(LocalizedFormats.DEGREES_OF_FREEDOM, e.getSpecifier());
            assertEquals(0, e.getArgument());
        }
    }

    @Test
    public void testConstructor_InvalidDenominatorDegreesOfFreedom() {
        try {
            fDistribution = new FDistribution(5, 0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals(LocalizedFormats.DEGREES_OF_FREEDOM, e.getSpecifier());
            assertEquals(0, e.getArgument());
        }
    }

    @Test
    public void testDensity_Typical() {
        assertEquals(0.0343, fDistribution.density(0.5), 0.0001);
    }

    @Test
    public void testDensity_EdgeCase() {
        assertEquals(0.0010, fDistribution.density(0), 0.0001);
    }

    @Test
    public void testDensity_ErrorScenario() {
        assertEquals(0, fDistribution.density(-1), 0);
    }

    @Test
    public void testCumulativeProbability_Typical() {
        assertEquals(0.3990, fDistribution.cumulativeProbability(0.5), 0.0001);
    }

    @Test
    public void testCumulativeProbability_EdgeCase() {
        assertEquals(0, fDistribution.cumulativeProbability(0), 0);
    }

    @Test
    public void testCumulativeProbability_ErrorScenario() {
        assertEquals(0, fDistribution.cumulativeProbability(-1), 0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(5, fDistribution.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(10, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, fDistribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(fDistribution.isSupportConnected());
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.25, fDistribution.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(6.25, fDistribution.getNumericalVariance(), 0);
    }
}