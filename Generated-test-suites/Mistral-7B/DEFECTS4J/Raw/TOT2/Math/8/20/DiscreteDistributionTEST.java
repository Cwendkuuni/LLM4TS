import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

 private List<Pair<Integer, Double>> samples;
 private DiscreteDistribution<Integer> distribution;
 private RandomGenerator rng;

 @Test
 public void testDiscreteDistributionWithValidInput() {
 rng = new Well19937c();
 samples = java.util.Arrays.asList(new Pair<Integer, Double>(1, 0.3), new Pair<Integer, Double>(2, 0.4),
 new Pair<Integer, Double>(3, 0.3));
 try {
 distribution = new DiscreteDistribution<Integer>(rng, samples);
 } catch (MathIllegalArgumentException e) {
 fail("Exception should not be thrown");
 }

 List<Integer> singletons = distribution.getSamples();
 double[] probabilities = distribution.probabilities;

 // Test method probability() with valid input
 assertEquals(0.3, distribution.probability(1));
 assertEquals(0.4, distribution.probability(2));
 assertEquals(0.3, distribution.probability(3));

 // Test method getSamples() with valid input
 List<Pair<Integer, Double>> getSamples = distribution.getSamples();
 assertEquals(singletons, getSamples);
 }

 @Test
 public void testDiscreteDistributionWithNullInput() {
 rng = new Well19937c();
 samples = java.util.Arrays.asList(new Pair<Integer, Double>(null, 0.3), new Pair<Integer, Double>(2, 0.4),
 new Pair<Integer, Double>(3, 0.3));
 try {
 distribution = new DiscreteDistribution<Integer>(rng, samples);
 fail("Exception should be thrown");
 } catch (MathIllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testDiscreteDistributionWithNegativeProbabilities() {
 rng = new Well19937c();
 samples = java.util.Arrays.asList(new Pair<Integer, Double>(1, -0.3), new Pair<Integer, Double>(2, 0.4),
 new Pair<Integer, Double>(3, 0.3));
 try {
 distribution = new DiscreteDistribution<Integer>(rng, samples);
 fail("Exception should be thrown");
 } catch (NotPositiveException e) {
 // expected
 }
 }

 @Test
 public void testDiscreteDistributionWithInfProbabilities() {
 rng = new Well19937c();
 samples = java.util.Arrays.asList(new Pair<Integer, Double>(1, Double.POSITIVE_INFINITY), new Pair<Integer, Double>(2, 0.4),
 new Pair<Integer, Double>(3, 0.3));
 try {
 distribution = new DiscreteDistribution<Integer>(rng, samples);
 fail("Exception should be thrown");
 } catch (MathIllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testDiscreteDistributionWithProbabilitiesSumToZero() {
 rng = new Well19937c();
 samples = java.util.Arrays.asList(new Pair<Integer, Double>(1, 0.3), new Pair<Integer, Double>(2, -0.1),
 new Pair<Integer, Double>(3, 0.4));
 try {
 distribution = new DiscreteDistribution<Integer>(rng, samples);
 fail("Exception should be thrown");
 } catch (MathArithmeticException e) {
 // expected
 }
 }

 @Test
 public void testDiscreteDistributionWithEmptyList() {
 rng = new Well19937c();
 samples = new ArrayList<>();
 try {
 distribution = new DiscreteDistribution<Integer>(rng, samples);
 fail("Exception should be thrown");
 } catch (NotPositiveException e) {
 // expected
 }
 }

 @Test
 public void testDiscreteDistributionSampleSingleValue() {
 rng = new Well19937c();
 samples = java.util.Arrays.asList(new Pair<Integer, Double>(1, 0.3),
 new Pair<Integer, Double>(2, 0.4), new Pair<Integer, Double>(3, 0.3));
 try {
 distribution = new DiscreteDistribution<Integer>(rng, samples);
 } catch (Exception e) {
 fail("Exception should not be thrown");
 }
 int sample = distribution.sample();
 assertEquals(1, sample.intValue());
 }

 @Test
 public void testDiscreteDistributionSampleMultipleValues() throws NotStrictlyPositiveException {
 rng = new Well19937c();
 samples = java.util.Arrays.asList(new Pair<Integer, Double>(1, 0.3),
 new Pair<Integer, Double>(2, 0.4), new Pair<Integer, Double>(3, 0.3));
 try {
 distribution = new DiscreteDistribution<Integer>(rng, samples);
 } catch (Exception e) {
 fail("Exception should not be thrown");
 }
 int[] samplesArray = distribution.sample(3);
 assertEquals(3, samplesArray.length);
 assertArrayEquals(new Integer[] {1, 1, 2}, samplesArray);
 }
}