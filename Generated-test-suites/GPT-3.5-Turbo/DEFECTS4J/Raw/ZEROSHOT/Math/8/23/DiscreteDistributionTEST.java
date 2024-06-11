import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {
    
    @Test
    public void testProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        assert Math.abs(distribution.probability("A") - 0.2) < 0.0001;
        assert Math.abs(distribution.probability("B") - 0.3) < 0.0001;
        assert Math.abs(distribution.probability("C") - 0.5) < 0.0001;
    }
    
    @Test
    public void testSample() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        String sample = distribution.sample();
        assert sample.equals("A") || sample.equals("B") || sample.equals("C");
    }
    
    @Test
    public void testSampleSize() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException, NotStrictlyPositiveException {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        String[] sample = distribution.sample(5);
        assert sample.length == 5;
        for (int i = 0; i < sample.length; i++) {
            assert sample[i].equals("A") || sample[i].equals("B") || sample[i].equals("C");
        }
    }
}