package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testExceptionWhenMatrixIsNotPositiveSemidefinite() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 1},
                {-1, 1}
        });

        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testRank1() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 1, 0},
                {1, 1, 1},
                {0, 1, 1}
        });

        RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix root = decompositon.getRootMatrix();
        int rank = decompositon.getRank();

        assertEquals(2, rank);
        assertNotNull(root);
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}
        });

        assertArrayEquals(expectedValues(expectedRoot.getData()), root.getData(), 0.001);
    }

    @Test
    public void testRank2() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 1, 0, 0},
                {1, 1, 0, 1},
                {0, 0, 1, 1},
                {0, 1, 1, 1}
        });

        RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 1.0e-5);
        RealMatrix root = decompositon.getRootMatrix();
        int rank = decompositon.getRank();

        assertEquals(2, rank);
        assertNotNull(root);
        assertEquals(4, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0, 0.0},
                {1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0), 0.0, 0.0},
                {1.0 / Math.sqrt(2.0), 0.0, 1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)},
                {-1.0 / Math.sqrt(2.0) / 2.0, 1.0 / Math.sqrt(2.0) / 2.0, 1.0 / Math.sqrt(2.0) / 2.0, 1.0 / Math.sqrt(2.0) / 2.0}
        });

        assertArrayEquals(expectedValues(expectedRoot.getData()), root.getData(), 0.001);
    }

    @Test
    public void testRank3() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 1, 0, 0},
                {1, 1, 0, 1},
                {0, 0, 1, 1},
                {0, 1, 1, 1},
                {0, 0, 1, 1}
        });

        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSingularValueDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 1, 0, 0},
                {1, 1, 0, 1},
                {0, 0, 1, 1},
                {0, 1, 1, 1}
        });

        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix vt = svd.getVT();

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0, 0.0},
                {1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0), 0.0, 0.0},
                {1.0 / Math.sqrt(2.0), 0.0, 1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)},
                {-1.0 / Math.sqrt(2.0) / 2.0, 1.0 / Math.sqrt(2.0) / 2.0, 1.0 / Math.sqrt(2.0) / 2.0, 1.0 / Math.sqrt(2.0) / 2.0}
        });

        assertArrayEquals(expectedValues(expectedRoot.getData()), u.getData(), 0.001);
        assertArrayEquals(expectedDiagonal(s.getData()), expectedValues(MatrixUtils.diagonal(matrix).getData()), 0.001);
        assertArrayEquals(expectedValues(vt.getData().transpose().getData()), expectedRoot.getData().transpose(), 0.001);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 1, 0, 0},
                {1, 1, 0, 1},
                {0, 0, 1, 1},
                {0, 1, 1, 1}
        });

        RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 1.0e-5);

        int rank = decompositon.getRank();
        assertEquals(2, rank);

        // test negative rank
        try {
            new RectangularCholeskyDecomposition(MatrixUtils.eye(4), -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMultiply() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 1, 0, 0},
                {1, 1, 0, 1},
                {0, 0, 1, 1},
                {0, 1, 1, 1}
        });

        RealMatrix root = new RectangularCholeskyDecomposition(matrix, 0.0).getRootMatrix();

        RealVector b = new ArrayRealVector(new double[]{-1.0, 1.0, 1.0, 0.0});
        RealVector x = root.operate(b);

        RealVector expectedX = new ArrayRealVector(new double[]{1.0, -0.5, 0.0, 0.5});
        assertEquals(expectedX, x);
    }

    @Test
    public void testMultiplyWithSingularValueDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 1, 0, 0},
                {1, 1, 0, 1},
                {0, 0, 1, 1},
                {0, 1, 1, 1}
        });

        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix vt = svd.getVT();

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0, 0.0},
                {1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0), 0.0, 0.0},
                {1.0 / Math.sqrt(2.0), 0.0, 1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)},
                {-1.0 / Math.sqrt(2.0) / 2.0, 1.0 / Math.sqrt(2.0) / 2.0, 1.0 / Math.sqrt(2.0) / 2.0, 1.0 / Math.sqrt(2.0) / 2.0}
        });

        assertEquals(expectedRoot, u.multiply(s).multiply(vt.transpose()));
    }

    private double[] expectedValues(double[] values) {
        return values;
    }

    private double[] expectedDiagonal(double[] values) {
        double[] result = new double[values.length];
        System.arraycopy(values, 0, result, 0, values.length);
        for (int i = 0; i < values.length; ++i) {
            result[i] = values[i] > 0 ? values[i] : 0;
        }
        return result;
    }
}