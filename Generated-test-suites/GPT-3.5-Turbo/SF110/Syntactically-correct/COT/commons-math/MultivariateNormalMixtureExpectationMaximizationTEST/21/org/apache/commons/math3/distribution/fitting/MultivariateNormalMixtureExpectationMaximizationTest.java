import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            assertEquals(data, em.data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFit() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(initialMixture, 1000, 1.0E-5);
            assertNotNull(em.fittedModel);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException | ConvergenceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFitDefault() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(initialMixture);
            assertNotNull(em.fittedModel);
        } catch (SingularMatrixException | NotStrictlyPositiveException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        int numComponents = 2;
        try {
            MixtureMultivariateNormalDistribution mixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
            assertNotNull(mixture);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException | NumberIsTooLargeException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(new MixtureMultivariateNormalDistribution());
            double logLikelihood = em.getLogLikelihood();
            assertEquals(logLikelihood, em.logLikelihood, 0.0);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException | ConvergenceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(new MixtureMultivariateNormalDistribution());
            MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
            assertEquals(fittedModel, em.fittedModel);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException | ConvergenceException e) {
            fail(e.getMessage());
        }
    }

}