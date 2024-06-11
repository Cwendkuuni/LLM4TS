import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.distribution.fitting.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor_ValidData() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            assertEquals(0.0, em.getLogLikelihood(), 0.001);
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor_InvalidData() {
        double[][] data = {};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFit_ValidParameters() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(initialMixture, 100, 1.0E-3);
            assertNotNull(em.getFittedModel());
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFit_InvalidParameters() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(initialMixture, 0, 1.0E-3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    // Add more test cases for other methods and edge cases

}