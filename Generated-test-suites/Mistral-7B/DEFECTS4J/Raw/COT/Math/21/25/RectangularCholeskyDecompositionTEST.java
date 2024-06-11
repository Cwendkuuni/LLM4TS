package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testConstructWithPositiveDefiniteMatrix() throws Exception {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 10.0}
        });

        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.7071067811865476, 1.414213741059604, 0.0},
                {-0.7071067811865476, -1.414213741059604, 3.1622776601683795}
        });
        RealMatrix actualRoot = rcd.getRootMatrix();
        assertEquals(expectedRoot, actualRoot);
    }

    @org.junit.Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructWithNegativeDefiniteMatrix() throws Exception {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, -5.0, 6.0},
                {3.0, 6.0, -10.0}
        });
        new RectangularCholeskyDecomposition(matrix, small);
        fail("Expected NonPositiveDefiniteMatrixException");
    }

    @org.junit.Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructWithSingularMatrix() throws Exception {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 0.0, 6.0},
                {3.0, 6.0, 0.0}
        });
        new RectangularCholeskyDecomposition(matrix, small);
        fail("Expected NonPositiveDefiniteMatrixException");
    }

    @org.junit.Test
    public void testConstructWithSmallDiagonalElements() throws Exception {
        double small = 1e-15;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 0.1, 6.0},
                {3.0, 6.0, 0.01}
        });
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.7071067811865476, 0.995004561598834, 0.0},
                {-0.7071067811865476, -1.204089943771286, 3.1622776601683795}
        });
        RealMatrix actualRoot = rcd.getRootMatrix();
        assertEquals(expectedRoot, actualRoot);
    }

    @org.junit.Test
    public void testGetRank() throws Exception {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 10.0}
        });
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        int actualRank = rcd.getRank();
        int expectedRank = 3;
        assertEquals(expectedRank, actualRank);
    }

    @org.junit.Test
    public void testGetRankFromSingularMatrix() throws Exception {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 0.0, 6.0},
                {3.0, 6.0, 0.0}
        });
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        int actualRank = rcd.getRank();
        int expectedRank = 2;
        assertEquals(expectedRank, actualRank);
    }

    @org.junit.Test
    public void testGetRankFromNegativeDefiniteMatrix() throws Exception {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, -5.0, 6.0},
                {3.0, 6.0, -10.0}
        });
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        int actualRank = rcd.getRank();
        int expectedRank = 0;
        assertEquals(expectedRank, actualRank);
    }

    @org.junit.Test
    public void testGetSquareRootFromSvd() throws Exception {
        double tolerance = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 10.0}
        });
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix expectedRoot = svd.getU();
        RealMatrix rcdRoot = new RectangularCholeskyDecomposition(matrix.transpose(), tolerance).getRootMatrix();
        RealMatrix actualRoot = rcdRoot.transpose();
        for (int i = 0; i < expectedRoot.getRowDimension(); i++) {
            RealVector expectedColumn = expectedRoot.getColumnVector(i);
            RealVector actualColumn = actualRoot.getColumnVector(i);
            for (int j = 0; j < expectedColumn.getDimension(); j++) {
                double difference = Precision.doubleValue(expectedColumn.getEntry(j) - actualColumn.getEntry(j));
                assertTrue(difference <= tolerance);
            }
        }
    }

    @org.junit.Test
    public void testGetSquareRootFromRandomMatrix() throws Exception {
        double tolerance = 1e-12;
        int size = 5;
        RealMatrix matrix = new Array2DRowRealMatrix(new Random().nextGauss(size, size).getData());
        while (!matrix.isSymmetric()) {
            matrix = new Array2DRowRealMatrix(new Random().nextGauss(size, size).getData());
        }
        RealMatrix rcdRoot = new RectangularCholeskyDecomposition(matrix.transpose(), tolerance).getRootMatrix();
        RealMatrix expectedRoot = new SingularValueDecomposition(matrix.transpose()).getU().transpose();
        for (int i = 0; i < expectedRoot.getRowDimension(); i++) {
            RealVector expectedColumn = expectedRoot.getColumnVector(i);
            RealVector actualColumn = rcdRoot.getColumnVector(i);
            for (int j = 0; j < expectedColumn.getDimension(); j++) {
                double difference = Precision.doubleValue(expectedColumn.getEntry(j) - actualColumn.getEntry(j));
                assertTrue(difference <= tolerance);
            }
        }
    }
}