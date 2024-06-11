import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    private RealMatrix matrix;
    private RectangularCholeskyDecomposition decomposition;
    
    @Before
    public void setUp() {
        matrix = new RealMatrix();
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(decomposition);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
    }
    
    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        assertEquals(0, rank);
    }
}