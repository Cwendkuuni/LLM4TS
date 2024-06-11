import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbabilityMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            Assert.assertEquals(0.3, distribution.probability(1), 0.0001);
            Assert.assertEquals(0.4, distribution.probability(2), 0.0001);
            Assert.assertEquals(0.3, distribution.probability(3), 0.0001);
            Assert.assertEquals(0.0, distribution.probability(4), 0.0001);

        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

            for (int i = 0; i < 1000; i++) {
                int sample = distribution.sample().intValue();
                double probability = distribution.probability(sample);
                Assert.assertTrue(probability > 0);
            }

        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSampleArrayMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

            int sampleSize = 5;

            Integer[] sample = distribution.sample(sampleSize);

            for (int i = 0; i < sampleSize; i++) {
                int sampleInt = sample[i].intValue();
                double probability = distribution.probability(sampleInt);
                Assert.assertTrue(probability > 0);
            }

        } catch (NotStrictlyPositiveException | NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleArrayWithNegativeSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        distribution.sample(-1);
    }
}