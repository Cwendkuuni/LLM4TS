package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testRectangularCholeskyDecomposition() {
        double small = 1e-9;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.25}, {0.5, 1.0, 0.5}, {0.25, 0.5, 1.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.25}, {0.5, 1.0, 0.5}, {0.0, 0.0, 1.0}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 3;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //expected
        }

        double[][] invalidMatrixData = {{1.0, -0.5}, {-0.5, 1.0}};
        RealMatrix invalidMatrix = RealMatrixUtils.createRealMatrix(invalidMatrixData);
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(invalidMatrix, small));

        double[][] singularMatrixData = {{1.0, 0.5}, {0.5, 0.0}};
        RealMatrix singularMatrix = RealMatrixUtils.createRealMatrix(singularMatrixData);
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(singularMatrix, small));

        double[][] zeroMatrixData = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix zeroMatrix = RealMatrixUtils.createRealMatrix(zeroMatrixData);
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(zeroMatrix, small));

        double[][] positiveDefiniteMatrixData = {{1.0, 0.5}, {0.5, 1.5}};
        RealMatrix positiveDefiniteMatrix = RealMatrixUtils.createRealMatrix(positiveDefiniteMatrixData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(positiveDefiniteMatrix, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.5}, {0.0, 0.0}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 3;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //should not be thrown
        }

        double[][] positiveSemidefiniteMatrixData = {{1.0, 0.5}, {0.5, 0.5}};
        RealMatrix positiveSemidefiniteMatrix = RealMatrixUtils.createRealMatrix(positiveSemidefiniteMatrixData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(positiveSemidefiniteMatrix, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 0.0}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 2;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //should not be thrown
        }

        double[][] psdMatrixWithSmallDiagonalData = {{1.0, 0.5}, {0.5, 0.001}};
        RealMatrix psdMatrixWithSmallDiagonal = RealMatrixUtils.createRealMatrix(psdMatrixWithSmallDiagonalData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(psdMatrixWithSmallDiagonal, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 0.0}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 2;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //should not be thrown
        }

        double[][] psdMatrixWithZeroDiagonalData = {{1.0, 0.5}, {0.5, 0.0}};
        RealMatrix psdMatrixWithZeroDiagonal = RealMatrixUtils.createRealMatrix(psdMatrixWithZeroDiagonalData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(psdMatrixWithZeroDiagonal, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 0.0}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 2;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //should not be thrown
        }

        double[][] psdMatrixWithNegativeDiagonalData = {{1.0, 0.5}, {0.5, -0.5}};
        RealMatrix psdMatrixWithNegativeDiagonal = RealMatrixUtils.createRealMatrix(psdMatrixWithNegativeDiagonalData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(psdMatrixWithNegativeDiagonal, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 0.0}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 2;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //should be thrown
        }

        double[][] psdMatrixWithSingularValuesData = {{1.0, 0.5}, {0.5, 0.5}, {0.1, 0.0}};
        RealMatrix psdMatrixWithSingularValues = RealMatrixUtils.createRealMatrix(psdMatrixWithSingularValuesData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(psdMatrixWithSingularValues, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 0.0}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 2;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //should not be thrown
        }

        double[][] psdMatrixWithPivotAndDiagonalSwappedData = {{1.0, 0.5}, {0.5, 1.0}, {0.5, 1.0}};
        RealMatrix psdMatrixWithPivotAndDiagonalSwapped = RealMatrixUtils.createRealMatrix(psdMatrixWithPivotAndDiagonalSwappedData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(psdMatrixWithPivotAndDiagonalSwapped, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 0.0}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 2;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //should not be thrown
        }

        double[][] psdMatrixWithTwoSmallDiagonalsData = {{1.0, 0.5}, {0.5, 0.001}, {0.2, 0.5}};
        RealMatrix psdMatrixWithTwoSmallDiagonals = RealMatrixUtils.createRealMatrix(psdMatrixWithTwoSmallDiagonalsData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(psdMatrixWithTwoSmallDiagonals, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 0.0}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 2;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //should not be thrown
        }

        double[][] psdMatrixWithOneLargeNegativeDiagonalData = {{1.0, 0.5}, {0.5, -5.0}, {0.2, 0.5}};
        RealMatrix psdMatrixWithOneLargeNegativeDiagonal = RealMatrixUtils.createRealMatrix(psdMatrixWithOneLargeNegativeDiagonalData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(psdMatrixWithOneLargeNegativeDiagonal, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 0.0}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 2;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //should be thrown
        }

        double[][] psdMatrixWithOneLargeNegativeDiagonalAndSmallDiagonalData = {{1.0, 0.5}, {0.5, -5.0}, {0.0, 0.01}};
        RealMatrix psdMatrixWithOneLargeNegativeDiagonalAndSmallDiagonal = RealMatrixUtils.createRealMatrix(psdMatrixWithOneLargeNegativeDiagonalAndSmallDiagonalData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(psdMatrixWithOneLargeNegativeDiagonalAndSmallDiagonal, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 0.0}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 2;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //should be thrown
        }

        double[][] psdMatrixWithTwoLargeNegativeDiagonalsData = {{1.0, 0.5}, {0.5, -5.0}, {-5.0, 1.0}, {0.2, 0.5}};
        RealMatrix psdMatrixWithTwoLargeNegativeDiagonals = RealMatrixUtils.createRealMatrix(psdMatrixWithTwoLargeNegativeDiagonalsData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(psdMatrixWithTwoLargeNegativeDiagonals, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 0.0}, {0.0, 0.0}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 2;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //should be thrown
        }

        double[][] psdMatrixWithTwoLargeNegativeDiagonalsSwappedData = {{1.0, 0.5}, {-5.0, 1.0}, {0.5, 1.0}, {0.2, 0.5}};
        RealMatrix psdMatrixWithTwoLargeNegativeDiagonalsSwapped = RealMatrixUtils.createRealMatrix(psdMatrixWithTwoLargeNegativeDiagonalsSwappedData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(psdMatrixWithTwoLargeNegativeDiagonalsSwapped, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.0, 0.0}, {0.5, 1.0}, {0.2, 0.5}});
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
            int expectedRank = 3;
            int actualRank = decomp.getRank();
            assertEquals("Rank is not correct.", expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            //should be thrown
        }
    }
}