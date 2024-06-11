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

 private DiscreteDistribution<Integer> distribution;
 private static final Well19937c RANDOM = new Well19937c();

 @Test
 public void testDiscreteDistributionWithValidInput() {
 List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.4), new Pair<>(3, 0.4));
 try {
 distribution = new DiscreteDistribution<>(RANDOM, samples);
 } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
 throw new RuntimeException(e);
 }

 List<Pair<Integer, Double>> expectedSamples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.4), new Pair<>(3, 0.4));
 List<Integer> singletons = distribution.getSamples();
 List<Pair<Integer, Double>> actualSamples = new ArrayList<>();
 for (int i = 0; i < singletons.size(); i++) {
 actualSamples.add(new Pair<>(singletons.get(i), distribution.probability(singletons.get(i))));
 }
 assertEquals(expectedSamples, actualSamples);
 }

 @Test(expected = NotPositiveException.class)
 public void testDiscreteDistributionWithNegativeProbability() {
 List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, -0.2), new Pair<>(2, 0.4), new Pair<>(3, 0.4));
 try {
 distribution = new DiscreteDistribution<>(RANDOM, samples);
 } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = MathArithmeticException.class)
 public void testDiscreteDistributionWithSumOfZeroProbabilities() {
 List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.1), new Pair<>(2, -0.1), new Pair<>(3, 0.1));
 try {
 distribution = new DiscreteDistribution<>(RANDOM, samples);
 } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testDiscreteDistributionWithInfProbability() {
 List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, Double.POSITIVE_INFINITY), new Pair<>(2, 0.4), new Pair<>(3, 0.4));
 try {
 distribution = new DiscreteDistribution<>(RANDOM, samples);
 } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testProbabilityOfValidSample() {
 List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.4), new Pair<>(3, 0.4));
 try {
 distribution = new DiscreteDistribution<>(RANDOM, samples);
 } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
 throw new RuntimeException(e);
 }

 double probability = distribution.probability(Integer.valueOf(1));
 assertEquals(0.2, probability, 0.001);
 }

 @Test
 public void testProbabilityOfInvalidSample() {
 List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.4), new Pair<>(3, 0.4));
 try {
 distribution = new DiscreteDistribution<>(RANDOM, samples);
 } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
 throw new RuntimeException(e);
 }

 double probability = distribution.probability(Integer.valueOf(4));
 assertEquals(0.0, probability, 0.001);
 }

 @Test
 public void testDiscreteDistributionSample() {
 List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.4), new Pair<>(3, 0.4));
 try {
 distribution = new DiscreteDistribution<>(RANDOM, samples);
 } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
 throw new RuntimeException(e);
 }

 Integer sample = distribution.sample();
 assertEquals(Integer.valueOf(2), sample);
 }

 @Test
 public void testDiscreteDistributionSampleSizeOne() {
 List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.4), new Pair<>(3, 0.4));
 try {
 distribution = new DiscreteDistribution<>(RANDOM, samples);
 } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
 throw new RuntimeException(e);
 }

 Integer[] samplesArray = distribution.sample(1);
 assertEquals(Integer.valueOf(1), samplesArray[0]);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testDiscreteDistributionNegativeSampleSize() {
 List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.4), new Pair<>(3, 0.4));
 try {
 distribution = new DiscreteDistribution<>(RANDOM, samples);
 } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
 throw new RuntimeException(e);
 }

 distribution.sample(-1);
 }

 @Test
 public void testDiscreteDistributionSampleSizeGreaterThanSingletonSize() {
 List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.4), new Pair<>(3, 0.4));
 try {
 distribution = new DiscreteDistribution<>(RANDOM, samples);
 } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
 throw new RuntimeException(e);
 }

 Integer[] samplesArray = distribution.sample(1000);
 assertEquals(samples.size(), samplesArray.length);
 }

}