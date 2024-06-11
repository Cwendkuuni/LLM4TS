import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DiscreteDistributionTest {

 private List<Pair<Integer, Double>> samples;
 private DiscreteDistribution<Integer> distribution;

 @org.junit.Before
 public void setUp() {
 samples = new ArrayList<>();
 samples.add(new Pair<Integer, Double>(1, 0.3));
 samples.add(new Pair<Integer, Double>(2, 0.4));
 samples.add(new Pair<Integer, Double>(3, 0.3));
 distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
 }

 @org.junit.Test
 public void testProbability() {
 assertEquals(0.3, distribution.probability(1), 0.001);
 assertEquals(0.4, distribution.probability(2), 0.001);
 assertEquals(0.3, distribution.probability(3), 0.001);
 }

 @org.junit.Test(expected = NotPositiveException.class)
 public void testNegativeProbability() {
 distribution = new DiscreteDistribution<Integer>(samples);
 }

 @org.junit.Test
 public void testGetSamples() {
 List<Pair<Integer, Double>> samplesFromDistribution = distribution.getSamples();
 assertNotNull(samplesFromDistribution);
 assertEquals(samples.size(), samplesFromDistribution.size());
 for (int i = 0; i < samples.size(); i++) {
 assertEquals(samples.get(i).getKey(), samplesFromDistribution.get(i).getKey());
 assertEquals(samples.get(i).getValue(), samplesFromDistribution.get(i).getValue(), 0.001);
 }
 }

 @org.junit.Test
 public void testSampleSingleValue() {
 Integer sample = distribution.sample();
 assertNotNull(sample);
 }

 @org.junit.Test
 public void testSampleMultipleValues() throws NotStrictlyPositiveException {
 Integer[] samples = distribution.sample(3);
 assertNotNull(samples);
 assertEquals(3, samples.length);
 }

 @org.junit.Test
 public void testReSeedRandomGenerator() {
 long seed = 123456789;
 distribution.reseedRandomGenerator(seed);
 // It's hard to test this method since we can't know the output
 // beforehand. Just check if no exception is thrown.
 }

}