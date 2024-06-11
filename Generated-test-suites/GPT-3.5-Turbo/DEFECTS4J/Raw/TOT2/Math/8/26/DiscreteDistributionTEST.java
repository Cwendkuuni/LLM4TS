import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.RandomGenerator;

public class DiscreteDistributionTest {
  
  private List<Pair<Integer, Double>> sampleList;
  private DiscreteDistribution<Integer> discreteDistribution;
  
  @Before
  public void setUp() {
    sampleList = new ArrayList<>();
    sampleList.add(new Pair<>(1, 0.25));
    sampleList.add(new Pair<>(2, 0.5));
    sampleList.add(new Pair<>(3, 0.25));
    
    try {
      discreteDistribution = new DiscreteDistribution<>(sampleList);
    } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testProbability() {
    assertEquals(0.25, discreteDistribution.probability(1), 0.001);
    assertEquals(0.5, discreteDistribution.probability(2), 0.001);
    assertEquals(0.25, discreteDistribution.probability(3), 0.001);
  }
  
  @Test
  public void testGetSamples() {
    List<Pair<Integer, Double>> result = discreteDistribution.getSamples();
    assertEquals(sampleList.size(), result.size());
    for (int i = 0; i < sampleList.size(); i++) {
      assertEquals(sampleList.get(i).getKey(), result.get(i).getKey());
      assertEquals(sampleList.get(i).getValue(), result.get(i).getValue(), 0.001);
    }
  }
  
  @Test
  public void testSample() {
    Integer sample = discreteDistribution.sample();
    assertNotNull(sample);
    assertTrue(sample == 1 || sample == 2 || sample == 3);
  }
  
  @Test
  public void testSampleWithSize() {
    try {
      Integer[] samples = discreteDistribution.sample(5);
      assertNotNull(samples);
      assertEquals(5, samples.length);
    } catch (Exception e) {
      fail("Exception thrown when it shouldn't have.");
    }
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSampleWithInvalidSize() {
    discreteDistribution.sample(0);
  }
}