import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithSmall() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.01);
        
        assertNotNull(decomp.getRootMatrix());
        assertEquals(3, decomp.getRank());
    }
}