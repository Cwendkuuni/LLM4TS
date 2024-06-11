import org.junit.Test;
import org.junit.Before;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import java.util.*;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private MultivariateNormalMixtureExpectationMaximization mixtureEM;
    private double[][] data;
    private MixtureMultivariateNormalDistribution initialMixture;

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NotStrictlyPositiveException() {
        data = new double[0][0];
        mixtureEM = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_DimensionMismatchException() {
        data = new double[2][2];
        mixtureEM = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_NumberIsTooSmallException() {
        data = new double[2][1];
        mixtureEM = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Before
    public void setUp() {
        data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {1.0, 4.0}, {4.0, 2.0}, {3.0, 3.0}};
        mixtureEM = new MultivariateNormalMixtureExpectationMaximization(data);
        initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_NotStrictlyPositiveException() {
        mixtureEM.fit(initialMixture, 0, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_NotStrictlyPositiveThresholdException() {
        mixtureEM.fit(initialMixture, 1000, 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFit_DimensionMismatchException() {
        mixtureEM.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test
    public void testFit() {
        try {
            mixtureEM.fit(initialMixture, 1000, 1.0E-5);
            MixtureMultivariateNormalDistribution fittedModel = mixtureEM.getFittedModel();
            assertNotNull(fittedModel);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException | ConvergenceException e) {
            fail();
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimate_NotStrictlyPositiveException() {
        MultivariateNormalMixtureExpectationMaximization.estimate(new double[0][0], 2);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimate_NumberIsTooSmallException() {
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimate_NumberIsTooLargeException() {
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 10);
    }

    @Test
    public void testEstimate() {
        try {
            MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            assertNotNull(estimatedModel);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            fail();
        }
    }

    @Test
    public void testGetLogLikelihood() {
        assertEquals(0.0, mixtureEM.getLogLikelihood(), 0.0);
    }
}