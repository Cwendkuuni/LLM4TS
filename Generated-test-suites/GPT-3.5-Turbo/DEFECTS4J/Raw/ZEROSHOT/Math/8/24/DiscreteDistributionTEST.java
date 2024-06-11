import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
        
        Assert.assertEquals(0.2, dist.probability(1), 0.0001);
        Assert.assertEquals(0.3, dist.probability(2), 0.0001);
        Assert.assertEquals(0.5, dist.probability(3), 0.0001);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeValueInSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, -1.0));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilitySum() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.4));
        samples.add(new Pair<Integer, Double>(2, 0.6));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.5));
        samples.add(new Pair<Integer, Double>(2, Double.POSITIVE_INFINITY));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
        
        List<Pair<Integer, Double>> result = dist.getSamples();
        Assert.assertEquals(samples, result);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
        
        for (int i = 0; i < 10; i++) {
            Integer sample = dist.sample();
            Assert.assertTrue(sample >= 1 && sample <= 3);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleSizeNotPositive() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
        
        int sampleSize = 0;
        dist.sample(sampleSize);
    }

    @Test
    public void testSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
        
        int sampleSize = 5;
        Integer[] result = dist.sample(sampleSize);
        Assert.assertEquals(sampleSize, result.length);
        
        for (int i = 0; i < result.length; i++) {
            Assert.assertTrue(result[i] >= 1 && result[i] <= 3);
        }
    }

}