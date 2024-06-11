import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecompositionConstructor() {
        RealMatrix matrix = new RealMatrix(3, 3);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);

        assertNotNull(rcd.getRootMatrix());
        assertEquals(3, rcd.getRank());
    }

    @Test
    public void testRectangularCholeskyDecompositionWithSmall() {
        RealMatrix matrix = new RealMatrix(3, 3);
        double small = 0.0001;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);

        assertNotNull(rcd.getRootMatrix());
        assertEquals(3, rcd.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionException() {
        RealMatrix matrix = new RealMatrix(3, 3);
        double small = -0.0001;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}