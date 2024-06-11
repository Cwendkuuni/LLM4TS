import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor_withValidInputs() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Valid input parameters caused an exception");
        }
    }

    @Test
    public void testConstructor_withInvalidInputs() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(-5, 5, 3);
            fail("Negative population size did not throw NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, -2, 3);
            fail("Negative number of successes did not throw NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        }

        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, -1);
            fail("Negative sample size did not throw NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        }

        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(5, 10, 3);
            fail("Number of successes > population size did not throw NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }

        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 15);
            fail("Sample size > population size did not throw NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }

    // Add more test cases for other methods as needed

}