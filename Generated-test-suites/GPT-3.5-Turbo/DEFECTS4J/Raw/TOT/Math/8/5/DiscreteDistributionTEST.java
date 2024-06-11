import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    // test probability method

    @Test
    public void testProbability_PositiveCase() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.4));
        samples.add(new Pair<String, Double>("B", 0.6));
        samples.add(new Pair<String, Double>("C", 0.0));

        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<String>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals(0.4, distribution.probability("A"), 0);
    }

    @Test
    public void testProbability_NullX() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>(null, 0.4));

        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<String>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals(0.4, distribution.probability(null), 0);
    }

    // test getSamples method

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.4));
        samples.add(new Pair<String, Double>("B", 0.6));
        samples.add(new Pair<String, Double>("C", 0.0));

        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<String>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        List<Pair<String, Double>> result = distribution.getSamples();

        assertEquals(samples, result);
    }

    // test sample method

    @Test
    public void testSample() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.4));
        samples.add(new Pair<String, Double>("B", 0.6));
        samples.add(new Pair<String, Double>("C", 0.0));

        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<String>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        String result = distribution.sample();

        assertTrue(result.equals("A") || result.equals("B") || result.equals("C"));
    }

    // test sample method for NotStrictlyPositiveException

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_NegativeSampleSize() throws NotStrictlyPositiveException {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.4));
        samples.add(new Pair<String, Double>("B", 0.6));
        samples.add(new Pair<String, Double>("C", 0.0));

        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<String>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        distribution.sample(-1);
    }

}