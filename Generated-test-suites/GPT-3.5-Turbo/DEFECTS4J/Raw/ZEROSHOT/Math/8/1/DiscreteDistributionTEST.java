import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {


    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.25));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
        double result = distribution.probability(1);
        Assert.assertEquals(0.25, result, 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.25));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
        List<Pair<Integer, Double>> result = distribution.getSamples();
        Assert.assertEquals(samples, result);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.25));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
        Integer result = distribution.sample();
        Assert.assertNotNull(result);
    }

    @Test
    public void testSampleWithSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.25));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
        Integer[] result = distribution.sample(5);
        Assert.assertEquals(5, result.length);
    }

}