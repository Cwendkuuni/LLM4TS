
package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor_NonPositiveDefiniteMatrixException() {
        // Test for NonPositiveDefiniteMatrixException
        double[][] data = {{-1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException was not thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Test passed
        }
    }
}
