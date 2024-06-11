import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {
  
  @Test
  public void testDiscreteDistribution_NormalCase() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
    List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
    samples.add(new Pair<Integer, Double>(1, 0.2));
    samples.add(new Pair<Integer, Double>(2, 0.3));
    samples.add(new Pair<Integer, Double>(3, 0.5));
    
    DiscreteDistribution<Integer> d = new DiscreteDistribution<Integer>(samples);
    
    assertEquals(0.2, d.probability(1), 0.0001);
    assertEquals(0.3, d.probability(2), 0.0001);
    assertEquals(0.5, d.probability(3), 0.0001);
    
    List<Pair<Integer, Double>> returnedSamples = d.getSamples();
    assertEquals(3, returnedSamples.size());
    
    assertEquals(1, returnedSamples.get(0).getKey().intValue());
    assertEquals(0.2, returnedSamples.get(0).getValue(), 0.0001);
    
    assertEquals(2, returnedSamples.get(1).getKey().intValue());
    assertEquals(0.3, returnedSamples.get(1).getValue(), 0.0001);
    
    assertEquals(3, returnedSamples.get(2).getKey().intValue());
    assertEquals(0.5, returnedSamples.get(2).getValue(), 0.0001);
  }
  
  @Test(expected = NotPositiveException.class)
  public void testDiscreteDistribution_NegativeProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
    List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
    samples.add(new Pair<Integer, Double>(1, 0.2));
    samples.add(new Pair<Integer, Double>(2, -0.3));
    samples.add(new Pair<Integer, Double>(3, 0.5));
    
    DiscreteDistribution<Integer> d = new DiscreteDistribution<Integer>(samples);
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testDiscreteDistribution_ZeroProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
    List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
    samples.add(new Pair<Integer, Double>(1, 0.2));
    samples.add(new Pair<Integer, Double>(2, 0.3));
    samples.add(new Pair<Integer, Double>(3, 0.0));
    
    DiscreteDistribution<Integer> d = new DiscreteDistribution<Integer>(samples);
  }
  
  @Test(expected = MathIllegalArgumentException.class)
  public void testDiscreteDistribution_InfiniteProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
    List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
    samples.add(new Pair<Integer, Double>(1, 0.2));
    samples.add(new Pair<Integer, Double>(2, Double.POSITIVE_INFINITY));
    samples.add(new Pair<Integer, Double>(3, 0.5));
    
    DiscreteDistribution<Integer> d = new DiscreteDistribution<Integer>(samples);
  }
  
  @Test
  public void testReseedRandomGenerator() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
    List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
    samples.add(new Pair<Integer, Double>(1, 0.2));
    samples.add(new Pair<Integer, Double>(2, 0.3));
    samples.add(new Pair<Integer, Double>(3, 0.5));
    
    DiscreteDistribution<Integer> d = new DiscreteDistribution<Integer>(samples);
    
    d.reseedRandomGenerator(12345);
    // No way to test this directly
  }
  
  @Test
  public void testSample() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
    List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
    samples.add(new Pair<Integer, Double>(1, 0.2));
    samples.add(new Pair<Integer, Double>(2, 0.3));
    samples.add(new Pair<Integer, Double>(3, 0.5));
    
    DiscreteDistribution<Integer> d = new DiscreteDistribution<Integer>(samples);
    
    Integer sample = d.sample();
    assertTrue(sample >= 1 && sample <= 3);
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testSample_InvalidSize() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException, NotStrictlyPositiveException {
    List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
    samples.add(new Pair<Integer, Double>(1, 0.2));
    samples.add(new Pair<Integer, Double>(2, 0.3));
    samples.add(new Pair<Integer, Double>(3, 0.5));
    
    DiscreteDistribution<Integer> d = new DiscreteDistribution<Integer>(samples);
    
    Integer[] sample = d.sample(0);
  }
  
  @Test
  public void testSample_CorrectSize() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException, NotStrictlyPositiveException {
    List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
    samples.add(new Pair<Integer, Double>(1, 0.2));
    samples.add(new Pair<Integer, Double>(2, 0.3));
    samples.add(new Pair<Integer, Double>(3, 0.5));
    
    DiscreteDistribution<Integer> d = new DiscreteDistribution<Integer>(samples);
    
    Integer[] sample = d.sample(10);
    assertEquals(10, sample.length);
  }
  
}