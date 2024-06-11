import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);

    @Test
    public void testGetMean() {
        assertEquals(0, normalDist.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDist.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDensity() {
        assertEquals(0.39894, normalDist.density(0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.5, normalDist.cumulativeProbability(0), 0.0001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            assertEquals(-0.9944, normalDist.inverseCumulativeProbability(0.2), 0.0001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        try {
            double sample = normalDist.sample();
            System.out.println("Sampled value: " + sample);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(1e-9, normalDist.getSolverAbsoluteAccuracy(), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDist.getDomainLowerBound(0.4), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, normalDist.getDomainUpperBound(0.6), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normalDist.getInitialDomain(0.4), 0.0001);
        assertEquals(1, normalDist.getInitialDomain(0.6), 0.0001);
        assertEquals(0, normalDist.getInitialDomain(0.5), 0.0001);
    }

}