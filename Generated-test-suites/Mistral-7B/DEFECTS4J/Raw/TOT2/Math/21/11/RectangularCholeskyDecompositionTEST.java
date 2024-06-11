package org.apache.commons.math3.linear.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.exception.NonPositiveDefiniteMatrixException;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

// Test cases from experts
// ...

// Additional test case for constructor with negative small value
@Test(expected = IllegalArgumentException.class)
public void testRectangularCholeskyDecompositionWithNegativeSmallValue() {
RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
RectangularCholeskyDecomposition decomposition;
try {
decomposition = new RectangularCholeskyDecomposition(matrix, -0.0);
fail("Expected IllegalArgumentException");
} catch (IllegalArgumentException e) {
// expected
}
}

}