import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        samples.add(new Pair<>(4, 0.1));

        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Failed to initialize DiscreteDistribution");
        }
    }

    @Test
    public void testProbability() {
        double prob1 = distribution.probability(1);
        double prob2 = distribution.probability(2);
        double prob3 = distribution.probability(3);
        double prob4 = distribution.probability(4);

        assertEquals(0.4, prob1, 0.0001);
        assertEquals(0.3, prob2, 0.0001);
        assertEquals(0.2, prob3, 0.0001);
        assertEquals(0.1, prob4, 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();

        assertEquals(4, samples.size());
        assertEquals(new Pair<>(1, 0.4), samples.get(0));
        assertEquals(new Pair<>(2, 0.3), samples.get(1));
        assertEquals(new Pair<>(3, 0.2), samples.get(2));
        assertEquals(new Pair<>(4, 0.1), samples.get(3));
    }

    @Test
    public void testSample() {
        Integer sample = distribution.sample();

        assertTrue(sample >= 1 && sample <= 4);
    }

    @Test
    public void testSampleWithSize() {
        try {
            Integer[] samples = distribution.sample(100);

            assertEquals(100, samples.length);
            for (Integer sample : samples) {
                assertTrue(sample >= 1 && sample <= 4);
            }
        } catch (NotStrictlyPositiveException e) {
            fail("Failed to generate random samples");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithInvalidSize() throws NotStrictlyPositiveException {
        distribution.sample(-1);
    }
}