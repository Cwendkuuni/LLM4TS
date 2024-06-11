import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.MathRuntimeException;

public class CholeskyDecompositionImplTest {

    public void testGetL() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(testMatrix);
        RealMatrix L = decomposition.getL();

        assertTrue(L.getColumnDimension() == testMatrix.getColumnDimension());
        assertTrue(L.getRowDimension() == testMatrix.getRowDimension());
        assertTrue(L.getEntry(0, 0) == 2.0);
        assertTrue(L.getEntry(1, 0) == 6.0);
        assertTrue(L.getEntry(1, 1) == 1.0);
        assertTrue(L.getEntry(2, 0) == -8.0);
        assertTrue(L.getEntry(2, 1) == 5.0);
        assertTrue(L.getEntry(2, 2) == 3.0);
    }

    public void testGetLT() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(testMatrix);
        RealMatrix LT = decomposition.getLT();

        assertTrue(LT.getColumnDimension() == testMatrix.getColumnDimension());
        assertTrue(LT.getRowDimension() == testMatrix.getRowDimension());
        assertTrue(LT.getEntry(0, 0) == 2.0);
        assertTrue(LT.getEntry(0, 1) == 6.0);
        assertTrue(LT.getEntry(0, 2) == -8.0);
        assertTrue(LT.getEntry(1, 1) == 1.0);
        assertTrue(LT.getEntry(1, 2) == 5.0);
        assertTrue(LT.getEntry(2, 2) == 3.0);
    }

    public void testGetDeterminant() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(testMatrix);
        double determinant = decomposition.getDeterminant();

        assertTrue(determinant == 16.0);
    }

    public void testGetSolver() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(testMatrix);
        DecompositionSolver solver = decomposition.getSolver();

        assertTrue(solver.isNonSingular());
    }

    public void testSolve() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(testMatrix);
        DecompositionSolver solver = decomposition.getSolver();

        double[] b = {1, 2, 3};
        double[] expectedX = {-0.25, 0.5, 2.0};

        assertArrayEquals(expectedX, solver.solve(b));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CholeskyDecompositionImplTest");
    }
}