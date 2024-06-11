import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    @Test
    public void testConstructorPositiveCase() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(em);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeCase() {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFitMethod() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(data);
        try {
            em.fit(initialMixture, 1000, 0.00001);
        } catch (Exception e) {
            fail("Exception thrown when fit method should not throw any exceptions");
        }
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitMethodInvalidCovarianceMatrix() {
        double[][] data = {{1.0, 2.0}, {1.0, 2.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(data);
        em.fit(initialMixture, 1000, 0.00001);
    }
    
    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(data);
        try {
            em.fit(initialMixture, 1000, 0.00001);
            assertTrue(em.getLogLikelihood() >= 0.0);
        } catch (Exception e) {
            fail("Exception thrown when getLogLikelihood method should not throw any exceptions");
        }
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(data);
        try {
            em.fit(initialMixture, 1000, 0.00001);
            assertNotNull(em.getFittedModel());
        } catch (Exception e) {
            fail("Exception thrown when getFittedModel method should not throw any exceptions");
        }
    }

}