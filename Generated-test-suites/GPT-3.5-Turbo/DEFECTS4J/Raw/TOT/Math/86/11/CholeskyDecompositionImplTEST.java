import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.linear.*;
import org.apache.commons.math.MathRuntimeException;

import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    private static final double EPSILON = 1e-10;
    
    private CholeskyDecompositionImpl decomposition;

    @Before
    public void setUp() throws Exception {

        double[][] matrixData = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        decomposition = new CholeskyDecompositionImpl(matrix);

    }

    @Test
    public void testGetL() {
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][] {
            {2.0, 0.0, 0.0},
            {6.0, 1.0, 0.0},
            {-8.0, 5.0, 3.0}
        });
        
        assertTrue(decomposition.getL().equals(expectedL, EPSILON));
    }

    @Test
    public void testGetLT() {
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][] {
            {2.0, 6.0, -8.0},
            {0.0, 1.0, 5.0},
            {0.0, 0.0, 3.0}
        });
        
        assertTrue(decomposition.getLT().equals(expectedLT, EPSILON));
    }

    @Test
    public void testGetDeterminant() {
        double expectedDeterminant = 36.0;
        
        assertEquals(expectedDeterminant, decomposition.getDeterminant(), EPSILON);
    }

    @Test
    public void testGetSolver() {
        DecompositionSolver solver = decomposition.getSolver();
        
        assertTrue(solver instanceof CholeskyDecompositionImpl.Solver);
    }

    @Test
    public void testIsNonSingular() {
        assertTrue(decomposition.getSolver().isNonSingular());
    }

    @Test
    public void testSolveDoubleArray() {

        double[] b = {5, 7, -9};
        double[] expectedX = {2, 1, -3};

        double[] actualX = decomposition.getSolver().solve(b);

        assertArrayEquals(expectedX, actualX, EPSILON);

    }

    @Test
    public void testSolveRealVectorImpl() {

        RealVectorImpl b = new RealVectorImpl(new double[] {5, 7, -9});
        RealVectorImpl expectedX = new RealVectorImpl(new double[] {2, 1, -3});

        RealVectorImpl actualX = decomposition.getSolver().solve(b);

        assertTrue(expectedX.equals(actualX, EPSILON));

    }

    @Test
    public void testSolveRealMatrix() {

        RealMatrix b = MatrixUtils.createColumnRealMatrix(new double[] {5, 7, -9});
        RealMatrix expectedX = MatrixUtils.createColumnRealMatrix(new double[] {2, 1, -3});

        RealMatrix actualX = decomposition.getSolver().solve(b);

        assertTrue(expectedX.equals(actualX, EPSILON));

    }

    @Test
    public void testGetInverse() {

        double[][] expectedInverseData = {
            {0.25, -0.75, 1.0},
            {-0.75, 3.25, -3.0},
            {1.0, -3.0, 3.0}
        };
        
        RealMatrix expectedInverse = MatrixUtils.createRealMatrix(expectedInverseData);

        RealMatrix actualInverse = decomposition.getSolver().getInverse();

        assertTrue(expectedInverse.equals(actualInverse, EPSILON));

    }

}