import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() throws NotPositiveException, MathIllegalArgumentException, MathArithmeticException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.4));
        samples.add(new Pair<>("D", 0.1));

        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        assertEquals(0.2, dist.probability("A"), 0);
        assertEquals(0.3, dist.probability("B"), 0);
        assertEquals(0.4, dist.probability("C"), 0);
        assertEquals(0.1, dist.probability("D"), 0);
    }

    @Test
    public void testSample() throws NotPositiveException, MathIllegalArgumentException, MathArithmeticException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.4));
        samples.add(new Pair<>("D", 0.1));

        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        for (int i = 0; i < 10000; i++) {
            String sample = dist.sample();
            assertTrue("A".equals(sample) || "B".equals(sample) || "C".equals(sample) || "D".equals(sample));
        }
    }

    @Test
    public void testSampleSize() throws NotPositiveException, MathIllegalArgumentException, MathArithmeticException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.4));
        samples.add(new Pair<>("D", 0.1));

        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        for (int size = 1; size <= 100; size++) {
            String[] sample = dist.sample(size);

            assertEquals(size, sample.length);

            for (String s : sample) {
                assertTrue("A".equals(s) || "B".equals(s) || "C".equals(s) || "D".equals(s));
            }
        }
    }
}