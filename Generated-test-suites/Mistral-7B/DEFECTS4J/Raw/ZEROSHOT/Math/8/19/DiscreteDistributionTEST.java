import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DiscreteDistributionTest {

 @Test
 public void testDiscreteDistributionConstruction() {
 List<Pair<Comparable, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(5d, 0.3));
 samples.add(new Pair<>(10d, 0.4));
 samples.add(new Pair<>(15d, 0.3));

 try {
 DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(samples);
 } catch (NotPositiveException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test(expected = MathArithmeticException.class)
 public void testZeroProbabilitySum() {
 List<Pair<Comparable, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(5d, 0.3));
 samples.add(new Pair<>(10d, -0.4));
 samples.add(new Pair<>(15d, 0.3));

 try {
 DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(samples);
 } catch (NotPositiveException | MathIllegalArgumentException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testInfiniteProbability() {
 List<Pair<Comparable, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(5d, 0.3));
 samples.add(new Pair<>(10d, Double.POSITIVE_INFINITY));
 samples.add(new Pair<>(15d, 0.3));

 try {
 DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(samples);
 } catch (NotPositiveException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test
 public void testProbability() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), new ArrayList<>());
 distribution.reseedRandomGenerator(123456789);

 assertEquals(0.0, distribution.probability((Integer) null), 1e-14);

 DiscreteDistribution<Integer> sampleDistribution = new DiscreteDistribution<>(
 new Well19937c(),
 new ArrayList<Pair<Integer, Double>>() {{
 add(new Pair<>(2, 0.1));
 add(new Pair<>(3, 0.4));
 add(new Pair<>(4, 0.3));
 add(new Pair<>(5, 0.2));
 }}
 );
 sampleDistribution.reseedRandomGenerator(123456789);

 assertEquals(0.1, sampleDistribution.probability(2), 1e-14);
 assertEquals(0.4, sampleDistribution.probability(3), 1e-14);
 assertEquals(0.3, sampleDistribution.probability(4), 1e-14);
 assertEquals(0.2, sampleDistribution.probability(5), 1e-14);
 }

 @Test
 public void testGetSamples() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(
 new Well19937c(),
 new ArrayList<Pair<Integer, Double>>() {{
 add(new Pair<>(2, 0.1));
 add(new Pair<>(3, 0.4));
 add(new Pair<>(4, 0.3));
 add(new Pair<>(5, 0.2));
 }}
 );
 List<Pair<Integer, Double>> samples = distribution.getSamples();

 assertEquals(samples.size(), 4);
 assertEquals(2, samples.get(0).getKey());
 assertEquals(0.1, samples.get(0).getValue(), 1e-14);
 assertEquals(3, samples.get(1).getKey());
 assertEquals(0.4, samples.get(1).getValue(), 1e-14);
 // ...
 }

 @Test
 public void testSample() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(
 new Well19937c(),
 new ArrayList<Pair<Integer, Double>>() {{
 add(new Pair<>(2, 0.1));
 add(new Pair<>(3, 0.4));
 add(new Pair<>(4, 0.3));
 add(new Pair<>(5, 0.2));
 }}
 );

 List<Integer> sampledValues = new ArrayList<>();
 for (int i = 0; i < 1000; i++) {
 sampledValues.add(distribution.sample());
 }

 assertEquals(sampledValues.size(), 1000);
 }

 @Test
 public void testSampleSize() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(
 new Well19937c(),
 new ArrayList<Pair<Integer, Double>>() {{
 add(new Pair<>(2, 0.1));
 add(new Pair<>(3, 0.4));
 add(new Pair<>(4, 0.3));
 add(new Pair<>(5, 0.2));
 }}
 );

 try {
 distribution.sample(-1);
 throw new AssertionError("Unexpected call success");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }
}