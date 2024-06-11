import org.junit.Test;
import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.ConvergenceException;

import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor_ThrowNotStrictlyPositiveException() {
        double[][] data = {};
        try {
            MultivariateNormalMixtureExpectationMaximization mnmec = new MultivariateNormalMixtureExpectationMaximization(data);
            fail("NotStrictlyPositiveException should have been thrown.");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        } catch (Exception e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testConstructor_ThrowDimensionMismatchException() {
        double[][] data = {
                {1, 2, 3},
                {4, 5, 6, 7} // Invalid dimension
        };
        try {
            MultivariateNormalMixtureExpectationMaximization mnmec = new MultivariateNormalMixtureExpectationMaximization(data);
            fail("DimensionMismatchException should have been thrown.");
        } catch (DimensionMismatchException e) {
            // Pass
        } catch (Exception e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testConstructor_ThrowNumberIsTooSmallException() {
        double[][] data = {
                {1}, // Invalid dimension
                {4, 5}
        };
        try {
            MultivariateNormalMixtureExpectationMaximization mnmec = new MultivariateNormalMixtureExpectationMaximization(data);
            fail("NumberIsTooSmallException should have been thrown.");
        } catch (NumberIsTooSmallException e) {
            // Pass
        } catch (Exception e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testFit_ThrowNotStrictlyPositiveException() {
        double[][] data = {
                {1, 2},
                {3, 4}
        };
        try {
            MultivariateNormalMixtureExpectationMaximization mnmec = new MultivariateNormalMixtureExpectationMaximization(data);
            mnmec.fit(new MixtureMultivariateNormalDistribution(), 0, 0.001);
            fail("NotStrictlyPositiveException should have been thrown.");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        } catch (Exception e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testFit_ThrowSingularMatrixException() {
        double[][] data = {
                {1, 2},
                {3, 4}
        };
        try {
            MultivariateNormalMixtureExpectationMaximization mnmec = new MultivariateNormalMixtureExpectationMaximization(data);
            mnmec.fit(new MixtureMultivariateNormalDistribution(), 1000, 0.001);
            fail("SingularMatrixException should have been thrown.");
        } catch (SingularMatrixException e) {
            // Pass
        } catch (Exception e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testFit_ThrowConvergenceException() {
        double[][] data = {
                {1, 2},
                {3, 4}
        };
        try {
            MultivariateNormalMixtureExpectationMaximization mnmec = new MultivariateNormalMixtureExpectationMaximization(data);
            mnmec.fit(new MixtureMultivariateNormalDistribution(), 1000, 1.0);
            fail("ConvergenceException should have been thrown.");
        } catch (ConvergenceException e) {
            // Pass
        } catch (Exception e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testFit_DefaultValues() {
        double[][] data = {
                {1, 2},
                {3, 4}
        };
        try {
            MultivariateNormalMixtureExpectationMaximization mnmec = new MultivariateNormalMixtureExpectationMaximization(data);
            mnmec.fit(new MixtureMultivariateNormalDistribution());
        } catch (Exception e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testEstimate_ThrowNotStrictlyPositiveException() {
        double[][] data = {{1, 2}};
        try {
            MixtureMultivariateNormalDistribution mnmd = MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
            fail("NotStrictlyPositiveException should have been thrown.");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        } catch (Exception e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testEstimate_ThrowNumberIsTooSmallException() {
        double[][] data = {
                {1, 2},
                {3, 4}
        };
        try {
            MixtureMultivariateNormalDistribution mnmd = MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
            fail("NumberIsTooSmallException should have been thrown.");
        } catch (NumberIsTooSmallException e) {
            // Pass
        } catch (Exception e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testEstimate_ThrowNumberIsTooLargeException() {
        double[][] data = {
                {1, 2},
                {3, 4}
        };
        try {
            MixtureMultivariateNormalDistribution mnmd = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
            fail("NumberIsTooLargeException should have been thrown.");
        } catch (NumberIsTooLargeException e) {
            // Pass
        } catch (Exception e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = {
                {1, 2},
                {3, 4}
        };
        try {
            MultivariateNormalMixtureExpectationMaximization mnmec = new MultivariateNormalMixtureExpectationMaximization(data);
            double logLikelihood = mnmec.getLogLikelihood();
            assertEquals(0.0, logLikelihood, 0.000001);
        } catch (Exception e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {
                {1, 2},
                {3, 4}
        };
        try {
            MultivariateNormalMixtureExpectationMaximization mnmec = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution fittedModel = mnmec.getFittedModel();
            assertNotNull(fittedModel);
        } catch (Exception e) {
            fail("Unexpected exception was thrown.");
        }
    }
}