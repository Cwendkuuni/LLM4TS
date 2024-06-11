import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.distribution.fitting.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.apache.commons.math3.distribution.*;
import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(m);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ThrowsNotStrictlyPositiveException() throws Exception {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_ThrowsDimensionMismatchException() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0}};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_ThrowsNumberIsTooSmallException() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = null;
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFit_ThrowsSingularMatrixException() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = null;
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_ThrowsNotStrictlyPositiveException() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = null;
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 0, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_ThrowsNotStrictlyPositiveException2() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = null;
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 1000, -1.0E-5);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFit_ThrowsDimensionMismatchException() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = null;
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test
    public void testGetLogLikelihood() throws Exception {
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(null);
        double logLikelihood = m.getLogLikelihood();
        assertEquals(0.0, logLikelihood, 0.0);
    }

    @Test
    public void testGetFittedModel() throws Exception {
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(null);
        MixtureMultivariateNormalDistribution fittedModel = m.getFittedModel();
        assertNotNull(fittedModel);
    }
}