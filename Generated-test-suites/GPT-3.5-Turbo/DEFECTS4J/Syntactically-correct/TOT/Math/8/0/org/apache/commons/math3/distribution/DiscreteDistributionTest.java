import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> dist = null;
        try {
            dist = new DiscreteDistribution<Integer>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception should not have been thrown");
        }
        
        assertEquals(0.2, dist.probability(1), 0.0);
        assertEquals(0.3, dist.probability(2), 0.0);
        assertEquals(0.5, dist.probability(3), 0.0);
        assertEquals(0, dist.probability(0), 0.0);
        assertEquals(0, dist.probability(4), 0.0);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> dist = null;
        try {
            dist = new DiscreteDistribution<Integer>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception should not have been thrown");
        }
        
        List<Pair<Integer, Double>> result = dist.getSamples();
        assertEquals(samples, result);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> dist = null;
        try {
            dist = new DiscreteDistribution<Integer>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception should not have been thrown");
        }
        
        Integer result = dist.sample();
        assertTrue(result >= 1 && result <= 3);
    }

    @Test
    public void testSampleWithSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> dist = null;
        try {
            dist = new DiscreteDistribution<Integer>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception should not have been thrown");
        }
        
        Integer[] result = dist.sample(5);
        assertEquals(5, result.length);
        for (Integer num : result) {
            assertTrue(num >= 1 && num <= 3);
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(-2, -0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroSumProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        samples.add(new Pair<>(4, -0.2));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInfiniteProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, Double.POSITIVE_INFINITY));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> dist = null;
        try {
            dist = new DiscreteDistribution<Integer>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception should not have been thrown");
        }
        
        RandomGenerator rng = new Well19937c();
        dist.reseedRandomGenerator(rng.nextLong());
        
        assertEquals(rng.nextLong(), dist.random.getSeed());
    }
}