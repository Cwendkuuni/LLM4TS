import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiscreteDistributionTest {

 private List<Pair<Object, Double>> createSampleList(Object[] values, double[] probabilities) {
 List<Pair<Object, Double>> sampleList = new ArrayList<>();
 for (int i = 0; i < values.length; i++) {
 sampleList.add(new Pair<>(values[i], probabilities[i]));
 }
 return sampleList;
 }

 @Test
 public void testProbabilityMethod() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(
 Collections.singletonList(new Pair<>(1, 0.3)), new Well19937c());
 assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProbabilityMethodWithNullValue() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(
 Collections.singletonList(new Pair<>(1, 0.3)), new Well19937c());
 distribution.probability(null);
 }

 @Test
 public void testGetSamplesMethod() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(
 Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4), new Pair<>(3, 0.3)), new Well19937c());
 List<Pair<Integer, Double>> samples = distribution.getSamples();
 assertEquals(3, samples.size());
 }

 @Test
 public void testSampleMethod() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(
 Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4), new Pair<>(3, 0.3)), new Well19937c());
 Integer sample = distribution.sample();
 assertEquals(1, sample.intValue());
 }

 @Test
 public void testSampleMethodWithMultipleValues() throws NotStrictlyPositiveException {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(
 Collections.singletonList(new Pair<>(1, 0.3)), new Well19937c());
 Integer[] samples = distribution.sample(5);
 List<Integer> sampleList = Arrays.asList(samples);
 ArrayList<Integer> expectedList = new ArrayList<>();
 expectedList.add(1);
 for (int i = 0; i < 4; i++) {
 expectedList.add(null);
 }
 assertEquals(expectedList, sampleList);
 }
}