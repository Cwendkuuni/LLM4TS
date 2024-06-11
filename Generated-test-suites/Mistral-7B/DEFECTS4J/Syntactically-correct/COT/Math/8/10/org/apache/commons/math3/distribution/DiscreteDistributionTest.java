import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DiscreteDistributionTest {

 private List<Pair<Object, Double>> samples;
 private DiscreteDistribution<Object> distribution;
 private RandomGenerator random;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 samples = new ArrayList<>();
 samples.add(new Pair<>(1.0, 0.3));
 samples.add(new Pair<>(2.0, 0.4));
 samples.add(new Pair<>(3.0, 0.3));
 random = new Well19937c();
 }

 @Test
 public void testConstructorWithList() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 distribution = new DiscreteDistribution<>(samples);
 assertEquals(3, distribution.singletons.size());
 assertTrue(distribution.random instanceof Well19937c);
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 samples.set(0, new Pair<>(1.0, -0.3));
 distribution = new DiscreteDistribution<>(samples);
 }

 @Test(expected = MathArithmeticException.class)
 public void testConstructorWithZeroProbabilities() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 samples.stream().forEach(sample -> sample.setValue(0.0));
 distribution = new DiscreteDistribution<>(samples);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testConstructorWithInfiniteProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 samples.set(0, new Pair<>(1.0, Double.POSITIVE_INFINITY));
 distribution = new DiscreteDistribution<>(samples);
 }

 @Test
 public void testProbabilityMethod() {
 assertEquals(0.3, distribution.probability((Double) 1.0), 0.001);
 assertEquals(0.4, distribution.probability((Double) 2.0), 0.001);
 assertEquals(0.3, distribution.probability((Double) 3.0), 0.001);
 }

 @Test
 public void testSampleMethod() {
 assertNotNull(distribution.sample());
 }

 @Test
 public void testSampleMethodWithArray() {
 final int sampleSize = 10;
 T[] sample = distribution.sample(sampleSize);
 assertEquals(sampleSize, sample.length);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testSampleMethodWithNegativeSampleSize() {
 distribution.sample(-1);
 }

 @Test
 public void testGetSamplesMethod() {
 List<Pair<Object, Double>> retrievedSamples = distribution.getSamples();
 assertEquals(samples.size(), retrievedSamples.size());
 }

 @Test
 public void testReseedRandomGeneratorMethod() {
 long seed = 123456789;
 distribution.reseedRandomGenerator(seed);
 }

}