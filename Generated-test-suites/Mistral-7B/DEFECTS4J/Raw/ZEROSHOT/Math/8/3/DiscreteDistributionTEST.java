import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstruction() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            assertEquals(3, distribution.singletons.size());
            assertEquals(0.1, distribution.probability(1), 1e-6);
            assertEquals(0.3, distribution.probability(2), 1e-6);
            assertEquals(0.6, distribution.probability(3), 1e-6);
        } catch (MathIllegalArgumentException | MathArithmeticException | NotPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        List<Pair<Double, Double>> invalidSamples = new ArrayList<>();
        invalidSamples.add(new Pair<>(1.5, 0.1));
        try {
            DiscreteDistribution<Double> distribution = new DiscreteDistribution<>(invalidSamples);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        List<Pair<Integer, Double>> nullSamples = null;
        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(nullSamples);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            assertEquals(0.1, distribution.probability(1), 1e-6);
            assertEquals(0.3, distribution.probability(2), 1e-6);
            assertEquals(0.6, distribution.probability(3), 1e-6);

            assertEquals(0, distribution.probability(4), 1e-6);
            assertEquals(1, distribution.probability(1).compareTo(distribution.probability(1)));
        } catch (NotStrictlyPositiveException | MathIllegalArgumentException | MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            List<Pair<Integer, Double>> result = distribution.getSamples();
            assertEquals(samples.size(), result.size());
            for (int i = 0; i < samples.size(); i++) {
                assertEquals(samples.get(i).getKey(), result.get(i).getKey());
                assertEquals(samples.get(i).getValue(), result.get(i).getValue(), 1e-6);
            }
        } catch (NotStrictlyPositiveException | MathIllegalArgumentException | MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            for (int i = 0; i < 100; i++) {
                int result = distribution.sample().intValue();
                for (Pair<Integer, Double> pair : samples) {
                    if (pair.getKey().intValue() == result) {
                        assertEquals(pair.getValue(), distribution.probability(result), 1e-6);
                        break;
                    }
                }
            }
        } catch (NotStrictlyPositiveException | MathIllegalArgumentException | MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            int[] result = distribution.sample(5);
            for (int value : result) {
                for (Pair<Integer, Double> pair : samples) {
                    if (pair.getKey().intValue() == value) {
                        assertEquals(pair.getValue(), distribution.probability(value), 1e-6);
                        break;
                    }
                }
            }
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotStrictlyPositiveException | MathIllegalArgumentException | MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        Well19937c rng = new Well19937c();
        distribution.reseedRandomGenerator(123);
        rng.setSeed(123);

        for (int i = 0; i < 10; i++) {
            int result1 = distribution.sample().intValue();
            int result2 = rng.nextInt(4);
            assertEquals(result1, result2);
        }
    }
}