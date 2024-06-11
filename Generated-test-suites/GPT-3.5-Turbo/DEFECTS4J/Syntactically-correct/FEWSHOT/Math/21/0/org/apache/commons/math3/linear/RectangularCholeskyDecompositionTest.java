import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecomposition() {
        double[][] testData = {{1, -2, 3}, {4, 5, -6}, {-7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        double small = 1e-6;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
        
        double[][] invalidTestData = {{-1, 2, 3}, {4, 5, 6}, {7, 8, -9}};
        RealMatrix invalidMatrix = MatrixUtils.createRealMatrix(invalidTestData);
        RectangularCholeskyDecomposition invalidDecomposition = new RectangularCholeskyDecomposition(invalidMatrix, small);
    }
}