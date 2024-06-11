import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testValidConstruction() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(rng, samples);
        } catch (MathIllegalArgumentException | NotPositiveException | MathArithmeticException e) {
            fail("construction should not throw an exception");
        }

        assertEquals(3, distribution.singletons.size());
        assertEquals(1.0, distribution.probability(1.0), 0.001);
        assertEquals(2.0, distribution.probability(2.0), 0.001);
        assertEquals(3.0, distribution.probability(3.0), 0.001);
        assertEquals(0.1, distribution.probability(1.0), 0.001);
        assertEquals(0.3, distribution.probability(2.0), 0.001);
        assertEquals(0.6, distribution.probability(3.0), 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbabilityThrowsException() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(rng, samples);
        } catch (NotStrictlyPositiveException | NotPositiveException | MathArithmeticException e) {
            // expected exception
        }
        fail("construction should have thrown an exception");
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilitySumThrowsException() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.1));
        samples.add(new Pair<>(3, -0.1));

        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(rng, samples);
        } catch (NotStrictlyPositiveException | NotPositiveException e) {
            // not expected exceptions
        }
        fail("construction should have thrown an exception");
    }

    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(rng, samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("construction should not throw an exception");
        }

        int sample = distribution.sample().intValue();

        int expectedSample = -1;
        double cumulativeProbability = 0;
        outer:
        for (int i = 0; i < distribution.singletons.size(); i++) {
            if (sample == distribution.singletons.get(i).intValue()) {
                expectedSample = i;
                break outer;
            }
            cumulativeProbability += distribution.probabilities[i];
        }

        assertEquals(expectedSample, sample);
        assertTrue(cumulativeProbability > 0.9999);
    }
}