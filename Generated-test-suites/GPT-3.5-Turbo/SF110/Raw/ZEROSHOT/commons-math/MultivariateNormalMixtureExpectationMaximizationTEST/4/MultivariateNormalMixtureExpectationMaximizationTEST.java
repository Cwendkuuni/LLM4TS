import org.junit.Test;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest
{
    private double[][] data;
    private MultivariateNormalMixtureExpectationMaximization m;

    @Test(expected=NotStrictlyPositiveException.class)
    public void testConstructorNotStrictlyPositiveException() {
        data = new double[0][0];
        m = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected=DimensionMismatchException.class)
    public void testConstructorDimensionMismatchException() {
        data = new double[2][2];
        data[1] = new double[3];
        m = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected=NumberIsTooSmallException.class)
    public void testConstructorNumberIsTooSmallException() {
        data = new double[2][1];
        m = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testFitNotStrictlyPositiveException() {
        data = new double[2][2];
        m = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 0;
        double threshold = 1.0E-5;
        m.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testFitThresholdNotStrictlyPositiveException() {
        data = new double[2][2];
        m = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 1000;
        double threshold = 0;
        m.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected=DimensionMismatchException.class)
    public void testFitDimensionMismatchException() {
        data = new double[2][2];
        m = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        m.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test(expected=SingularMatrixException.class)
    public void testFitSingularMatrixException() {
        data = new double[2][2];
        data[0] = new double[]{1, 2};
        data[1] = new double[]{2, 4};
        m = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addComponent(1, new MultivariateNormalDistribution(new double[]{1, 2}, new double[][]{{1, 1}, {1, 2}}));
        m.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test
    public void testFit() {
        data = new double[2][2];
        data[0] = new double[]{1, 2};
        data[1] = new double[]{2, 3};
        m = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addComponent(1, new MultivariateNormalDistribution(new double[]{1, 2}, new double[][]{{1, 1}, {1, 2}}));
        m.fit(initialMixture, 1000, 1.0E-5);
        double logLikelihood = m.getLogLikelihood();
        MixtureMultivariateNormalDistribution fittedModel = m.getFittedModel();
        // Assertions here
    }

    @Test
    public void testFitDefaultParameters() {
        data = new double[2][2];
        data[0] = new double[]{1, 2};
        data[1] = new double[]{2, 3};
        m = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addComponent(1, new MultivariateNormalDistribution(new double[]{1, 2}, new double[][]{{1, 1}, {1, 2}}));
        m.fit(initialMixture);
        double logLikelihood = m.getLogLikelihood();
        MixtureMultivariateNormalDistribution fittedModel = m.getFittedModel();
        // Assertions here
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testEstimateNotStrictlyPositiveException() {
        data = new double[1][1];
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test(expected=NumberIsTooSmallException.class)
    public void testEstimateNumberIsTooSmallException() {
        data = new double[2][2];
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected=NumberIsTooLargeException.class)
    public void testEstimateNumberIsTooLargeException() {
        data = new double[1][1];
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test
    public void testEstimate() {
        data = new double[2][2];
        data[0] = new double[]{1, 2};
        data[1] = new double[]{2, 3};
        int numComponents = 2;
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
        // Assertions here
    }

    @Test
    public void testGetLogLikelihood() {
        data = new double[2][2];
        data[0] = new double[]{1, 2};
        data[1] = new double[]{2, 3};
        m = new MultivariateNormalMixtureExpectationMaximization(data);
        double logLikelihood = m.getLogLikelihood();
        // Assertions here
    }

    @Test
    public void testGetFittedModel() {
        data = new double[2][2];
        data[0] = new double[]{1, 2};
        data[1] = new double[]{2, 3};
        m = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution fittedModel = m.getFittedModel();
        // Assertions here
    }

    @Test
    public void testDataRowCompareTo() {
        MultivariateNormalMixtureExpectationMaximization.DataRow d1 = m.new DataRow(new double[]{1, 2});
        MultivariateNormalMixtureExpectationMaximization.DataRow d2 = m.new DataRow(new double[]{2, 3});
        int result = d1.compareTo(d2);
        // Assertions here
    }

    @Test
    public void testDataRowEquals() {
        MultivariateNormalMixtureExpectationMaximization.DataRow d1 = m.new DataRow(new double[]{1, 2});
        MultivariateNormalMixtureExpectationMaximization.DataRow d2 = m.new DataRow(new double[]{1, 2});
        boolean result = d1.equals(d2);
        // Assertions here
    }

    @Test
    public void testDataRowHashCode() {
        MultivariateNormalMixtureExpectationMaximization.DataRow d = m.new DataRow(new double[]{1, 2});
        int hashCode = d.hashCode();
        // Assertions here
    }

    @Test
    public void testDataRowGetRow() {
        MultivariateNormalMixtureExpectationMaximization.DataRow d = m.new DataRow(new double[]{1, 2});
        double[] row = d.getRow();
        // Assertions here
    }

}