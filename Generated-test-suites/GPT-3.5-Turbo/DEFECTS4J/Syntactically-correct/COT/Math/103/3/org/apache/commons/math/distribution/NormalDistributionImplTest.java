import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl distribution;
    
    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        double expected = 0.0;
        double result = distribution.getMean();
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testSetMean() {
        double expected = 5.0;
        distribution.setMean(expected);
        double result = distribution.getMean();
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        double expected = 1.0;
        double result = distribution.getStandardDeviation();
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        double expected = 2.5;
        distribution.setStandardDeviation(expected);
        double result = distribution.getStandardDeviation();
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        double x = 1.0;
        double expected = 0.841344746;
        double result = distribution.cumulativeProbability(x);
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double p = 0.5;
        double expected = 0.0;
        double result = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, result, 0.0001);
    }
}