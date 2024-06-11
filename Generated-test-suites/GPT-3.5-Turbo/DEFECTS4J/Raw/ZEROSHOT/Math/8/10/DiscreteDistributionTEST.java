import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.5));
            samples.add(new Pair<>(2, 0.3));
            samples.add(new Pair<>(3, 0.2));
            
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            
            Assert.assertEquals(0.5, distribution.probability(1), 0.0001);
            Assert.assertEquals(0.3, distribution.probability(2), 0.0001);
            Assert.assertEquals(0.2, distribution.probability(3), 0.0001);
            Assert.assertEquals(0.0, distribution.probability(0), 0.0001);
            
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSample() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.5));
            samples.add(new Pair<>(2, 0.3));
            samples.add(new Pair<>(3, 0.2));
            
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            distribution.reseedRandomGenerator(1234567890L);
            
            Integer[] samples = distribution.sample(100);
            
            Assert.assertEquals(100, samples.length);
            
            int count1 = 0;
            int count2 = 0;
            int count3 = 0;
            
            for (int i = 0; i < samples.length; i++) {
                if (samples[i] == 1) {
                    count1++;
                } else if (samples[i] == 2) {
                    count2++;
                } else if (samples[i] == 3) {
                    count3++;
                }
            }
            
            Assert.assertEquals(50, count1, 5);
            Assert.assertEquals(30, count2, 5);
            Assert.assertEquals(20, count3, 5);
            
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException | NotStrictlyPositiveException e) {
            Assert.fail(e.getMessage());
        }
    }

}