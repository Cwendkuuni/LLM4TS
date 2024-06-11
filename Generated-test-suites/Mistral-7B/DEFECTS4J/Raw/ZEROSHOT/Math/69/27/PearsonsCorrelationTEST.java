import org.apache.commons.math3.linear.Array2DRrilMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        Assert.assertNull(pearsonCorrelation.getCorrelationMatrix());
        Assert.assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataArray() {
        double[][] data = new double[1][1];
        new PearsonsCorrelation(data);
    }

    @Test
    public void testValidDataArray() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();

        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());

        // verify correlation matrix is correct
        assertThat(correlationMatrix.getEntry(0, 0), is(1.0));
        assertThat(correlationMatrix.getEntry(0, 1), is(0.7416));
        assertThat(correlationMatrix.getEntry(0, 2), is(0.5735));

        assertThat(correlationMatrix.getEntry(1, 0), is(0.7416));
        assertThat(correlationMatrix.getEntry(1, 1), is(1.0));
        assertThat(correlationMatrix.getEntry(1, 2), is(0.5735));

        assertThat(correlationMatrix.getEntry(2, 0), is(0.5735));
        assertThat(correlationMatrix.getEntry(2, 1), is(0.5735));
        assertThat(correlationMatrix.getEntry(2, 2), is(1.0));

        Assert.assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testValidRealMatrix() {
        RealMatrix matrix = new Array2DRrilMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();

        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());

        // verify correlation matrix is correct
        assertThat(correlationMatrix.getEntry(0, 0), is(1.0));
        assertThat(correlationMatrix.getEntry(0, 1), is(0.7416));
        assertThat(correlationMatrix.getEntry(0, 2), is(0.5735));

        assertThat(correlationMatrix.getEntry(1, 0), is(0.7416));
        assertThat(correlationMatrix.getEntry(1, 1), is(1.0));
        assertThat(correlationMatrix.getEntry(1, 2), is(0.5735));

        assertThat(correlationMatrix.getEntry(2, 0), is(0.5735));
        assertThat(correlationMatrix.getEntry(2, 1), is(0.5735));
        assertThat(correlationMatrix.getEntry(2, 2), is(1.0));

        Assert.assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCovariance() {
        new PearsonsCorrelation(null);
    }

    @Test
    public void testValidCovariance() {
        Covariance covariance = new Covariance(new double[][]{{1, 2}, {2, 3}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN());

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();

        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(2, correlationMatrix.getRowDimension());
        Assert.assertEquals(2, correlationMatrix.getColumnDimension());

        // verify correlation matrix is correct
        assertThat(correlationMatrix.getEntry(0, 0), is(1.0));
        assertThat(correlationMatrix.getEntry(0, 1), is(0.8165));
        assertThat(correlationMatrix.getEntry(1, 0), is(0.8165));
        assertThat(correlationMatrix.getEntry(1, 1), is(1.0));

        Assert.assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pearsonCorrelation.computeCorrelationMatrix(data);

        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());

        // verify correlation matrix is correct
        assertThat(correlationMatrix.getEntry(0, 0), is(1.0));
        assertThat(correlationMatrix.getEntry(0, 1), is(0.7416));
        assertThat(correlationMatrix.getEntry(0, 2), is(0.5735));

        assertThat(correlationMatrix.getEntry(1, 0), is(0.7416));
        assertThat(correlationMatrix.getEntry(1, 1), is(1.0));
        assertThat(correlationMatrix.getEntry(1, 2), is(0.5735));

        assertThat(correlationMatrix.getEntry(2, 0), is(0.5735));
        assertThat(correlationMatrix.getEntry(2, 1), is(0.5735));
        assertThat(correlationMatrix.getEntry(2, 2), is(1.0));

        Assert.assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        double correlation = pearsonCorrelation.correlation(xArray, yArray);

        Assert.assertEquals(0.8165, correlation, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataForCorrelation() {
        double[] xArray = new double[1];
        double[] yArray = new double[0];
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataForCorrelation1() {
        double[] xArray = new double[1];
        double[] yArray = new double[1];
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRrilMatrix(new double[][]{{1, 2}, {2, 3}});
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);

        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(2, correlationMatrix.getRowDimension());
        Assert.assertEquals(2, correlationMatrix.getColumnDimension());

        // verify correlation matrix is correct
        assertThat(correlationMatrix.getEntry(0, 0), is(1.0));
        assertThat(correlationMatrix.getEntry(0, 1), is(0.8165));
        assertThat(correlationMatrix.getEntry(1, 0), is(0.8165));
        assertThat(correlationMatrix.getEntry(1, 1), is(1.0));
    }
}