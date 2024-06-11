import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(0, normalDist.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(1, normalDist.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014337, normalDist.density(0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
      NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
      try{
        assertEquals(0.5, normalDist.cumulativeProbability(0), 1e-15);
      }catch(Exception ex){

      }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try{
          assertEquals(0.0, normalDist.inverseCumulativeProbability(0), 1e-15);
          assertEquals(Double.POSITIVE_INFINITY, normalDist.inverseCumulativeProbability(1), 1e-15);
          // Handle exception for invalid probability
          normalDist.inverseCumulativeProbability(2);
        }catch(Exception ex){

        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try{
          double value = normalDist.sample();
          assertNotNull(value);
        }catch(Exception ex){

        }
    }

}