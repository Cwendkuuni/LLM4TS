import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well1024a;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution hyperGeomDist;

    @Before
    public void setUp() {
        RandomGenerator rng = new Well1024a();
        hyperGeomDist = new HypergeometricDistribution(rng, 10, 5, 3);
    }

    @Test
    public void testConstructor_validParameters() {
        try {
            new HypergeometricDistribution(10, 5, 3);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testConstructor_invalidNumberOfSuccesses() {
        try {
            new HypergeometricDistribution(10, -1, 3);
            fail("NotPositiveException expected");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.5714, hyperGeomDist.probability(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.8, hyperGeomDist.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(0.2, hyperGeomDist.upperCumulativeProbability(2), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.5, hyperGeomDist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.375, hyperGeomDist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, hyperGeomDist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(3, hyperGeomDist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(hyperGeomDist.isSupportConnected());
    }

}