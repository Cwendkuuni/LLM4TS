import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DiscreteDistributionTest {

 private List<Pair<Object, Double>> validSamples;
 private List<Pair<Object, Double>> invalidSamples;
 private List<Pair<Object, Double>> infiniteSamples;
 private List<Pair<Object, Double>> negativeSamples;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 List<Pair<Object, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(1L, 0.3));
 samples.add(new Pair<>(2L, 0.2));
 samples.add(new Pair<>(3L, 0.5));
 this.validSamples = samples;

 samples = new ArrayList<>();
 samples.add(new Pair<>(null, 0.3));
 this.invalidSamples = samples;

 samples = new ArrayList<>();
 samples.add(new Pair<>(1L, Double.POSITIVE_INFINITY));
 this.infiniteSamples = samples;

 samples = new ArrayList<>();
 samples.add(new Pair<>(1L, -0.1));
 this.negativeSamples = samples;
 }

 @Test
 public void testProbabilityMethod() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 DiscreteDistribution<Long> distribution = new DiscreteDistribution<>(validSamples);

 assertEquals(0.3, distribution.probability((long) 1), 0.001);
 assertEquals(0.2, distribution.probability((long) 2), 0.001);
 assertEquals(0.5, distribution.probability((long) 3), 0.001);
 assertNull(distribution.probability(null));
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testProbabilityMethodWithInvalidSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<>(invalidSamples);
 distribution.probability(null);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testProbabilityMethodWithInfiniteSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<>(infiniteSamples);
 distribution.probability(null);
 }

 @Test(expected = NotPositiveException.class)
 public void testProbabilityMethodWithNegativeSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<>(negativeSamples);
 distribution.probability(null);
 }

 @Test
 public void testGetSamplesMethod() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 DiscreteDistribution<Long> distribution = new DiscreteDistribution<>(validSamples);
 List<Pair<Long, Double>> samples = distribution.getSamples();

 assertEquals(samples.size(), validSamples.size());
 for (int i = 0; i < samples.size(); i++) {
 assertEquals(samples.get(i).getKey(), singleton.get(i));
 assertEquals(samples.get(i).getValue(), validSamples.get(i).getValue(), 0.001);
 }
 }

 @Test
 public void testSampleMethod() throws NotStrictlyPositiveException {
 DiscreteDistribution<Long> distribution = new DiscreteDistribution<>(validSamples);
 Long sample = distribution.sample();
 assertNotNull(sample);
 }

 @Test
 public void testSampleMethodWithZeroSampleSize() throws NotStrictlyPositiveException {
 DiscreteDistribution<Long> distribution = new DiscreteDistribution<>(validSamples);
 long[] samples = distribution.sample(0);
 assertNotNull(samples);
 assertEmpty(samples);
 }

 @Test
 public void testSampleMethodWithSampleSizeGreaterThanTotalSamples() throws NotStrictlyPositiveException {
 DiscreteDistribution<Long> distribution = new DiscreteDistribution<>(validSamples);
 long[] samples = distribution.sample(validSamples.size() + 1);
 assertNotNull(samples);
 }

 @Test
 public void testSampleMethodMultipleSamples() throws NotStrictlyPositiveException {
 DiscreteDistribution<Long> distribution = new DiscreteDistribution<>(validSamples);
 long[] samples = distribution.sample(5);
 for (int i = 0; i < samples.length; i++) {
 assertNotNull(samples[i]);
 }
 }

 @Test
 public void testSampleMethodWithSameSampleMultipleTimes() throws NotStrictlyPositiveException {
 DiscreteDistribution<Long> distribution = new DiscreteDistribution<>(validSamples);
 long sample = distribution.sample();
 long[] samples = distribution.sample(5);
 for (int i = 0; i < samples.length; i++) {
 assertEquals(sample, samples[i]);
 }
 }

 @Test
 public void testReseedRandomGenerator() {
 DiscreteDistribution<Long> distribution = new DiscreteDistribution<>(validSamples);
 long seed = 123456L;
 distribution.reseedRandomGenerator(seed);
 }
}