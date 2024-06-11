import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testConstructor() {
        double[][] data = {{2, 1}, {1, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix() {
        double[][] data = {{-1, 1}, {1, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new RectangularCholeskyDecomposition(matrix, 0.001);
    }
}