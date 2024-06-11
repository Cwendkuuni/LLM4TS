import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Test case for getRootMatrix() method
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        // Assertions
        assertNotNull(rootMatrix);
    }

    @Test
    public void testGetRank() {
        // Test case for getRank() method
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposition.getRank();

        // Assertions
        assertEquals(expectedRank, rank);
    }

}