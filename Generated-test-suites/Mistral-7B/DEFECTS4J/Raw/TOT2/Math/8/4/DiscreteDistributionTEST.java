import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

 private static final Well19937c RNG = new Well19937c();

 @Test
 public void testProbability() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(RNG,
 new java.util.ArrayList<Pair<Integer, Double>>() {{
 add(new Pair<Integer, Double>(1, 0.3));
 add(new Pair<Integer, Double>(2, 0.4));
 add(new Pair<Integer, Double>(3, 0.3));
 }});

 assertEquals(0.3, distribution.probability(1), 0.01);
 assertEquals(0.4, distribution.probability(2), 0.01);
 assertEquals(0.3, distribution.probability(3), 0.01);
 }

 @Test(expected = MathArithmeticException.class)
 public void testCreateWithZeroProbabilitiesSum() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(RNG,
 new java.util.ArrayList<Pair<Integer, Double>>() {{
 add(new Pair<Integer, Double>(1, 0.3));
 add(new Pair<Integer, Double>(2, 0.3));
 add(new Pair<Integer, Double>(3, -0.1));
 }});
 }

 @Test(expected = NotPositiveException.class)
 public void testCreateWithNegativeProbability() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(RNG,
 new java.util.ArrayList<Pair<Integer, Double>>() {{
 add(new Pair<Integer, Double>(1, 0.3));
 add(new Pair<Integer, Double>(2, 0.4));
 add(new Pair<Integer, Double>(3, -0.1));
 }});
 }

 @Test
 public void testGetSamples() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(RNG,
 new java.util.ArrayList<Pair<Integer, Double>>() {{
 add(new Pair<Integer, Double>(1, 0.3));
 add(new Pair<Integer, Double>(2, 0.4));
 add(new Pair<Integer, Double>(3, 0.3));
 }});

 List<Pair<Integer, Double>> samples = distribution.getSamples();

 assertEquals(3, samples.size());
 assertEquals(1, samples.get(0).getKey());
 assertEquals(0.3, samples.get(0).getValue(), 0.01);
 assertEquals(2, samples.get(1).getKey());
 assertEquals(0.4, samples.get(1).getValue(), 0.01);
 assertEquals(3, samples.get(2).getKey());
 assertEquals(0.3, samples.get(2).getValue(), 0.01);
 }

 @Test
 public void testSample() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(RNG,
 new java.util.ArrayList<Pair<Integer, Double>>() {{
 add(new Pair<Integer, Double>(1, 0.3));
 add(new Pair<Integer, Double>(2, 0.4));
 add(new Pair<Integer, Double>(3, 0.3));
 }});

 List<Integer> samples = new ArrayList<Integer>();
 int sample;
 for (int i = 0; i < 100; i++) {
 sample = distribution.sample();
 samples.add(sample);
 }

 double p1 = distribution.probability(1);
 double p2 = distribution.probability(2);
 double p3 = distribution.probability(3);

 double expected = p1 * 100 + p2 * 100 + p3 * 100;

 assertArrayEquals(expected.intValue(), samples.stream().mapToInt(i -> i.intValue()).toArray(), 0);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testSampleNegativeSize() {
 DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(RNG, new DiscreteDistribution<Integer>(RNG,
 new java.util.ArrayList<Pair<Integer, Double>>() {{
 add(new Pair<Integer, Double>(1, 0.3));
 add(new Pair<Integer, Double>(2, 0.4));
 add(new Pair<Integer, Double>(3, 0.3));
 }}).getSamples());

 distribution.sample(-1);
 }

}