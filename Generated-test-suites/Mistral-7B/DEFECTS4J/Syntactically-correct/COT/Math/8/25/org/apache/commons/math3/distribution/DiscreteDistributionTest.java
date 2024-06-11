import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DiscreteDistributionTest {

 private List<Pair<Object, Double>> getSampleList() {
 return new ArrayList<Pair<Object, Double>>() {{
 add(new Pair<>(1.0, 0.3));
 add(new Pair<>(2.0, 0.4));
 add(new Pair<>(3.0, 0.3));
 }};
 }

 @Test
 public void testConstructorWithValidSampleList() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), getSampleList());
 assertNotNull(distribution.probabilities);
 assertNotNull(distribution.singletons);
 assertArrayEquals(new Object[] {1.0, 2.0, 3.0}, distribution.singletons.toArray());
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeProbabilitySampleList() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), getSampleListWithNegativeProbability());
 }

 @Test(expected = MathArithmeticException.class)
 public void testConstructorWithZeroProbabilitySumSampleList() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), getSampleListWithZeroProbabilitySum());
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testConstructorWithInfiniteProbabilitySampleList() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), getSampleListWithInfiniteProbability());
 }

 @Test
 public void testProbabilityMethodWithValidValue() {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), getSampleList());
 assertEquals(0.3, distribution.probability(1.0), 0.001);
 }

 @Test
 public void testProbabilityMethodWithInvalidValue() {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), getSampleList());
 assertNull(distribution.probability(2.0));
 }

 @Test
 public void testGetSamplesMethod() {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), getSampleList());
 List<Pair<Object, Double>> samples = distribution.getSamples();
 assertNotNull(samples);
 assertArrayEquals(new Object[] {new Pair<>(1.0, 0.3), new Pair<>(2.0, 0.4), new Pair<>(3.0, 0.3)}, samples.toArray());
 }

 @Test
 public void testSampleMethodWithSingleCall() {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), getSampleList());
 Object sample = distribution.sample();
 assertNotNull(sample);
 assertEquals(1.0, sample);
 }

 @Test
 public void testSampleMethodWithMultipleCalls() {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), getSampleList());
 Set<Object> samples = new HashSet<Object>();
 for (int i = 0; i < 1000; i++) {
 samples.add(distribution.sample());
 }
 assertEquals(1000, samples.size());
 }

 @Test
 public void testSampleMethodWithZeroSize() throws NotStrictlyPositiveException {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), getSampleList());
 Object[] sample = distribution.sample(0);
 assertArrayEquals(new Object[0], sample);
 }

 @Test
 public void testSampleMethodWithValidSize() throws NotStrictlyPositiveException {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), getSampleList());
 Object[] sample = distribution.sample(5);
 assertNotNull(sample);
 assertArrayEquals(new Object[] {1.0, 2.0, 1.0, 3.0, 2.0}, sample);
 }

 @Test
 public void testReseedRandomGeneratorMethod() {
 DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), getSampleList());
 distribution.reseedRandomGenerator(1234L);
 }

 private List<Pair<Object, Double>> getSampleListWithNegativeProbability() {
 return new ArrayList<Pair<Object, Double>>() {{
 add(new Pair<>(1.0, -0.3));
 add(new Pair<>(2.0, 0.4));
 add(new Pair<>(3.0, 0.3));
 }};
 }

 private List<Pair<Object, Double>> getSampleListWithZeroProbabilitySum() {
 return new ArrayList<Pair<Object, Double>>() {{
 add(new Pair<>(1.0, 0.3));
 add(new Pair<>(2.0, 0.3));
 add(new Pair<>(3.0, 0));
 }};
 }

 private List<Pair<Object, Double>> getSampleListWithInfiniteProbability() {
 return new ArrayList<Pair<Object, Double>>() {{
 add(new Pair<>(1.0, Double.POSITIVE_INFINITY));
 add(new Pair<>(2.0, 0.4));
 add(new Pair<>(3.0, 0.3));
 }};
 }

}