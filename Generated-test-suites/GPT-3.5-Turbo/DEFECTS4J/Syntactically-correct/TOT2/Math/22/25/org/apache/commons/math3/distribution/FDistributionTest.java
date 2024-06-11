import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {
    
    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(5.0, 3.0);
        assertEquals(0.021704623, f.density(1.5), 1e-6);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(5.0, 3.0);
        assertEquals(0.557736, f.cumulativeProbability(1.5), 1e-6);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution f = new FDistribution(5.0, 3.0);
        assertEquals(5.0, f.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution f = new FDistribution(5.0, 3.0);
        assertEquals(3.0, f.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution f = new FDistribution(5.0, 3.0);
        assertEquals(3.0, f.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution f = new FDistribution(5.0, 3.0);
        assertEquals(4.285714285714286, f.getNumericalVariance(), 1e-6);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(5.0, 3.0);
        assertEquals(0.0, f.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(5.0, 3.0);
        assertTrue(Double.isInfinite(f.getSupportUpperBound()));
    }
  
}