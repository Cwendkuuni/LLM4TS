import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructorValidData() {
        double[][] validData = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization mixture = new MultivariateNormalMixtureExpectationMaximization(validData);
        assertNotNull(mixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidData() {
        double[][] invalidData = {};
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test
    public void testFitValidInput() {
        double[][] validData = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(someComponents);
        MultivariateNormalMixtureExpectationMaximization mixture = new MultivariateNormalMixtureExpectationMaximization(validData);
        mixture.fit(initialMixture, 1000, 1.0E-5);
        assertNotNull(mixture.getFittedModel());
    }

    @Test(expected = ConvergenceException.class)
    public void testFitInvalidInput() {
        double[][] invalidData = {{1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(someComponents);
        MultivariateNormalMixtureExpectationMaximization mixture = new MultivariateNormalMixtureExpectationMaximization(invalidData);
        mixture.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test
    public void testEstimateValidInput() {
        double[][] validData = {{1.0, 2.0}, {3.0, 4.0}};
        int numComponents = 2;
        MixtureMultivariateNormalDistribution mixture = MultivariateNormalMixtureExpectationMaximization.estimate(validData, numComponents);
        assertNotNull(mixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateInvalidInput() {
        double[][] invalidData = {{1.0}};
        int numComponents = 1;
        MultivariateNormalMixtureExpectationMaximization.estimate(invalidData, numComponents);
    }
}