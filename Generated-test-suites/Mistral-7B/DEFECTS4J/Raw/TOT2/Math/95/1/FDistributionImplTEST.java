// Merge the test cases from all the experts
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_CumulativeProbability() {
        FDistribution fDist = new FDistributionImpl(1.0, 1.0);
        double result = fDist.cumulativeProbability(0.5);
        double expected = 0.5;
        assertEquals(expected, result, 0.001);

        result = fDist.cumulativeProbability(Double.MAX_VALUE);
        expected = 1.0;
        assertEquals(expected, result, 0.001);

        result = fDist.cumulativeProbability(Double.MIN_VALUE);
        expected = 0.0;
        assertEquals(expected, result, 0.001);

        FDistribution fDistNegativeX = new FDistributionImpl(1.0, 1.0);
        try {
            result = fDistNegativeX.cumulativeProbability(-1.0);
            fail("MathException expected");
        } catch (MathException e) {
            // Expected
        }

        FDistribution fDistInvalidProbability = new FDistributionImpl(1.0, 1.0);
        try {
            result = fDistInvalidProbability.cumulativeProbability(1.5);
            fail("MathException expected");
        } catch (MathException e) {
            // Expected
        }

        FDistribution fDistNaNX = new FDistributionImpl(1.0, 1.0);
        try {
            result = fDistNaNX.cumulativeProbability(Double.NaN);
            fail("MathException expected");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testFDistributionImpl_InverseCumulativeProbability() {
        FDistribution fDist = new FDistributionImpl(1.0, 1.0);
        double p = 0.5;
        double result = fDist.inverseCumulativeProbability(p);
        double expected = 0.5;
        assertEquals(expected, result, 0.001);

        p = 1.0;
        result = fDist.inverseCumulativeProbability(p);
        expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, result, 0.0);

        p = 0.0;
        result = fDist.inverseCumulativeProbability(p);
        expected = 0.0;
        assertEquals(expected, result, 0.001);

        FDistribution fDistNegativeProbability = new FDistributionImpl(1.0, 1.0);
        try {
            result = fDistNegativeProbability.inverseCumulativeProbability(-1.0);
            fail("MathException expected");
        } catch (MathException e) {
            // Expected
        }

        FDistribution fDistInvalidProbability = new FDistributionImpl(1.0, 1.0);
        try {
            result = fDistInvalidProbability.inverseCumulativeProbability(1.5);
            fail("MathException expected");
        } catch (MathException e) {
            // Expected
        }

        FDistribution fDistNaNProbability = new FDistributionImpl(1.0, 1.0);
        try {
            result = fDistNaNProbability.inverseCumulativeProbability(Double.NaN);
            fail("MathException expected");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testFDistributionImpl_Setters() {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 1.0);
        try {
            fDist.setNumeratorDegreesOfFreedom(-1.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            fDist.setDenominatorDegreesOfFreedom(-1.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}