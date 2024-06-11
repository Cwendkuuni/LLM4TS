package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.MathUtils;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();
        double[] singularValues = svd.getSingularValues();

        // Check U, U^T, S, and V
        assertEquals("U should be orthogonal", 1.0, u.determinant().doubleValue(), 1e-9);
        assertEquals("UT should be orthogonal", 1.0, ut.determinant().doubleValue(), 1e-9);
        assertEquals("S should be diagonal", 1.0, s.getEntry(0, 0), 1e-9);
        assertEquals("S should be diagonal", 1.543080635555576, s.getEntry(1, 1), 1e-9);
        assertEquals("V should be orthogonal", 1.0, v.determinant().doubleValue(), 1e-9);
        assertEquals("VT should be orthogonal", 1.0, vt.determinant().doubleValue(), 1e-9);

        // Check U * S * V^T = Original Matrix
        RealMatrix product = u.multiply(s).multiply(vt);
        RealMatrix originalMatrix = new Array2DRowRealMatrix(new double[][] {{1, 2}, {3, 4}});
        assertEquals("U * S * V^T should be equal to the original matrix", originalMatrix, product);

        // Check singular values are correct
        assertArrayEquals("Singular values should be correct", new double[] {1.0, 1.543080635555576}, singularValues, 1e-9);

        // Test with singular matrix
        RealMatrix singularMatrix = new Array2DRowRealMatrix(new double[][] {{1, 1}, {1, 1}});
        assertThrows(InvalidMatrixException.class, () -> new SingularValueDecompositionImpl(singularMatrix));

        // Test with empty matrix
        RealMatrix emptyMatrix = new Array2DRowRealMatrix(new double[][] {});
        assertThrows(InvalidMatrixException.class, () -> new SingularValueDecompositionImpl(emptyMatrix));

        // Test with diagonal matrix
        RealMatrix diagonalMatrix = new Array2DRowRealMatrix(new double[][] {{1, 0}, {0, 2}});
        SingularValueDecompositionImpl svdDiagonal = new SingularValueDecompositionImpl(diagonalMatrix);
        RealMatrix uDiagonal = svdDiagonal.getU();
        RealMatrix sDiagonal = svdDiagonal.getS();
        RealMatrix vDiagonal = svdDiagonal.getV();

        // Check U, U^T, S, and V
        assertEquals("U should be orthogonal", 1.0, uDiagonal.determinant().doubleValue(), 1e-9);
        assertEquals("UT should be orthogonal", 1.0, ut.determinant().doubleValue(), 1e-9);
        assertEquals("S should be diagonal", 1.0, sDiagonal.getEntry(0, 0), 1e-9);
        assertEquals("S should be diagonal", 2.0, sDiagonal.getEntry(1, 1), 1e-9);
        assertEquals("V should be orthogonal", 1.0, vDiagonal.determinant().doubleValue(), 1e-9);
        assertEquals("VT should be orthogonal", 1.0, vt.determinant().doubleValue(), 1e-9);

        // Check U * S * V^T = Original Matrix
        product = uDiagonal.multiply(sDiagonal).multiply(vt);
        RealMatrix originalDiagonalMatrix = new Array2DRowRealMatrix(new double[][] {{1, 0}, {0, 2}});
        assertEquals("U * S * V^T should be equal to the original matrix", originalDiagonalMatrix, product);

        // Test truncated SVD
        RealMatrix truncatedMatrix = new Array2DRowRealMatrix(new double[][] {{1, 2}, {3, 4}, {5, 6}});
        SingularValueDecompositionImpl svdTruncated = new SingularValueDecompositionImpl(truncatedMatrix, 2);
        u = svdTruncated.getU();
        s = svdTruncated.getS();
        v = svdTruncated.getV();
        ut = svdTruncated.getUT();
        vt = svdTruncated.getVT();

        // Check U, U^T, S, and V
        assertEquals("U should be orthogonal", 1.0, u.determinant().doubleValue(), 1e-9);
        assertEquals("UT should be orthogonal", 1.0, ut.determinant().doubleValue(), 1e-9);
        assertEquals("S should be diagonal and have 2 singular values", 2, s.getRowDimension(), 1);
        assertEquals("V should be orthogonal", 1.0, v.determinant().doubleValue(), 1e-9);
        assertEquals("VT should be orthogonal", 1.0, vt.determinant().doubleValue(), 1e-9);

        // Check U * S * V^T = Original Matrix (truncated)
        product = u.multiply(s).multiply(vt);
        RealMatrix originalTruncatedMatrix = new Array2DRowRealMatrix(new double[][] {{1, 2}, {3, 4}});
        assertEquals("U * S * V^T should be equal to the original truncated matrix", originalTruncatedMatrix, product);

        // Test getting singular values
        double[] singularValuesTruncated = svdTruncated.getSingularValues();
        assertEquals("First singular value should be correct", 1.0, singularValuesTruncated[0], 1e-9);
        assertEquals("Second singular value should be correct", 1.414213741264341, singularValuesTruncated[1], 1e-9);

        // Test getting rank
        assertEquals("Rank should be 2", 2, svdTruncated.getRank());

        // Test Solver
        double[] b = new double[] {1, 2};
        RealVector x = svdTruncated.getSolver().solve(new ArrayRealVector(b));
        double[] expected = new double[] {1.414213741264341, -0.7071067811865476};
        assertArrayEquals("Solver should return correct result", expected, x.toArray(), 1e-9);

        // Test Solver, non-square matrix
        double[] bNonSquare = new double[] {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> svdTruncated.getSolver().solve(new ArrayRealVector(bNonSquare)));

        // Test Solver, singular matrix
        SingularValueDecompositionImpl svdSingular = new SingularValueDecompositionImpl(singularMatrix);
        assertThrows(IllegalArgumentException.class, () -> svdSingular.getSolver().solve(new ArrayRealVector(b)));

        // Test Solver, empty matrix
        assertThrows(IllegalArgumentException.class, () -> svdEmpty.getSolver().solve(new ArrayRealVector(b)));

        // Test Solver, rank deficient matrix
        RealMatrix rankDeficientMatrix = new Array2DRowRealMatrix(new double[][] {{1, 2}, {1, 2}, {1, 0}});
        SingularValueDecompositionImpl svdRankDeficient = new SingularValueDecompositionImpl(rankDeficientMatrix);
        assertThrows(IllegalArgumentException.class, () -> svdRankDeficient.getSolver().solve(new ArrayRealVector(b)));

        // Test Solver, non-singular matrix
        RealMatrix nonSingularMatrix = new Array2DRowRealMatrix(new double[][] {{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svdNonSingular = new SingularValueDecompositionImpl(nonSingularMatrix);
        RealVector xNonSingular = svdNonSingular.getSolver().solve(new ArrayRealVector(b));
        assertArrayEquals("Solver should return correct result for non-singular matrix", new double[] {0.4, 0.6}, xNonSingular.toArray(), 1e-9);
    }

}