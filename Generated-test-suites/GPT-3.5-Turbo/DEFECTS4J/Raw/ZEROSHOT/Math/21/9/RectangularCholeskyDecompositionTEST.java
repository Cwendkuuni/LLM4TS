import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    private RealMatrix matrix;
    private double small;
    private RectangularCholeskyDecomposition decomposition;
    private RealMatrix root;
    private int rank;
    
    @Before
    public void setUp() throws Exception {
        matrix = new RectangularCholeskyDecomposition(new double[][]{{1, 2}, {2, 5}});
        small = 1e-6;
        decomposition = new RectangularCholeskyDecomposition(matrix, small);
        root = decomposition.getRootMatrix();
        rank = decomposition.getRank();
    }
    
    @Test
    public void testGetRootMatrix() {
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
        assertEquals(1.0, root.getEntry(0, 0), 1e-6);
        assertEquals(0.0, root.getEntry(1, 0), 1e-6);
        assertEquals(2.0, root.getEntry(0, 1), 1e-6);
        assertEquals(1.0, root.getEntry(1, 1), 1e-6);
    }
    
    @Test
    public void testGetRank() {
        assertEquals(2, rank);
    }
}