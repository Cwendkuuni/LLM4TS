import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

   private static final double EPSILON = 1e-6;
   private static final int SEED = 12345;

   private DiscreteDistribution<Integer> distribution;

   @Before
   public void setUp() {
       List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
       samples.add(new Pair<Integer, Double>(1, 0.4));
       samples.add(new Pair<Integer, Double>(2, 0.3));
       samples.add(new Pair<Integer, Double>(3, 0.1));
       samples.add(new Pair<Integer, Double>(4, 0.2));
       distribution = new DiscreteDistribution<Integer>(samples);
       distribution.reseedRandomGenerator(SEED);
   }

   @Test
   public void testProbability() {
       assertEquals(0.4, distribution.probability(1), EPSILON);
       assertEquals(0.3, distribution.probability(2), EPSILON);
       assertEquals(0.1, distribution.probability(3), EPSILON);
       assertEquals(0.2, distribution.probability(4), EPSILON);

       assertEquals(0.0, distribution.probability(0), EPSILON); // edge case where x is outside the range
   }

   @Test
   public void testGetSamples() {
       List<Pair<Integer, Double>> samples = distribution.getSamples();

       assertEquals(4, samples.size()); // check size

       assertEquals(1, samples.get(0).getKey().intValue()); // check key
       assertEquals(0.4, samples.get(0).getValue(), EPSILON); // check value

       assertEquals(2, samples.get(1).getKey().intValue());
       assertEquals(0.3, samples.get(1).getValue(), EPSILON);

       assertEquals(3, samples.get(2).getKey().intValue());
       assertEquals(0.1, samples.get(2).getValue(), EPSILON);

       assertEquals(4, samples.get(3).getKey().intValue());
       assertEquals(0.2, samples.get(3).getValue(), EPSILON);
   }

   @Test
   public void testSample() {
       Integer sample1 = distribution.sample();
       Integer sample2 = distribution.sample();

       assertNotNull(sample1); // check if not null
       assertNotNull(sample2);

       assertNotEquals(sample1, sample2); // check if different samples

       assertTrue(sample1 >= 1 && sample1 <= 4); // check if within range
       assertTrue(sample2 >= 1 && sample2 <= 4);
   }

   @Test
   public void testSample_invalidSampleSize() {
       try {
           distribution.sample(0);
           fail("Expected NotStrictlyPositiveException");
       } catch (NotStrictlyPositiveException e) {
           // pass
       }
   }

   @Test
   public void testSample_validSampleSize() {
       int sampleSize = 5;
       Integer[] sample = distribution.sample(sampleSize);

       assertNotNull(sample); // check if not null
       assertEquals(sampleSize, sample.length); // check if correct size

       for (Integer value : sample) {
           assertTrue(value >= 1 && value <= 4); // check if within range
       }
   }

   @Test
   public void testSample_allValuesInsideRange() {
       int sampleSize = 10000;
       Integer[] sample = distribution.sample(sampleSize);

       assertNotNull(sample); // check if not null
       assertEquals(sampleSize, sample.length); // check if correct size

       int count1 = 0;
       int count2 = 0;
       int count3 = 0;
       int count4 = 0;

       for (Integer value : sample) {
           assertTrue(value >= 1 && value <= 4); // check if within range

           if (value == 1) {
               count1++;
           } else if (value == 2) {
               count2++;
           } else if (value == 3) {
               count3++;
           } else if (value == 4) {
               count4++;
           }
       }

       double probability1 = (double) count1 / sampleSize;
       double probability2 = (double) count2 / sampleSize;
       double probability3 = (double) count3 / sampleSize;
       double probability4 = (double) count4 / sampleSize;

       assertEquals(0.4, probability1, 0.01); // check if probabilities are close to expected
       assertEquals(0.3, probability2, 0.01);
       assertEquals(0.1, probability3, 0.01);
       assertEquals(0.2, probability4, 0.01);
   }
}