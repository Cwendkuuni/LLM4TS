import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

 private DiscreteDistribution<Integer> distribution;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 List<Pair<Integer, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(1, 0.1));
 samples.add(new Pair<>(2, 0.3));
 samples.add(new Pair<>(3, 0.4));
 samples.add(new Pair<>(4, 0.2));

 distribution = new DiscreteDistribution<>(new Well19937c(), samples);
 }

 @Test
 public void testProbability() {
 List<Pair<Integer, Double>> samples = distribution.getSamples();
 for (Pair<Integer, Double> sample : samples) {
 assertEquals(sample.getValue(), distribution.probability(sample.getKey()), 0.0001);
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testNegativeProbability() throws Exception {
 distribution = new DiscreteDistribution<>(new Well19937c(),
 new ArrayList<Pair<Integer, Double>>() {{
 add(new Pair<>(1, -0.1));
 addAll(samples);
 }});
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testInfiniteProbability() throws Exception {
 distribution = new DiscreteDistribution<>(new Well19937c(),
 new ArrayList<Pair<Integer, Double>>() {{
 addAll(samples);
 add(new Pair<>(Integer.MAX_VALUE, Double.POSITIVE_INFINITY));
 }});
 }

 @Test(expected = MathArithmeticException.class)
 public void testZeroProbability() throws Exception {
 List<Pair<Integer, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(1, 0.1));
 samples.addAll(new ArrayList<Pair<Integer, Double>>(DiscreteDistribution.DEFAULT_PROBABILITIES.length) {{
 for (int i = 0; i < DiscreteDistribution.DEFAULT_PROBABILITIES.length; i++) {
 add(new Pair<>(null, DiscreteDistribution.DEFAULT_PROBABILITIES[i]));
 }
 }});

 distribution = new DiscreteDistribution<>(new Well19937c(), samples);
 }

 @Test
 public void testSample() {
 Integer sample = distribution.sample();
 List<Pair<Integer, Double>> samples = distribution.getSamples();
 int index = -1;
 for (int i = 0; i < samples.size(); i++) {
 if (sample.equals(samples.get(i).getKey())) {
 index = i;
 break;
 }
 }

 assertTrue(index != -1);
 }

 @Test(expected = IllegalStateException.class)
 public void testEmptySample() {
 distribution.sample(0);
 }

 @Test
 public void testSampleSize() throws Exception {
 Integer[] samples = (Integer[]) distribution.sample(5);
 for (Integer sample : samples) {
 List<Pair<Integer, Double>> distributionSamples = distribution.getSamples();
 int index = -1;
 for (int i = 0; i < distributionSamples.size(); i++) {
 if (sample.equals(distributionSamples.get(i).getKey())) {
 index = i;
 break;
 }
 }

 assertTrue(index != -1);
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeSampleSize() throws Exception {
 distribution.sample(-1);
 }
}