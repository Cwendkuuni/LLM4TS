import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {
    
    @Test
    public void testGetL() {
        CholeskyDecompositionImpl cd = new CholeskyDecompositionImpl(createPositiveDefiniteMatrix());
        RealMatrix l = cd.getL();
        RealMatrix expectedL = createLowerTriangularMatrix();
        assertTrue(l.equals(expectedL));
    }
    
    @Test
    public void testGetLT() {
        CholeskyDecompositionImpl cd = new CholeskyDecompositionImpl(createPositiveDefiniteMatrix());
        RealMatrix lt = cd.getLT();
        RealMatrix expectedLT = createUpperTriangularMatrix();
        assertTrue(lt.equals(expectedLT));
    }
    
    @Test
    public void testGetDeterminant() {
        CholeskyDecompositionImpl cd = new CholeskyDecompositionImpl(createPositiveDefiniteMatrix());
        double determinant = cd.getDeterminant();
        double expectedDeterminant = calculateDeterminant();
        assertEquals(expectedDeterminant, determinant, 0.0);
    }
    
    @Test
    public void testGetSolver() {
        CholeskyDecompositionImpl cd = new CholeskyDecompositionImpl(createPositiveDefiniteMatrix());
        DecompositionSolver solver = cd.getSolver();
        assertNotNull(solver);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 3);
        new CholeskyDecompositionImpl(matrix);
    }
    
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
         RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3},
                                                                        {2, 5, 6},
                                                                        {3, 6, 9}});
         new CholeskyDecompositionImpl(matrix);
    }
    
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
         RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, -1},
                                                                        {-1, 1}});
         new CholeskyDecompositionImpl(matrix);
    }
    
    private RealMatrix createPositiveDefiniteMatrix() {
        return MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16},
                                                           {12, 37, -43},
                                                           {-16, -43, 98}});
    }
    
    private RealMatrix createLowerTriangularMatrix() {
        return MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0},
                                                           {6, 1, 0},
                                                           {-8, 5, 3}});
    }
    
    private RealMatrix createUpperTriangularMatrix() {
        return MatrixUtils.createRealMatrix(new double[][]{{2, 6, -8},
                                                           {0, 1, 5},
                                                           {0, 0, 3}});
    }
    
    private double calculateDeterminant() {
        return 12;
    }
}