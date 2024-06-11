
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {

    private List<Pair<String, Double>> samples;
    
    @Before
    public void setup() {
        samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.3));
        samples.add(new Pair<String, Double>("C", 0.5));
    }
    
    @Test
    public void testConstructor() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
        assertNotNull(distribution);
    }
    
    @Test(expected=NotPositiveException.class)
    public void testConstructor_InvalidProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        samples.add(new Pair<String, Double>("D", -0.1));
        new DiscreteDistribution<String>(samples);
    }
    
    @Test(expected=MathArithmeticException.class)
    public void testConstructor_ZeroProbabilitySum() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        samples.add(new Pair<String, Double>("D", 0.1));
        new DiscreteDistribution<String>(samples);
    }
    
    @Test(expected=MathIllegalArgumentException.class)
    public void testConstructor_InvalidProbabilityInfinite() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        samples.add(new Pair<String, Double>("D", Double.POSITIVE_INFINITY));
        new DiscreteDistribution<String>(samples);
    }
    
    @Test
    public void testGetSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
        List<Pair<String, Double>> actualSamples = distribution.getSamples();
        assertEquals(samples, actualSamples);
    }
    
    @Test
    public void testProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
        
        assertEquals(0.2, distribution.probability("A"), 0.0);
        assertEquals(0.3, distribution.probability("B"), 0.0);
        assertEquals(0.5, distribution.probability("C"), 0.0);
    }
    
    @Test
    public void testSample() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        int sampleSize = 1000;
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
        String[] randomSamples = distribution.sample(sampleSize);
        
        int countA = 0;
        int countB = 0;
        int countC = 0;
        for (String sample : randomSamples) {
            if (sample.equals("A")) {
                countA++;
            } else if (sample.equals("B")) {
                countB++;
            } else if (sample.equals("C")) {
                countC++;
            }
        }
        
        double proportionA = (double) countA / sampleSize;
        double proportionB = (double) countB / sampleSize;
        double proportionC = (double) countC / sampleSize;
        
        assertEquals(0.2, proportionA, 0.05);
        assertEquals(0.3, proportionB, 0.05);
        assertEquals(0.5, proportionC, 0.05);
    }
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testSample_InvalidSampleSize() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
        distribution.sample(0);
    }
    
    @Test
    public void testReseedRandomGenerator() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
        distribution.reseedRandomGenerator(1234);
    }
}
