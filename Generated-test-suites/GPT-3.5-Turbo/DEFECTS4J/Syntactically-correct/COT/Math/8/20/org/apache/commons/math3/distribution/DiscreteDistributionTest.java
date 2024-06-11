
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

  @Test
  public void testProbability() {
    //Test case 1: Check probability of valid value
    List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
    samples.add(new Pair<String, Double>("A", 0.2));
    samples.add(new Pair<String, Double>("B", 0.3));
    samples.add(new Pair<String, Double>("C", 0.5));
    DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
    double p = distribution.probability("B");
    assertEquals(0.3, p, 1e-6);
    
    //Test case 2: Check probability of null value
    p = distribution.probability(null);
    assertEquals(0.0, p, 1e-6);
  }

  @Test
  public void testGetSamples() {
    //Test case 1: Check if getSamples returns the correct size list 
    List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
    samples.add(new Pair<String, Double>("A", 0.2));
    samples.add(new Pair<String, Double>("B", 0.3));
    samples.add(new Pair<String, Double>("C", 0.5));
    DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
    List<Pair<String, Double>> result = distribution.getSamples();
    assertEquals(samples.size(), result.size());
    
    //Test case 2: Check if getSamples returns the correct values
    boolean equal = true;
    for(int i=0; i<samples.size(); i++){
      if(!samples.get(i).equals(result.get(i))){
        equal = false;
        break;
      }
    }
    assertTrue(equal);
  }

  @Test
  public void testSample() {
    //Test case 1: Check if the sample is in the correct range
    List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
    samples.add(new Pair<String, Double>("A", 0.2));
    samples.add(new Pair<String, Double>("B", 0.3));
    samples.add(new Pair<String, Double>("C", 0.5));
    DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
    String sample = distribution.sample();
    assertTrue(samples.contains(sample));
  }

  @Test
  public void testSampleIntArray() {
    //Test case 1: Check if the sample array has correct length
    List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
    samples.add(new Pair<String, Double>("A", 0.2));
    samples.add(new Pair<String, Double>("B", 0.3));
    samples.add(new Pair<String, Double>("C", 0.5));
    DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
    int sampleSize = 10;
    String[] sample = distribution.sample(sampleSize);
    assertEquals(sampleSize, sample.length);

    //Test case 2: Check if the sample array contains valid values
    boolean validValues = true;
    for(String s : sample){
      if(!samples.contains(s)){
        validValues = false;
        break;
      }
    }
    assertTrue(validValues);
  }
}
