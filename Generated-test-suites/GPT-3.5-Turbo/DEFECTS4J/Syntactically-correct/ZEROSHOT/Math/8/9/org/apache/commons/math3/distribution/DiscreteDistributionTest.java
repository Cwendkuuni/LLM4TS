import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiscreteDistributionTest {

    public void testProbability() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.4));
            samples.add(new Pair<>(3, 0.4));
            
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            
            double probability1 = distribution.probability(1);
            double probability2 = distribution.probability(2);
            double probability3 = distribution.probability(3);
            
            assert probability1 == 0.2 : "Probability of 1 should be 0.2";
            assert probability2 == 0.4 : "Probability of 2 should be 0.4";
            assert probability3 == 0.4 : "Probability of 3 should be 0.4";
        } catch (Exception e) {
            assert false : "An exception occurred: " + e.getMessage();
        }
    }

    public void testGetSamples() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.4));
            samples.add(new Pair<>(3, 0.4));
            
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            
            List<Pair<Integer, Double>> retrievedSamples = distribution.getSamples();
            
            assert samples.equals(retrievedSamples) : "Samples should be equal";
        } catch (Exception e) {
            assert false : "An exception occurred: " + e.getMessage();
        }
    }

    public void testSample() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.4));
            samples.add(new Pair<>(3, 0.4));
            
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            
            Integer sample1 = distribution.sample();
            Integer sample2 = distribution.sample();
            Integer sample3 = distribution.sample();
            
            assert sample1 != null : "Sample 1 should not be null";
            assert sample2 != null : "Sample 2 should not be null";
            assert sample3 != null : "Sample 3 should not be null";
        } catch (Exception e) {
            assert false : "An exception occurred: " + e.getMessage();
        }
    }

    public void testSampleWithSize() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.4));
            samples.add(new Pair<>(3, 0.4));
            
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            
            Integer[] sample = distribution.sample(3);
            
            assert sample.length == 3 : "Sample size should be 3";
        } catch (Exception e) {
            assert false : "An exception occurred: " + e.getMessage();
        }
    }

    public void testReseedRandomGenerator() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.4));
            samples.add(new Pair<>(3, 0.4));
            
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            
            distribution.reseedRandomGenerator(12345);
            Integer sample1 = distribution.sample();
            distribution.reseedRandomGenerator(54321);
            Integer sample2 = distribution.sample();
            
            assert sample1 != sample2 : "Samples should be different after reseeding";
        } catch (Exception e) {
            assert false : "An exception occurred: " + e.getMessage();
        }
    }

    public void testConstructorWithRandomGenerator() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.4));
            samples.add(new Pair<>(3, 0.4));
            
            Well19937c rng = new Well19937c(12345);
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(rng, samples);
            
            Integer sample1 = distribution.sample();
            Integer sample2 = distribution.sample();
            
            assert sample1 != null : "Sample 1 should not be null";
            assert sample2 != null : "Sample 2 should not be null";
        } catch (Exception e) {
            assert false : "An exception occurred: " + e.getMessage();
        }
    }

    public void testConstructorWithNegativeProbability() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, -0.4));
            samples.add(new Pair<>(3, 0.4));
            
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            
            assert false : "An exception should have been thrown";
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            assert false : "Unexpected exception: " + e.getMessage();
        }
    }

    public void testConstructorWithInfiniteProbability() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, Double.POSITIVE_INFINITY));
            samples.add(new Pair<>(3, 0.4));
            
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            
            assert false : "An exception should have been thrown";
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            assert false : "Unexpected exception: " + e.getMessage();
        }
    }

    public void testConstructorWithZeroProbabilitySum() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.3));
            samples.add(new Pair<>(3, 0.5));
            
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            
            assert false : "An exception should have been thrown";
        } catch (MathArithmeticException e) {
            // Expected exception
        } catch (Exception e) {
            assert false : "Unexpected exception: " + e.getMessage();
        }
    }

}