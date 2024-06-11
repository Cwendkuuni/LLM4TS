import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

 private DiscreteDistribution<Integer> distribution;
 private List<Pair<Integer, Double>> samples;
 private Well19937c rng;

 @Before
 public void setUp() {
 rng = new Well19937c();
 samples = new ArrayList<>();
 samples.add(new Pair<>(1, 0.3));
 samples.add(new Pair<>(2, 0.4));
 samples.add(new Pair<>(3, 0.3));
 try {
 distribution = new DiscreteDistribution<>(rng, samples);
 } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
 e.printStackTrace();
 fail("Failed to create distribution instance.");
 }
 }

 @Test
 public void testProbability() {
 assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
 assertEquals(0.4, distribution.probability((Integer) 2), 0.001);
 assertEquals(0.3, distribution.probability((Integer) 3), 0.001);
 assertNull(distribution.probability((Integer) 4));
 }

 @Test
 public void testGetSamples() {
 List<Pair<Integer, Double>> samplesFromDistribution = distribution.getSamples();
 assertEquals(samples.size(), samplesFromDistribution.size());
 for (int i = 0; i < samples.size(); i++) {
 assertEquals(samples.get(i).getKey(), samplesFromDistribution.get(i).getKey());
 assertEquals(samples.get(i).getValue(), samplesFromDistribution.get(i).getValue(), 0.001);
 }
 }

 @Test
 public void testSample() {
 Integer sample = distribution.sample();
 assertTrue(samples.contains(new Pair<>(sample, 1.0)));
 }

 @Test
 public void testSampleArray() throws NotStrictlyPositiveException {
 Integer[] samplesArray = distribution.sample(3);
 for (int i = 0; i < samplesArray.length; i++) {
 assertTrue(samples.contains(new Pair<>(samplesArray[i], 1.0)));
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testSampleNegativeSize() throws NotStrictlyPositiveException {
 distribution.sample(-1);
 fail("Expected NotStrictlyPositiveException but didn't get one.");
 }
}