
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setup() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.3));
        
        try {
            distribution = new DiscreteDistribution<Integer>(samples);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProbability() {
        assertNotNull(distribution);
        double prob = distribution.probability(2);
        assertEquals(0.5, prob, 0.001);
    }

    @Test
    public void testSample() {
        assertNotNull(distribution);
        Integer sample = distribution.sample();
        assertNotNull(sample);
    }

    // Add more test methods for other public methods in DiscreteDistribution class

}
