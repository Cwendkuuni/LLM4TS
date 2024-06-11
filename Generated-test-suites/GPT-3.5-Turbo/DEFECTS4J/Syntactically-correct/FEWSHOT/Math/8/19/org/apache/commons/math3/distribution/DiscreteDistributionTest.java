package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DiscreteDistributionTest {
    
    @Mock
    RandomGenerator rng;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testConstructor() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.1));
        samples.add(new Pair<>("B", 0.2));
        samples.add(new Pair<>("C", 0.3));
        
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        assertArrayEquals(samples.toArray(), distribution.getSamples().toArray());
    }
    
    @Test(expected=NotPositiveException.class)
    public void testConstructor_withNegativeProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", -0.1));
        samples.add(new Pair<>("B", 0.2));
        samples.add(new Pair<>("C", 0.3));
        
        new DiscreteDistribution<>(samples);
    }
    
    @Test
    public void testProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.1));
        samples.add(new Pair<>("B", 0.2));
        samples.add(new Pair<>("C", 0.3));
        
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        assertEquals(0.1, distribution.probability("A"), 0);
        assertEquals(0.2, distribution.probability("B"), 0);
        assertEquals(0.3, distribution.probability("C"), 0);
    }
    
    @Test
    public void testSample() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.1));
        samples.add(new Pair<>("B", 0.2));
        samples.add(new Pair<>("C", 0.3));
        
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        String[] expected = {"A", "B", "C"};
        String[] actual = distribution.sample(3);
        
        assertArrayEquals(expected, actual);
    }
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testSample_withNonPositiveSampleSize() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException, NotStrictlyPositiveException {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.1));
        samples.add(new Pair<>("B", 0.2));
        samples.add(new Pair<>("C", 0.3));
        
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        distribution.sample(0);
    }

}