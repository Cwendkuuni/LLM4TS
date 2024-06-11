import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private double[][] data;
    private MultivariateNormalMixtureExpectationMaximization mnmEm;

    @Before
    public void setUp() {
        data = new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } };
        mnmEm = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testConstructorValidData() {
        assertEquals(data, mnmEm.data);
        assertEquals(0.0, mnmEm.logLikelihood, 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroLengthData() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] zeroLengthData = new double[0][0];
        mnmEm = new MultivariateNormalMixtureExpectationMaximization(zeroLengthData);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorMismatchedDataDimensions() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] mismatchedData = new double[][] { { 1.0, 2.0 }, { 3.0, 4.0, 5.0 }, { 6.0, 7.0, 8.0 } };
        mnmEm = new MultivariateNormalMixtureExpectationMaximization(mismatchedData);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorTooSmallDataDimension() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] smallDataDimension = new double[][] { { 1.0 }, { 2.0 } };
        mnmEm = new MultivariateNormalMixtureExpectationMaximization(smallDataDimension);
    }

    @Test
    public void testFitValidParameters() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        int maxIterations = 1000;
        double threshold = 1.0E-5;
        mnmEm.fit(initialMixture, maxIterations, threshold);
        
        assertEquals(maxIterations, mnmEm.fittedModel.getComponents().size());
        assertTrue(mnmEm.logLikelihood > 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitInvalidMaxIterations() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        int maxIterations = 0;
        double threshold = 1.0E-5;
        mnmEm.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitInvalidThreshold() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        int maxIterations = 1000;
        double threshold = -1.0E-5;
        mnmEm.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitMismatchedDataDimensions() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] mismatchedData = new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0, 7.0 } };
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        int maxIterations = 1000;
        double threshold = 1.0E-5;
        mnmEm.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = ConvergenceException.class)
    public void testFitNoConvergence() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        int maxIterations = 100;
        double threshold = 1.0E-10;
        mnmEm.fit(initialMixture, maxIterations, threshold);
    }

    @Test
    public void testGetLogLikelihood() {
        assertEquals(0.0, mnmEm.getLogLikelihood(), 0.0001);
    }

    @Test
    public void testGetFittedModel() {
        MixtureMultivariateNormalDistribution fittedModel = mnmEm.getFittedModel();
        assertNotNull(fittedModel);
        assertEquals(mnmEm.fittedModel.getComponents().size(), fittedModel.getComponents().size());
    }
}