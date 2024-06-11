import org.apache.commons.math.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    private RealMatrix positiveDefiniteMatrix;
    private RealMatrix nonPositiveDefiniteMatrix;
    private RealMatrix nonSquareMatrix;
    private RealMatrix nonSymmetricMatrix;
    private CholeskyDecompositionImpl choleskyPositiveDefinite;
    private CholeskyDecompositionImpl choleskyNonPositiveDefinite;
    private CholeskyDecompositionImpl choleskyNonSquare;
    private CholeskyDecompositionImpl choleskyNonSymmetric;
    
    @Before
    public void setUp() {
        positiveDefiniteMatrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2, 2}, {2, 5, 1}, {2, 1, 6}});
        nonPositiveDefiniteMatrix = MatrixUtils.createRealMatrix(new double[][]{{2, 4, 2}, {4, 5, 1}, {2, 1, 6}});
        nonSquareMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        nonSymmetricMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        
        try {
            choleskyPositiveDefinite = new CholeskyDecompositionImpl(positiveDefiniteMatrix);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
        
        try {
            choleskyNonPositiveDefinite = new CholeskyDecompositionImpl(nonPositiveDefiniteMatrix);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
        
        try {
            choleskyNonSquare = new CholeskyDecompositionImpl(nonSquareMatrix);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
        
        try {
            choleskyNonSymmetric = new CholeskyDecompositionImpl(nonSymmetricMatrix);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetL() {
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0}, {1, Math.sqrt(3), 0}, {1, -1, Math.sqrt(2)}});
        RealMatrix actualL = choleskyPositiveDefinite.getL();
        assertTrue(expectedL.equals(actualL));
    }
    
    @Test
    public void testGetLT() {
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{2, 1, 1}, {0, Math.sqrt(3), -1}, {0, 0, Math.sqrt(2)}});
        RealMatrix actualLT = choleskyPositiveDefinite.getLT();
        assertTrue(expectedLT.equals(actualLT));
    }
    
    @Test
    public void testGetDeterminant() {
        double expectedDeterminant = 12;
        double actualDeterminant = choleskyPositiveDefinite.getDeterminant();
        assertEquals(expectedDeterminant, actualDeterminant, 0);
    }
    
    @Test
    public void testGetSolver() {
        DecompositionSolver expectedSolver = new Solver(choleskyPositiveDefinite.getLT().getData());
        DecompositionSolver actualSolver = choleskyPositiveDefinite.getSolver();
        assertTrue(expectedSolver.equals(actualSolver));
    }
    
    // Additional Test Cases
    
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() throws NonSquareMatrixException, NotSymmetricMatrixException, NotPositiveDefiniteMatrixException {
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(nonSquareMatrix);
        cholesky.getL();
    }
    
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricMatrixException() throws NonSquareMatrixException, NotSymmetricMatrixException, NotPositiveDefiniteMatrixException {
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(nonSymmetricMatrix);
        cholesky.getL();
    }
    
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() throws NonSquareMatrixException, NotSymmetricMatrixException, NotPositiveDefiniteMatrixException {
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(nonPositiveDefiniteMatrix);
        cholesky.getL();
    }
}