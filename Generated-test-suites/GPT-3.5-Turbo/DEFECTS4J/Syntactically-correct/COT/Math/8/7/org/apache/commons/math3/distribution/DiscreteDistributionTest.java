
import java.util.List;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    @Test
    public void testConstructor() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.25));
        samples.add(new Pair<>("B", 0.75));
        try {
            DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
            // Assert statements
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testProbability() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.25));
        samples.add(new Pair<>("B", 0.75));
        try {
            DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
            double probabilityA = distribution.probability("A");
            double probabilityB = distribution.probability("B");
            // Assert statements
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.25));
        samples.add(new Pair<>("B", 0.75));
        try {
            DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
            List<Pair<String, Double>> result = distribution.getSamples();
            // Assert statements
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSample() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.25));
        samples.add(new Pair<>("B", 0.75));
        try {
            DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
            String sample = distribution.sample();
            // Assert statements
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSample() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.25));
        samples.add(new Pair<>("B", 0.75));
        try {
            DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
            String[] sample = distribution.sample(5);
            // Assert statements
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
