import org.junit.Test;
import org.apache.commons.math.distribution.PoissonDistributionImpl;
import org.apache.commons.math.MathException;

public class PoissonDistributionImplTest {
  
  @Test
  public void testProbability() {
    PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
    
    // test x = 0
    double p1 = poisson.probability(0);
    assertEquals(0.0820849986238988, p1, 0.00001);
    
    // test x = 1
    double p2 = poisson.probability(1);
    assertEquals(0.2052124965597471, p2, 0.00001);
    
    // test x = 2
    double p3 = poisson.probability(2);
    assertEquals(0.2565156206996838, p3, 0.00001);
    
    // test x = 3
    double p4 = poisson.probability(3);
    assertEquals(0.2137630172497366, p4, 0.00001);
    
    // test large x
    double p5 = poisson.probability(100);
    assertEquals(0, p5, 0.00001);    
  }
  
  @Test
  public void testCumulativeProbability() throws MathException {
    PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
    
    // test x = 0
    double p1 = poisson.cumulativeProbability(0);
    assertEquals(0.0820849986238988, p1, 0.00001);
    
    // test x = 1
    double p2 = poisson.cumulativeProbability(1);
    assertEquals(0.287297495183646, p2, 0.00001);
    
    // test x = 2
    double p3 = poisson.cumulativeProbability(2);
    assertEquals(0.5438131158833298, p3, 0.00001);
    
    // test x = 3
    double p4 = poisson.cumulativeProbability(3);
    assertEquals(0.7575761331330665, p4, 0.00001);
    
    // test large x
    double p5 = poisson.cumulativeProbability(100);
    assertEquals(1, p5, 0.00001);    
  }
  
  @Test
  public void testNormalApproximateProbability() throws MathException {
    PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
    
    // test x = 0
    double p1 = poisson.normalApproximateProbability(0);
    assertEquals(0.5912041096993996, p1, 0.00001);
    
    // test x = 1
    double p2 = poisson.normalApproximateProbability(1);
    assertEquals(0.8021517454877057, p2, 0.00001);
    
    // test x = 2
    double p3 = poisson.normalApproximateProbability(2);
    assertEquals(0.929362418032347, p3, 0.00001);
    
    // test x = 3
    double p4 = poisson.normalApproximateProbability(3);
    assertEquals(0.9763482144963957, p4, 0.00001);
    
    // test large x
    double p5 = poisson.normalApproximateProbability(100);
    assertEquals(1, p5, 0.00001);    
  }
  
  @Test
  public void testSample() throws MathException {
    PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
    
    // test sample with low mean
    int s1 = poisson.sample();
    assertTrue(s1 >= 0);
    assertTrue(s1 <= 1000 * 2.5);
    
    // test sample with high mean
    poisson = new PoissonDistributionImpl(1000);
    int s2 = poisson.sample();
    assertTrue(s2 >= 0);
    assertEquals(Integer.MAX_VALUE, s2);    
  }
  
}