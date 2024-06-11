import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    @Test
    public void testDataConstructor() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.816496580927726, 0.816496580927726},
                {0.816496580927726, 1.0, 0.816496580927726},
                {0.816496580927726, 0.816496580927726, 1.0}});
        RealMatrix actualCorrelationMatrix = pc.getCorrelationMatrix();
        Assert.assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testRealMatrixConstructor() throws MathException {
        RealMatrix matrix = new SimpleRegression().covariance(new Array2DRowRealMatrix(new double[][]{
                {1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}}));
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix, 4);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.816496580927726, 0.816496580927726, 0.752985206196652},
                {0.816496580927726, 1.0, 0.816496580927726, 0.752985206196652},
                {0.816496580927726, 0.816496580927726, 1.0, 0.752985206196652},
                {0.752985206196652, 0.752985206196652, 0.752985206196652, 1.0}});
        RealMatrix actualCorrelationMatrix = pc.getCorrelationMatrix();
        Assert.assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
        assertEquals(4, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDataConstructor() {
        double[][] data = new double[][]{{1, 2}, {3, 4, 5}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = MathRuntimeException.class)
    public void testNullCovarianceInstance() {
        Covariance covariance = null;
        new PearsonsCorrelation(covariance);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {2, 3, 4, 5, 6};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArraysForCorrelation() {
        double[] xArray = {1};
        double[] yArray = {2, 3};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test
    public void testCorrelationMatrix() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.816496580927726, 0.816496580927726},
                {0.816496580927726, 1.0, 0.816496580927726},
                {0.816496580927726, 0.816496580927726, 1.0}});
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    @Test
    public void testCorrelationStandardErrors() throws Exception {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        double[][] expectedCorrelationStandardErrors = new double[][]{
                {0.0, 0.5769925236166242, 0.5769925236166242},
                {0.5769925236166242, 0.0, 0.5769925236166242},
                {0.5769925236166242, 0.5769925236166242, 0.0}};
        Assert.assertArrayEquals(expectedCorrelationStandardErrors, correlationStandardErrors.getData(), 0.0);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        double[][] expectedCorrelationPValues = new double[][]{
                {1.0, 0.154522712439311, 0.154522712439311},
                {0.154522712439311, 1.0, 0.154522712439311},
                {0.154522712439311, 0.154522712439311, 1.0}};
        Assert.assertEquals(expectedCorrelationPValues, correlationPValues);
    }
}