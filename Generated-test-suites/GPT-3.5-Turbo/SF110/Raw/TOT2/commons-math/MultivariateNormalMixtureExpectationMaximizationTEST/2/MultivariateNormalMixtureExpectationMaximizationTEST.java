import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(em);
    }

    @Test
    public void testFit() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            em.fit(initialMixture, 1000, 1.0E-5);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            assertNotNull(result);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        assertEquals(0.0, em.getLogLikelihood(), 0.0);
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(em.getFittedModel());
    }

    @Test
    public void testEdgeCases() {
        // Add edge cases test scenarios here
    }

    @Test
    public void testExceptionHandling() {
        // Add exception handling test scenarios here
    }
}