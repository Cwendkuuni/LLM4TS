package org.apache.commons.math3.linear;

import junit.framework.Assert;
import org.apache.commons.math3.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

RectangularCholeskyDecompositionTest() {
}

// Test cases for constructor
@Test
public void testRectangularCholeskyDecompositionValidInput() {
RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(5);
double small = 1e-6;
RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, small);
// Add assertions here to check the expected behavior of the constructor
Assert.assertNotNull(rectangularCholeskyDecomposition);
Assert.assertNotNull(rectangularCholeskyDecomposition.getRootMatrix());
Assert.assertEquals(rectangularCholeskyDecomposition.getRank(), 5);
}

// Test cases for constructors with invalid inputs
@Test(expected = NonPositiveDefiniteMatrixException.class)
public void testRectangularCholeskyDecompositionNonPositiveDefiniteMatrix() {
RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(5);
double small = 1e-6;
// Set a negative diagonal element
matrix.setEntry(1, 1, -1);
RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, small);
}

@Test(expected = IllegalArgumentException.class)
public void testRectangularCholeskyDecompositionEmptyMatrix() {
RealMatrix matrix = MatrixUtils.createRealMatrix(0, 0);
double small = 1e-6;
RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, small);
}

@Test(expected = IllegalArgumentException.class)
public void testRectangularCholeskyDecompositionNonSymmetricMatrix() {
RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
double small = 1e-6;
// Set a non-symmetric element
matrix.setEntry(1, 0, 1);
RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, small);
}

// Test cases for getRootMatrix() and getRank() methods
@Test
public void testGetRootMatrixValidInput() {
RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3);
double small = 1e-6;
RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, small);
RealMatrix rootMatrix = rectangularCholeskyDecomposition.getRootMatrix();
int rank = rectangularCholeskyDecomposition.getRank();
// Add assertions here to check the expected behavior of getRootMatrix() and getRank()
Assert.assertNotNull(rootMatrix);
Assert.assertEquals(rootMatrix.getRowDimension(), 3);
Assert.assertEquals(rootMatrix.getColumnDimension(), rank);
Assert.assertEquals(rank, 3);
}

}