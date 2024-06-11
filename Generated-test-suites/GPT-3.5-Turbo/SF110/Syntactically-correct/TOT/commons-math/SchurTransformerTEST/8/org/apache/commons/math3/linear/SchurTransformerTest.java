import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class SchurTransformerTest {
    
    private double[][] testMatrix;
    private SchurTransformer schurTransformer;
    
    @Before
    public void setUp() {
        testMatrix = new double[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        schurTransformer = new SchurTransformer(MatrixUtils.createRealMatrix(testMatrix));
    }
    
    // Test getP() method
    @Test
    public void testGetP() {
        RealMatrix pMatrix = schurTransformer.getP();
        double[][] expectedP = new double[][] {{-0.21483724, -0.88723017, 0.40824829},
                                               {-0.52058739, -0.24964395, -0.81649658},
                                               {-0.82633755, 0.38794226, 0.40824829}};
        assertArrayEquals(expectedP, pMatrix.getData());
    }
    
    // Test getPT() method
    @Test
    public void testGetPT() {
        RealMatrix ptMatrix = schurTransformer.getPT();
        double[][] expectedPT = new double[][] {{-0.21483724, -0.52058739, -0.82633755},
                                                {-0.88723017, -0.24964395, 0.38794226},
                                                {0.40824829, -0.81649658, 0.40824829}};
        assertArrayEquals(expectedPT, ptMatrix.getData());
    }
    
    // Test getT() method
    @Test
    public void testGetT() {
        RealMatrix tMatrix = schurTransformer.getT();
        double[][] expectedT = new double[][] {{1.73863, 0.3872983, 5.54038},
                                               {0, -0.37228, 1.1722},
                                               {0, 0, -0.36694}};
        assertArrayEquals(expectedT, tMatrix.getData());
    }
    
    // Test throwing exception for non-square matrix
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] nonSquareMatrix = new double[][] {{1, 2, 3}, {4, 5, 6}};
        new SchurTransformer(MatrixUtils.createRealMatrix(nonSquareMatrix));
    }
    
    // Test throwing exception for failed convergence
    @Test(expected = MaxCountExceededException.class)
    public void testConvergenceFailedException() {
        double[][] testMatrix = new double[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        SchurTransformer schurTransformer = new SchurTransformer(MatrixUtils.createRealMatrix(testMatrix));
        // Modify the matrix to fail convergence
        schurTransformer.getT().setEntry(1, 1, 0);
        schurTransformer.getT().setEntry(2, 2, 0);
        schurTransformer.transform();
    }
}