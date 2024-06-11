import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        double[][] matrixData = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-6;
        RectangularCholeskyDecomposition decomposition = null;
        
        // Test case for normal symmetric positive semidefinite matrix
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            Assert.assertEquals(3, rank);
            
            // Check if A is almost equal to B.B^T
            RealMatrix approxA = rootMatrix.multiply(rootMatrix.transpose());
            for (int i = 0; i < matrix.getRowDimension(); i++) {
                for (int j = 0; j < matrix.getColumnDimension(); j++) {
                    Assert.assertEquals(matrix.getEntry(i, j), approxA.getEntry(i, j), 0.01);
                }
            }
            
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Should not throw NonPositiveDefiniteMatrixException");
        }
        
        // Test case for matrix with zero diagonal element
        double[][] matrixData2 = {
            {0, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(matrixData2);
        
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix2, small);
            Assert.fail("Should throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception, do nothing
        }
        
        // Test case for matrix with small diagonal element
        double[][] matrixData3 = {
            {1e-6, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        RealMatrix matrix3 = MatrixUtils.createRealMatrix(matrixData3);
        
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix3, small);
            Assert.assertEquals(1, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Should not throw NonPositiveDefiniteMatrixException");
        }
    }
}