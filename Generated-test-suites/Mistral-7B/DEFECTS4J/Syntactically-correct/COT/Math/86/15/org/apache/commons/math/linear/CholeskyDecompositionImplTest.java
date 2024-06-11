import org.apache.commons.math3.linear.*;
import org.junit.Assert.*;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructionWithSquareSymmetricPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.5, 2.5}, {2.5, 5.0, 3.0}, {2.5, 3.0, 5.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        Assert.assertEquals(lt.transpose(), l);
        Assert.assertTrue(l.isSymmetric());
        Assert.assertTrue(l.isPositiveDefinite());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructionWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.5}, {2.5, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructionWithNonSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.5}, {2.5, 3.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructionWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.5}, {2.5, 0.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.5, 2.5}, {2.5, 5.0, 3.0}, {2.5, 3.0, 5.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector result = solver.solve(vector);
        RealVector expected = new ArrayRealVector(new double[]{1.5, 0.5, 0.0});
        Assert.assertArrayEquals(expected.toArray(), result.toArray(), 0.0);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.5, 2.5}, {2.5, 5.0, 3.0}, {2.5, 3.0, 5.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();
        double expected = 38.5;
        assertEquals(expected, determinant, 0.0);
    }

    @Test
    public void testGetL() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.5, 2.5}, {2.5, 5.0, 3.0}, {2.5, 3.0, 5.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865475, 0.7615777615777617, 0.0}, {0.7071067811865475, 0.43301270189221925, 0.7615777615777617}});
        Assert.assertEquals(expected, l);
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.5, 2.5}, {2.5, 5.0, 3.0}, {2.5, 3.0, 5.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = choleskyDecomposition.getLT();
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.5, 2.5}, {2.5, 2.8867513459485356, 1.4142137413793103}, {2.5, 1.4142137413793103, 2.8867513459485356}});
        Assert.assertEquals(expected, lt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithInvalidVector() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.5, 2.5}, {2.5, 5.0, 3.0}, {2.5, 3.0, 5.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0, 4.0});
        solver.solve(vector);
    }

    @Test
    public void testSolverWithIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealMatrix identityMatrix = MatrixUtils.createRealIdentityMatrix(matrix.getRowDimension());
        RealMatrix result = solver.solve(identityMatrix);
        RealMatrix expected = identityMatrix;
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSolverWithVector() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.5, 2.5}, {2.5, 5.0, 3.0}, {2.5, 3.0, 5.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector result = solver.solve(vector);
        RealVector expected = new ArrayRealVector(new double[]{1.5, 0.5, 0.0});
        Assert.assertArrayEquals(expected.toArray(), result.toArray(), 0.0);
    }

    @Test
    public void testSolverWithMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.5, 2.5}, {2.5, 5.0, 3.0}, {2.5, 3.0, 5.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RealMatrix result = solver.solve(b);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{-0.9894389644039626, -0.0198629322471784}, {-0.0198629322471784, 0.9894389644039626}});
        Assert.assertEquals(expected, result);
    }

}