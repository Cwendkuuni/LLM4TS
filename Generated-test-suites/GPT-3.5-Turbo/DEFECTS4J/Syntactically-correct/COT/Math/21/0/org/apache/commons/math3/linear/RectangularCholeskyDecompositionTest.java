import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-6;
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        
        Assert.assertNotNull(decomp);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionNonPositiveDefinite() {
        double[][] data = {{1, 2}, {2, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-6;
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-6;
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        
        RealMatrix root = decomp.getRootMatrix();
        
        Assert.assertNotNull(root);
    }

    @Test
    public void testGetRank() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-6;
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        
        int rank = decomp.getRank();
        
        Assert.assertEquals(3, rank);
    }
}