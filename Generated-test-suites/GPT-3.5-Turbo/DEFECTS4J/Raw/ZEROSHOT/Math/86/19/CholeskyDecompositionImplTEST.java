import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.linear.RealVectorImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CholeskyDecompositionImplTest {
    
    private CholeskyDecompositionImpl choleskyDecomposition;
    private RealMatrix matrix;
    
    @Before
    public void setUp() {
        double[][] data = {{4, 12, -16}, 
                           {12, 37, -43}, 
                           {-16, -43, 98}};
        matrix = new RealMatrixImpl(data);
        choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }
    
    @Test
    public void testGetL() {
        RealMatrix expectedL = new RealMatrixImpl(new double[][]{{2.0, 0.0, 0.0}, 
                                                                 {6.0, 1.0, 0.0}, 
                                                                 {-8.0, 5.0, 3.0}});
        RealMatrix l = choleskyDecomposition.getL();
        assertEquals(expectedL, l);
    }
    
    @Test
    public void testGetLT() {
        RealMatrix expectedLT = new RealMatrixImpl(new double[][]{{2.0, 6.0, -8.0}, 
                                                                  {0.0, 1.0, 5.0}, 
                                                                  {0.0, 0.0, 3.0}});
        RealMatrix lt = choleskyDecomposition.getLT();
        assertEquals(expectedLT, lt);
    }
    
    @Test
    public void testGetDeterminant() {
        double expectedDeterminant = 792.0;
        double determinant = choleskyDecomposition.getDeterminant();
        assertEquals(expectedDeterminant, determinant, 0.0);
    }
    
    @Test
    public void testGetSolver() {
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        // TODO: Add tests for the solver methods
    }
    
    @Test
    public void testSolveVector() {
        RealVector b = new RealVectorImpl(new double[] {14, 41, -40});
        RealVector expectedX = new RealVectorImpl(new double[] {1.0, 2.0, -2.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        assertEquals(expectedX, x);
    }
    
    @Test
    public void testSolveMatrix() {
        RealMatrix b = new RealMatrixImpl(new double[][] {{14, 41}, 
                                                          {10, -13}, 
                                                          {-8, 39}});
        RealMatrix expectedX = new RealMatrixImpl(new double[][] {{1.0, 2.0}, 
                                                                  {2.0, -3.0}, 
                                                                  {-2.0, 1.0}});
        RealMatrix x = choleskyDecomposition.getSolver().solve(b);
        assertEquals(expectedX, x);
    }
    
    @Test
    public void testGetInverse() {
        RealMatrix expectedInverse = new RealMatrixImpl(new double[][] {{0.5714285714, -0.2857142857, 0.2571428571}, 
                                                                        {-0.2857142857, 0.4857142857, 0.05714285714}, 
                                                                        {0.2571428571, 0.05714285714, 0.1428571429}});
        RealMatrix inverse = choleskyDecomposition.getSolver().getInverse();
        assertEquals(expectedInverse, inverse);
    }
}