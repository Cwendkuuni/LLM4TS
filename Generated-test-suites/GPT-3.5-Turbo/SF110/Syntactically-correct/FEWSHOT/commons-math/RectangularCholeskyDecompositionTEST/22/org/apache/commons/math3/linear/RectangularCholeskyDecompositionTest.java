
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorThrowsNonPositiveDefiniteMatrixException() {
        try {
            RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
            matrix.setRow(0, new double[]{1, 2, 3});
            matrix.setRow(1, new double[]{2, 5, 6});
            matrix.setRow(2, new double[]{3, 6, 9});
            double small = 0.0;
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(0.0, e.getEigenspaceDimension());
            assertEquals(0.0, e.getSmall());
            assertEquals(0, e.getRow());
        }
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setRow(0, new double[]{1, 2, 3});
        matrix.setRow(1, new double[]{2, 5, 6});
        matrix.setRow(2, new double[]{3, 6, 9});
        double small = 0.1;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(matrix.getColumnDimension(), root.getColumnDimension());
        assertEquals(matrix.getRowDimension(), root.getRowDimension());
        double[][] expected = {{1.0, 0.0}, {2.0, 1.0}, {3.0, 0.5}};
        double[][] actual = root.getData();
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals(expected[i][j], actual[i][j], 0.0001);
            }
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setRow(0, new double[]{1, 2, 3});
        matrix.setRow(1, new double[]{2, 5, 6});
        matrix.setRow(2, new double[]{3, 6, 9});
        double small = 0.1;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(2, decomposition.getRank());
    }
}
