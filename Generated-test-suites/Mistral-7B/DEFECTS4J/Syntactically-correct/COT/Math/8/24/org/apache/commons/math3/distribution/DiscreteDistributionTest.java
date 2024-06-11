import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DiscreteDistributionTest {

 private RandomGenerator rng = new Well19937c();

 @Test
 public void testDiscreteDistributionConstructorWithInvalidSamples() {
 try {
 new DiscreteDistribution<>(null);
 assertFalse("Expected IllegalArgumentException due to null input", true);
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 new DiscreteDistribution<>(rng, null);
 assertFalse("Expected IllegalArgumentException due to null input", true);
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 List<Pair<Object, Double>> samples = new ArrayList<>();
 new DiscreteDistribution<>(rng, samples);
 assertFalse("Expected NotPositiveException", true);
 } catch (NotPositiveException e) {
 // Expected
 }
 }

 @Test
 public void testDiscreteDistributionConstructorWithValidSamples() {
 List<Pair<Object, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(1D, 0.5));
 samples.add(new Pair<>(2D, 0.3));
 samples.add(new Pair<>(3D, 0.2));

 try {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<>(rng, samples);
 assertNotNull(distribution);
 assertEquals(samples.size(), distribution.probabilities.length);
 } catch (NotPositiveException e) {
 assertFalse("Expected no exceptions", true);
 } catch (MathArithmeticException e) {
 assertFalse("Expected no exceptions", true);
 } catch (MathIllegalArgumentException e) {
 assertFalse("Expected no exceptions", true);
 }
 }

 @Test (expected = NotPositiveException.class)
 public void testDiscreteDistributionConstructorWithNegativeProbability() {
 List<Pair<Object, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(1D, -0.5));
 new DiscreteDistribution<>(rng, samples);
 }

 @Test (expected = MathArithmeticException.class)
 public void testDiscreteDistributionConstructorWithZeroProbabilities() {
 List<Pair<Object, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(1D, 0.0));
 samples.add(new Pair<>(2D, 0.0));
 new DiscreteDistribution<>(rng, samples);
 }

 @Test (expected = MathIllegalArgumentException.class)
 public void testDiscreteDistributionConstructorWithInfiniteProbability() {
 List<Pair<Object, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(1D, Double.POSITIVE_INFINITY));
 new DiscreteDistribution<>(rng, samples);
 }

 @Test
 public void testProbabilityMethod() {
 DiscreteDistribution<Object> distribution = getSampleDistribution();

 Object value1 = new Object();
 double probability1 = distribution.probability(value1);
 assertTrue(probability1 > 0);

 Object value2 = new Object();
 double probability2 = distribution.probability(value2);
 assertTrue(probability2 > 0);

 Object value3 = new Object();
 double probability3 = distribution.probability(value3);
 assertTrue(probability3 > 0);

 Object valueNull = null;
 double probabilityNull = distribution.probability(valueNull);
 assertFalse(probabilityNull > 0);
 }

 @Test
 public void testGetSamplesMethod() {
 DiscreteDistribution<Object> distribution = getSampleDistribution();
 List<Pair<Object, Double>> samples = distribution.getSamples();
 assertNotNull(samples);
 assertEquals(distribution.singletons.size(), samples.size());

 for (int i = 0; i < samples.size(); i++) {
 assertEquals(distribution.singletons.get(i), samples.get(i).getKey());
 assertEquals(distribution.probabilities[i], samples.get(i).getValue(), 0.001);
 }
 }

 @Test
 public void testSampleMethod() {
 DiscreteDistribution<Object> distribution = getSampleDistribution();
 Object sample = distribution.sample();
 assertNotNull(sample);
 }

 @Test
 public void testSampleArrayMethod() {
 DiscreteDistribution<Object> distribution = getSampleDistribution();
 int sampleSize = 10;
 Object[] sample = new Object[sampleSize];
 try {
 distribution.sample(sampleSize);
 assertArrayEquals(distribution.singletons.subList(0, distribution.singletons.size()).toArray(), sample);
 } catch (NotStrictlyPositiveException e) {
 assertFalse("Expected no exceptions", true);
 }
 }

 @Test
 public void testReseedRandomGeneratorMethod() {
 DiscreteDistribution<Object> distribution = getSampleDistribution();
 long seed = System.currentTimeMillis();
 distribution.reseedRandomGenerator(seed);
 long newSeed = distribution.random.getSeed();
 assertEquals(seed, newSeed);
 }

 private DiscreteDistribution<Object> getSampleDistribution() {
 List<Pair<Object, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(1D, 0.5));
 samples.add(new Pair<>(2D, 0.3));
 samples.add(new Pair<>(3D, 0.2));

 try {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<>(rng, samples);
 return distribution;
 } catch (NotPositiveException e) {
 assertFalse("Expected no exceptions", true);
 } catch (MathArithmeticException e) {
 assertFalse("Expected no exceptions", true);
 } catch (MathIllegalArgumentException e) {
 assertFalse("Expected no exceptions", true);
 }

 return null;
 }

}