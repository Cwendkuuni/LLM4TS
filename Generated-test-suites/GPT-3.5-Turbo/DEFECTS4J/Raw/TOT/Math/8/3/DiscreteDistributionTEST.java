import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {
    
    private DiscreteDistribution<Integer> distribution1;  // for testing with integer type
    private DiscreteDistribution<String> distribution2; // for testing with string type
    private DiscreteDistribution<Pair<Integer, Integer>> distribution3; // for testing with custom type
    
    @Before
    public void setup() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        // Setting up the samples for distribution1 (integer type)
        List<Pair<Integer, Double>> samples1 = new ArrayList<Pair<Integer, Double>>();
        samples1.add(new Pair<Integer, Double>(1, 0.3));
        samples1.add(new Pair<Integer, Double>(2, 0.4));
        samples1.add(new Pair<Integer, Double>(3, 0.2));
        samples1.add(new Pair<Integer, Double>(4, 0.1));
        distribution1 = new DiscreteDistribution<Integer>(samples1);
        
        // Setting up the samples for distribution2 (string type)
        List<Pair<String, Double>> samples2 = new ArrayList<Pair<String, Double>>();
        samples2.add(new Pair<String, Double>("A", 0.1));
        samples2.add(new Pair<String, Double>("B", 0.2));
        samples2.add(new Pair<String, Double>("C", 0.3));
        samples2.add(new Pair<String, Double>("D", 0.4));
        distribution2 = new DiscreteDistribution<String>(samples2);
        
        // Setting up the samples for distribution3 (custom type)
        List<Pair<Pair<Integer, Integer>, Double>> samples3 = new ArrayList<Pair<Pair<Integer, Integer>, Double>>();
        samples3.add(new Pair<Pair<Integer, Integer>, Double>(new Pair<Integer, Integer>(1, 1), 0.2));
        samples3.add(new Pair<Pair<Integer, Integer>, Double>(new Pair<Integer, Integer>(1, 2), 0.3));
        samples3.add(new Pair<Pair<Integer, Integer>, Double>(new Pair<Integer, Integer>(2, 1), 0.4));
        samples3.add(new Pair<Pair<Integer, Integer>, Double>(new Pair<Integer, Integer>(2, 2), 0.1));
        distribution3 = new DiscreteDistribution<Pair<Integer, Integer>>(samples3);
    }
    
    @Test
    public void testProbability_IntegerType() {
        // Test typical use case
        assertEquals(0.3, distribution1.probability(1), 0.0001);
        
        // Test edge case
        assertEquals(0, distribution1.probability(5), 0.0001);
        
        // Test error scenario
        try {
            distribution1.probability(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception should be thrown
        }
    }
    
    @Test
    public void testProbability_StringType() {
        // Test typical use case
        assertEquals(0.1, distribution2.probability("A"), 0.0001);
        
        // Test edge case
        assertEquals(0, distribution2.probability("E"), 0.0001);
        
        // Test error scenario
        try {
            distribution2.probability(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception should be thrown
        }
    }
    
    @Test
    public void testProbability_CustomType() {
        // Test typical use case
        assertEquals(0.2, distribution3.probability(new Pair<Integer, Integer>(1, 1)), 0.0001);
        
        // Test edge case
        assertEquals(0, distribution3.probability(new Pair<Integer, Integer>(3, 2)), 0.0001);
        
        // Test error scenario
        try {
            distribution3.probability(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception should be thrown
        }
    }
    
    @Test
    public void testGetSamples_IntegerType() {
        // Test typical use case
        List<Pair<Integer, Double>> expected1 = new ArrayList<Pair<Integer, Double>>();
        expected1.add(new Pair<Integer, Double>(1, 0.3));
        expected1.add(new Pair<Integer, Double>(2, 0.4));
        expected1.add(new Pair<Integer, Double>(3, 0.2));
        expected1.add(new Pair<Integer, Double>(4, 0.1));
        assertEquals(expected1, distribution1.getSamples());
        
        // Test edge case
        List<Pair<Integer, Double>> expected2 = new ArrayList<Pair<Integer, Double>>();
        assertEquals(expected2, distribution1.getSamples());
    }
    
    @Test
    public void testGetSamples_StringType() {
        // Test typical use case
        List<Pair<String, Double>> expected1 = new ArrayList<Pair<String, Double>>();
        expected1.add(new Pair<String, Double>("A", 0.1));
        expected1.add(new Pair<String, Double>("B", 0.2));
        expected1.add(new Pair<String, Double>("C", 0.3));
        expected1.add(new Pair<String, Double>("D", 0.4));
        assertEquals(expected1, distribution2.getSamples());
        
        // Test edge case
        List<Pair<String, Double>> expected2 = new ArrayList<Pair<String, Double>>();
        assertEquals(expected2, distribution2.getSamples());
    }
    
    @Test
    public void testGetSamples_CustomType() {
        // Test typical use case
        List<Pair<Pair<Integer, Integer>, Double>> expected1 = new ArrayList<Pair<Pair<Integer, Integer>, Double>>();
        expected1.add(new Pair<Pair<Integer, Integer>, Double>(new Pair<Integer, Integer>(1, 1), 0.2));
        expected1.add(new Pair<Pair<Integer, Integer>, Double>(new Pair<Integer, Integer>(1, 2), 0.3));
        expected1.add(new Pair<Pair<Integer, Integer>, Double>(new Pair<Integer, Integer>(2, 1), 0.4));
        expected1.add(new Pair<Pair<Integer, Integer>, Double>(new Pair<Integer, Integer>(2, 2), 0.1));
        assertEquals(expected1, distribution3.getSamples());
        
        // Test edge case
        List<Pair<Pair<Integer, Integer>, Double>> expected2 = new ArrayList<Pair<Pair<Integer, Integer>, Double>>();
        assertEquals(expected2, distribution3.getSamples());
    }
    
    @Test
    public void testSample_IntegerType() throws NotStrictlyPositiveException {
        // Test typical use case
        RandomDataGenerator randomData = new RandomDataGenerator();
        randomData.reSeed(12345);
        Integer[] output1 = distribution1.sample(1000);
        assertTrue(output1.length == 1000);
        
        // Random samples should be within the range of the distribution
        for (Integer value : output1) {
            assertTrue(value >= 1 && value <= 4);
        }
        
        // Test edge case
        try {
            distribution1.sample(0);
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            // Exception should be thrown
        }
    }
    
    @Test
    public void testSample_StringType() throws NotStrictlyPositiveException {
        // Test typical use case
        RandomDataGenerator randomData = new RandomDataGenerator();
        randomData.reSeed(12345);
        String[] output1 = distribution2.sample(1000);
        assertTrue(output1.length == 1000);
        
        // Random samples should be within the range of the distribution
        for (String value : output1) {
            assertTrue(value.equals("A") || value.equals("B") || value.equals("C") || value.equals("D"));
        }
        
        // Test edge case
        try {
            distribution2.sample(0);
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            // Exception should be thrown
        }
    }
    
    @Test
    public void testSample_CustomType() throws NotStrictlyPositiveException {
        // Test typical use case
        RandomDataGenerator randomData = new RandomDataGenerator();
        randomData.reSeed(12345);
        Pair<Integer, Integer>[] output1 = distribution3.sample(1000);
        assertTrue(output1.length == 1000);
        
        // Random samples should be within the range of the distribution
        for (Pair<Integer, Integer> value : output1) {
            assertTrue(value.getFirst() == 1 || value.getFirst() == 2);
            assertTrue(value.getSecond() == 1 || value.getSecond() == 2);
        }
        
        // Test edge case
        try {
            distribution3.sample(0);
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            // Exception should be thrown
        }
    }
}