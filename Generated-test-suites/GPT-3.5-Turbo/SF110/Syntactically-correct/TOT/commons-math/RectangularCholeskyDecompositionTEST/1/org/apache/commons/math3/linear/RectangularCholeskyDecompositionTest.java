import org.junit.*;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {

    // Test case for constructor 1
    @Test
    public void testConstructor1() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12}, {12, 37}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            Assert.assertEquals(2, decomposition.getRank());
            Assert.assertEquals(2, rootMatrix.getRowDimension());
            Assert.assertEquals(2, rootMatrix.getColumnDimension());
            Assert.assertEquals(3, rootMatrix.getEntry(0, 0), 0.0001);
            Assert.assertEquals(0, rootMatrix.getEntry(0, 1), 0.0001);
            Assert.assertEquals(9, rootMatrix.getEntry(1, 0), 0.0001);
            Assert.assertEquals(2, rootMatrix.getEntry(1, 1), 0.0001);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception caught: " + e.getMessage());
        }
    }

    // Test case for constructor 2
    @Test
    public void testConstructor2() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {2, 5, 8}, {3, 8, 14}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            Assert.assertEquals(3, decomposition.getRank());
            Assert.assertEquals(3, rootMatrix.getRowDimension());
            Assert.assertEquals(3, rootMatrix.getColumnDimension());
            Assert.assertEquals(1, rootMatrix.getEntry(0, 0), 0.0001);
            Assert.assertEquals(0, rootMatrix.getEntry(0, 1), 0.0001);
            Assert.assertEquals(0, rootMatrix.getEntry(0, 2), 0.0001);
            Assert.assertEquals(1, rootMatrix.getEntry(1, 0), 0.0001);
            Assert.assertEquals(1, rootMatrix.getEntry(1, 1), 0.0001);
            Assert.assertEquals(0, rootMatrix.getEntry(1, 2), 0.0001);
            Assert.assertEquals(3, rootMatrix.getEntry(2, 0), 0.0001);
            Assert.assertEquals(2, rootMatrix.getEntry(2, 1), 0.0001);
            Assert.assertEquals(1, rootMatrix.getEntry(2, 2), 0.0001);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception caught: " + e.getMessage());
        }
    }

    // Test case for constructor 1 with non-positive definite matrix
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor1NonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1, 2}, {2, 4}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    // Test case for constructor 2 with non-positive definite matrix
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor2NonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 5}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
    }

    // Test case for constructor 1 with small value 0.0
    @Test
    public void testConstructor1SmallZero() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12}, {12, 37}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            Assert.assertEquals(2, decomposition.getRank());
            Assert.assertEquals(2, rootMatrix.getRowDimension());
            Assert.assertEquals(2, rootMatrix.getColumnDimension());
            Assert.assertEquals(3, rootMatrix.getEntry(0, 0), 0.0001);
            Assert.assertEquals(0, rootMatrix.getEntry(0, 1), 0.0001);
            Assert.assertEquals(9, rootMatrix.getEntry(1, 0), 0.0001);
            Assert.assertEquals(2, rootMatrix.getEntry(1, 1), 0.0001);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception caught: " + e.getMessage());
        }
    }

    // Test case for constructor 1 with small value -0.1
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor1SmallNegative() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12}, {12, 37}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, -0.1);
    }

    // Test case for getRootMatrix
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {2, 5, 8}, {3, 8, 14}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            Assert.assertNotSame(rootMatrix, decomposition.getRootMatrix()); // Ensure different object instance
            Assert.assertEquals(3, rootMatrix.getRowDimension());
            Assert.assertEquals(3, rootMatrix.getColumnDimension());
            Assert.assertEquals(1, rootMatrix.getEntry(0, 0), 0.0001);
            Assert.assertEquals(0, rootMatrix.getEntry(0, 1), 0.0001);
            Assert.assertEquals(0, rootMatrix.getEntry(0, 2), 0.0001);
            Assert.assertEquals(1, rootMatrix.getEntry(1, 0), 0.0001);
            Assert.assertEquals(1, rootMatrix.getEntry(1, 1), 0.0001);
            Assert.assertEquals(0, rootMatrix.getEntry(1, 2), 0.0001);
            Assert.assertEquals(3, rootMatrix.getEntry(2, 0), 0.0001);
            Assert.assertEquals(2, rootMatrix.getEntry(2, 1), 0.0001);
            Assert.assertEquals(1, rootMatrix.getEntry(2, 2), 0.0001);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception caught: " + e.getMessage());
        }
    }

    // Test case for getRank
    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12}, {12, 37}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            Assert.assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception caught: " + e.getMessage());
        }
    }

}