import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.25));
        samples.add(new Pair<>(3, 0.25));
        
        DiscreteDistribution<Integer> distribution = null; // Initialize with the samples
        
        assertEquals(0.5, distribution.probability(1), 0.0001);
        assertEquals(0.25, distribution.probability(2), 0.0001);
        assertEquals(0.25, distribution.probability(3), 0.0001);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.25));
        samples.add(new Pair<>(3, 0.25));
        
        DiscreteDistribution<Integer> distribution = null; // Initialize with the samples
        
        Integer sample = distribution.sample();
        assertTrue(sample == 1 || sample == 2 || sample == 3);
    }

    // Additional test cases for edge cases and exceptions go here

}