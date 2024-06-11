package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, weights[0]);
        expected.setEntry(1, 1, weights[1]);
        expected.setEntry(2, 2, weights[2]);

        RealMatrix actual = weight.getWeight();
        assertNotNull(actual);
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
    }

    @Test
    public void testWeightMatrixConstruction() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weight = new Weight(weightMatrix);

        RealMatrix expected = weightMatrix.copy();
        RealMatrix actual = weight.getWeight();
        assertNotNull(actual);
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightMatrixNonSquareConstruction() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 1.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        new Weight(weightMatrix);
    }

    @Test
    public void testSingularValueDecomposition() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weight = new Weight(weightMatrix);

        RealMatrix actual = weight.getWeight();
        SingularValueDecomposition svd = new SingularValueDecomposition(actual);

        assertArrayEquals(new double[]{Math.sqrt(1.0), Math.sqrt(2.0)},
                svd.getS(), 0.001);
        assertArrayEquals(new double[][]{
                {Math.sqrt(1.0) / Math.sqrt(1.0 + Math.sqrt(2.0)*Math.sqrt(2.0)),
                 Math.sqrt(1.0) / Math.sqrt(1.0 + Math.sqrt(2.0)*Math.sqrt(2.0))},
                {-Math.sqrt(1.0) / Math.sqrt(1.0 + Math.sqrt(2.0)*Math.sqrt(2.0)),
                 Math.sqrt(2.0) / Math.sqrt(1.0 + Math.sqrt(2.0)*Math.sqrt(2.0))}},
                svd.getU(), 0.001);
        assertArrayEquals(new double[][]{
                {Math.sqrt(1.0) / Math.sqrt(1.0 + Math.sqrt(2.0)*Math.sqrt(2.0)),
                 0.0},
                {-Math.sqrt(1.0) / Math.sqrt(1.0 + Math.sqrt(2.0)*Math.sqrt(2.0)),
                 Math.sqrt(2.0) / Math.sqrt(1.0 + Math.sqrt(2.0)*Math.sqrt(2.0))}},
                svd.getVT(), 0.001);
    }

    @Test
    public void testWeightMatrixDeterminant() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weight = new Weight(weightMatrix);

        RealMatrix actual = weight.getWeight();
        double determinant = actual.determinant();

        assertEquals(2.0, determinant, 0.001);
    }

    @Test
    public void testWeightMatrixTrace() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weight = new Weight(weightMatrix);

        RealMatrix actual = weight.getWeight();
        double trace = actual.trace();

        assertEquals(3.0, trace, 0.001);
    }

    @Test
    public void testWeightMatrixInverse() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weight = new Weight(weightMatrix);

        RealMatrix actual = weight.getWeight();
        RealMatrix inverse = actual.inverse();

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {2.0 / (1.0 + 2.0), -0.5},
                {-0.5, 1.0 / 2.0}});
        assertArrayEquals(expected.getData(), inverse.getData(), 0.001);
    }

    @Test
    public void testWeightMatrixMultiplication() {
        double[][] weightMatrixData1 = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix1 = new Array2DRowRealMatrix(weightMatrixData1);

        double[][] weightMatrixData2 = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix weightMatrix2 = new Array2DRowRealMatrix(weightMatrixData2);

        Weight weight1 = new Weight(weightMatrix1);
        Weight weight2 = new Weight(weightMatrix2);

        RealMatrix actual = weight1.getWeight().multiply(weight2.getWeight());

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0}});
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
    }

    @Test
    public void testWeightMatrixMultiplicationWithVector() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weight = new Weight(weightMatrix);

        RealVector vector = new Array2DRowRealVector(new double[]{1.0, 2.0});

        RealVector actual = weight.getWeight().operate(vector);

        RealVector expected = new Array2DRowRealVector(new double[]{1.0, 2.0 * 2.0});
        assertArrayEquals(expected.toArray(), actual.toArray(), 0.001);
    }
}