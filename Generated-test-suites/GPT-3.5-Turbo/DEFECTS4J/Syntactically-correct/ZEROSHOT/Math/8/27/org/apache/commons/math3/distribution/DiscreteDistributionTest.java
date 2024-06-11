import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {
    
    private DiscreteDistribution<Integer> dist;
    
    @Before
    public void setUp() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.4));
        samples.add(new Pair<Integer, Double>(4, 0.2));
        dist = new DiscreteDistribution<Integer>(samples);
    }

    @After
    public void tearDown() throws Exception {
        dist = null;
    }

    @Test
    public void testProbability() {
        assertEquals(0.1, dist.probability(1), 0.000001);
        assertEquals(0.3, dist.probability(2), 0.000001);
        assertEquals(0.4, dist.probability(3), 0.000001);
        assertEquals(0.2, dist.probability(4), 0.000001);
        assertEquals(0, dist.probability(0), 0.000001);
        assertEquals(0, dist.probability(5), 0.000001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expected = new ArrayList<Pair<Integer, Double>>();
        expected.add(new Pair<Integer, Double>(1, 0.1));
        expected.add(new Pair<Integer, Double>(2, 0.3));
        expected.add(new Pair<Integer, Double>(3, 0.4));
        expected.add(new Pair<Integer, Double>(4, 0.2));
        assertEquals(expected, dist.getSamples());
    }

    @Test
    public void testSample() {
        Set<Integer> sampleSet = new HashSet<Integer>(Arrays.asList(1,2,3,4));
        for(int i=0; i<100; i++) {
            Integer sample = dist.sample();
            assertTrue(sampleSet.contains(sample));
        }
    }

    @Test
    public void testSampleWithSampleSize() {
        int sampleSize = 100;
        Integer[] samples = dist.sample(sampleSize);
        assertEquals(sampleSize, samples.length);
        Set<Integer> sampleSet = new HashSet<Integer>(Arrays.asList(1,2,3,4));
        for (Integer sample : samples) {
            assertTrue(sampleSet.contains(sample));
        }
    }
}