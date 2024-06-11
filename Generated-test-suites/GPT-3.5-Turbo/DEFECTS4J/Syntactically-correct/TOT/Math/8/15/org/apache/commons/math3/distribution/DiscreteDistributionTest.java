import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;


public class DiscreteDistributionTest {

    private DiscreteDistribution<String> distribution;

    @Before
    public void setUp() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.1));
        samples.add(new Pair<>("B", 0.5));
        samples.add(new Pair<>("C", 0.2));
        samples.add(new Pair<>("D", 0.2));

        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testExceptionForNegativeProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException{
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", -0.5));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testExceptionForSumOfProbabilitiesZero() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException{
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.0));
        samples.add(new Pair<>("B", 0.0));

        new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() {
        assertEquals(0.1, distribution.probability("A"), 0.0001);
        assertEquals(0.5, distribution.probability("B"), 0.0001);
        assertEquals(0.2, distribution.probability("C"), 0.0001);
        assertEquals(0.2, distribution.probability("D"), 0.0001);
        assertEquals(0.0, distribution.probability("E"), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> expected = new ArrayList<>();
        expected.add(new Pair<>("A", 0.1));
        expected.add(new Pair<>("B", 0.5));
        expected.add(new Pair<>("C", 0.2));
        expected.add(new Pair<>("D", 0.2));

        assertEquals(expected, distribution.getSamples());
    }

    @Test
    public void testSample() {
        String sample = distribution.sample();
        assertTrue(sample.equals("A") || sample.equals("B") || sample.equals("C") || sample.equals("D"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSampleWithZeroSampleSize() throws NotPositiveException {
        distribution.sample(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSampleWithNegativeSampleSize() throws NotPositiveException {
        distribution.sample(-5);
    }

    @Test
    public void testSampleWithSampleSize() throws NotPositiveException {
        String[] sample = distribution.sample(3);
        assertEquals(3, sample.length);
    }

}