import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

 private RandomGenerator rng = new Well19937c();

 @Test
 public void testDiscreteDistributionNegativeProbability() {
  List<Pair<Number, Double>> samples = new ArrayList<>();
  samples.add(new Pair<>(1.0, -1.0));

  try {
   new DiscreteDistribution<>(rng, samples);
   fail("Expected NotPositiveException");
  } catch (NotPositiveException e) {
   // Expected
  }
 }

 @Test
 public void testDiscreteDistributionZeroProbabilitiesSum() {
  List<Pair<Number, Double>> samples = new ArrayList<>();
  samples.add(new Pair<>(1.0, 0.0));
  samples.add(new Pair<>(2.0, 0.0));

  try {
   new DiscreteDistribution<>(rng, samples);
   fail("Expected MathArithmeticException");
  } catch (MathArithmeticException e) {
   // Expected
  }
 }

 @Test
 public void testDiscreteDistributionInfiniteProbability() {
  List<Pair<Number, Double>> samples = new ArrayList<>();
  samples.add(new Pair<>(1.0, Double.POSITIVE_INFINITY));

  try {
   new DiscreteDistribution<>(rng, samples);
   fail("Expected MathIllegalArgumentException");
  } catch (MathIllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testDiscreteDistributionEmptyList() {
  List<Pair<Number, Double>> samples = new ArrayList<>();

  try {
   new DiscreteDistribution<>(rng, samples);
   fail("Expected NotStrictlyPositiveException");
  } catch (NotStrictlyPositiveException e) {
   // Expected
  }
 }

 @Test
 public void testDiscreteDistributionProbability() {
  List<Pair<Number, Double>> samples = new ArrayList<>();
  samples.add(new Pair<>(1.0, 0.25));
  samples.add(new Pair<>(2.0, 0.5));
  samples.add(new Pair<>(3.0, 0.25));

  DiscreteDistribution<Number> dist = new DiscreteDistribution<>(rng, samples);

  assertEquals(0.25, dist.probability(1.0), 0.001);
  assertEquals(0.5, dist.probability(2.0), 0.001);
  assertEquals(0.25, dist.probability(3.0), 0.001);
 }

 @Test
 public void testDiscreteDistributionSample() {
  List<Pair<Number, Double>> samples = new ArrayList<>();
  samples.add(new Pair<>(1.0, 0.25));
  samples.add(new Pair<>(2.0, 0.5));
  samples.add(new Pair<>(3.0, 0.25));

  DiscreteDistribution<Number> dist = new DiscreteDistribution<>(rng, samples);

  assertEquals(1.0, dist.sample());
  assertEquals(1.0, dist.sample());
  assertEquals(2.0, dist.sample());
  assertEquals(3.0, dist.sample());
  assertEquals(1.0, dist.sample());
 }

 @Test
 public void testDiscreteDistributionSampleArray() {
  List<Pair<Number, Double>> samples = new ArrayList<>();
  samples.add(new Pair<>(1.0, 0.25));
  samples.add(new Pair<>(2.0, 0.5));
  samples.add(new Pair<>(3.0, 0.25));

  DiscreteDistribution<Number> dist = new DiscreteDistribution<>(rng, samples);

  Number[] out = dist.sample(5);
  Number[] expected = {1.0, 1.0, 2.0, 2.0, 3.0};
  assertArrayEquals(expected, out);
 }

 @Test
 public void testDiscreteDistributionGetSamples() {
  List<Pair<Number, Double>> samples = new ArrayList<>();
  samples.add(new Pair<>(1.0, 0.25));
  samples.add(new Pair<>(2.0, 0.5));
  samples.add(new Pair<>(3.0, 0.25));

  DiscreteDistribution<Number> dist = new DiscreteDistribution<>(rng, samples);

  List<Pair<Number, Double>> expected = Arrays.asList(new Pair<>(1.0, 0.25),
          new Pair<>(2.0, 0.5), new Pair<>(3.0, 0.25));
  assertEquals(expected, dist.getSamples());
 }

}