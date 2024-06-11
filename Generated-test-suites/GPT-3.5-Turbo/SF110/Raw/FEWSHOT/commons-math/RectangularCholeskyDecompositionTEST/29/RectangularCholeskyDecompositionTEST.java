import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    private double[][] data;
    private RealMatrix matrix;

    @Before
    public void setUp() {
        data = new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        matrix = MatrixUtils.createRealMatrix(data);
    }

    @Test
    public void testConstructor() {
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertEquals(3, rank);
        assertArrayEquals(data, root.getData());
    }
}