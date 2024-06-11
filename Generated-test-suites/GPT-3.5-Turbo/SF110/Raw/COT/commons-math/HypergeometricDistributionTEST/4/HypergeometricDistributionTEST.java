
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.util.LocalizedFormats;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.distribution.HypergeometricDistribution;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(1.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testGetDomain(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        int[] expectedDomain = {0, 3};
        assertArrayEquals(expectedDomain, distribution.getDomain(10, 3, 5));
    }

    @Test
    public void testGetLowerDomain(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(2, distribution.getLowerDomain(10, 3, 5));
    }

    @Test
    public void testGetNumberOfSuccesses(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(3, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(5, distribution.getSampleSize());
    }

    @Test
    public void testGetUpperDomain(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(3, distribution.getUpperDomain(3, 5));
    }

    @Test
    public void testProbability(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.14285714285714285, distribution.probability(0), 0.0001);
        assertEquals(0.2, distribution.probability(3), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(3), 0.0001);
    }

    @Test
    public void testInnerCumulativeProbability(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.14285714285714285, distribution.innerCumulativeProbability(0, 0, 1), 0.0001);
        assertEquals(0.34285714285714286, distribution.innerCumulativeProbability(0, 3, 1), 0.0001);
    }

    @Test
    public void testGetNumericalMean(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.5357142857142857, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testCalculateNumericalVariance(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.5357142857142857, distribution.calculateNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected(){
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertTrue(distribution.isSupportConnected());
    }
}
