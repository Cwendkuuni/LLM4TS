import org.junit.Test;
import org.apache.commons.math.linear.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetL() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lMatrix = choleskyDecomposition.getL();
        RealMatrix expectedLMatrix = MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}});
        Assert.assertEquals(expectedLMatrix, lMatrix);
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix ltMatrix = choleskyDecomposition.getLT();
        RealMatrix expectedLTMatrix = MatrixUtils.createRealMatrix(new double[][]{{2, 6, -8}, {0, 1, 5}, {0, 0, 3}});
        Assert.assertEquals(expectedLTMatrix, ltMatrix);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();
        double expectedDeterminant = 36;
        Assert.assertEquals(expectedDeterminant, determinant, 0.0);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector bVector = new ArrayRealVector(new double[]{1, 2, 3});
        RealVector xVector = solver.solve(bVector);
        RealVector expectedXVector = new ArrayRealVector(new double[]{1, -1, 0});
        Assert.assertEquals(expectedXVector, xVector);
    }

    @Test
    public void testIsNonSingular() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        boolean nonSingular = choleskyDecomposition.getSolver().isNonSingular();
        boolean expectedNonSingular = true;
        Assert.assertEquals(expectedNonSingular, nonSingular);
    }

    @Test
    public void testSolve() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealVector bVector = new ArrayRealVector(new double[]{1, 2, 3});
        RealVector xVector = choleskyDecomposition.getSolver().solve(bVector);
        RealVector expectedXVector = new ArrayRealVector(new double[]{1, -1, 0});
        Assert.assertEquals(expectedXVector, xVector);
    }

    @Test
    public void testGetInverse() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix inverseMatrix = choleskyDecomposition.getSolver().getInverse();
        RealMatrix expectedInverseMatrix = MatrixUtils.createRealMatrix(new double[][]{{0.108, -0.036, -0.036}, {-0.036, 0.027, 0.073}, {-0.036, 0.073, 0.318}});
        Assert.assertEquals(expectedInverseMatrix, inverseMatrix);
    }

}