import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor1() throws NonPositiveDefiniteMatrixException {
        // Generate test data
        double[][] data = {{4.0, 1.0, 2.0},
                           {1.0, 5.0, 3.0},
                           {2.0, 3.0, 6.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        // Test constructor
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        // Verify result
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        double[][] expectedRootData = {{2.0, 0.0, 0.0},
                                       {0.5, 2.345207879911715, 0.0},
                                       {1.0, 0.6823176510094829, 1.36785513550195}};
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedRootData);
        int expectedRank = 3;
        assertEquals(expectedRoot, root);
        assertEquals(expectedRank, rank);
    }

    @Test
    public void testConstructor2() throws NonPositiveDefiniteMatrixException {
        // Generate test data
        double[][] data = {{4.0, 1.0, 2.0},
                           {1.0, 5.0, 3.0},
                           {2.0, 3.0, 6.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.1;

        // Test constructor
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // Verify result
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        double[][] expectedRootData = {{2.0, 0.0, 0.0},
                                       {0.5, 2.345207879911715, 0.0},
                                       {1.0, 0.6823176510094829, 1.36785513550195}};
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedRootData);
        int expectedRank = 3;
        assertEquals(expectedRoot, root);
        assertEquals(expectedRank, rank);
    }
}