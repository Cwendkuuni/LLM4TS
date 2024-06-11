
package org.apache.commons.math3.linear;

import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        double[][] data = {
                {1, 2, 3},
                {2, 5, 4},
                {3, 4, 6}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        double[][] expectedData = {
                {1, 0},
                {2, 1},
                {3, 2}
        };
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedData);

        assertEquals(expectedRoot, decomposition.getRootMatrix());
    }

    @Test
    public void testGetRank() {
        double[][] data = {
                {1, 2, 3},
                {2, 5, 4},
                {3, 4, 6}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        assertEquals(2, decomposition.getRank());
    }
}
