package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testConstructor() {
        // Test case 1: Construct with matrix and default small value
        double[][] testData1 = {
            {4, 2},
            {2, 3}
        };
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(testData1);
        RectangularCholeskyDecomposition rcDecomposition1;
        try {
            rcDecomposition1 = new RectangularCholeskyDecomposition(matrix1);
            RealMatrix rootMatrix1 = rcDecomposition1.getRootMatrix();
            int rank1 = rcDecomposition1.getRank();
            
            RealMatrix expectedRootMatrix1 = MatrixUtils.createRealMatrix(testData1);
            int expectedRank1 = 2;
            
            assertEquals(expectedRootMatrix1, rootMatrix1);
            assertEquals(expectedRank1, rank1);
        } catch (NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
        
        // Test case 2: Construct with matrix and custom small value
        double[][] testData2 = {
            {4, 2},
            {2, 3}
        };
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(testData2);
        double small2 = 0.5;
        RectangularCholeskyDecomposition rcDecomposition2;
        try {
            rcDecomposition2 = new RectangularCholeskyDecomposition(matrix2, small2);
            RealMatrix rootMatrix2 = rcDecomposition2.getRootMatrix();
            int rank2 = rcDecomposition2.getRank();
            
            RealMatrix expectedRootMatrix2 = MatrixUtils.createRealMatrix(testData2);
            int expectedRank2 = 2;
            
            assertEquals(expectedRootMatrix2, rootMatrix2);
            assertEquals(expectedRank2, rank2);
        } catch (NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }
}