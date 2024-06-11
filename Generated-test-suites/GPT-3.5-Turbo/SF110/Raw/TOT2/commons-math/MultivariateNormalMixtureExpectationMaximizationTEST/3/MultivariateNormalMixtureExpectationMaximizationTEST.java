import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructorValidData() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            assertNotNull(em);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidDataLength() throws NotStrictlyPositiveException {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFitValidData() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(initialMixture);
            assertNotNull(em.getFittedModel());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitInvalidMaxIterations() throws SingularMatrixException {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(initialMixture, 0, 1.0E-5);
    }

    // Add more test cases for other methods as needed

}