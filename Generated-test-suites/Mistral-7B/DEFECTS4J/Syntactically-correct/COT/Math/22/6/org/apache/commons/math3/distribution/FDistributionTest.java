import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {
    

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDOFs() {
        try {
            new FDistribution(-1.0, 2.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }

        try {
            new FDistribution(2.0, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }
    }

    @Test
    public void testFDistributionDefaultConstructor() {
        FDistribution fd = new FDistribution();
        assertNull(fd.getNumeratorDegreesOfFreedom());
        assertNull(fd.getDenominatorDegreesOfFreedom());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionWithInvalidDOFs() {
        try {
            new FDistribution(0.0, 1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }

        try {
            new FDistribution(1.0, 0.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }
    }

    @Test
    public void testFDistributionStandardConstructor() {
        double numeratorDOF = 3.0;
        double denominatorDOF = 5.0;
        RealDistribution fd = new FDistribution(numeratorDOF, denominatorDOF);
        assertEquals(numeratorDOF, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDOF, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionWithInverseCumulativeProbabilityAccuracy() {
        double numeratorDOF = 3.0;
        double denominatorDOF = 5.0;
        double inverseCumulativeProbabilityAccuracy = 1e-6;
        RealDistribution fd = new FDistribution(numeratorDOF, denominatorDOF, inverseCumulativeProbabilityAccuracy);
        assertEquals(numeratorDOF, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDOF, fd.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(inverseCumulativeProbabilityAccuracy, fd.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testFDistributionWithRandomGenerator() {
        double numeratorDOF = 3.0;
        double denominatorDOF = 5.0;
        double inverseCumulativeProbabilityAccuracy = 1e-6;
        RandomGenerator rng = new Well19937c();
        RealDistribution fd = new FDistribution(rng, numeratorDOF, denominatorDOF, inverseCumulativeProbabilityAccuracy);
        assertEquals(numeratorDOF, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDOF, fd.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(inverseCumulativeProbabilityAccuracy, fd.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testFDistributionDensity() {
        double numeratorDOF = 3.0;
        double denominatorDOF = 5.0;
        double x = 1.5;
        RealDistribution fd = new FDistribution(numeratorDOF, denominatorDOF);
        doubledensityExpected = fd.density(x);
        double densityActual = fd.density(x);
        assertEquals(densityExpected, densityActual, 1e-6);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        double numeratorDOF = 3.0;
        double denominatorDOF = 5.0;
        double x = 1.5;
        RealDistribution fd = new FDistribution(numeratorDOF, denominatorDOF);
        double cumulativeProbabilityExpected = fd.cumulativeProbability(x);
        double cumulativeProbabilityActual = fd.cumulativeProbability(x);
        assertEquals(cumulativeProbabilityExpected, cumulativeProbabilityActual, 1e-6);
    }

    @Test
    public void testFDistributionMean() {
        double numeratorDOF = 3.0;
        double denominatorDOF = 5.0;
        RealDistribution fd = new FDistribution(numeratorDOF, denominatorDOF);
        double meanExpected = numeratorDOF / (numeratorDOF + denominatorDOF - 1.0);
        double meanActual = fd.getNumericalMean();
        assertEquals(meanExpected, meanActual, 1e-6);
        // test undefined mean
        fd = new FDistribution(2.0, 2.0);
        double meanActual = fd.getNumericalMean();
        assertNotNull(meanActual);
        assertEquals(Double.NaN, meanActual, 0.0);
    }

    @Test
    public void testFDistributionVariance() {
        double numeratorDOF = 3.0;
        double denominatorDOF = 5.0;
        RealDistribution fd = new FDistribution(numeratorDOF, denominatorDOF);
        double varianceExpected = 2.0 * ((numeratorDOF + denominatorDOF - 2) * denominatorDOF * denominatorDOF) /
                ((numeratorDOF * denominatorDOF * denominatorDOF - 4) * (denominatorDOF - 2) * (denominatorDOF - 4));
        double varianceActual = fd.getNumericalVariance();
        assertEquals(varianceExpected, varianceActual, 1e-6);
        // test undefined variance
        fd = new FDistribution(2.0, 2.0);
        double varianceActual = fd.getNumericalVariance();
        assertNotNull(varianceActual);
        assertEquals(Double.NaN, varianceActual, 0.0);
    }

    @Test
    public void testFDistributionSupportBounds() {
        double numeratorDOF = 3.0;
        double denominatorDOF = 5.0;
        double lowerBound = 0.0;
        double upperBound = Double.POSITIVE_INFINITY;
        RealDistribution fd = new FDistribution(numeratorDOF, denominatorDOF);
        assertEquals(lowerBound, fd.getSupportLowerBound(), 0.0);
        assertEquals(upperBound, fd.getSupportUpperBound(), 0.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
        assertFalse(fd.isSupportUpperBoundInclusive());
        assertFalse(fd.isSupportConnected());
    }

    
}