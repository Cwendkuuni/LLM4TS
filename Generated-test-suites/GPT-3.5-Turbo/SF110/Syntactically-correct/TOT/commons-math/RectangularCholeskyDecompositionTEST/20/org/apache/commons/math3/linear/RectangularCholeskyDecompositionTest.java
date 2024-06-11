import org.apache.commons.math3.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    private double[][] testData1 = {{1.0, 0.0}, {0.0, 1.0}};
    private double[][] testData2 = {{4.0, 12.0}, {12.0, 37.0}};
    private double[][] testData3 = {{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 9.0}};
    private double[][] testData4 = {{-1.0, -2.0}, {-3.0, -4.0}};
    private double[][] testData5 = {{0.0, 0.0}, {0.0, 0.0}};
    
    private RealMatrix matrix1;
    private RealMatrix matrix2;
    private RealMatrix matrix3;
    private RealMatrix matrix4;
    private RealMatrix matrix5;

    @Before
    public void setUp() {
        matrix1 = MatrixUtils.createRealMatrix(testData1);
        matrix2 = MatrixUtils.createRealMatrix(testData2);
        matrix3 = MatrixUtils.createRealMatrix(testData3);
        matrix4 = MatrixUtils.createRealMatrix(testData4);
        matrix5 = MatrixUtils.createRealMatrix(testData5);
    }
    
    @Test
    public void testConstructor_withValidMatrix_noExceptionThrown() {
        try {
            new RectangularCholeskyDecomposition(matrix1);
            new RectangularCholeskyDecomposition(matrix2);
            new RectangularCholeskyDecomposition(matrix3);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_withInvalidMatrix_exceptionThrown() {
        new RectangularCholeskyDecomposition(matrix4);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_withZeroMatrix_exceptionThrown() {
        new RectangularCholeskyDecomposition(matrix5);
    }
    
    @Test
    public void testGetRootMatrix_withValidMatrix_returnRootMatrix() {
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix1);
        assertArrayEquals(testData1, decomp.getRootMatrix().getData());
        
        decomp = new RectangularCholeskyDecomposition(matrix2);
        assertArrayEquals(testData2, decomp.getRootMatrix().getData());
        
        decomp = new RectangularCholeskyDecomposition(matrix3);
        assertArrayEquals(testData3, decomp.getRootMatrix().getData());
    }
    
    @Test
    public void testGetRank_withValidMatrix_returnRank() {
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix1);
        assertEquals(2, decomp.getRank());
        
        decomp = new RectangularCholeskyDecomposition(matrix2);
        assertEquals(2, decomp.getRank());
        
        decomp = new RectangularCholeskyDecomposition(matrix3);
        assertEquals(3, decomp.getRank());
    }
}