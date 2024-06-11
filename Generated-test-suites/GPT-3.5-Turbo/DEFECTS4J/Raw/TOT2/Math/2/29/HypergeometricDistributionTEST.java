import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructorWithValidParameters() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
            assertEquals(10, distribution.getPopulationSize());
            assertEquals(5, distribution.getNumberOfSuccesses());
            assertEquals(3, distribution.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testConstructorWithInvalidParameters() {
        try {
            new HypergeometricDistribution(10, -5, 3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            assertTrue(true);
        } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.35714285714, distribution.cumulativeProbability(2), 0.0001);
    }

    // Additional test cases can be added for other methods and edge cases

}